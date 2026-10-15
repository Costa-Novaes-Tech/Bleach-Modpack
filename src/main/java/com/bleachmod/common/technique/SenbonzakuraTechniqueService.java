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
        final Set<UUID> hits=new HashSet<>(); int cut=-1;
        Attack(Kind k,int s,long now,Vec3 o,Vec3 f) { kind=k;slot=s;start=now;origin=o;forward=f; }
    }
    private static final class Field {
        final Vec3 center; final long start; long bloom=-1; boolean opened;
        Field(Vec3 c,long now) { center=c;start=now; }
    }
    private static final class State {
        final ServerPlayer owner; final ServerLevel level; final String form; final long created;
        Attack attack; Field field; long wardUntil,sceneStart=-1; int budget; long budgetTick=Long.MIN_VALUE;
        State(ServerPlayer p,String form) { owner=p;level=p.serverLevel();this.form=form;created=level.getGameTime(); }
        void draw(int type,Vec3 o,Vec3 f,double radius,double progress,int pattern) {
            long now=level.getGameTime(); if (budgetTick!=now) { budgetTick=now;budget=0; }
            int count=type==AURA ? 16 : type==ARC ? 84 : 96;
            if (budget+count>SenbonPolicy.VISUAL_BUDGET || !level.hasChunkAt(net.minecraft.core.BlockPos.containing(o))) return;
            var packet=new SenbonPetalsS2C(type,o,f,(float)radius,(float)Math.max(0,Math.min(1,progress)),pattern,(int)(now*31+owner.getId()*997+type));
            if (!packet.valid()) return;
            budget+=count;
            for (ServerPlayer receiver:level.players()) if (receiver.position().distanceToSqr(o)<=64*64)
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
                && (form.equals("shikai") || form.equals("bankai")) && TransformationsHelper.isUnlocked(d,"zanpakuto",form);
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
            field.bloom=now; sync(p); return true;
        }
        if (bankai && slot==3 && scene(p)) {
            var def=TechniqueDefinitions.IKKA;
            if (now-s.sceneStart<def.readyTicks()) return fail(p,"scene_wait");
            if (!d.getResources().consumeReiatsu(def.cost())) return fail(p,"energy");
            s.sceneStart=-1;
            s.attack=new Attack(Kind.IKKA_ATTACK,3,now,p.position().add(0,1,0),SenbonGeometry.forward(p.getYRot()));
            sync(p);return true;
        }
        var definition=TechniqueDefinitions.require(CharacterData.SENBONZAKURA,s.form,slot);
        int cooldown=cooldown(d.getStatus(),slot);
        if (cooldown>0) { p.displayClientMessage(Component.translatable("message.bleachmod.technique.cooldown",(cooldown+19)/20),true);return false; }
        if ((!bankai && slot==4 && ward(p)) || (bankai && slot==2 && s.field!=null)) return false;
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
        if (d.getGuard().broken()) s.attack=null;
        if (s.sceneStart>=0 && !scene(p)) s.sceneStart=-1;
        if (s.attack!=null) tickAttack(s);
        if (s.field!=null) tickField(s);
        Vec3 f=SenbonGeometry.forward(p.getYRot());
        if (ward(p) && p.tickCount%3==0) s.draw(GUARD,p.position(),f,1.5,(now%20)/20.0,0);
        if (scene(p) && p.tickCount%4==0) s.draw(SWORDS,p.position(),f,5,(now%40)/80.0,0);
        if (now-s.created<12 && p.tickCount%3==0) s.draw(RADIAL,p.position(),f,3,(now-s.created+1)/12.0,0);
        if (s.form.equals("bankai") && p.tickCount%4==0) s.draw(AURA,p.position(),f,1,(now%40)/40.0,0);
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
                if (age%12==0) for (LivingEntity t:targets(s,a.origin,SenbonPolicy.STREAM_RANGE))
                    if (t.getBoundingBox().inflate(.45).clip(a.origin,a.origin.add(a.forward.scale(length))).isPresent()) hurt(s,t,SenbonPolicy.STREAM_DAMAGE,2);
            }
            case CONVERGENCE -> {
                if (age%2==0) s.draw(CONVERGE,a.origin,a.forward,3,Math.min(1,age/12.0),0);
                if (age>=12) { pulse(s,a.origin,3,SenbonPolicy.CONVERGENCE_DAMAGE,3);s.draw(RISE,a.origin,a.forward,3,1,0);s.attack=null; }
            }
            case IKKA_ATTACK -> {
                if (age%2==0) s.draw(SWORDS,a.origin,a.forward,5,Math.min(1,age/12.0),0);
                if (age>=12) { thrust(s,a,12,1.6,SenbonPolicy.IKKA_DAMAGE,3);s.draw(STREAM,a.origin,a.forward,12,1,0);s.attack=null; }
            }
            case HAKU -> {
                if (age%2==0) s.draw(WINGS,s.owner.position(),a.forward,4,Math.min(1,age/(double)SenbonPolicy.HAKU_WINDUP),0);
                if (age>=SenbonPolicy.HAKU_WINDUP) {
                    thrust(s,a,10,1.25,SenbonPolicy.HAKU_DAMAGE,4);s.draw(STREAM,a.origin,a.forward,10,1,0);s.attack=null;
                    s.level.playSound(null,s.owner.blockPosition(),SoundEvents.PLAYER_ATTACK_STRONG,s.owner.getSoundSource(),1,.6F);
                }
            }
        }
    }
    private static void thrust(State s,Attack a,double range,double width,float damage,int slot) {
        Vec3 end=a.origin.add(a.forward.scale(range));
        if (!loadedPath(s.level,a.origin,end)) return;
        var obstruction=s.level.clip(new ClipContext(a.origin,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,s.owner));
        end=obstruction.getLocation();
        for (LivingEntity t:targets(s,a.origin,range))
            if (t.getBoundingBox().inflate(width).clip(a.origin,end).isPresent()) hurt(s,t,damage,slot);
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
        if (field.bloom>=0) {
            int stage=(int)(now-field.bloom);
            if (stage%2==0) s.draw(FLOWER,field.center,f,SenbonPolicy.FIELD_RADIUS,stage/(double)SenbonPolicy.BLOOM_WINDUP,0);
            if (stage>=SenbonPolicy.BLOOM_WINDUP) { pulse(s,field.center,SenbonPolicy.FIELD_RADIUS,SenbonPolicy.FIELD_BLOOM_DAMAGE,2);s.draw(RISE,field.center,f,SenbonPolicy.FIELD_RADIUS,1,0);s.field=null;sync(s.owner); }
            return;
        }
        if (age>=TechniqueDefinitions.FLORAL_BLOOM.windowTicks()) { s.field=null;sync(s.owner);return; }
        if (age<6 && age%2==0) s.draw(GUARD,s.owner.position(),f,1.5,age/6.0,0);
        if (age>=6 && !field.opened) { pulse(s,field.center,SenbonPolicy.FIELD_RADIUS,SenbonPolicy.FIELD_OPEN_DAMAGE,2);field.opened=true; }
        if (age>=6 && age<18 && age%2==0) s.draw(RADIAL,field.center,f,SenbonPolicy.FIELD_RADIUS,(age-5)/12.0,0);
        else if (age>=18 && age%4==0) s.draw(FIELD,field.center,f,SenbonPolicy.FIELD_RADIUS,.5,0);
    }
    public static CompoundTag hud(ServerPlayer p) {
        var tag=new CompoundTag();State s=current(p);if(s==null)return tag;long now=s.level.getGameTime();
        if(s.field!=null) { tag.putInt("fieldReady",(int)Math.max(0,TechniqueDefinitions.FLORAL_BLOOM.readyTicks()-(now-s.field.start)));
            tag.putInt("fieldRemaining",(int)Math.max(0,TechniqueDefinitions.FLORAL_BLOOM.windowTicks()-(now-s.field.start)));tag.putBoolean("bloom",s.field.bloom>=0); }
        if(scene(p)) { tag.putInt("sceneReady",(int)Math.max(0,TechniqueDefinitions.IKKA.readyTicks()-(now-s.sceneStart)));tag.putInt("sceneRemaining",(int)(TechniqueDefinitions.IKKA.windowTicks()-(now-s.sceneStart))); }
        tag.putInt("ward",(int)Math.max(0,s.wardUntil-now)); return tag;
    }
    public static void cancel(ServerPlayer p) {
        ACTIVE.remove(p.getUUID());var reach=p.getAttribute(ForgeMod.ENTITY_REACH.get());if(reach!=null)reach.removeModifier(REACH_ID);
        PlayerCapability.get(p).ifPresent(d->{ if(d.getCharacter().getSenbonBlade()!=0) { d.getCharacter().setSenbonBlade(0);SyncHelper.appearance(p); } });
    }
    public static void clear() { for(State s:List.copyOf(ACTIVE.values()))cancel(s.owner);ACTIVE.clear(); }
    public static void unload(net.minecraft.world.level.Level level) { for(State s:List.copyOf(ACTIVE.values()))if(s.level==level)cancel(s.owner); }
}
