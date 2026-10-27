package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.IceTechniqueVisuals;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.SyncHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Reworked fire/ice casts. Timelines, targeting and damage are owned by the server. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class ElementalTechniqueService {
    private static final String ASH="bleachmod_ash_soldier";
    /** Ephemeral owned summon, vanilla entity type/packet/rig. Reloads are rejected by orphan(). */
    private static final class AshSoldier extends WitherSkeleton {
        AshSoldier(net.minecraft.server.level.ServerLevel level){super(EntityType.WITHER_SKELETON,level);}
        @Override protected boolean shouldDespawnInPeaceful(){return false;}
    }
    private static final Map<UUID,List<Cast>> CASTS=new HashMap<>();
    private static final Map<UUID,Long> CONTROL_RECOVERY=new HashMap<>();
    private static final class Cast {
        final ServerPlayer owner;final String identity,form,kind;final Vec3 origin,forward;final long start;
        final int slot,duration,gesture;final double radius;final Set<UUID> hits=new HashSet<>(),controlled=new HashSet<>();
        boolean stopped;
        com.bleachmod.entity.IceDragonEntity hydra;
        final java.util.Map<UUID,LivingEntity> cocoons=new HashMap<>();
        final Set<UUID> ruptured=new HashSet<>();
        final List<Vec3> crystals=new ArrayList<>();
        void cleanup(){soldiers.forEach(Entity::discard);if(hydra!=null)hydra.discard();}
        final List<AbstractSkeleton> soldiers=new ArrayList<>();
        final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
        final Map<UUID,Long> nextHit=new HashMap<>();
        Cast(ServerPlayer p,PlayerData d,String kind,int slot,int duration,int gesture,Vec3 origin,double radius){
            owner=p;identity=d.getCharacter().getZanpakutoIdentity();form=d.getCharacter().getActiveForm();this.kind=kind;this.slot=slot;
            this.duration=duration;this.gesture=gesture;this.origin=origin;this.radius=radius;start=p.level().getGameTime();
            forward=Vec3.directionFromRotation(0,p.getYRot());dimension=p.level().dimension();
        }
        int age(){return (int)(owner.level().getGameTime()-start);}
    }
    private ElementalTechniqueService(){ }
    public static boolean elemental(PlayerData d){return List.of(CharacterData.RYUJIN,CharacterData.HYORINMARU).contains(d.getCharacter().getZanpakutoIdentity());}
    public static boolean busy(ServerPlayer p){return CASTS.getOrDefault(p.getUUID(),List.of()).stream().anyMatch(c->c.owner==p && c.age()<c.gesture);}
    private static Cast add(ServerPlayer p,PlayerData d,String kind,int slot,int duration,int gesture,Vec3 origin,double radius){
        var list=CASTS.computeIfAbsent(p.getUUID(),id->new ArrayList<>());
        list.removeIf(c->{if(c.kind.equals(kind)){c.cleanup();return true;}return false;});
        if(list.size()>=4){list.remove(0).cleanup();}
        var c=new Cast(p,d,kind,slot,duration,gesture,origin,radius);
        if(kind.equals("ice_eruption"))for(int row=1;row<=8;row++)for(int spread=-2;spread<=2;spread++){
            double reach=row*4,side=spread*reach*.24;
            Vec3 at=origin.add(c.forward.scale(reach)).add(c.forward.z*side,0,-c.forward.x*side);
            if(!p.level().hasChunkAt(BlockPos.containing(at)))continue;
            var wall=p.level().clip(new ClipContext(origin.add(0,1,0),at.add(0,1,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));if(wall.getType()!=HitResult.Type.MISS)continue;
            var ground=p.level().clip(new ClipContext(at.add(0,2,0),at.add(0,-3,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));
            if(ground.getType()!=HitResult.Type.MISS)c.crystals.add(ground.getLocation());
        }
        list.add(c);SyncHelper.appearance(p);return c;
    }
    public static CompoundTag snapshot(ServerPlayer p){var out=new CompoundTag();var list=new ListTag();
        for(var c:CASTS.getOrDefault(p.getUUID(),List.of())){var t=new CompoundTag();t.putString("kind",c.kind);t.putInt("age",c.age());t.putInt("duration",c.duration);t.putInt("gesture",c.gesture);t.putFloat("yaw",p.getYRot());
            t.putDouble("x",c.origin.x);t.putDouble("y",c.origin.y);t.putDouble("z",c.origin.z);t.putDouble("dx",c.forward.x);t.putDouble("dz",c.forward.z);t.putDouble("radius",c.radius);t.putBoolean("ice",c.identity.equals(CharacterData.HYORINMARU));
            t.putIntArray("targets",c.cocoons.values().stream().filter(tgt->tgt.isAlive() && !c.ruptured.contains(tgt.getUUID())).limit(16).mapToInt(Entity::getId).toArray());
            var crystals=new ListTag();for(var point:c.crystals){var q=new CompoundTag();q.putDouble("x",point.x);q.putDouble("y",point.y);q.putDouble("z",point.z);crystals.add(q);}t.put("crystals",crystals);
            if(c.kind.equals("minami"))t.putIntArray("soldiers",c.soldiers.stream().filter(Entity::isAlive).limit(10).mapToInt(Entity::getId).toArray());list.add(t);}
        out.put("casts",list);return out;
    }
    public static void cancel(ServerPlayer p){var list=CASTS.remove(p.getUUID());if(list!=null){list.forEach(c->c.cleanup());SyncHelper.appearance(p);}}
    private static int cooldown(PlayerData d,int slot){var s=d.getStatus();return switch(slot){case 1->s.getTechniqueSlot1CooldownTicks();case 2->s.getTechniqueSlot2CooldownTicks();case 3->s.getTechniqueSlot3CooldownTicks();default->s.getTechniqueSlot4CooldownTicks();};}
    private static void cooldown(PlayerData d,int slot,int value){var s=d.getStatus();switch(slot){case 1->s.setTechniqueSlot1CooldownTicks(value);case 2->s.setTechniqueSlot2CooldownTicks(value);case 3->s.setTechniqueSlot3CooldownTicks(value);default->s.setTechniqueSlot4CooldownTicks(value);}}
    public static void execute(ServerPlayer p,PlayerData d,int slot){
        if(!TechniqueProgression.authorize(p,d,slot) || busy(p))return;
        String identity=d.getCharacter().getZanpakutoIdentity(),form=d.getCharacter().getActiveForm();
        boolean ice=identity.equals(CharacterData.HYORINMARU),bankai=form.equals("bankai");

        var def=TechniqueDefinitions.require(identity,form,slot);
        if(def.id().equals("unavailable"))return;
        if(cooldown(d,slot)>0){p.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.bleachmod.technique.cooldown",(cooldown(d,slot)+19)/20),true);return;}
        if(!d.getResources().canAffordReiatsu(def.cost())){p.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.bleachmod.technique.no_reiatsu",(int)def.cost()),true);return;}
        // Existing approved executors retain ownership of cost, dash, terrain and ice fields.
        if(ice && !form.equals("sealed") && ((slot==1 && !bankai) || slot==4)){
            HyorinmaruTechniqueService.executeSlot(p,d,slot);
            add(p,d,def.id(),slot,slot==4?(bankai?120:80):14,12,p.position(),slot==4?(bankai?30:10):15);return;
        }
        if(!ice && bankai && slot==1){TechniqueService.executeFlameDash(p,d);add(p,d,"flame_dash",1,16,16,p.position(),20);return;}
        if(!d.getResources().consumeReiatsu(def.cost()))return;
        cooldown(d,slot,def.cooldownTicks());
        String kind=def.id();Vec3 origin=p.position();int duration=20,gesture=16;double radius=6;
        switch(kind){
            case "frost_armor"->{IceArmorService.begin(p,d);duration=120;gesture=14;radius=0;}
            case "flame_barrage"->{duration=22;gesture=16;radius=8;}
            case "flame_fan"->{duration=24;gesture=22;radius=14;}
            case "flame_tornado"->{RyujinTechniqueService.executePaidTornado(p,d);duration=18;gesture=18;radius=9;}
            case "jokaku"->{origin=surfaceAim(p,10);duration=120;gesture=20;radius=6;}
            case "higashi"->{duration=22;gesture=22;radius=4;}
            case "minami"->{duration=180;gesture=22;radius=16;}
            case "steam_cut"->{duration=56;gesture=56;radius=100;}
            case "crescent_chain"->{duration=60;gesture=18;radius=12;}
            case "ryusenka"->{duration=64;gesture=22;radius=14;}
            case "hydra_assault"->{duration=156;gesture=18;radius=32;}
            case "ice_eruption"->{duration=90;gesture=24;radius=32;}
            case "sennen"->{origin=surfaceAim(p,24);duration=100;gesture=24;radius=10;}
            case "duel_control"->{duration=14;gesture=14;radius=5;}
            default->{duration=16;gesture=16;radius=6;}
        }
        var accepted=add(p,d,kind,slot,duration,gesture,origin,radius);
        p.serverLevel().playSound(null,p.blockPosition(),ice?net.minecraft.sounds.SoundEvents.GLASS_BREAK:net.minecraft.sounds.SoundEvents.BLAZE_SHOOT,p.getSoundSource(),.65F,bankai?.7F:1.1F);
        SyncHelper.resources(p);
    }
    private static Vec3 surfaceAim(ServerPlayer p,int range){
        Vec3 end=p.getEyePosition().add(p.getLookAngle().scale(range));
        var result=p.level().clip(new ClipContext(p.getEyePosition(),end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));
        Vec3 point=result.getType()==HitResult.Type.MISS?p.position().add(Vec3.directionFromRotation(0,p.getYRot()).scale(range*.65)):result.getLocation();
        var ground=p.level().clip(new ClipContext(point.add(0,3,0),point.add(0,-8,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));
        return ground.getType()==HitResult.Type.MISS?new Vec3(point.x,p.getY(),point.z):ground.getLocation().add(0,.05,0);
    }
    private static List<LivingEntity> targets(Cast c,Vec3 center,double radius){return c.owner.serverLevel().getEntitiesOfClass(LivingEntity.class,new AABB(center,center).inflate(radius,c.kind.equals("sennen")?12:c.kind.equals("ice_eruption")?14:4,radius),t->TechniqueTargets.allowed(c.owner,t));}
    private static boolean visible(Cast c,LivingEntity t,Vec3 origin){return c.owner.level().clip(new ClipContext(origin.add(0,1,0),t.getBoundingBox().getCenter(),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,c.owner)).getType()==HitResult.Type.MISS;}
    private static List<LivingEntity> cutVictims(Cast c,double angle){return targets(c,c.origin,c.radius).stream().filter(t->{Vec3 delta=t.position().subtract(c.origin);double along=delta.dot(c.forward),side=Math.abs(delta.x*c.forward.z-delta.z*c.forward.x);return Math.abs(delta.y)<=(c.kind.equals("ice_eruption")?14:3)&&along>=0&&along<=c.radius&&side<=.7+along*angle&&visible(c,t,c.origin);}).toList();}
    private static LivingEntity executionTarget(Cast c,List<LivingEntity> victims){return victims.stream().filter(t->HigashiExecutionService.eligible(c.owner,t)).min(Comparator.comparingDouble(t->1-c.owner.getLookAngle().dot(t.getBoundingBox().getCenter().subtract(c.owner.getEyePosition()).normalize()))).orElse(null);}
    private static boolean hit(Cast c,LivingEntity t,float damage,boolean once){
        if(c.hits.size()>=64 && !c.hits.contains(t.getUUID()) || once && c.hits.contains(t.getUUID()))return false;
        c.hits.add(t.getUUID());
        boolean dealt=MasteryPracticeService.hurt(c.owner,t,c.owner.damageSources().playerAttack(c.owner),damage,c.slot);
        if(dealt && c.kind.equals("higashi")){
            var pos=t.getBoundingBox().getCenter();var level=c.owner.serverLevel();
            // Incineration flames are delayed by the confirmed-death cinematic.
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,pos.x,pos.y,pos.z,12,.17,.24,.17,.07);
            level.sendParticles(ParticleTypes.SMOKE,pos.x,pos.y,pos.z,4,.18,.25,.18,.02);
            level.playSound(null,t.blockPosition(),net.minecraft.sounds.SoundEvents.FIRECHARGE_USE,c.owner.getSoundSource(),.65F,1.35F);
        }
        if(dealt && t.isAlive() && c.identity.equals(CharacterData.RYUJIN) && !c.form.equals("sealed"))t.setSecondsOnFire(c.form.equals("bankai")?4:3);return dealt;
    }
    private static void chill(Cast c,LivingEntity t,int ticks){
        if(c.controlled.size()>=64 || CONTROL_RECOVERY.size()>=1024 || c.controlled.contains(t.getUUID()) || CONTROL_RECOVERY.getOrDefault(t.getUUID(),0L)>c.owner.level().getGameTime())return;
        c.controlled.add(t.getUUID());CONTROL_RECOVERY.put(t.getUUID(),c.owner.level().getGameTime()+80);
        boolean resistant=t instanceof net.minecraft.world.entity.player.Player || t.getMaxHealth()>=100;
        IceControlService.chill(c.owner,t,resistant?Math.min(20,ticks):ticks,resistant?1:3,c.slot);
    }
    private static void effect(Cast c){
            int age=c.age();var p=c.owner;
            switch(c.kind){
            case "front_cut","wide_cut","sealed_cut","sealed_sweep","higashi"->{
                if(c.kind.equals("higashi")&&age>=8&&age<=11){
                    // Short planted approach, bounded by collision and the original cast frame.
                    double nearest=cutVictims(c,.35).stream().mapToDouble(p::distanceTo).min().orElse(0);
                    Vec3 step=c.forward.scale(Math.min(.7,Math.max(0,nearest-1.0)));
                    if(step.lengthSqr()>.0001&&p.position().distanceToSqr(c.origin.add(c.forward.scale(2.8)))>.0001
                            &&p.position().distanceToSqr(c.origin)<2.8*2.8-.0001
                            &&p.level().hasChunkAt(BlockPos.containing(p.position().add(step)))
                            &&p.level().noCollision(p,p.getBoundingBox().expandTowards(step))){
                        p.move(MoverType.SELF,step);p.setDeltaMovement(0,p.getDeltaMovement().y,0);p.hurtMarked=true;
                        p.connection.teleport(p.getX(),p.getY(),p.getZ(),p.getYRot(),p.getXRot());
                    }
                }
                if(age!=(c.kind.equals("higashi")?12:6))break;
                double angle=c.kind.endsWith("sweep") || c.kind.equals("wide_cut")?.8:.35;
                var victims=cutVictims(c,angle);
                LivingEntity execution=c.kind.equals("higashi")?executionTarget(c,victims):null;
                boolean cinematicKill=false;
                for(var t:victims){
                    if(t==execution){c.hits.add(t.getUUID());cinematicKill=HigashiExecutionService.execute(p,t);}
                    else {
                        var position=t.position();float width=t.getBbWidth(),height=t.getBbHeight();
                        if(c.kind.equals("higashi")){
                            HigashiExecutionService.sendHint(p,t,position,width,height);
                        }
                        try {
                            boolean dealt=hit(c,t,c.kind.equals("higashi")?16:3,true);
                            if(c.kind.equals("higashi")){
                                boolean finalDead=dealt&&!t.isAlive()&&!cinematicKill&&!t.getPersistentData().getBoolean("bleachmod_death_canceled_transient");
                                if(finalDead){
                                    HigashiExecutionService.confirmedDeath(p,t,position,width,height);
                                    cinematicKill=true;
                                } else {
                                    HigashiExecutionService.cancelHint(p,t);
                                }
                            }
                        } finally {
                            if(c.kind.equals("higashi")){
                                t.getPersistentData().remove("bleachmod_death_canceled_transient");
                            }
                        }
                    }
                }
            }
            case "flame_barrage"->{if(age<6 || age>14)break;double step=age-6;
                Vec3 a=c.origin.add(c.forward.scale(Math.max(0,step-1))),b=c.origin.add(c.forward.scale(step));
                if(p.level().clip(new ClipContext(a.add(0,.6,0),b.add(0,.6,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p)).getType()!=HitResult.Type.MISS){c.stopped=true;return;}
                for(var t:targets(c,b,1.2))if(t.getBoundingBox().intersects(new AABB(a,b).inflate(1,2,1)) && visible(c,t,c.origin))hit(c,t,6,true);
            }
            case "flame_fan"->{if(age<6 || age>20)break;double angle=-70+(age-6)*10;
                Vec3 dir=c.forward.yRot((float)Math.toRadians(angle));
                for(var t:targets(c,c.origin,c.radius)){Vec3 delta=t.position().subtract(c.origin);if(delta.horizontalDistance()>c.radius || Math.abs(delta.y)>3)continue;
                    if(delta.horizontalDistance()<.8 || delta.normalize().dot(dir)>.975)if(visible(c,t,c.origin))hit(c,t,8,true);}
            }
            case "duel_control"->{if(age==6)targets(c,c.origin,5).stream().filter(t->t.position().subtract(c.origin).normalize().dot(c.forward)>.88 && visible(c,t,c.origin)).min(Comparator.comparingDouble(p::distanceToSqr)).ifPresent(t->{chill(c,t,60);t.push(c.forward.x*.45,.1,c.forward.z*.45);t.hurtMarked=true;});}
            case "crescent_chain"->{
                if(age==8)targets(c,c.origin,12).stream().filter(t->t.position().subtract(c.origin).normalize().dot(c.forward)>.92 && visible(c,t,c.origin)).min(Comparator.comparingDouble(p::distanceToSqr)).ifPresent(t->{c.hits.add(t.getUUID());chill(c,t,50);MasteryPracticeService.hurt(p,t,p.damageSources().playerAttack(p),3,c.slot);});
                if(age>8 && age<42 && age%4==0)for(var t:targets(c,p.position(),12))if(c.hits.contains(t.getUUID()) && p.hasLineOfSight(t)){var delta=p.position().subtract(t.position());if(delta.length()>2){var v=delta.normalize().scale(.16);t.push(v.x,.03,v.z);t.hurtMarked=true;}chain(p,t);}
            }
            case "ryusenka"->{
                if(age>=6 && age<=19){
                    Vec3 move=c.forward;
                    if(p.position().subtract(c.origin).dot(c.forward)<14 && p.level().hasChunkAt(BlockPos.containing(p.position().add(move))) && p.level().noCollision(p,p.getBoundingBox().expandTowards(move))){
                        p.move(MoverType.SELF,move);p.setDeltaMovement(0,p.getDeltaMovement().y,0);p.hurtMarked=true;
                        p.connection.teleport(p.getX(),p.getY(),p.getZ(),p.getYRot(),p.getXRot());
                        for(var t:targets(c,p.position().subtract(move.scale(.5)),2.2))if(p.hasLineOfSight(t) && hit(c,t,8,true)){chill(c,t,50);c.cocoons.put(t.getUUID(),t);c.nextHit.put(t.getUUID(),p.level().getGameTime()+40);iceCross(p,t.position());}
                    }
                    IceTechniqueVisuals.ryusenkaThrustTrail(p, p.position(), c.forward);
                }
                if(age==20)SyncHelper.appearance(p);
                if(age>=46)for(var t:c.cocoons.values())if(t.isAlive() && p.level().getGameTime()>=c.nextHit.getOrDefault(t.getUUID(),Long.MAX_VALUE) && c.ruptured.add(t.getUUID())){hit(c,t,6,false);iceCross(p,t.position());SyncHelper.appearance(p);}
            }
            case "hydra_assault"->{
                if(age==1){c.hydra=com.bleachmod.registry.ModEntities.ICE_DRAGON.get().create(p.serverLevel());if(c.hydra!=null){c.hydra.configureAssault(p,c.origin,(float)Math.toDegrees(Math.atan2(-c.forward.x,c.forward.z)),32);p.serverLevel().addFreshEntity(c.hydra);}}

            }
            case "ice_eruption"->{
                if(age>=12 && age<=42)for(var t:cutVictims(c,.577)){
                    double along=t.position().subtract(c.origin).dot(c.forward);
                    if(along>(age-12)*32/30.0 || !groundConnected(c,t))continue;
                    if(hit(c,t,8,true)){chill(c,t,60);iceCross(p,t.position());}
                }
                if(age==56)for(var t:cutVictims(c,.577))if(c.hits.contains(t.getUUID()) && c.ruptured.add(t.getUUID()) && groundConnected(c,t)){hit(c,t,16,false);iceCross(p,t.position());}
            }
            case "sennen"->{if(age<40 || age>85 || age%10!=0)break;
                for(var t:targets(c,c.origin,c.radius))if(TechniqueGeometry.intersectsCylinder(c.origin,c.radius,1,12,t.getBoundingBox()) && visible(c,t,c.origin)){
                    chill(c,t,60);var v=c.origin.subtract(t.position());if(v.horizontalDistance()>1){v=v.normalize().scale(.18);t.push(v.x,.03,v.z);t.hurtMarked=true;}
                    if(age==50 || age==80)hit(c,t,6,false);
                }
            }
            case "jokaku"->{if(age<20 || age%20!=0)break;
                for(var t:targets(c,c.origin,6))if(TechniqueGeometry.intersectsCylinder(c.origin,6,1,5,t.getBoundingBox()) && visible(c,t,c.origin))hit(c,t,4,false);
            }
            case "steam_cut"->{
                if(age==10 || age==30)p.serverLevel().playSound(null,p.blockPosition(),net.minecraft.sounds.SoundEvents.FIRE_AMBIENT,p.getSoundSource(),.35F,age==10?.65F:.85F);
                if(age==40)TechniqueService.applyConcentratedSteamCut(p);
            }
            case "minami"->army(c);
        }
    }
    private static boolean groundConnected(Cast c,LivingEntity target){
        Vec3 end=new Vec3(target.getX(),c.origin.y,target.getZ());double distance=end.distanceTo(c.origin);if(distance>36)return false;
        for(int i=0;i<=Math.ceil(distance/2);i++){
            Vec3 at=c.origin.lerp(end,i/Math.max(1,Math.ceil(distance/2)));
            if(!c.owner.level().hasChunkAt(BlockPos.containing(at)))return false;
            var hit=c.owner.level().clip(new ClipContext(at.add(0,2,0),at.add(0,-3,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,c.owner));
            if(hit.getType()==HitResult.Type.MISS)return false;
        }
        return true;
    }
    private static void army(Cast c){var p=c.owner;int age=c.age();
        if(age==16)for(int i=0;i<10;i++){var mob=new AshSoldier(p.serverLevel());
            Vec3 side=new Vec3(-c.forward.z,0,c.forward.x);
            Vec3 at=p.position().add(side.scale((i%5-2)*1.25+(i/5)*.35)).subtract(c.forward.scale(1.5+(i/5)*1.5));
            var ground=p.level().clip(new ClipContext(at.add(0,2,0),at.add(0,-3,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));
            if(ground.getType()==HitResult.Type.MISS)continue;
            mob.setPos(ground.getLocation());
            if(!p.level().hasChunkAt(mob.blockPosition()) || !p.level().noCollision(mob,mob.getBoundingBox()))continue;
            mob.setNoAi(true);mob.setAggressive(true);mob.setYRot(p.getYRot());mob.yBodyRot=p.getYRot();mob.getPersistentData().putBoolean(ASH,true);mob.getPersistentData().putUUID("ashOwner",p.getUUID());mob.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);
            for(var slot:EquipmentSlot.values())mob.setDropChance(slot,0);mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(6);mob.setHealth(6);c.soldiers.add(mob);p.serverLevel().addFreshEntity(mob);
        }
        if(age==16)SyncHelper.appearance(p); // Publish IDs once, for emergence and late observers.
        c.soldiers.removeIf(e->!e.isAlive() || e.isRemoved());
        for(var mob:c.soldiers){
            var t=targets(c,p.position(),16).stream().filter(e->!e.getPersistentData().getBoolean(ASH) && (e instanceof Enemy || e instanceof ServerPlayer) && mob.hasLineOfSight(e)).min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
            int index=c.soldiers.indexOf(mob);
            Vec3 formation=p.position().subtract(c.forward.scale(2+(index/5)*1.4)).add(new Vec3(-c.forward.z,0,c.forward.x).scale((index%5-2)*1.25));
            Vec3 goal=t==null?formation:t.position(),delta=goal.subtract(mob.position());
            if(delta.horizontalDistance()>.1){float yaw=(float)Math.toDegrees(Math.atan2(-delta.x,delta.z));mob.setYRot(yaw);mob.yBodyRot=yaw;mob.setYHeadRot(yaw);}
            if(delta.horizontalDistance()>1){var step=new Vec3(delta.x,0,delta.z).normalize().scale(.22);if(p.level().hasChunkAt(BlockPos.containing(mob.position().add(step))))mob.move(MoverType.SELF,step);}
            if(t!=null && mob.distanceToSqr(t)<3 && c.nextHit.getOrDefault(t.getUUID(),0L)<=p.level().getGameTime()){
                c.nextHit.put(t.getUUID(),p.level().getGameTime()+20);hit(c,t,3,false);mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
            }
            if(age%6==0)p.serverLevel().sendParticles(ParticleTypes.ASH,mob.getX(),mob.getY()+1,mob.getZ(),2,.2,.6,.2,.01);
        }
    }
    private static void chain(ServerPlayer p,LivingEntity target){var a=p.getEyePosition();var b=target.getBoundingBox().getCenter();for(int i=0;i<=12;i++){var q=a.lerp(b,i/12.0);p.serverLevel().sendParticles(ParticleTypes.END_ROD,q.x,q.y,q.z,1,0,0,0,0);}}
    private static void iceCross(ServerPlayer p,Vec3 at){for(int i=-4;i<=4;i++){p.serverLevel().sendParticles(ParticleTypes.SNOWFLAKE,at.x+i*.25,at.y+1,at.z,3,.1,.1,.1,.02);p.serverLevel().sendParticles(ParticleTypes.END_ROD,at.x,at.y+1+i*.3,at.z,1,0,0,0,0);}}
    public static void tick(ServerPlayer p){var list=CASTS.get(p.getUUID());if(list==null)return;var d=PlayerCapability.get(p).orElse(null);boolean changed=false;
        for(var c:new ArrayList<>(list)){
            if(c.stopped || d==null || p.isRemoved() || !p.isAlive() || p.isSpectator() || !TechniqueProgression.equipped(p,d) || !c.identity.equals(d.getCharacter().getZanpakutoIdentity()) || !c.form.equals(d.getCharacter().getActiveForm()) || !c.dimension.equals(p.level().dimension()) || c.age()>=c.duration){c.cleanup();list.remove(c);changed=true;continue;}
            if(c.age()<c.gesture)p.setSprinting(false);effect(c);
        }
        CONTROL_RECOVERY.values().removeIf(t->t<=p.level().getGameTime());
        if(list.isEmpty())CASTS.remove(p.getUUID());if(changed)SyncHelper.appearance(p);
    }
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent e){if(e.phase==TickEvent.Phase.END)for(var list:new ArrayList<>(CASTS.values()))if(!list.isEmpty())tick(list.get(0).owner);}
    @SubscribeEvent public static void logout(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p)cancel(p);}
    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST, receiveCanceled = true)
    public static void onLivingDeathLowest(LivingDeathEvent e){
        if(e.getEntity()!=null && e.isCanceled()){
            e.getEntity().getPersistentData().putBoolean("bleachmod_death_canceled_transient",true);
        }
    }
    @SubscribeEvent public static void ashDeath(LivingDeathEvent e){
        if(e.getEntity().getPersistentData().getBoolean(ASH) && e.getEntity().level() instanceof net.minecraft.server.level.ServerLevel level){
            var mob=e.getEntity();level.sendParticles(ParticleTypes.ASH,mob.getX(),mob.getY()+1,mob.getZ(),14,.25,.6,.25,.025);
            level.sendParticles(ParticleTypes.SMOKE,mob.getX(),mob.getY()+1,mob.getZ(),6,.2,.5,.2,.02);
        }
    }
    @SubscribeEvent public static void drops(LivingDropsEvent e){if(e.getEntity().getPersistentData().getBoolean(ASH))e.getDrops().clear();}
    @SubscribeEvent public static void xp(LivingExperienceDropEvent e){if(e.getEntity().getPersistentData().getBoolean(ASH))e.setDroppedExperience(0);}
    @SubscribeEvent public static void orphan(EntityJoinLevelEvent e){if(!e.getLevel().isClientSide() && e.getEntity().getPersistentData().getBoolean(ASH) && CASTS.values().stream().flatMap(List::stream).noneMatch(c->c.soldiers.contains(e.getEntity())))e.setCanceled(true);}
    @SubscribeEvent public static void stop(net.minecraftforge.event.server.ServerStoppingEvent e){CASTS.values().forEach(l->l.forEach(c->c.cleanup()));CASTS.clear();CONTROL_RECOVERY.clear();}
}
