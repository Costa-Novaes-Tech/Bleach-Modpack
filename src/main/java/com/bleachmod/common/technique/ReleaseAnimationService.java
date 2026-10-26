package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.server.events.FormModeHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** One authoritative timeline; visual state is transient and supplied to late observers. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class ReleaseAnimationService {
    private static final Map<UUID, Release> ACTIVE = new HashMap<>();
    private static final class Release {
        final ServerPlayer owner; final String identity, oldForm, group, form;
        final Item weapon; final long start; final int duration, commit; final float cost;
        final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
        boolean committed, committing;
        Release(ServerPlayer p, PlayerData d, String group, String form, float cost) {
            owner=p;identity=d.getCharacter().getZanpakutoIdentity();oldForm=d.getCharacter().getActiveForm();
            this.group=group;this.form=form;this.cost=cost;weapon=p.getMainHandItem().getItem();
            start=p.level().getGameTime();dimension=p.level().dimension();duration=form.equals("bankai")?(identity.equals(CharacterData.HYORINMARU)?HyorinmaruCinematicService.DURATION:60):24;commit=identity.equals(CharacterData.HYORINMARU)&&form.equals("bankai")?com.bleachmod.common.visual.HyorinmaruTimeline.COMMIT:duration*2/3;
        }
    }
    private ReleaseAnimationService() { }
    public static boolean supports(PlayerData d, String group, String form) {
        return group.equals("zanpakuto") && List.of("shikai","bankai").contains(form)
                && List.of(CharacterData.RYUJIN,CharacterData.HYORINMARU,CharacterData.SENBONZAKURA,CharacterData.BENIHIME,CharacterData.ASAUCHI).contains(d.getCharacter().getZanpakutoIdentity());
    }
    public static boolean busy(ServerPlayer p) { return ACTIVE.containsKey(p.getUUID()) || NozarashiReleaseEffects.inCinematic(p); }
    /** Form listeners clear old techniques while the release still owns its presentation. */
    public static boolean committing(ServerPlayer p) { var r=ACTIVE.get(p.getUUID());return r!=null && r.committing; }
    public static void begin(ServerPlayer p, PlayerData d, String group, String form, float cost) {
        if(busy(p) || !TechniqueProgression.equipped(p,d) || d.getStatus().isDodging() || d.getStatus().getFlameDashTicks()>0) return;
        RyujinTechniqueService.cancel(p);
        ACTIVE.put(p.getUUID(),new Release(p,d,group,form,cost));
        if(d.getCharacter().getZanpakutoIdentity().equals("benihime")&&form.equals("bankai"))BenihimeEntranceService.begin(p);
        if(d.getCharacter().getZanpakutoIdentity().equals("hyorinmaru")&&form.equals("bankai"))HyorinmaruCinematicService.begin(p);
        if(d.getCharacter().getZanpakutoIdentity().equals(CharacterData.SENBONZAKURA)&&form.equals("bankai"))SenbonzakuraCinematicService.begin(p);
        if(d.getCharacter().getZanpakutoIdentity().equals(CharacterData.RYUJIN)&&form.equals("bankai"))RyujinBankaiEntranceService.begin(p);
        d.getStatus().setIgnitionActive(false);d.getGuard().interrupt();
        SyncHelper.appearance(p);
    }
    public static CompoundTag snapshot(ServerPlayer p) {
        var tag=new CompoundTag();var r=ACTIVE.get(p.getUUID());if(r==null)return tag;
        tag.putString("identity",r.identity);tag.putString("form",r.form);tag.putInt("duration",r.duration);
        tag.putInt("elapsed",(int)Math.max(0,Math.min(r.duration,p.level().getGameTime()-r.start)));return tag;
    }
    public static void cancel(ServerPlayer p) {
        var removed=ACTIVE.remove(p.getUUID());
        if(removed!=null) {
            if(removed.identity.equals(CharacterData.HYORINMARU))HyorinmaruCinematicService.cancel(p);
            if(removed.identity.equals(CharacterData.SENBONZAKURA))SenbonzakuraCinematicService.cancel(p);
            if(removed.identity.equals(CharacterData.RYUJIN))RyujinBankaiEntranceService.cancel(p);
            SyncHelper.appearance(p);
        }
    }
    public static void tick(ServerPlayer p) {
        var r=ACTIVE.get(p.getUUID());if(r==null)return;
        var d=PlayerCapability.get(p).orElse(null);
        if(d==null || !d.getStatus().hasCreatedCharacter() || !p.isAlive() || p.isSpectator() || !p.level().dimension().equals(r.dimension)
                || !p.getMainHandItem().is(r.weapon) || !d.getCharacter().getZanpakutoIdentity().equals(r.identity)
                || !d.getCharacter().getActiveForm().equals(r.committed?r.form:r.oldForm)) { cancel(p);return; }
        long age=p.level().getGameTime()-r.start;
        p.setSprinting(false);
        if(age>=r.commit && !r.committed) {
            boolean completed;
            r.committing=true;
            try { completed=FormModeHandler.completeAnimatedRelease(p,d,r.group,r.form,r.cost); }
            finally { r.committing=false; }
            if(!completed) {cancel(p);return;}
            r.committed=true;SyncHelper.appearance(p);
        }
        if(age>=r.duration) {
            if((r.identity.equals(CharacterData.SENBONZAKURA) || r.identity.equals(CharacterData.RYUJIN)) && r.form.equals("bankai")) {
                ACTIVE.remove(p.getUUID());
                SyncHelper.appearance(p);
                return;
            }
            cancel(p);
        }
    }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e) {
        if(e.phase==TickEvent.Phase.END && e.player instanceof ServerPlayer p)tick(p);
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e) {if(e.getEntity() instanceof ServerPlayer p)cancel(p);}
    @SubscribeEvent public static void stop(ServerStoppingEvent e) {ACTIVE.clear();}
}
