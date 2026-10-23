package com.bleachmod.common.technique;

import com.bleachmod.Reference;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

/** One follow-up strike by the caster; environmental damage cannot consume the mark. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public final class GlacialFragilityService {
    private record Mark(UUID owner, LivingEntity target, long until) { }
    private static final Map<UUID,Mark> MARKS=new HashMap<>();
    private GlacialFragilityService() { }
    public static void mark(ServerPlayer owner,LivingEntity target) {
        MARKS.values().removeIf(m->!m.target.isAlive() || m.target.level().getGameTime()>=m.until);
        if(TechniqueTargets.allowed(owner,target) && (MARKS.size()<256 || MARKS.containsKey(target.getUUID())))
            MARKS.put(target.getUUID(),new Mark(owner.getUUID(),target,target.level().getGameTime()+80));
    }
    @SubscribeEvent public static void hurt(LivingHurtEvent e) {
        var mark=MARKS.get(e.getEntity().getUUID());
        if(mark==null)return;
        if(e.getEntity()!=mark.target || e.getEntity().level().getGameTime()>=mark.until){MARKS.remove(e.getEntity().getUUID());return;}
        if(e.getAmount()>0 && e.getSource().getDirectEntity() instanceof ServerPlayer p
                && p.level()==mark.target.level() && mark.owner.equals(p.getUUID()) && TechniqueTargets.allowed(p,e.getEntity())) {
            MARKS.remove(e.getEntity().getUUID());e.setAmount(e.getAmount()*1.15F);
        }
    }
    @SubscribeEvent public static void tick(net.minecraftforge.event.TickEvent.ServerTickEvent e){
        if(e.phase==net.minecraftforge.event.TickEvent.Phase.END)MARKS.values().removeIf(m->m.target.isRemoved() || !m.target.isAlive() || m.target.level().getGameTime()>=m.until);
    }
    @SubscribeEvent public static void logout(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent e){MARKS.values().removeIf(m->m.owner.equals(e.getEntity().getUUID()) || m.target==e.getEntity());}
    @SubscribeEvent public static void stop(ServerStoppingEvent e){MARKS.clear();}
}
