package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.CharacterData;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.entity.IceDragonEntity;
import com.bleachmod.init.ModSounds;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Server-authoritative lifecycle controller for Daiguren Hyorinmaru's Bankai Transformation Cinematic.
 * Total presentation duration: 400 ticks (20 seconds).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class HyorinmaruCinematicService {
    public static final int DURATION = com.bleachmod.common.visual.HyorinmaruTimeline.DURATION;

    private static final class Scene {
        final long start; final Vec3 anchor; final float yaw;
        final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
        final java.util.Set<UUID> hits=new java.util.HashSet<>();
        boolean suppressBreath;
        Scene(long start,Vec3 anchor,float yaw,net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension){this.start=start;this.anchor=anchor;this.yaw=yaw;this.dimension=dimension;}
    }

    private static final Map<UUID, Scene> SCENES = new HashMap<>();
    private static final Map<UUID, UUID> MANIFESTATIONS = new HashMap<>();

    private HyorinmaruCinematicService() { }

    public static void begin(ServerPlayer player) {
        cancel(player);
        Vec3 anchor = player.position();
        SCENES.put(player.getUUID(), new Scene(player.level().getGameTime(), anchor, player.getYRot(), player.level().dimension()));
        SyncHelper.appearance(player);
    }

    public static boolean active(ServerPlayer player) {
        Scene s = SCENES.get(player.getUUID());
        if (s == null) return false;
        long age = player.level().getGameTime() - s.start;
        return age >= 0 && age < DURATION;
    }

    public static CompoundTag snapshot(ServerPlayer player) {
        var tag = new CompoundTag();
        var scene = SCENES.get(player.getUUID());
        if (scene == null) return tag;
        long age = player.level().getGameTime() - scene.start;
        if (age < 0 || age >= DURATION) return tag;
        tag.putInt("elapsed", (int) age);
        tag.putInt("duration", DURATION);
        tag.putDouble("x", scene.anchor.x);
        tag.putDouble("y", scene.anchor.y);
        tag.putDouble("z", scene.anchor.z);
        tag.putFloat("yaw", scene.yaw);
        return tag;
    }

    public static void cancel(ServerPlayer player) {
        UUID hydraId = MANIFESTATIONS.remove(player.getUUID());
        if (hydraId != null && player.getServer() != null) {
            for (var sl : player.getServer().getAllLevels()) {
                var entity = sl.getEntity(hydraId);
                if (entity != null) entity.discard();
            }
        }
        if (SCENES.remove(player.getUUID()) != null) {
            SyncHelper.appearance(player);
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer p)) return;
        var s = SCENES.get(p.getUUID());
        if (s == null) return;

        long elapsed = p.level().getGameTime() - s.start;
        double age = com.bleachmod.common.visual.HyorinmaruTimeline.motionAge(elapsed);
        var data = PlayerCapability.get(p).orElse(null);

        boolean valid = data != null
                && CharacterData.HYORINMARU.equals(data.getCharacter().getZanpakutoIdentity())
                && HyorinmaruTechniqueService.equipped(p, data)
                && (ReleaseAnimationService.busy(p) || "bankai".equals(data.getCharacter().getActiveForm()))
                && p.isAlive()
                && !p.isSpectator()
                && s.dimension.equals(p.level().dimension())
                && elapsed >= 0 && elapsed < DURATION
                && p.position().distanceToSqr(s.anchor) < 256.0;

        if (age >= 175 && data != null && (data.getGuard().shownActive() || data.getStatus().isDodging())) {
            valid = false;
        }

        if (!valid) {
            cancel(p);
            return;
        }

        if(p.position().distanceToSqr(s.anchor)>9)s.suppressBreath=true;
        if(age>=108 && age<=144 && elapsed%6==0) breath(p,s,(int)age);
        ServerLevel level = p.serverLevel();
        Vec3 pos = s.anchor;

        // Dialogue/music follow the synchronized snapshot on clients; world effects remain server-owned.
        if (age < 44) {
            if (elapsed % 6 == 0) {
                level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 0.1, pos.z, 20, 1.2, 0.2, 1.2, 0.02);
            }
        }
        // Tick88 (4.4s): Ice eruption and slower hydra emergence.
        else if (elapsed == 88) {
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F);
            level.sendParticles(ParticleTypes.ITEM_SNOWBALL, pos.x, pos.y + 0.3, pos.z, 60, 2.5, 0.5, 2.5, 0.1);

            // Spawn the stationary 3-headed Hydra entity behind the player
            IceDragonEntity hydra = ModEntities.ICE_DRAGON.get().create(level);
            if (hydra != null) {
                hydra.configureStationary(p, pos, s.yaw, DURATION - 88);
                if (level.addFreshEntity(hydra)) {
                    MANIFESTATIONS.put(p.getUUID(), hydra.getUUID());
                }
            }
        }
        // Tick330 (16.5s): Full name, after the breath clears.
        else if (elapsed == 330) {
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 0.12F, 1.3F);
            level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 1.2, pos.z, 40, 2.0, 0.8, 2.0, 0.05);
        }
        // Tick216 (10.8s): Preserved hydra roar and extended frost shockwave.
        else if (elapsed == 216) {
            // Roar follows the synchronized client cue: local caster stays audible during the wide camera shot.
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.2F, 0.5F);
            level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 1.5, pos.z, 120, 3.5, 1.0, 3.5, 0.2);
        }
    }

    private static void breath(ServerPlayer p,Scene s,int age) {
        Vec3 forward=Vec3.directionFromRotation(0,s.yaw),side=new Vec3(forward.z,0,-forward.x);
        double reach=com.bleachmod.common.visual.HyorinmaruTimeline.BREATH_REACH*Math.min(1,(age-105)/24.0);
        var level=p.serverLevel();
        for(int head=0;head<3;head++){
            Vec3 local=com.bleachmod.common.visual.HydraMotion.point(false,head,age-44,1,32);
            Vec3 mouth=s.anchor.subtract(forward.scale(8)).add(side.scale(local.x)).add(0,local.y+.4,0).add(forward.scale(local.z));
            Vec3 merge=s.anchor.add(forward.scale(7)).add(0,1.3,0);
            for(int step=0;step<8;step++){Vec3 at=mouth.lerp(merge,step/7.0);level.sendParticles(ParticleTypes.SNOWFLAKE,at.x,at.y,at.z,4,.7,.6,.7,.06);}
        }
        // A bounded set of rays forms the three converging streams, then the broad frontal snow fan.
        for(int i=0;i<13;i++){
            double offset=(i-6)/6.0;
            Vec3 end=s.anchor.add(forward.scale(reach)).add(side.scale(offset*reach*.9)).add(0,1.1,0);
            var hit=level.clip(new net.minecraft.world.level.ClipContext(s.anchor.add(0,1.1,0),end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
            if(hit.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)end=hit.getLocation();
            for(int step=0;step<5;step++){
                Vec3 at=s.anchor.add(0,1.1,0).lerp(end,step/4.0);
                level.sendParticles(ParticleTypes.SNOWFLAKE,at.x,at.y,at.z,3,.6,.7,.6,.08);
                if(step%2==0)level.sendParticles(ParticleTypes.ITEM_SNOWBALL,at.x,at.y,at.z,1,.3,.4,.3,.03);
            }
        }
        if(s.suppressBreath || age<120)return;
        for(var t:level.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class,new net.minecraft.world.phys.AABB(s.anchor,s.anchor.add(forward.scale(com.bleachmod.common.visual.HyorinmaruTimeline.BREATH_REACH))).inflate(com.bleachmod.common.visual.HyorinmaruTimeline.BREATH_REACH,4,com.bleachmod.common.visual.HyorinmaruTimeline.BREATH_REACH),t->TechniqueTargets.allowed(p,t))){
            Vec3 d=t.getBoundingBox().getCenter().subtract(s.anchor);double along=d.dot(forward),lateral=Math.abs(d.dot(side));
            if(along<0 || along>reach || lateral>1+along || Math.abs(d.y)>4 || s.hits.size()>=64 || s.hits.contains(t.getUUID()))continue;
            var wall=level.clip(new net.minecraft.world.level.ClipContext(s.anchor.add(0,1.2,0),t.getBoundingBox().getCenter(),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,p));
            if(wall.getType()!=net.minecraft.world.phys.HitResult.Type.MISS)continue;
            s.hits.add(t.getUUID());
            if(MasteryPracticeService.hurt(p,t,p.damageSources().playerAttack(p),8,1)){
                IceControlService.chill(p,t,80,1,1);GlacialFragilityService.mark(p,t);
            }
        }
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer p) cancel(p);
    }

    @SubscribeEvent
    public static void hurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer p && active(p)) {
            ReleaseAnimationService.cancel(p);
            cancel(p);
        }
    }

    @SubscribeEvent
    public static void stop(ServerStoppingEvent event) {
        for (var level : event.getServer().getAllLevels()) {
            for (var id : MANIFESTATIONS.values()) {
                var entity = level.getEntity(id);
                if (entity != null) entity.discard();
            }
        }
        SCENES.clear();
        MANIFESTATIONS.clear();
    }
}
