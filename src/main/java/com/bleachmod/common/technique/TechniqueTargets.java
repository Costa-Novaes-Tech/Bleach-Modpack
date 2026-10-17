package com.bleachmod.common.technique;

import com.bleachmod.entity.QuestNpcEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/** Shared target policy for both Zanpakuto kits and the older Ryujin executors. */
public final class TechniqueTargets {
    private TechniqueTargets() { }
    private static boolean ashAlly(ServerPlayer owner,LivingEntity target){
        var tag=target.getPersistentData();if(!tag.getBoolean("bleachmod_ash_soldier") || !tag.hasUUID("ashOwner"))return false;
        var id=tag.getUUID("ashOwner");if(id.equals(owner.getUUID()))return true;
        var caster=owner.getServer().getPlayerList().getPlayer(id);return caster!=null && owner.isAlliedTo(caster);
    }
    public static boolean allowed(ServerPlayer owner, LivingEntity target) {
        if (target == owner || !target.isAlive() || target.isSpectator() || owner.isAlliedTo(target)
                || target instanceof com.bleachmod.entity.IceDragonEntity
                || target instanceof QuestNpcEntity || ashAlly(owner,target) || target.getPersistentData().getBoolean("bleachmod_flame_bat")) return false;
        return !(target instanceof Player other)
                || (owner.getServer().isPvpAllowed() && owner.canHarmPlayer(other));
    }
}
