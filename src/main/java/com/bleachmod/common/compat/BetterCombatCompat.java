package com.bleachmod.common.compat;

import com.bleachmod.Reference;
import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import java.util.HashSet;
import java.util.Set;

/** The pinned BC request opens/closes a scope through its injected setComboCount method. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class BetterCombatCompat {
    private static final ThreadLocal<Swing> CURRENT=new ThreadLocal<>();
    private static final class Swing {
        final ServerPlayer owner;final long tick;final boolean allowed;final Set<Integer> targets=new HashSet<>();boolean charged;
        Swing(ServerPlayer p){owner=p;tick=p.level().getGameTime();allowed=canAttack(p);}
    }
    private BetterCombatCompat(){ }
    public static boolean managed(ItemStack stack){return BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals(Reference.MOD_ID);}
    public static boolean pilot(ItemStack stack){return stack.is(ModItems.ASAUCHI.get()) || stack.is(ModItems.RYUJIN_JAKKA.get()) || stack.is(ModItems.HYORINMARU.get());}
    public static boolean excluded(ItemStack stack){return managed(stack) && !pilot(stack);}
    public static boolean stateAllows(PlayerData d){return !d.getGuard().active() && !d.getGuard().broken()
            && !d.getStatus().isDodging() && !d.getStatus().isActionCharging() && d.getStatus().getFlameDashTicks()==0;}
    public static boolean canAttack(Player p){return p.isAlive() && !p.isSpectator() && !p.isUsingItem() && !p.isSleeping() && !p.isFullyFrozen()
            && PlayerCapability.get(p).map(BetterCombatCompat::stateAllows).orElse(true)
            && (!(p instanceof ServerPlayer server) || !ReleaseAnimationService.busy(server) && !ElementalTechniqueService.busy(server) && !SenbonzakuraTechniqueService.busy(server) && !BenihimeTechniqueService.busy(server));}
    public static void comboChanged(Player player,int combo){
        if(player instanceof ServerPlayer p && combo>=0 && pilot(p.getMainHandItem()))CURRENT.set(new Swing(p));
        else CURRENT.remove();
    }
    private static Swing current(ServerPlayer p){var s=CURRENT.get();return s!=null && s.owner==p && s.tick==p.level().getGameTime()?s:null;}
    public static boolean insideSwing(ServerPlayer p){return current(p)!=null;}
    public static boolean chargeStamina(ServerPlayer p){var s=current(p);if(s==null)return true;if(s.charged)return false;s.charged=true;return true;}
    public static void clear(){CURRENT.remove();}
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void attack(AttackEntityEvent event){
        if(!(event.getEntity() instanceof ServerPlayer p) || !pilot(p.getMainHandItem()))return;
        if(!canAttack(p)){event.setCanceled(true);return;}
        var swing=current(p);if(swing==null)return;
        Entity target=event.getTarget();
        if(!swing.allowed || swing.targets.size()>=64 || swing.targets.contains(target.getId()) || !target.isAlive()
                || target instanceof LivingEntity living && !TechniqueTargets.allowed(p,living)
                || !withinPilotReach(p,target)) {event.setCanceled(true);return;}
        swing.targets.add(target.getId());
    }
    public static boolean withinPilotReach(ServerPlayer p,Entity target){
        var box=target.getBoundingBox();Vec3 eye=p.getEyePosition();
        Vec3 closest=new Vec3(Math.max(box.minX,Math.min(box.maxX,eye.x)),Math.max(box.minY,Math.min(box.maxY,eye.y)),Math.max(box.minZ,Math.min(box.maxZ,eye.z)));
        Vec3 direction=box.getCenter().subtract(eye);
        return closest.distanceToSqr(eye)<=3.05*3.05 && (direction.lengthSqr()<1 || p.getLookAngle().dot(direction.normalize())>=-.15)
                && p.hasLineOfSight(target);
    }
}
