package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.visual.SignatureChoreography;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Cosmetic lease only. Never authorizes, blocks, delays or charges a technique. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class SignatureAnimationService {
    private record Gesture(ServerPlayer owner,String identity,String form,String kind,int duration,long start,net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension){ }
    private static final Map<UUID,Gesture> ACTIVE=new HashMap<>();
    private SignatureAnimationService(){ }
    public static void play(ServerPlayer p,String kind){play(p,kind,SignatureChoreography.duration(kind));}
    public static void play(ServerPlayer p,String kind,int duration){
        var d=PlayerCapability.get(p).orElse(null);if(d==null || SignatureChoreography.duration(kind)==0 || duration<1 || duration>60)return;
        String id=d.getCharacter().getZanpakutoIdentity();if(!List.of(CharacterData.SENBONZAKURA,CharacterData.BENIHIME).contains(id) || !TechniqueProgression.equipped(p,d))return;
        ACTIVE.put(p.getUUID(),new Gesture(p,id,d.getCharacter().getActiveForm(),kind,duration,p.level().getGameTime(),p.level().dimension()));SyncHelper.appearance(p);
    }
    public static CompoundTag snapshot(ServerPlayer p){var out=new CompoundTag();var g=ACTIVE.get(p.getUUID());if(g==null || g.owner!=p)return out;
        out.putString("identity",g.identity);out.putString("form",g.form);out.putString("kind",g.kind);out.putInt("age",(int)(p.level().getGameTime()-g.start));out.putInt("duration",g.duration);return out;
    }
    public static void cancel(ServerPlayer p){if(ACTIVE.remove(p.getUUID())!=null)SyncHelper.appearance(p);}
    public static void cancel(ServerPlayer p,String identity){var g=ACTIVE.get(p.getUUID());if(g!=null&&g.identity.equals(identity))cancel(p);}
    public static void tick(ServerPlayer p){var g=ACTIVE.get(p.getUUID());if(g==null)return;
        var d=PlayerCapability.get(p).orElse(null);
        if(d==null || !p.isAlive() || p.isSpectator() || !g.dimension.equals(p.level().dimension()) || !g.identity.equals(d.getCharacter().getZanpakutoIdentity()) || !g.form.equals(d.getCharacter().getActiveForm()) || !TechniqueProgression.equipped(p,d) || p.level().getGameTime()-g.start>=g.duration)cancel(p);
    }
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent e){if(e.phase==TickEvent.Phase.END)for(var g:new ArrayList<>(ACTIVE.values()))tick(g.owner);}
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p)cancel(p);}
    @SubscribeEvent public static void stop(ServerStoppingEvent e){ACTIVE.clear();}
}
