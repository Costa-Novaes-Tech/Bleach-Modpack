package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.evolution.TransformationsHelper;
import com.bleachmod.common.network.*;
import com.bleachmod.common.network.s2c.SenbonPetalsS2C;
import com.bleachmod.common.visual.SenbonGeometry;
import com.bleachmod.init.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ForgeMod;
import java.util.*;
import static com.bleachmod.common.network.s2c.SenbonPetalsS2C.*;

/** One bounded runtime per owner: an attack, a fixed field, a ward and a Senkei lease. */
public final class SenbonzakuraTechniqueService {
    private static final Map<UUID, State> ACTIVE=new HashMap<>();
    public static final UUID REACH_ID=UUID.fromString("8c0578b7-29a8-4475-96b9-e53d4b87fd44");
    private enum Kind { CUT, STREAM_ATTACK, CONVERGENCE, IKKA_ATTACK, HAKU }
    private static final class Attack {
        final Kind kind; final int slot; final long start; Vec3 origin, forward;
        final Set<UUID> hits=new HashSet<>(); int cut=-1,pulse=-1;
        int dashTicks; double travelled; long movedAt=Long.MIN_VALUE; Vec3 dashOrigin,lastPosition;
        Attack(Kind k,int s,long now,Vec3 o,Vec3 f) { kind=k;slot=s;start=now;origin=o;forward=f; }
    }
    private static final class Field {
        final Vec3 center; final long start; long bloom=-1; int bloomTicks=SenbonPolicy.BLOOM_WINDUP; boolean opened;
        Field(Vec3 c,long now) { center=c;start=now; }
    }
    private static final class Prison {
        final Vec3 center; final long start; final Set<UUID> targets=new HashSet<>();
        Prison(Vec3 center,long start) { this.center=center;this.start=start; }
    }
    private static final class State {
        final ServerPlayer owner; final ServerLevel level; final String form; final long created;
        Attack attack; Field field; Prison prison; long wardUntil,sceneStart=-1; int budget; long budgetTick=Long.MIN_VALUE;
        State(ServerPlayer p,String form) { owner=p;level=p.serverLevel();this.form=form;created=level.getGameTime(); }
        void draw(int type,Vec3 o,Vec3 f,double radius,double progress,int pattern) {
            long now=level.getGameTime(); if (budgetTick!=now) { budgetTick=now;budget=0; }
            int count=SenbonPetalsS2C.particleCount(type,pattern);
            if (budget+count>SenbonPolicy.VISUAL_BUDGET || !level.hasChunkAt(net.minecraft.core.BlockPos.containing(o))) return;
            var packet=new SenbonPetalsS2C(type,o,f,(float)radius,(float)Math.max(0,Math.min(1,progress)),pattern,(int)(now*31+owner.getId()*997+type),owner.getId());
            if (!packet.valid()) return;
            budget+=count;
            for (ServerPlayer receiver:level.players()) if (receiver.position().distanceToSqr(o)<=96*96)
                NetworkHandler.sendToPlayer(packet,receiver);
        }
    }
    private SenbonzakuraTechniqueService() { }
    public static boolean equipped(ServerPlayer p,PlayerData d) {
        return CharacterData.SENBONZAKURA.equals(d.getCharacter().getZanpakutoIdentity()) && p.getMainHandItem().is(ModItems.SENBONZAKURA.get());
    }
    private static boolean released(ServerPlayer p,PlayerData d) {
        String form=d.getCharacter().getActiveForm();
        return p.isAlive() && !p.isSpectator() && d.getStatus().hasCreatedCharacter() && equipped(p,d)
                && Reference.RACE_SHINIGAMI.equals(d.getCharacter().getRace())
                && Reference.GROUP_ZANPAKUTO.equals(d.getCharacter().getActiveFormGroup())
                && (form.equals("sealed") || form.equals("shikai") || form.equals("bankai")) && TransformationsHelper.isUnlocked(d,"zanpakuto",form);
    }
    private static State state(ServerPlayer p,PlayerData d) {
        State s=ACTIVE.get(p.getUUID());
        if (s!=null && (s.owner!=p || s.level!=p.serverLevel() || !s.form.equals(d.getCharacter().getActiveForm()))) { cancel(p);s=null; }
        if (s==null) { s=new State(p,d.getCharacter().getActiveForm());ACTIVE.put(p.getUUID(),s); }
        return s;
    }
    private static State current(ServerPlayer p) {
        State s=ACTIVE.get(p.getUUID()); return s!=null && s.owner==p && s.level==p.serverLevel() ? s : null;
    }
    public static boolean busy(ServerPlayer p) { State s=current(p);return s!=null && s.attack!=null; }
    public static boolean ward(ServerPlayer p) { State s=current(p);return s!=null && s.wardUntil>s.level.getGameTime(); }
    public static boolean scene(ServerPlayer p) {
        State s=current(p); return s!=null && s.sceneStart>=0 && s.level.getGameTime()-s.sceneStart<TechniqueDefinitions.IKKA.windowTicks();
    }
    public static float offensiveBonus(ServerPlayer p) {
        return scene(p) && PlayerCapability.get(p).map(d->released(p,d)).orElse(false) ? SenbonPolicy.SENKEI_BONUS : 0;
    }
    public static boolean allowMelee(ServerPlayer p, net.minecraft.world.entity.Entity target) {
        var reach=p.getAttribute(ForgeMod.ENTITY_REACH.get());
        if (reach!=null && reach.getModifier(REACH_ID)!=null && !PlayerCapability.get(p).map(d->released(p,d)
                && d.getCharacter().getActiveForm().equals("bankai")).orElse(false)) {
            cancel(p); return p.canReach(target,0);
        }
        return true;
    }
    public static float protection(ServerPlayer p, DamageSource source) {
        if (!ward(p) || p.isUsingItem() || !PlayerCapability.get(p).map(d->released(p,d)).orElse(false)
                || source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)
                || source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_SHIELD) || source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)) return 0;
        boolean physical=com.bleachmod.common.GuardService.blockable(source) || source.is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE);
        Vec3 point=source.getSourcePosition();
        return physical && point!=null && com.bleachmod.common.GuardPolicy.inFront(p.getYRot(),point.subtract(p.position()))
                ? SenbonPolicy.WARD_REDUCTION : 0;
    }
    public static boolean executeSlot(ServerPlayer p,PlayerData d,int slot) {
        if (!TechniqueProgression.authorize(p,d,slot) || !released(p,d) || p.isUsingItem() || d.getStatus().isActionCharging()) return false;
        State s=state(p,d); long now=s.level.getGameTime();
        if (s.attack!=null) return fail(p,"busy");
        boolean bankai=s.form.equals("bankai");
        if (bankai && slot==2 && s.field!=null) {
            Field field=s.field; long age=now-field.start; var def=TechniqueDefinitions.FLORAL_BLOOM;
            if (field.bloom>=0 || age<def.readyTicks() || age>=def.windowTicks()
                    || !SenbonGeometry.inside(field.center,p.position(),SenbonPolicy.FIELD_RADIUS,4)) return fail(p,"field_wait");
            if (!d.getResources().consumeReiatsu(def.cost())) return fail(p,"energy");
            field.bloom=now;
            field.bloomTicks=(int)Math.min(SenbonPolicy.BLOOM_WINDUP,def.windowTicks()-age-1);
            if(field.bloomTicks==0)finishField(s);
            SignatureAnimationService.play(p,"floral_bloom",Math.max(1,field.bloomTicks));
            sync(p); return true;
        }
        if (bankai && slot==3 && scene(p)) {
            var def=TechniqueDefinitions.IKKA;
            if (now-s.sceneStart<def.readyTicks()) return fail(p,"scene_wait");
            if (!d.getResources().consumeReiatsu(def.cost())) return fail(p,"energy");
            s.sceneStart=-1;
            s.attack=new Attack(Kind.IKKA_ATTACK,3,now,p.position().add(0,1,0),SenbonGeometry.forward(p.getYRot()));
            SignatureAnimationService.play(p,"ikka_senjinka");
            sync(p);return true;
        }
        var definition=TechniqueDefinitions.require(CharacterData.SENBONZAKURA,s.form,slot);
        int cooldown=cooldown(d.getStatus(),slot);
        if (cooldown>0) { p.displayClientMessage(Component.translatable("message.bleachmod.technique.cooldown",(cooldown+19)/20),true);return false; }
        if ((!bankai && slot==4 && ward(p)) || (bankai && slot==2 && s.field!=null)) return false;
        if(bankai && slot==4 && (p.isPassenger() || p.isSleeping() || p.isFallFlying()))return false;
        Vec3 convergence=!bankai && slot==3 ? aim(p,SenbonPolicy.CONVERGENCE_RANGE) : null;
        if (!bankai && slot==3 && convergence==null) return fail(p,"invalid_target");
        if (!d.getResources().consumeReiatsu(definition.cost())) {
            p.displayClientMessage(Component.translatable("message.bleachmod.technique.no_reiatsu",(int)definition.cost()),true);return false;
        }
        setCooldown(d.getStatus(),slot,definition.cooldownTicks());
        Vec3 f=SenbonGeometry.forward(p.getYRot()),o=p.position().add(0,1.2,0);
        if (slot==1) s.attack=new Attack(Kind.CUT,slot,now,o,f);
        else if (!bankai && slot==2) s.attack=new Attack(Kind.STREAM_ATTACK,slot,now,o,p.getLookAngle().normalize());
        else if (!bankai && slot==3) s.attack=new Attack(Kind.CONVERGENCE,slot,now,convergence,f);
        else if (!bankai) { s.wardUntil=now+SenbonPolicy.WARD_TICKS;d.getGuard().release(); }
        else if (slot==2) s.field=new Field(p.position(),now);
        else if (slot==3) { s.sceneStart=now;s.wardUntil=0; }
        else { s.sceneStart=-1;s.wardUntil=0;s.attack=new Attack(Kind.HAKU,slot,now,o,f); }
        p.serverLevel().playSound(null,p.blockPosition(),SoundEvents.AMETHYST_BLOCK_CHIME,p.getSoundSource(),.6F,bankai?.7F:1.3F);
        SignatureAnimationService.play(p,definition.id());
        sync(p); return true;
    }
    private static boolean fail(ServerPlayer p,String key) { p.displayClientMessage(Component.translatable("message.bleachmod.senbonzakura."+key),true);return false; }
    private static void sync(ServerPlayer p) { SyncHelper.resources(p); }
    private static Vec3 aim(ServerPlayer p,double range) {
        Vec3 eye=p.getEyePosition(),end=eye.add(p.getLookAngle().scale(range));
        if (!loadedPath(p.serverLevel(),eye,end) || !p.level().getWorldBorder().isWithinBounds(net.minecraft.core.BlockPos.containing(end))) return null;
        var hit=p.level().clip(new ClipContext(eye,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,p));
        return hit.getType()==HitResult.Type.MISS ? end : hit.getLocation().add(hit.getDirection().getStepX()*.1,hit.getDirection().getStepY()*.1,hit.getDirection().getStepZ()*.1);
    }
    private static boolean loadedPath(ServerLevel level,Vec3 a,Vec3 b) {
        return level.hasChunksAt(net.minecraft.core.BlockPos.containing(Math.min(a.x,b.x),Math.min(a.y,b.y),Math.min(a.z,b.z)),
                net.minecraft.core.BlockPos.containing(Math.max(a.x,b.x),Math.max(a.y,b.y),Math.max(a.z,b.z)));
    }
    private static int cooldown(StatusData s,int slot) { return switch(slot) { case 1->s.getTechniqueSlot1CooldownTicks();case 2->s.getTechniqueSlot2CooldownTicks();case 3->s.getTechniqueSlot3CooldownTicks();default->s.getTechniqueSlot4CooldownTicks(); }; }
    private static void setCooldown(StatusData s,int slot,int ticks) { switch(slot) { case 1->s.setTechniqueSlot1CooldownTicks(ticks);case 2->s.setTechniqueSlot2CooldownTicks(ticks);case 3->s.setTechniqueSlot3CooldownTicks(ticks);default->s.setTechniqueSlot4CooldownTicks(ticks); } }
    public static void tick(ServerPlayer p,PlayerData d) {
        if (!released(p,d)) { cancel(p);return; }
        State s=state(p,d); long now=s.level.getGameTime();
        var reach=p.getAttribute(ForgeMod.ENTITY_REACH.get());
        if (reach!=null) {
            if (s.form.equals("bankai") && reach.getModifier(REACH_ID)==null) reach.addTransientModifier(new AttributeModifier(REACH_ID,"Senbonzakura Bankai reach",SenbonPolicy.REACH_BONUS,AttributeModifier.Operation.ADDITION));
            else if (!s.form.equals("bankai")) reach.removeModifier(REACH_ID);
        }
        if (d.getGuard().broken())stopAttack(s);
        if (s.sceneStart>=0 && !scene(p)) s.sceneStart=-1;
        if (s.attack!=null) tickAttack(s);
        if (s.prison!=null) tickPrison(s);
        if (s.field!=null) tickField(s);
        Vec3 f=SenbonGeometry.forward(p.getYRot());
        if (ward(p) && p.tickCount%5==0) s.draw(GUARD,p.position(),f,SenbonPolicy.WARD_RADIUS,(now%40)/40.0,0);
        if (scene(p) && p.tickCount%4==0) s.draw(SWORDS,p.position(),f,SenbonPolicy.SCENE_RADIUS,.2,0);
        if (!s.form.equals("sealed") && now-s.created<12 && p.tickCount%4==0) s.draw(RADIAL,p.position(),f,3,(now-s.created+1)/12.0,0);
        if (s.form.equals("bankai") && p.tickCount%8==0) s.draw(AURA,p.position(),f,1,(now%80)/80.0,0);
        int blade=s.attack!=null && s.attack.kind==Kind.HAKU ? 2 : scene(p) ? 1 : 0;
        if (d.getCharacter().getSenbonBlade()!=blade) { d.getCharacter().setSenbonBlade(blade);SyncHelper.appearance(p); }
    }
    private static List<LivingEntity> targets(State s,Vec3 center,double radius) {
        return s.level.getEntitiesOfClass(LivingEntity.class,new AABB(center,center).inflate(radius),
                e->TechniqueTargets.allowed(s.owner,e) && s.owner.hasLineOfSight(e)).stream().limit(SenbonPolicy.MAX_TARGETS).toList();
    }
    private static void hurt(State s,LivingEntity target,float amount,int slot) {
        MasteryPracticeService.hurt(s.owner,target,s.owner.damageSources().playerAttack(s.owner),amount,slot);
    }
    private static boolean intersectsCut(Attack a,LivingEntity target,int pattern,double previous,double progress) {
        AABB box=target.getBoundingBox().inflate(.45);
        for (double radius=1;radius<=SenbonPolicy.CUT_RANGE;radius+=.5) {
            Vec3 from=SenbonGeometry.cut(a.origin,a.forward,pattern,previous,radius),to=SenbonGeometry.cut(a.origin,a.forward,pattern,progress,radius);
            if (box.contains(from) || box.contains(to) || box.clip(from,to).isPresent()) return true;
        }
        return false;
    }
    private static void tickAttack(State s) {
        Attack a=s.attack; int age=(int)(s.level.getGameTime()-a.start); boolean bankai=s.form.equals("bankai");
        switch(a.kind) {
            case CUT -> {
                int cut=bankai ? age/SenbonGeometry.CUT_SPACING : 0,step=bankai ? age%SenbonGeometry.CUT_SPACING : age;
                if (cut>=(bankai?5:1)) { s.attack=null;return; }
                if (step>=SenbonGeometry.CUT_TICKS) { if (!bankai) s.attack=null;return; }
                if (a.cut!=cut) { a.cut=cut;a.hits.clear();a.origin=s.owner.position().add(0,1.2,0);a.forward=SenbonGeometry.forward(s.owner.getYRot());
                    s.level.playSound(null,s.owner.blockPosition(),SoundEvents.PLAYER_ATTACK_SWEEP,s.owner.getSoundSource(),.8F,1.1F-cut*.08F); }
                double previous=(double)step/SenbonGeometry.CUT_TICKS,progress=(step+1.0)/SenbonGeometry.CUT_TICKS;
                s.draw(ARC,a.origin,a.forward,SenbonPolicy.CUT_RANGE,progress,cut);
                for (LivingEntity t:targets(s,a.origin,SenbonPolicy.CUT_RANGE+1))
                    if (!a.hits.contains(t.getUUID()) && intersectsCut(a,t,cut,previous,progress)) {
                        a.hits.add(t.getUUID());hurt(s,t,bankai?SenbonPolicy.BANKAI_CUT_DAMAGE:SenbonPolicy.CUT_DAMAGE,1);
                    }
                if ((!bankai || cut==4) && step==SenbonGeometry.CUT_TICKS-1) s.attack=null;
            }
            case STREAM_ATTACK -> {
                if (age>24) { s.attack=null;return; }
                a.origin=s.owner.getEyePosition().add(0,-.2,0);a.forward=s.owner.getLookAngle().normalize();
                if (!loadedPath(s.level,a.origin,a.origin.add(a.forward.scale(SenbonPolicy.STREAM_RANGE)))) { s.attack=null;return; }
                var hit=s.level.clip(new ClipContext(a.origin,a.origin.add(a.forward.scale(SenbonPolicy.STREAM_RANGE)),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,s.owner));
                double length=hit.getLocation().distanceTo(a.origin);
                if (length>.1 && age%2==0) s.draw(STREAM,a.origin,a.forward,length,age/24.0,0);
                // A packet can arrive after this player's tick. The first processed tick still owns pulse zero.
                int pulse=age/12;
                if (a.pulse!=pulse) {
                    a.pulse=pulse;
                    for (LivingEntity t:targets(s,a.origin,SenbonPolicy.STREAM_RANGE))
                        if (t.getBoundingBox().inflate(.45).clip(a.origin,a.origin.add(a.forward.scale(length))).isPresent()) hurt(s,t,SenbonPolicy.STREAM_DAMAGE,2);
                }
            }
            case CONVERGENCE -> {
                if (age%3==0) s.draw(CONVERGE,a.origin,a.forward,SenbonPolicy.CONVERGENCE_RADIUS,Math.min(1,age/12.0),0);
                if (age>=12) {
                    Prison prison=new Prison(a.origin,s.level.getGameTime());
                    for(var t:targets(s,a.origin,SenbonPolicy.CONVERGENCE_RADIUS+2))
                        if (SenbonGeometry.inside(a.origin,t.position(),SenbonPolicy.CONVERGENCE_RADIUS,3) && clearLine(s,a.origin,t)) prison.targets.add(t.getUUID());
                    s.prison=prison;s.attack=null;
                }
            }
            case IKKA_ATTACK -> {
                if (age%2==0) s.draw(SWORDS,a.origin,a.forward,SenbonPolicy.IKKA_RANGE,Math.min(1,age/12.0),2);
                if (age>=12) {
                    pulse(s,a.origin,SenbonPolicy.IKKA_RANGE,SenbonPolicy.IKKA_DAMAGE,3);
                    s.draw(RADIAL,a.origin,a.forward,SenbonPolicy.IKKA_RANGE,1,1);s.attack=null;
                }
            }
            case HAKU -> {
                a.origin=s.owner.position().add(0,1.2,0);
                if(age<SenbonPolicy.HAKU_WINDUP) {
                    a.forward=SenbonGeometry.forward(s.owner.getYRot());
                    if(age%3==0)s.draw(WINGS,s.owner.position(),a.forward,4,age/(double)SenbonPolicy.HAKU_WINDUP,0);
                } else tickHakuDash(s,a);
            }
        }
    }
    private static void tickHakuDash(State s,Attack a) {
        ServerPlayer p=s.owner;long now=s.level.getGameTime();
        if(a.movedAt==now)return;
        a.movedAt=now;
        if(p.isPassenger() || p.isSleeping() || p.isFallFlying()
                || (a.lastPosition!=null && p.position().distanceToSqr(a.lastPosition)>9)) { stopAttack(s);return; }
        if(a.dashOrigin==null) {
            a.dashOrigin=p.position();a.forward=SenbonGeometry.forward(p.getYRot());
            s.level.playSound(null,p.blockPosition(),SoundEvents.PLAYER_ATTACK_STRONG,p.getSoundSource(),1,.6F);
        }
        boolean blocked=false;int steps=(int)Math.ceil(SenbonPolicy.HAKU_STEP/SenbonPolicy.HAKU_SUBSTEP);
        double distance=Math.min(SenbonPolicy.HAKU_STEP,SenbonPolicy.HAKU_RANGE-a.travelled);
        for(int i=0;i<steps && distance>1e-6;i++) {
            Vec3 before=p.position(),motion=a.forward.scale(Math.min(SenbonPolicy.HAKU_SUBSTEP,distance));
            AABB sweep=p.getBoundingBox().expandTowards(motion);
            if(!s.level.hasChunksAt(net.minecraft.core.BlockPos.containing(sweep.minX,sweep.minY,sweep.minZ),net.minecraft.core.BlockPos.containing(sweep.maxX,sweep.maxY,sweep.maxZ))
                    || !s.level.getWorldBorder().isWithinBounds(sweep) || !s.level.noCollision(p,sweep)) { blocked=true;break; }
            p.move(net.minecraft.world.entity.MoverType.SELF,motion);
            double moved=p.position().distanceTo(before);a.travelled+=moved;distance-=moved;
            dashHits(s,a,before.add(0,1,0),p.position().add(0,1,0));
            if(moved<motion.length()-.001) { blocked=true;break; }
        }
        a.lastPosition=p.position();a.dashTicks++;
        p.setSprinting(false);stopHorizontal(p);
        // Vanilla owner correction also updates the connection's accepted position; clients never supply the endpoint.
        p.connection.teleport(p.getX(),p.getY(),p.getZ(),p.getYRot(),p.getXRot());
        if(a.travelled>.01) {
            if(a.dashTicks%2==1)s.draw(WINGS,p.position(),a.forward,4,1,1);
            s.draw(LANCE,a.dashOrigin.add(0,1.2,0),a.forward,a.travelled,1,0);
        }
        if(blocked || a.dashTicks>=SenbonPolicy.HAKU_DASH_TICKS || a.travelled>=SenbonPolicy.HAKU_RANGE-.001)stopAttack(s);
    }
    private static void dashHits(State s,Attack a,Vec3 from,Vec3 to) {
        if(a.hits.size()>=SenbonPolicy.MAX_TARGETS)return;
        for(var t:s.level.getEntitiesOfClass(LivingEntity.class,new AABB(from,to).inflate(1.6),e->TechniqueTargets.allowed(s.owner,e))) {
            if(a.hits.size()>=SenbonPolicy.MAX_TARGETS)break;
            double projection=t.position().subtract(a.dashOrigin).dot(a.forward);
            if(projection<0 || projection>SenbonPolicy.HAKU_RANGE || a.hits.contains(t.getUUID()) || !s.owner.hasLineOfSight(t) || !clearLine(s,from,t))continue;
            var box=t.getBoundingBox().inflate(1.25,.4,1.25);
            if(box.contains(from) || box.contains(to) || box.clip(from,to).isPresent()) {
                a.hits.add(t.getUUID());hurt(s,t,SenbonPolicy.HAKU_DAMAGE,4);
            }
        }
    }
    private static void stopHorizontal(ServerPlayer p) {
        p.setDeltaMovement(0,p.getDeltaMovement().y,0);p.hurtMarked=true;
    }
    private static void stopAttack(State s) {
        if(s.attack!=null && s.attack.kind==Kind.HAKU && s.attack.dashOrigin!=null){stopHorizontal(s.owner);s.draw(RADIAL,s.owner.position().add(0,1,0),s.attack.forward,SenbonPolicy.CUT_RANGE,1.0,2);}
        s.attack=null;
    }
    private static boolean clearLine(State s,Vec3 center,LivingEntity target) {
        Vec3 start=center.add(0,.3,0),end=target.getBoundingBox().getCenter();
        return loadedPath(s.level,start,end) && s.level.clip(new ClipContext(start,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,s.owner)).getType()==HitResult.Type.MISS;
    }
    private static void tickPrison(State s) {
        Prison prison=s.prison;int age=(int)(s.level.getGameTime()-prison.start);
        if(age>=SenbonPolicy.PRISON_TICKS) { s.prison=null;return; }
        int drawn=0;
        for(var id:List.copyOf(prison.targets)) {
            var entity=s.level.getEntity(id);
            if(!(entity instanceof LivingEntity t) || !TechniqueTargets.allowed(s.owner,t) || !s.owner.hasLineOfSight(t)
                    || !SenbonGeometry.inside(prison.center,t.position(),SenbonPolicy.CONVERGENCE_RADIUS+1,6) || !clearLine(s,prison.center,t)) { prison.targets.remove(id);continue; }
            if(age%4==0) {
                t.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION,SenbonPolicy.CONTROL_LEASE,0,false,false,true));
                t.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN,SenbonPolicy.CONTROL_LEASE,4,false,false,true));
                Vec3 delta=prison.center.subtract(t.position()).multiply(1,0,1);
                if(delta.lengthSqr()>.36) { Vec3 pull=delta.normalize().scale(.1);t.setDeltaMovement(pull.x,t.getDeltaMovement().y,pull.z);t.hurtMarked=true; }
                if(drawn++<3)s.draw(CONVERGE,t.position(),SenbonGeometry.forward(s.owner.getYRot()),1.3,age/(double)SenbonPolicy.PRISON_TICKS,1);
            }
            if(age%SenbonPolicy.PRISON_PULSE==0)hurt(s,t,SenbonPolicy.CONVERGENCE_DAMAGE,3);
        }
        if(prison.targets.isEmpty())s.prison=null;
    }
    private static void pulse(State s,Vec3 center,double radius,float damage,int slot) {
        for (LivingEntity t:targets(s,center,radius+2)) {
            if (!SenbonGeometry.inside(center,t.position(),radius,3)) continue;
            Vec3 start=center.add(0,1,0),end=t.getBoundingBox().getCenter();
            if (!loadedPath(s.level,start,end)) continue;
            if (s.level.clip(new ClipContext(start,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,s.owner)).getType()==HitResult.Type.MISS) hurt(s,t,damage,slot);
        }
    }
    private static void tickField(State s) {
        Field field=s.field;long now=s.level.getGameTime();int age=(int)(now-field.start);Vec3 f=SenbonGeometry.forward(s.owner.getYRot());
        if (age>=TechniqueDefinitions.FLORAL_BLOOM.windowTicks()) { s.field=null;sync(s.owner);return; }
        if (field.bloom>=0) {
            int stage=(int)(now-field.bloom);
            if(stage>=field.bloomTicks) { finishField(s);sync(s.owner);return; }
            if (stage%2==0) { s.draw(FIELD,field.center,f,SenbonPolicy.FIELD_RADIUS,stage/(double)field.bloomTicks,3);s.draw(FLOWER,field.center,f,SenbonPolicy.FIELD_RADIUS,stage/(double)field.bloomTicks,3); }
            return;
        }
        if (age<6 && age%2==0) s.draw(GUARD,s.owner.position(),f,1.5,age/6.0,0);
        if (age>=6 && !field.opened) { pulse(s,field.center,SenbonPolicy.FIELD_RADIUS,SenbonPolicy.FIELD_OPEN_DAMAGE,2);field.opened=true; }
        if (age>=6 && age<18 && age%2==0) s.draw(RADIAL,field.center,f,SenbonPolicy.FIELD_RADIUS,(age-5)/12.0,2);
        else if (age>=18 && age%2==0) s.draw(FIELD,field.center,f,SenbonPolicy.FIELD_RADIUS,.5,0);
    }
    private static void finishField(State s) {
        Field field=s.field;if(field==null)return;
        var f=SenbonGeometry.forward(s.owner.getYRot());
        s.draw(FLOWER,field.center,f,SenbonPolicy.FIELD_RADIUS,1,4);
        pulse(s,field.center,SenbonPolicy.FIELD_RADIUS,SenbonPolicy.FIELD_BLOOM_DAMAGE,2);
        s.draw(RISE,field.center,f,SenbonPolicy.FIELD_RADIUS,1,2);s.field=null;
    }
    public static CompoundTag hud(ServerPlayer p) {
        var tag=new CompoundTag();State s=current(p);if(s==null)return tag;long now=s.level.getGameTime();
        if(s.prison!=null)tag.putInt("prison",(int)Math.max(0,SenbonPolicy.PRISON_TICKS-(now-s.prison.start)));
        if(s.field!=null) { tag.putInt("fieldReady",(int)Math.max(0,TechniqueDefinitions.FLORAL_BLOOM.readyTicks()-(now-s.field.start)));
            tag.putInt("fieldRemaining",(int)Math.max(0,TechniqueDefinitions.FLORAL_BLOOM.windowTicks()-(now-s.field.start)));tag.putBoolean("bloom",s.field.bloom>=0); }
        if(scene(p)) { tag.putInt("sceneReady",(int)Math.max(0,TechniqueDefinitions.IKKA.readyTicks()-(now-s.sceneStart)));tag.putInt("sceneRemaining",(int)(TechniqueDefinitions.IKKA.windowTicks()-(now-s.sceneStart))); }
        tag.putInt("ward",(int)Math.max(0,s.wardUntil-now)); return tag;
    }
    public static void cancel(ServerPlayer p) {
        SignatureAnimationService.cancel(p,CharacterData.SENBONZAKURA);
        State s=ACTIVE.remove(p.getUUID());if(s!=null)stopAttack(s);
        var reach=p.getAttribute(ForgeMod.ENTITY_REACH.get());if(reach!=null)reach.removeModifier(REACH_ID);
        PlayerCapability.get(p).ifPresent(d->{ if(d.getCharacter().getSenbonBlade()!=0) { d.getCharacter().setSenbonBlade(0);SyncHelper.appearance(p); } });
    }
    public static void clear() { for(State s:List.copyOf(ACTIVE.values()))cancel(s.owner);ACTIVE.clear(); }
    public static void unload(net.minecraft.world.level.Level level) { for(State s:List.copyOf(ACTIVE.values()))if(s.level==level)cancel(s.owner); }
}
