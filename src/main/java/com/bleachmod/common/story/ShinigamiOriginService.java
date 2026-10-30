package com.bleachmod.common.story;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.*;
import com.bleachmod.common.network.s2c.OriginSceneS2C;
import com.bleachmod.common.quest.*;
import com.bleachmod.common.visual.OriginTimeline;
import com.bleachmod.entity.*;
import com.bleachmod.init.ModItems;
import com.bleachmod.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** Server-owned first encounter. Never installs a house, changes terrain, or advances a world era. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class ShinigamiOriginService {
    public static final String QUEST = "shinigami_awakening", OWNER = "OriginOwner", TEMP = "OriginTemporary";
    private record Scene(ServerLevel level, ServerPlayer player, QuestNpcEntity rukia, HollowBossEntity boss,
                         List<Entity> props, Vec3 anchor, Vec3 forward, float yaw, long start, int originalSlot) {
        Vec3 point(double x, double y, double z) { return anchor.add(new Vec3(forward.z, 0, -forward.x).scale(x)).add(0,y,0).add(forward.scale(z)); }
        OriginSceneS2C packet(boolean stop) { return new OriginSceneS2C(player.getId(), rukia.getId(), boss.getId(), start, anchor, forward, stop); }
    }
    private static final Map<UUID, Scene> SCENES = new HashMap<>();
    private static final Map<UUID,Integer> MISSING = new HashMap<>();
    private ShinigamiOriginService() { }
    public static boolean locked(net.minecraft.world.entity.player.Player player) { return SCENES.containsKey(player.getUUID()); }

    public static void request(ServerPlayer player, PlayerData data) {
        data.getOrigin().request();
        giveBook(player);
        SyncHelper.full(player);
    }
    public static void giveBook(ServerPlayer player) {
        if (!player.getInventory().contains(new ItemStack(ModItems.STORY_JOURNAL.get()))) {
            var book = new ItemStack(ModItems.STORY_JOURNAL.get());
            if (!player.getInventory().add(book)) player.drop(book, false);
        }
    }

    /** Both participants and the entire short rush corridor must fit; failed setup never locks the player. */
    public static boolean canStage(ServerLevel level, Vec3 anchor, Vec3 forward) {
        if (!level.getWorldBorder().isWithinBounds(BlockPos.containing(anchor))) return false;
        Vec3 side=new Vec3(forward.z,0,-forward.x);
        for (Vec3 local : List.of(Vec3.ZERO,new Vec3(0,0,1.1),new Vec3(0,0,3),new Vec3(-3.8,0,.8),new Vec3(-7,0,.8),new Vec3(-10,0,.8),new Vec3(-14,0,.8))) {
            Vec3 p = anchor.add(forward.scale(local.z)).add(side.scale(local.x));
            if (!level.hasChunkAt(BlockPos.containing(p)) || !level.getWorldBorder().isWithinBounds(BlockPos.containing(p))) return false;
            double radius = local.x < -3 ? 1.5 : .5, height = local.x < -3 ? 5.6 : 2.1;
            if (!level.noCollision(new AABB(p.x-radius,p.y+.05,p.z-radius,p.x+radius,p.y+height,p.z+radius))) return false;
            if (level.getBlockState(BlockPos.containing(p.add(0,-.1,0))).getCollisionShape(level, BlockPos.containing(p.add(0,-.1,0))).isEmpty()) return false;
        }
        return true;
    }

    public static boolean start(ServerPlayer player, boolean cinematic) {
        var data = PlayerCapability.get(player).orElse(null);
        if (data == null || !player.isAlive() || player.isPassenger() || !player.onGround()
                || player.level().dimension() != Level.OVERWORLD || locked(player) || QuestRegistry.getQuest(QUEST) == null) return false;
        if (cinematic && data.getOrigin().stage() != OriginData.Stage.PENDING
                || !cinematic && data.getOrigin().stage() != OriginData.Stage.RETRY) return false;
        Vec3 f = null;
        for (int i = 0; i < 8; i++) {
            Vec3 direction = Vec3.directionFromRotation(0, player.getYRot() + i * 45);
            if (canStage(player.serverLevel(), player.position(), direction)
                    && SCENES.values().stream().noneMatch(s->s.level==player.level()&&s.anchor.distanceToSqr(player.position())<900)) { f = direction; break; }
        }
        if (f == null) return false;
        var level = player.serverLevel();
        var boss = ModEntities.HOLLOW_BOSS.get().create(level);
        var rukia = ModEntities.NPC_RUKIA.get().create(level);
        if (boss == null || rukia == null) return false;
        float yaw = (float)Math.toDegrees(Math.atan2(-f.x, f.z));
        var scene = new Scene(level,player,rukia,boss,new ArrayList<>(),player.position(),f,yaw,level.getGameTime(),player.getInventory().selected);
        Vec3 initial=cinematic?OriginTimeline.bossOffset(0):OriginTimeline.bossOffset(OriginTimeline.END);
        boss.moveTo(scene.point(initial.x,0,initial.z).x, scene.anchor.y,
                scene.point(initial.x,0,initial.z).z, yaw-90,0);
        boss.finishEntrance(); boss.setPersistenceRequired(); boss.setNoAi(cinematic);
        boss.getPersistentData().putUUID(OWNER,player.getUUID());
        boss.getPersistentData().putBoolean(TEMP,cinematic);
        boss.getPersistentData().putString(Reference.TAG_QUEST_KEY,QUEST);
        boss.getPersistentData().putInt(Reference.TAG_QUEST_OBJECTIVE_INDEX,0);
        boss.getPersistentData().putUUID(Reference.TAG_QUEST_OWNER,player.getUUID());
        if (!level.addFreshEntity(boss)) return false;
        if (!cinematic) {
            rukia.discard(); boss.setOriginArmLost(true); beginFight(scene,data); return true;
        }
        Vec3 rp=scene.point(0,0,OriginTimeline.rukiaDistance(0)); rukia.moveTo(rp.x,rp.y,rp.z,yaw+180,0);
        rukia.setNoAi(true); rukia.setCustomNameVisible(false);
        rukia.setPose(Pose.SITTING);
        rukia.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(ModItems.ASAUCHI.get()));
        rukia.getPersistentData().putBoolean(TEMP,true);
        rukia.getPersistentData().putUUID(OWNER,player.getUUID());
        if (!level.addFreshEntity(rukia)) { boss.discard(); return false; }
        // Three non-colliding visual post pieces; never overwrite world blocks.
        for (int y=0;y<3;y++) {
            var post=EntityType.BLOCK_DISPLAY.create(level);
            if(post==null) continue;
            Vec3 p=scene.point(0,y,3.22).add(-.5,0,-.5); post.setPos(p);
            var postData=post.saveWithoutId(new net.minecraft.nbt.CompoundTag());
            postData.put("block_state",net.minecraft.nbt.NbtUtils.writeBlockState(Blocks.DARK_OAK_FENCE.defaultBlockState()));post.load(postData);
            post.getPersistentData().putBoolean(TEMP,true); if(level.addFreshEntity(post)) scene.props.add(post);
        }
        data.getOrigin().scene(); SCENES.put(player.getUUID(),scene);
        data.getGuard().release();data.getStatus().stopDodge();data.getStatus().setActionCharging(false);
        data.getStatus().setOriginLocked(true);
        for(int i=0;i<9;i++)if(player.getInventory().getItem(i).isEmpty()) { selectSlot(player,i);break; }
        player.connection.teleport(scene.anchor.x,scene.anchor.y,scene.anchor.z,yaw,0);
        player.setDeltaMovement(Vec3.ZERO);
        NetworkHandler.sendToTrackingAndSelf(scene.packet(false),player); SyncHelper.full(player);
        return true;
    }

    private static void beginFight(Scene s, PlayerData data) {
        data.getStatus().setOriginLocked(false);
        var quest=QuestRegistry.getQuest(QUEST);
        if (quest == null) {
            s.boss.discard();s.rukia.discard();s.props.forEach(Entity::discard);
            data.getOrigin().retry();SyncHelper.full(s.player);return;
        }
        if (!data.getPlayerQuestData().isCompleted(QUEST)) data.getPlayerQuestData().acceptQuest(QUEST,quest);
        data.getOrigin().fight(s.boss.getUUID());
        data.getOrigin().rememberBoss(s.boss.blockPosition());
        s.boss.getPersistentData().putBoolean(TEMP,false);
        s.boss.setOriginRushing(false); s.boss.setNoAi(false); s.boss.setTarget(s.player);
        if(!s.rukia.isRemoved()) {
            var props=new net.minecraft.nbt.ListTag();props.add(net.minecraft.nbt.NbtUtils.createUUID(s.rukia.getUUID()));
            s.props.forEach(e->props.add(net.minecraft.nbt.NbtUtils.createUUID(e.getUUID())));
            s.boss.getPersistentData().put("OriginProps",props);
        }
        giveBlade(s.player);
        SyncHelper.full(s.player);
        s.player.displayClientMessage(Component.translatable("story.bleachmod.origin.fight"),true);
    }
    private static void giveBlade(ServerPlayer player) {
        var sword=new ItemStack(ModItems.ASAUCHI.get());
        if(!player.getInventory().contains(sword)) { if(!player.getInventory().add(sword)) player.drop(sword,false); }
        for(int i=0;i<9;i++) if(player.getInventory().getItem(i).is(ModItems.ASAUCHI.get())) { selectSlot(player,i); break; }
    }
    private static void selectSlot(ServerPlayer player,int slot) {
        player.getInventory().selected=slot;
        player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket(slot));
    }

    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e) {
        if(e.phase!=TickEvent.Phase.END || !(e.player instanceof ServerPlayer p)) return;
        var data=PlayerCapability.get(p).orElse(null); if(data==null) return;
        Scene s=SCENES.get(p.getUUID());
        if(s==null) {
            data.getStatus().setOriginLocked(false);
            if(data.getOrigin().stage()==OriginData.Stage.PENDING && p.tickCount%40==0) {
                if(!start(p,true) && p.tickCount%200==0) p.displayClientMessage(Component.translatable("story.bleachmod.origin.space"),true);
            }
            if(data.getOrigin().stage()==OriginData.Stage.FIGHT && p.tickCount%20==0) {
                var entity=data.getOrigin().boss()==null?null:p.serverLevel().getEntity(data.getOrigin().boss());
                if(entity instanceof HollowBossEntity b) {
                    MISSING.remove(p.getUUID());
                    if(b.isAlive()) { b.setTarget(p);data.getOrigin().rememberBoss(b.blockPosition()); }
                } else {
                    var at=data.getOrigin().bossPosition();
                    // Ten seconds of absence in a FULL loaded chunk distinguishes removal from an unload.
                    if(at!=null&&p.serverLevel().getChunkSource().getChunkNow(at.getX()>>4,at.getZ()>>4)!=null) {
                        if(MISSING.merge(p.getUUID(),20,Integer::sum)>=200) {
                            MISSING.remove(p.getUUID());data.getOrigin().retry();SyncHelper.full(p);
                        }
                    } else MISSING.remove(p.getUUID());
                }
            }
            return;
        }
        if(!p.isAlive() || p.level()!=s.level || !s.rukia.isAlive() || !s.boss.isAlive()) { cancel(p); return; }
        int t=(int)(s.level.getGameTime()-s.start);
        data.getStatus().setOriginLocked(true);
        p.setDeltaMovement(Vec3.ZERO); p.fallDistance=0;
        Vec3 playerMark=s.point(0,0,OriginTimeline.playerAdvance(t));float actorYaw=s.yaw+OriginTimeline.playerTurn(t);
        if(p.position().distanceToSqr(playerMark)>.0001 || Math.abs(p.getYRot()-actorYaw)>.1 || p.getXRot()!=0)
            p.connection.teleport(playerMark.x,playerMark.y,playerMark.z,actorYaw,0);
        s.rukia.setPos(s.point(0,0,OriginTimeline.rukiaDistance(t)));
        s.rukia.setYRot(s.yaw+180); s.rukia.setYBodyRot(s.yaw+180); s.rukia.setYHeadRot(s.yaw+180);
        Vec3 bossAt=OriginTimeline.bossOffset(t);s.boss.setPos(s.point(bossAt.x,0,bossAt.z));
        s.boss.setYRot(s.yaw-90); s.boss.setYBodyRot(s.yaw-90); s.boss.setYHeadRot(s.yaw-90);
        s.boss.setOriginRushing(t>=180 && t<222);
        if(t==OriginTimeline.THRUST) s.level.playSound(null,p.blockPosition(),SoundEvents.PLAYER_ATTACK_SWEEP,SoundSource.PLAYERS,1,.7F);
        if(t==OriginTimeline.BURST) {
            giveBlade(p);
            s.rukia.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);
            s.level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,p.getX(),p.getY()+1,p.getZ(),320,1,1.3,1,.18);
            s.level.sendParticles(ParticleTypes.END_ROD,p.getX(),p.getY()+1,p.getZ(),160,.7,1.2,.7,.25);
            s.level.playSound(null,p.blockPosition(),SoundEvents.GENERIC_EXPLODE,SoundSource.PLAYERS,2.2F,.65F);
            s.level.playSound(null,p.blockPosition(),SoundEvents.BEACON_ACTIVATE,SoundSource.PLAYERS,1.6F,.7F);
        }
        if(t==OriginTimeline.CUT) {
            s.boss.setOriginArmLost(true);
            Vec3 at=s.boss.position().add(s.forward.scale(-1.5)).add(0,3.8,0);
            for(int i=0;i<=12;i++) {
                Vec3 slash=p.position().add(0,1.2,0).lerp(at,i/12.0);
                s.level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,slash.x,slash.y,slash.z,3,.05,.05,.05,.005);
            }
            Vec3 sweep=s.point(0,1.5,2.2);s.level.sendParticles(ParticleTypes.SWEEP_ATTACK,sweep.x,sweep.y,sweep.z,1,0,0,0,0);
            s.level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,at.x,at.y,at.z,75,.4,.8,.4,.09);
            s.level.playSound(null,s.boss.blockPosition(),SoundEvents.PLAYER_ATTACK_CRIT,SoundSource.PLAYERS,1.8F,.6F);
        }
        if(t>=OriginTimeline.END) {
            SCENES.remove(p.getUUID());
            NetworkHandler.sendToTrackingAndSelf(s.packet(true),p);
            beginFight(s,data);
        }
    }

    public static void cancel(ServerPlayer p) {
        var s=SCENES.remove(p.getUUID()); if(s==null) return;
        selectSlot(p,s.originalSlot);
        s.rukia.discard(); s.boss.discard(); s.props.forEach(Entity::discard);
        NetworkHandler.sendToTrackingAndSelf(s.packet(true),p);
        PlayerCapability.get(p).ifPresent(d->{d.getStatus().setOriginLocked(false);d.getOrigin().request();SyncHelper.full(p);});
    }
    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        if(e.getEntity() instanceof ServerPlayer p) PlayerCapability.get(p).ifPresent(d->{
            if(d.getOrigin().stage()==OriginData.Stage.CINEMATIC) d.getOrigin().request();
            if(d.getOrigin().stage()!=OriginData.Stage.NONE) giveBook(p);
        });
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e) { if(e.getEntity() instanceof ServerPlayer p) { MISSING.remove(p.getUUID());cancel(p); } }
    @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        if(e.getEntity() instanceof ServerPlayer p) {
            cancel(p);
            PlayerCapability.get(p).ifPresent(d->{if(d.getOrigin().stage()==OriginData.Stage.FIGHT) {
                var old=p.server.getLevel(e.getFrom());
                var boss=old==null||d.getOrigin().boss()==null?null:old.getEntity(d.getOrigin().boss());
                if(boss!=null) { cleanupProps(old,boss);boss.discard(); }d.getOrigin().retry();SyncHelper.full(p);
            }});
        }
    }
    @SubscribeEvent public static void tracking(PlayerEvent.StartTracking e) {
        var s=SCENES.get(e.getTarget().getUUID());
        if(s!=null&&e.getEntity() instanceof ServerPlayer viewer)NetworkHandler.sendToPlayer(s.packet(false),viewer);
    }
    @SubscribeEvent public static void stop(ServerStoppingEvent e) { for(var s:List.copyOf(SCENES.values())) cancel(s.player); SCENES.clear();MISSING.clear(); }
    @SubscribeEvent public static void cleanup(EntityJoinLevelEvent e) {
        if(!e.getLevel().isClientSide && e.loadedFromDisk() && e.getEntity().getPersistentData().getBoolean(TEMP)) e.setCanceled(true);
        if(e.loadedFromDisk()&&e.getEntity() instanceof HollowBossEntity boss&&e.getLevel() instanceof ServerLevel level&&boss.getPersistentData().hasUUID(OWNER)) {
            var owner=level.getServer().getPlayerList().getPlayer(boss.getPersistentData().getUUID(OWNER));
            if(owner!=null)PlayerCapability.get(owner).ifPresent(d->{
                if(d.getOrigin().stage()!=OriginData.Stage.FIGHT||!boss.getUUID().equals(d.getOrigin().boss()))e.setCanceled(true);
            });
        }
    }
    @SubscribeEvent public static void damage(LivingAttackEvent e) {
        if(e.getEntity() instanceof ServerPlayer p && locked(p) && !e.getSource().is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) e.setCanceled(true);
        if(e.getEntity() instanceof HollowBossEntity b && b.getPersistentData().hasUUID(OWNER)) {
            var owner=b.getPersistentData().getUUID(OWNER);
            if(!e.getSource().is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)
                    && (b.getPersistentData().getBoolean(TEMP) || e.getSource().getEntity()==null || !owner.equals(e.getSource().getEntity().getUUID()))) e.setCanceled(true);
        }
    }
    @SubscribeEvent public static void attack(AttackEntityEvent e) { if(locked(e.getEntity())) e.setCanceled(true); }
    @SubscribeEvent public static void interact(PlayerInteractEvent e) { if(locked(e.getEntity()) && e.isCancelable()) e.setCanceled(true); }
    @SubscribeEvent public static void death(LivingDeathEvent e) {
        if(e.getEntity() instanceof ServerPlayer p) {
            cancel(p);
            PlayerCapability.get(p).ifPresent(d->{ if(d.getOrigin().stage()==OriginData.Stage.FIGHT) {
                var b=d.getOrigin().boss()==null?null:p.serverLevel().getEntity(d.getOrigin().boss()); if(b!=null) { cleanupProps(p.serverLevel(),b);b.discard(); }d.getOrigin().retry();
            }});
        }
        if(e.getEntity() instanceof HollowBossEntity b && b.getPersistentData().hasUUID(OWNER) && b.level() instanceof ServerLevel level) {
            cleanupProps(level,b);
            var p=level.getServer().getPlayerList().getPlayer(b.getPersistentData().getUUID(OWNER));
            if(p!=null) PlayerCapability.get(p).ifPresent(d->{
                if(d.getOrigin().stage()!=OriginData.Stage.FIGHT || !b.getUUID().equals(d.getOrigin().boss())) return;
                var quest=QuestRegistry.getQuest(QUEST);
                if(quest!=null && e.getSource().getEntity()==p) {
                    QuestService.incrementObjective(p,d,QUEST,quest,0,1);
                    if(d.getPlayerQuestData().isCompleted(QUEST))d.getOrigin().complete();else d.getOrigin().retry();
                } else d.getOrigin().retry();
                SyncHelper.full(p);
            });
        }
    }
    private static void cleanupProps(ServerLevel level,Entity boss) {
        var props=boss.getPersistentData().getList("OriginProps",net.minecraft.nbt.Tag.TAG_INT_ARRAY);
        for(var tag:props)if(tag instanceof net.minecraft.nbt.IntArrayTag array&&array.getAsIntArray().length==4) {
            var prop=level.getEntity(net.minecraft.nbt.NbtUtils.loadUUID(tag));
            if(prop!=null&&prop.getPersistentData().getBoolean(TEMP))prop.discard();
        }
        boss.getPersistentData().remove("OriginProps");
    }
}
