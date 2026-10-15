package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.TransformationsHelper;
import com.bleachmod.common.network.*;
import com.bleachmod.common.network.s2c.BenihimeFrameS2C;
import com.bleachmod.common.visual.SenbonGeometry;
import com.bleachmod.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import java.util.*;
import static com.bleachmod.common.network.s2c.BenihimeFrameS2C.*;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class BenihimeTechniqueService {
    private static final Map<UUID,State> ACTIVE=new HashMap<>();
    private record Support(ServerLevel level,long until) { }
    private static final Map<UUID,Support> SUPPORT=new HashMap<>();
    private static final class Action {
        final int slot;final long start;final Vec3 origin,forward;Vec3 end;final Set<UUID> hits=new HashSet<>();UUID recipient;int pulse=-1;
        Action(int slot,long now,Vec3 o,Vec3 f,Vec3 e){this.slot=slot;start=now;origin=o;forward=f;end=e;}
    }
    private static final class Net {
        final Vec3 center;final long start;final Map<UUID,Vec3> captured=new LinkedHashMap<>();
        Net(Vec3 c,long now){center=c;start=now;}
    }
    private static final class Corridor {
        final Vec3 from,to;final long start;final Set<UUID> used=new HashSet<>();
        Corridor(Vec3 a,Vec3 b,long n){from=a;to=b;start=n;}
    }
    private static final class State {
        final ServerPlayer owner;final ServerLevel level;final String form;Action action;Net net;Corridor path;
        float shield;long shieldUntil,buffUntil,nextSelf,nextAura,frameTick=Long.MIN_VALUE;int charges,frames;final Map<UUID,Long> exposed=new HashMap<>();
        State(ServerPlayer p,String form){owner=p;level=p.serverLevel();this.form=form;nextSelf=level.getGameTime()+20;nextAura=level.getGameTime()+40;}
        void draw(int kind,Vec3 o,Vec3 end,double radius,double progress,int variant){
            long now=level.getGameTime();if(frameTick!=now){frameTick=now;frames=0;}
            if(frames>=4 || !level.hasChunkAt(BlockPos.containing(o)))return;
            var m=new BenihimeFrameS2C(owner.getId(),kind,o,end,SenbonGeometry.forward(owner.getYRot()),(float)radius,(float)Math.max(0,Math.min(1,progress)),variant);
            if(!m.valid())return;frames++;
            for(var observer:level.players())if(observer.position().distanceToSqr(o)<80*80)NetworkHandler.sendToPlayer(m,observer);
        }
    }
    private BenihimeTechniqueService() { }
    public static boolean equipped(ServerPlayer p,PlayerData d){return CharacterData.BENIHIME.equals(d.getCharacter().getZanpakutoIdentity()) && p.getMainHandItem().is(ModItems.BENIHIME.get());}
    private static boolean valid(ServerPlayer p,PlayerData d){String form=d.getCharacter().getActiveForm();return p.isAlive() && !p.isSpectator() && d.getStatus().hasCreatedCharacter()
            && equipped(p,d) && Reference.RACE_SHINIGAMI.equals(d.getCharacter().getRace()) && "zanpakuto".equals(d.getCharacter().getActiveFormGroup())
            && (form.equals("shikai") || form.equals("bankai")) && TransformationsHelper.isUnlocked(d,"zanpakuto",form);}
    private static State current(ServerPlayer p){var s=ACTIVE.get(p.getUUID());return s!=null && s.owner==p && s.level==p.serverLevel()?s:null;}
    private static State state(ServerPlayer p,PlayerData d){var s=current(p);if(s==null || !s.form.equals(d.getCharacter().getActiveForm())){cancel(p);s=new State(p,d.getCharacter().getActiveForm());ACTIVE.put(p.getUUID(),s);}return s;}
    private static boolean legitimate(State s){return s!=null && PlayerCapability.get(s.owner).map(d->valid(s.owner,d) && s.form.equals(d.getCharacter().getActiveForm())).orElse(false);}
    public static boolean busy(ServerPlayer p){var s=current(p);return legitimate(s) && s.action!=null;}
    public static boolean ward(ServerPlayer p){var s=current(p);return legitimate(s) && s.shield>0 && s.shieldUntil>s.level.getGameTime();}
    public static boolean healable(ServerPlayer owner,LivingEntity target){
        if(!target.isAlive() || target.isSpectator() || target.level()!=owner.level())return false;
        if(target==owner)return true;
        if(target instanceof ServerPlayer)return owner.getTeam()!=null && owner.isAlliedTo(target);
        return target instanceof TamableAnimal pet && pet.isTame() && owner.getUUID().equals(pet.getOwnerUUID());
    }
    public static float damageBonus(ServerPlayer p,LivingEntity target){var s=current(p);if(!legitimate(s))return 0;long now=s.level.getGameTime();
        return (s.exposed.getOrDefault(target.getUUID(),0L)>now?BenihimePolicy.EXPOSURE:0)
                +(!MasteryPracticeService.isTechniqueDamage() && s.charges>0 && s.buffUntil>now?BenihimePolicy.MELEE_BONUS:0);}
    public static void damageApplied(ServerPlayer p,LivingEntity target){var s=current(p);if(legitimate(s) && !MasteryPracticeService.isTechniqueDamage() && s.charges>0 && s.buffUntil>s.level.getGameTime() && TechniqueTargets.allowed(p,target)){s.charges--;SyncHelper.resources(p);}}
    private static boolean loaded(ServerLevel level,Vec3 a,Vec3 b){return level.hasChunksAt(BlockPos.containing(Math.min(a.x,b.x),Math.min(a.y,b.y),Math.min(a.z,b.z)),BlockPos.containing(Math.max(a.x,b.x),Math.max(a.y,b.y),Math.max(a.z,b.z)));}
    private static boolean line(State s,Vec3 a,Vec3 b){return loaded(s.level,a,b) && s.level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,s.owner)).getType()==HitResult.Type.MISS;}
    private static Vec3 aim(ServerPlayer p,double range){Vec3 a=p.getEyePosition(),b=a.add(p.getLookAngle().scale(range));if(!loaded(p.serverLevel(),a,b) || !p.level().getWorldBorder().isWithinBounds(BlockPos.containing(b)))return null;
        var hit=p.level().clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));return hit.getLocation().add(hit.getType()==HitResult.Type.MISS?Vec3.ZERO:Vec3.atLowerCornerOf(hit.getDirection().getNormal()).scale(.15));}
    private static List<LivingEntity> enemies(State s,Vec3 center,double radius){return s.level.getEntitiesOfClass(LivingEntity.class,new AABB(center,center).inflate(radius+1),e->TechniqueTargets.allowed(s.owner,e) && s.owner.hasLineOfSight(e))
            .stream().filter(e->SenbonGeometry.inside(center,e.position(),radius,3)).filter(e->line(s,center.add(0,.3,0),e.getBoundingBox().getCenter())).limit(BenihimePolicy.MAX_TARGETS).toList();}
    private static void hurt(State s,LivingEntity t,float damage,int slot){MasteryPracticeService.hurt(s.owner,t,s.owner.damageSources().playerAttack(s.owner),damage,slot);}
    private static int cooldown(StatusData d,int slot){return switch(slot){case 1->d.getTechniqueSlot1CooldownTicks();case 2->d.getTechniqueSlot2CooldownTicks();case 3->d.getTechniqueSlot3CooldownTicks();default->d.getTechniqueSlot4CooldownTicks();};}
    private static void cooldown(StatusData d,int slot,int ticks){switch(slot){case 1->d.setTechniqueSlot1CooldownTicks(ticks);case 2->d.setTechniqueSlot2CooldownTicks(ticks);case 3->d.setTechniqueSlot3CooldownTicks(ticks);default->d.setTechniqueSlot4CooldownTicks(ticks);}}
    private static boolean fail(ServerPlayer p,String key){p.displayClientMessage(Component.translatable("message.bleachmod.benihime."+key),true);return false;}
    public static boolean executeSlot(ServerPlayer p,PlayerData d,int slot){
        if(!TechniqueProgression.authorize(p,d,slot) || !valid(p,d) || p.isUsingItem() || d.getStatus().isActionCharging())return false;
        var s=state(p,d);boolean bankai=s.form.equals("bankai");long now=s.level.getGameTime();
        if(s.action!=null)return fail(p,"busy");
        if(cooldown(d.getStatus(),slot)>0)return fail(p,"cooldown");
        if(!bankai && slot==4 && (s.net==null || now-s.net.start>=BenihimePolicy.NET_TICKS || p.position().distanceToSqr(s.net.center)>BenihimePolicy.AREA*BenihimePolicy.AREA))return fail(p,"need_net");
        if(!bankai && slot==2 && ward(p))return fail(p,"busy");
        if(bankai && slot==4 && s.path!=null)return fail(p,"busy");
        Vec3 origin=p.position().add(0,1,0),f=p.getLookAngle().normalize(),end=origin;LivingEntity recipient=p;
        if(!bankai && slot==3 || bankai && slot==1){end=aim(p,bankai?16:BenihimePolicy.NET_RANGE);if(end==null)return fail(p,"target");}
        if(!bankai && slot==4)end=s.net.center;
        if(bankai && slot==2){recipient=healingTarget(s);if(recipient.getHealth()>=recipient.getMaxHealth())return fail(p,"full_health");end=recipient.position().add(0,1,0);}
        if(bankai && slot==4){end=pathEnd(p);if(end==null)return fail(p,"path");}
        var def=TechniqueDefinitions.require(CharacterData.BENIHIME,s.form,slot);
        if(!d.getResources().consumeReiatsu(def.cost()))return fail(p,"energy");cooldown(d.getStatus(),slot,def.cooldownTicks());
        if(!bankai && slot==2){s.shield=BenihimePolicy.SHIELD_CAPACITY;s.shieldUntil=now+BenihimePolicy.SHIELD_TICKS;d.getGuard().release();}
        else if(bankai && slot==3){s.charges=3;s.buffUntil=now+BenihimePolicy.BUFF_TICKS;}
        else {s.action=new Action(slot,now,origin,f,end);if(bankai && slot==2)s.action.recipient=recipient.getUUID();if(!bankai && slot==4)s.net=null;}
        s.level.playSound(null,p.blockPosition(),SoundEvents.AMETHYST_BLOCK_CHIME,p.getSoundSource(),.65F,bankai?.65F:1.15F);SyncHelper.resources(p);return true;
    }
    private static LivingEntity healingTarget(State s){Vec3 from=s.owner.getEyePosition(),to=from.add(s.owner.getLookAngle().scale(BenihimePolicy.HEAL_RANGE));
        return s.level.getEntitiesOfClass(LivingEntity.class,new AABB(from,to).inflate(1),e->healable(s.owner,e) && e!=s.owner && s.owner.hasLineOfSight(e))
                .stream().filter(e->e.getBoundingBox().inflate(.4).clip(from,to).isPresent()).min(Comparator.comparingDouble(s.owner::distanceToSqr)).orElse(s.owner);}
    private static Vec3 pathEnd(ServerPlayer p){
        if(p.isPassenger() || p.isSleeping() || p.isFallFlying())return null;
        Vec3 f=SenbonGeometry.forward(p.getYRot());
        for(double distance=BenihimePolicy.PATH_RANGE;distance>=3;distance-=1){Vec3 candidate=p.position().add(f.scale(distance));
            var box=p.getBoundingBox().move(candidate.subtract(p.position()));var sweep=p.getBoundingBox().expandTowards(candidate.subtract(p.position()));
            if(!loaded(p.serverLevel(),p.position(),candidate) || !p.level().getWorldBorder().isWithinBounds(box) || !p.level().noCollision(p,sweep))continue;
            var support=p.level().clip(new ClipContext(candidate.add(0,.1,0),candidate.add(0,-2.1,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));
            if(support.getType()!=HitResult.Type.MISS){Vec3 dest=support.getLocation().add(0,.02,0);if(p.level().noCollision(p,p.getBoundingBox().move(dest.subtract(p.position()))))return dest;}
        }return null;
    }
    public static void tick(ServerPlayer p,PlayerData d){
        if(!valid(p,d)){cancel(p);return;}var s=state(p,d);long now=s.level.getGameTime();
        s.exposed.values().removeIf(until->until<=now);if(s.buffUntil<=now)s.charges=0;if(s.shieldUntil<=now)s.shield=0;
        if(d.getGuard().broken())s.action=null;
        if(s.action!=null)tickAction(s,now);
        if(s.net!=null)tickNet(s,now);
        if(s.path!=null)tickPath(s,now);
        if(ward(p) && now%4==0)s.draw(SHIELD,p.position(),p.position(),1.6,s.shield/BenihimePolicy.SHIELD_CAPACITY,0);
        if(s.charges>0 && now%4==0)s.draw(BUFF,p.position(),p.position(),1,(s.buffUntil-now)/(double)BenihimePolicy.BUFF_TICKS,s.charges);
        if(s.form.equals("bankai")){
            if(now%4==0)s.draw(QUEEN,p.position(),p.position(),BenihimePolicy.AREA,s.action==null?(now%80)/80.0:Math.min(1,(now-s.action.start)/16.0),s.action==null?0:s.action.slot);
            tickRegeneration(s,now);
        }
    }
    private static void tickAction(State s,long now){Action a=s.action;int age=(int)(now-a.start);boolean bankai=s.form.equals("bankai");
        if(!bankai)switch(a.slot){
            case 1->{
                double distance=Math.min(BenihimePolicy.SHOT_RANGE,(age+1)*1.2);Vec3 end=a.origin.add(a.forward.scale(distance));
                if(!loaded(s.level,a.origin,end)){s.action=null;return;}
                var block=s.level.clip(new ClipContext(a.origin,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,s.owner));end=block.getLocation();
                s.draw(SLASH,a.end,end,.9,Math.min(1,distance/BenihimePolicy.SHOT_RANGE),0);
                for(var t:s.level.getEntitiesOfClass(LivingEntity.class,new AABB(a.end,end).inflate(1),e->TechniqueTargets.allowed(s.owner,e)))
                    if(a.hits.size()<BenihimePolicy.MAX_TARGETS && !a.hits.contains(t.getUUID()) && s.owner.hasLineOfSight(t) && (t.getBoundingBox().inflate(.6).contains(end) || t.getBoundingBox().inflate(.6).clip(a.end,end).isPresent())){a.hits.add(t.getUUID());hurt(s,t,BenihimePolicy.SHOT_DAMAGE,1);}
                a.end=end;if(block.getType()!=HitResult.Type.MISS || distance>=BenihimePolicy.SHOT_RANGE)s.action=null;
            }
            case 3->{s.draw(NET,a.end,a.end,BenihimePolicy.NET_RADIUS,Math.min(1,age/10.0),0);if(age>=10){s.net=new Net(a.end,now);for(var t:enemies(s,a.end,BenihimePolicy.NET_RADIUS)){s.net.captured.put(t.getUUID(),t.position());MasteryPracticeService.control(s.owner,t,3);}s.action=null;}}
            case 4->{s.draw(BEADS,a.origin,a.end,4,Math.min(1,age/16.0),0);if(age>=16){for(var t:enemies(s,a.end,4))hurt(s,t,BenihimePolicy.BLAST_DAMAGE,4);s.draw(INCISION,a.end,a.end,4,1,1);s.action=null;}}
        } else switch(a.slot){
            case 1->{if(s.owner.position().distanceToSqr(a.end)>BenihimePolicy.AREA*BenihimePolicy.AREA){s.action=null;return;}s.draw(INCISION,a.origin,a.end,4,Math.min(1,age/12.0),0);if(age>=12){for(var t:enemies(s,a.end,4)){if(s.exposed.size()<32)s.exposed.put(t.getUUID(),now+60);hurt(s,t,BenihimePolicy.INCISION_DAMAGE,1);}s.action=null;}}
            case 2->{
                LivingEntity t=a.recipient.equals(s.owner.getUUID())?s.owner:s.level.getEntity(a.recipient) instanceof LivingEntity living?living:null;
                if(t==null || !healable(s.owner,t) || s.owner.distanceToSqr(t)>BenihimePolicy.HEAL_RANGE*BenihimePolicy.HEAL_RANGE || !s.owner.hasLineOfSight(t)){s.action=null;return;}
                s.draw(HEAL,s.owner.position().add(0,1,0),t.position(),1,Math.min(1,age/30.0),0);
                if(age>=10 && a.pulse<(age/10)-1 && age<=30){a.pulse=(age/10)-1;float before=t.getHealth();t.heal(BenihimePolicy.HEAL_PULSE);MasteryPracticeService.healing(s.owner,t,2,t.getHealth()-before);}
                if(age>=30)s.action=null;
            }
            case 4->{s.draw(CORRIDOR,s.owner.position(),a.end,1,Math.min(1,age/6.0),0);if(age>=6){s.path=new Corridor(s.owner.position(),a.end,now);s.action=null;}}
        }
    }
    private static void tickNet(State s,long now){Net net=s.net;if(now-net.start>=BenihimePolicy.NET_TICKS || s.owner.position().distanceToSqr(net.center)>BenihimePolicy.AREA*BenihimePolicy.AREA){s.net=null;return;}
        if(now%4==0){s.draw(NET,net.center,net.center,BenihimePolicy.NET_RADIUS,1,0);
            for(var id:List.copyOf(net.captured.keySet())){var entity=s.level.getEntity(id);if(!(entity instanceof LivingEntity t) || !TechniqueTargets.allowed(s.owner,t) || !s.owner.hasLineOfSight(t) || !SenbonGeometry.inside(net.center,t.position(),5,4)){net.captured.remove(id);continue;}
                t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,BenihimePolicy.LEASE,3,false,false,true));
                Vec3 back=net.captured.get(id).subtract(t.position()).multiply(1,0,1);if(back.lengthSqr()>.25){Vec3 pull=back.normalize().scale(.09);t.setDeltaMovement(pull.x,t.getDeltaMovement().y,pull.z);t.hurtMarked=true;}
            }
        }
    }
    private static boolean ownRegeneration(LivingEntity t){return t instanceof ServerPlayer p && PlayerCapability.get(p).map(d->valid(p,d) && d.getCharacter().getActiveForm().equals("bankai")).orElse(false);}
    private static void tickRegeneration(State s,long now){
        if(now>=s.nextSelf){s.nextSelf=now+20;float before=s.owner.getHealth();s.owner.heal(BenihimePolicy.SELF_REGEN);if(s.owner.getHealth()>before)s.draw(HEAL,s.owner.position(),s.owner.position(),1,.5,0);}
        if(now<s.nextAura)return;s.nextAura=now+40;SUPPORT.entrySet().removeIf(e->e.getValue().until<=now || e.getValue().level!=s.level && e.getValue().level.getServer()!=s.level.getServer());
        for(var t:s.level.getEntitiesOfClass(LivingEntity.class,s.owner.getBoundingBox().inflate(BenihimePolicy.AURA_RADIUS),e->e!=s.owner && healable(s.owner,e) && !ownRegeneration(e)).stream().limit(BenihimePolicy.MAX_TARGETS).toList()) {
            if(t.distanceToSqr(s.owner)>BenihimePolicy.AURA_RADIUS*BenihimePolicy.AURA_RADIUS || !s.owner.hasLineOfSight(t) || t.getHealth()>=t.getMaxHealth())continue;
            var lease=SUPPORT.get(t.getUUID());if(lease!=null && lease.level==s.level && lease.until>now)continue;
            if(SUPPORT.size()>=1024)break;float before=t.getHealth();t.heal(BenihimePolicy.ALLY_REGEN);
            if(t.getHealth()>before){SUPPORT.put(t.getUUID(),new Support(s.level,now+40));s.draw(HEAL,s.owner.position().add(0,1,0),t.position(),1,.5,1);}
        }
    }
    private static void tickPath(State s,long now){var path=s.path;if(now-path.start>=BenihimePolicy.PATH_TICKS || s.owner.position().distanceToSqr(path.from)>BenihimePolicy.AREA*BenihimePolicy.AREA || s.owner.position().distanceToSqr(path.to)>BenihimePolicy.AREA*BenihimePolicy.AREA){s.path=null;return;}
        if(now%4!=0)return;s.draw(CORRIDOR,path.from,path.to,1,1,0);
        for(var other:ACTIVE.values())if(other!=s && other.shield>0 && other.level==s.level && TechniqueTargets.allowed(s.owner,other.owner)
                && other.owner.getBoundingBox().inflate(1.5).clip(path.from.add(0,1,0),path.to.add(0,1,0)).isPresent()){other.shield=0;SyncHelper.resources(other.owner);}
        for(var projectile:s.level.getEntitiesOfClass(Projectile.class,new AABB(path.from,path.to).inflate(1.5)))
            if(projectile.getOwner() instanceof LivingEntity attacker && TechniqueTargets.allowed(s.owner,attacker) && projectile.getBoundingBox().inflate(.8).clip(path.from.add(0,1,0),path.to.add(0,1,0)).isPresent())projectile.discard();
        var travellers=new LinkedHashSet<ServerPlayer>(s.level.players());travellers.add(s.owner);
        for(var p:travellers)if(healable(s.owner,p) && p.position().distanceToSqr(path.from)<1.5*1.5 && !p.isPassenger() && !p.isSleeping() && !path.used.contains(p.getUUID()) && path.used.size()<16){
            var box=p.getBoundingBox().move(path.to.subtract(p.position()));
            if(!loaded(s.level,p.position(),path.to))continue;
            var support=s.level.clip(new ClipContext(path.to.add(0,.1,0),path.to.add(0,-.2,0),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));
            if(!s.level.getWorldBorder().isWithinBounds(box) || !s.level.noCollision(p,p.getBoundingBox().expandTowards(path.to.subtract(p.position()))) || support.getType()==HitResult.Type.MISS
                    || !line(s,path.from.add(0,1,0),path.to.add(0,1,0)))continue;
            path.used.add(p.getUUID());p.setPos(path.to);p.connection.teleport(path.to.x,path.to.y,path.to.z,p.getYRot(),p.getXRot());p.setDeltaMovement(Vec3.ZERO);p.hurtMarked=true;
        }
    }
    @SubscribeEvent(priority=EventPriority.LOW)
    public static void shieldHit(LivingHurtEvent event){
        if(event.isCanceled() || !(event.getEntity() instanceof ServerPlayer p) || !ward(p) || p.isUsingItem() || !Float.isFinite(event.getAmount()) || event.getAmount()<=0)return;
        DamageSource source=event.getSource();if(source.is(DamageTypeTags.BYPASSES_SHIELD) || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.is(DamageTypeTags.IS_EXPLOSION)
                || !(com.bleachmod.common.GuardService.blockable(source) || source.is(DamageTypeTags.IS_PROJECTILE)))return;
        Vec3 from=source.getSourcePosition();if(from==null || !com.bleachmod.common.GuardPolicy.inFront(p.getYRot(),from.subtract(p.position())))return;
        State s=current(p);float absorbed=Math.min(s.shield,event.getAmount()*BenihimePolicy.SHIELD_REDUCTION);s.shield-=absorbed;event.setAmount(event.getAmount()-absorbed);
        if(absorbed>0 && source.getEntity() instanceof LivingEntity attacker)MasteryPracticeService.control(p,attacker,2);
        s.level.playSound(null,p.blockPosition(),s.shield<=0?SoundEvents.SHIELD_BREAK:SoundEvents.SHIELD_BLOCK,p.getSoundSource(),.7F,.8F);SyncHelper.resources(p);
    }
    public static CompoundTag hud(ServerPlayer p){var tag=new CompoundTag();var s=current(p);if(s==null)return tag;long now=s.level.getGameTime();
        tag.putInt("shield",(int)Math.ceil(Math.max(0,s.shield)));if(s.net!=null)tag.putInt("net",(int)Math.max(0,BenihimePolicy.NET_TICKS-(now-s.net.start)));
        tag.putInt("charges",s.buffUntil>now?s.charges:0);if(s.path!=null)tag.putInt("path",(int)Math.max(0,BenihimePolicy.PATH_TICKS-(now-s.path.start)));return tag;}
    public static void cancel(ServerPlayer p){var s=ACTIVE.remove(p.getUUID());if(s!=null)s.draw(CLEAR,p.position(),p.position(),0,0,0);}
    public static void clear(){for(var s:List.copyOf(ACTIVE.values()))cancel(s.owner);ACTIVE.clear();SUPPORT.clear();}
    public static void unload(net.minecraft.world.level.Level level){for(var s:List.copyOf(ACTIVE.values()))if(s.level==level)cancel(s.owner);SUPPORT.values().removeIf(v->v.level==level);}
}
