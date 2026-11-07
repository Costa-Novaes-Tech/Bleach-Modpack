package com.bleachmod.common.quincy;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.SyncHelper;
import com.bleachmod.common.technique.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Quincy-only prototype. No Shinigami form/archive writes and no automatic Antithesis combat ability. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class QuincyVollstandigService {
    public static final int ENTRANCE=180,COMMIT=160,CHARGE=12;
    public static final float ENTRANCE_COST=30,MAINTENANCE=2,VOLLEY_COST=4;
    private static final Map<UUID,State> ACTIVE=new HashMap<>();
    private static final class State {
        long start,lastMaintenance=Long.MIN_VALUE,lastShot=Long.MIN_VALUE;boolean active;int steps=3;long refill;
        Vec3 anchor,convergence;float yaw;long convergeAt;
        final Set<QuincyArrowEntity> projectiles=new HashSet<>();
        int barrage;long nextBarrage;
        State(ServerPlayer p){start=p.level().getGameTime();anchor=p.position();yaw=p.getYRot();}
    }
    private QuincyVollstandigService(){ }
    public static boolean active(ServerPlayer p){var s=ACTIVE.get(p.getUUID());return s!=null&&s.active;}
    public static boolean preparing(ServerPlayer p){var s=ACTIVE.get(p.getUUID());return s!=null&&p.level().getGameTime()-s.start<ENTRANCE;}
    public static void toggle(ServerPlayer p,PlayerData d){
        if(ACTIVE.containsKey(p.getUUID())){stop(p,d);return;}
        if(!QuincyTechniqueService.isQuincy(d)||!d.getStatus().hasCreatedCharacter()||!p.isAlive()||p.isSpectator())return;
        if(!d.getQuincy().hasVollstandig()){p.displayClientMessage(Component.translatable("message.bleachmod.quincy.vollstandig_locked"),true);return;}
        if(d.getGuard().active()||d.getGuard().broken()||d.getStatus().isDodging()||p.isUsingItem()
                ||ReleaseAnimationService.busy(p)||ElementalTechniqueService.busy(p)||d.getQuincy().isLichtRegenActive()
                ||d.getQuincy().isSprengerActive()||!d.getResources().canAffordReiatsu(ENTRANCE_COST))return;
        ACTIVE.put(p.getUUID(),new State(p));SyncHelper.appearance(p);SyncHelper.resources(p);
    }
    public static void stop(ServerPlayer p,PlayerData d){
        var old=ACTIVE.remove(p.getUUID());
        boolean had=old!=null||d.getQuincy().isVollstandigActive();
        if(old!=null)List.copyOf(old.projectiles).forEach(a->a.discard());
        d.getQuincy().setVollstandigActive(false);
        if(had){p.stopUsingItem();SyncHelper.appearance(p);SyncHelper.resources(p);}
    }
    public static CompoundTag snapshot(ServerPlayer p){var tag=new CompoundTag();var s=ACTIVE.get(p.getUUID());if(s==null)return tag;
        tag.putInt("age",(int)Math.max(0,p.level().getGameTime()-s.start));tag.putBoolean("active",s.active);
        tag.putDouble("x",s.anchor.x);tag.putDouble("y",s.anchor.y);tag.putDouble("z",s.anchor.z);
        tag.putInt("shotAge",s.lastShot==Long.MIN_VALUE?120:(int)Math.min(120,p.level().getGameTime()-s.lastShot));tag.putFloat("yaw",s.yaw);tag.putInt("steps",s.steps);return tag;}
    public static void tick(ServerPlayer p,PlayerData d){
        var s=ACTIVE.get(p.getUUID());if(s==null){d.getQuincy().setVollstandigActive(false);return;}
        s.projectiles.removeIf(a->a.isRemoved());
        long now=p.level().getGameTime(),age=now-s.start;
        if(!QuincyTechniqueService.isQuincy(d)||!p.isAlive()||p.isSpectator()||!d.getQuincy().hasVollstandig()) {stop(p,d);return;}
        if(age<COMMIT){
            if(p.position().distanceToSqr(s.anchor)>4||p.hurtTime>0||d.getGuard().active()||d.getStatus().isDodging()) {stop(p,d);return;}
            if(age==20||age==80||age==135)p.level().playSound(null,p.blockPosition(),SoundEvents.AMETHYST_BLOCK_CHIME,p.getSoundSource(),.7F,.8F+age/150F);
            if(age%4==0)p.serverLevel().sendParticles(ParticleTypes.ELECTRIC_SPARK,p.getX(),p.getY()+1,p.getZ(),5,.6,1,.6,.02);
        }
        if(!s.active&&age>=COMMIT){
            if(!d.getResources().consumeReiatsu(ENTRANCE_COST)){stop(p,d);return;}
            s.active=true;d.getQuincy().setVollstandigActive(true);s.refill=now+120;
            p.level().playSound(null,p.blockPosition(),SoundEvents.BEACON_ACTIVATE,p.getSoundSource(),1,1.3F);
            SyncHelper.appearance(p);SyncHelper.resources(p);
        }
        if(!s.active)return;
        if(!d.getResources().areCostsDisabled()&&d.getResources().getCurrentReiatsu()<=0){stop(p,d);return;}
        if(age>=ENTRANCE&&(s.lastMaintenance==Long.MIN_VALUE||now-s.lastMaintenance>=20)){s.lastMaintenance=now;if(!d.getResources().consumeReiatsu(MAINTENANCE)){stop(p,d);return;}}
        if(s.steps<3&&now>=s.refill){s.steps++;s.refill=now+120;SyncHelper.appearance(p);SyncHelper.resources(p);}
        if(s.barrage>0&&now>=s.nextBarrage){
            if(!p.getMainHandItem().is(com.bleachmod.init.ModItems.QUINCY_BOW.get()))s.barrage=0;
            else {int n=12-s.barrage;spawnAux(p,QuincyFormation.star(p.position(),p.getYRot(),n%4,now),p.getEyePosition().add(p.getLookAngle().yRot((float)Math.toRadians((n%3-1)*6)).scale(64)),2,2);s.barrage--;s.nextBarrage=now+3;}
        }
        if(s.convergence!=null){
            if(now>=s.convergeAt){
                for(var t:p.serverLevel().getEntitiesOfClass(LivingEntity.class,new AABB(s.convergence,s.convergence).inflate(6),e->TechniqueTargets.allowed(p,e)).stream().sorted(java.util.Comparator.comparingDouble(e->e.distanceToSqr(s.convergence))).limit(16).toList()){
                    if(t.position().distanceToSqr(s.convergence)>36||!clear(p,s.convergence.add(0,.5,0),t.getBoundingBox().getCenter()))continue;
                    MasteryPracticeService.hurt(p,t,p.damageSources().playerAttack(p),18,4);
                }
                p.serverLevel().sendParticles(ParticleTypes.END_ROD,s.convergence.x,s.convergence.y,s.convergence.z,45,3,2,3,.08);
                p.level().playSound(null,net.minecraft.core.BlockPos.containing(s.convergence),SoundEvents.GENERIC_EXPLODE,p.getSoundSource(),.8F,1.5F);s.convergence=null;
            }else if(now%4==0)for(int i=0;i<4;i++){
                Vec3 a=s.convergence.add(Math.cos(i*Math.PI/2)*5,2,Math.sin(i*Math.PI/2)*5);
                for(int k=0;k<8;k++){Vec3 q=a.lerp(s.convergence,k/8.0);p.serverLevel().sendParticles(ParticleTypes.END_ROD,q.x,q.y,q.z,1,0,0,0,0);}
            }
        }
        if(age%20==0)SyncHelper.appearance(p);
    }
    public static boolean clear(ServerPlayer p,Vec3 a,Vec3 b){return p.serverLevel().hasChunksAt(net.minecraft.core.BlockPos.containing(a),net.minecraft.core.BlockPos.containing(b))
            &&p.level().clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p)).getType()==HitResult.Type.MISS;}
    private static Vec3 safeOrigin(ServerPlayer p,Vec3 star){return clear(p,p.getEyePosition(),star)?star:p.getEyePosition();}
    public static void track(ServerPlayer p,QuincyArrowEntity arrow){var s=ACTIVE.get(p.getUUID());if(s!=null&&s.active&&arrow.getOwner()==p)s.projectiles.add(arrow);}
    private static void spawnAux(ServerPlayer p,Vec3 origin,Vec3 end,float damage,int slot){
        var arrow=new QuincyArrowEntity(p.level(),p);arrow.setPos(safeOrigin(p,origin));arrow.configureSatellite(damage);arrow.setTechniqueSlot(slot);
        Vec3 f=end.subtract(arrow.position()).normalize();arrow.shoot(f.x,f.y,f.z,3.6F,0);if(p.level().addFreshEntity(arrow))track(p,arrow);
    }
    public static void satellites(ServerPlayer p){var state=ACTIVE.get(p.getUUID());if(state==null||!state.active)return;state.lastShot=p.level().getGameTime();Vec3 aim=p.getEyePosition().add(p.getLookAngle().scale(96));
        for(int i=0;i<4;i++)spawnAux(p,QuincyFormation.star(p.position(),p.getYRot(),i,p.level().getGameTime()),aim,1.5F,0);
        SyncHelper.appearance(p);}
    public static void execute(ServerPlayer p,PlayerData d,int slot,int forward,int left){
        var s=ACTIVE.get(p.getUUID());if(s==null||!s.active||preparing(p))return;
        int[] cd={d.getStatus().getTechniqueSlot1CooldownTicks(),d.getStatus().getTechniqueSlot2CooldownTicks(),d.getStatus().getTechniqueSlot3CooldownTicks(),d.getStatus().getTechniqueSlot4CooldownTicks()};if(cd[slot-1]>0)return;
        if(slot==3){
            if(s.steps<=0)return;
            if(HirenkyakuService.execute(p,d,forward,left)){s.steps--;d.getStatus().setTechniqueSlot3CooldownTicks(s.steps>0?0:120);SyncHelper.appearance(p);SyncHelper.resources(p);}
            return;
        }
        float cost=slot==1?16:slot==2?25:40;
        if(slot==2&&s.barrage>0||slot==4&&s.convergence!=null)return;
        Vec3 endpoint=p.getEyePosition().add(p.getLookAngle().scale(slot==4?32:96));
        if(slot==4){if(!p.serverLevel().hasChunksAt(net.minecraft.core.BlockPos.containing(p.getEyePosition()),net.minecraft.core.BlockPos.containing(endpoint)))return;var hit=p.level().clip(new ClipContext(p.getEyePosition(),endpoint,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));endpoint=hit.getLocation().add(0,.15,0);
            if(!p.serverLevel().hasChunkAt(net.minecraft.core.BlockPos.containing(endpoint)))return;}
        if(!d.getResources().consumeReiatsu(cost))return;
        MasteryPracticeService.noteIntent(p);
        if(slot==1){var a=new QuincyArrowEntity(p.level(),p);a.configureVollstandig(1);a.setSpiritualDamage(12);Vec3 f=p.getLookAngle();a.shoot(f.x,f.y,f.z,3.6F,0);if(p.level().addFreshEntity(a)){track(p,a);satellites(p);}d.getStatus().setTechniqueSlot1CooldownTicks(120);}
        else if(slot==2){s.barrage=12;s.nextBarrage=p.level().getGameTime();d.getStatus().setTechniqueSlot2CooldownTicks(240);}
        else {s.convergence=endpoint;s.convergeAt=p.level().getGameTime()+30;d.getStatus().setTechniqueSlot4CooldownTicks(400);}
        SyncHelper.resources(p);
    }
    @SubscribeEvent public static void stopping(net.minecraftforge.event.server.ServerStoppingEvent e){ACTIVE.values().forEach(s->List.copyOf(s.projectiles).forEach(a->a.discard()));}
    @SubscribeEvent public static void stopped(net.minecraftforge.event.server.ServerStoppedEvent e){ACTIVE.clear();}
    @SubscribeEvent public static void died(net.minecraftforge.event.entity.living.LivingDeathEvent e){if(e.getEntity() instanceof ServerPlayer p)PlayerCapability.get(p).ifPresent(d->stop(p,d));}
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p)PlayerCapability.get(p).ifPresent(d->stop(p,d));}
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e){if(e.getEntity() instanceof ServerPlayer p)PlayerCapability.get(p).ifPresent(d->stop(p,d));}
}
