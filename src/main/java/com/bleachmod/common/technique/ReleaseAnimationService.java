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
        boolean committed;
        Release(ServerPlayer p, PlayerData d, String group, String form, float cost) {
            owner=p;identity=d.getCharacter().getZanpakutoIdentity();oldForm=d.getCharacter().getActiveForm();
            this.group=group;this.form=form;this.cost=cost;weapon=p.getMainHandItem().getItem();
            start=p.level().getGameTime();dimension=p.level().dimension();duration=form.equals("bankai")?60:24;commit=duration*2/3;
        }
    }
    private ReleaseAnimationService() { }
    public static boolean supports(PlayerData d, String group, String form) {
        return group.equals("zanpakuto") && List.of("shikai","bankai").contains(form)
                && List.of(CharacterData.RYUJIN,CharacterData.HYORINMARU).contains(d.getCharacter().getZanpakutoIdentity());
    }
    public static boolean busy(ServerPlayer p) { return ACTIVE.containsKey(p.getUUID()); }
    public static void begin(ServerPlayer p, PlayerData d, String group, String form, float cost) {
        if(busy(p) || !TechniqueProgression.equipped(p,d) || d.getStatus().isDodging() || d.getStatus().getFlameDashTicks()>0) return;
        RyujinTechniqueService.cancel(p);
        ACTIVE.put(p.getUUID(),new Release(p,d,group,form,cost));
        d.getStatus().setIgnitionActive(false);d.getGuard().interrupt();
        SyncHelper.appearance(p);
    }
    public static CompoundTag snapshot(ServerPlayer p) {
        var tag=new CompoundTag();var r=ACTIVE.get(p.getUUID());if(r==null)return tag;
        tag.putString("identity",r.identity);tag.putString("form",r.form);tag.putInt("duration",r.duration);
        tag.putInt("elapsed",(int)Math.max(0,Math.min(r.duration,p.level().getGameTime()-r.start)));return tag;
    }
    public static void cancel(ServerPlayer p) { if(ACTIVE.remove(p.getUUID())!=null)SyncHelper.appearance(p); }
    public static void tick(ServerPlayer p) {
        var r=ACTIVE.get(p.getUUID());if(r==null)return;
        var d=PlayerCapability.get(p).orElse(null);
        if(d==null || !d.getStatus().hasCreatedCharacter() || !p.isAlive() || p.isSpectator() || !p.level().dimension().equals(r.dimension)
                || !p.getMainHandItem().is(r.weapon) || !d.getCharacter().getZanpakutoIdentity().equals(r.identity)
                || !d.getCharacter().getActiveForm().equals(r.committed?r.form:r.oldForm)) { cancel(p);return; }
        long age=p.level().getGameTime()-r.start;
        p.setSprinting(false);
        if(age>=r.commit && !r.committed) {
            if(!FormModeHandler.completeAnimatedRelease(p,d,r.group,r.form,r.cost)) {cancel(p);return;}
            r.committed=true;SyncHelper.appearance(p);
        }
        if(age>=r.duration)cancel(p);
    }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e) {
        if(e.phase==TickEvent.Phase.END && e.player instanceof ServerPlayer p)tick(p);
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e) {if(e.getEntity() instanceof ServerPlayer p)cancel(p);}
    @SubscribeEvent public static void stop(ServerStoppingEvent e) {ACTIVE.clear();}
}
