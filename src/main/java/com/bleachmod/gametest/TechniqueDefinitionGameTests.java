package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class TechniqueDefinitionGameTests {
    @GameTest(template = "empty", batch = "definitions")
    public static void everyKitCastUsesItsDefinitionAndKeepsTestOverrides(GameTestHelper h) {
        for (var definition : TechniqueDefinitions.all()) {
            var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "DefinitionTest"));
            var pos = h.absolutePos(new BlockPos(1, 1, 1)); p.setPos(pos.getX() + .5, h.getLevel().getMaxBuildHeight() - 20, pos.getZ() + .5);
            h.getLevel().getChunkAt(p.blockPosition());
            var data = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
            data.initializeShinigami(); data.getCharacter().bindZanpakuto(definition.identity()); data.getCharacter().preserveLegacyTechniques();
            data.getSkills().get("zanpakuto").setLevel(2); data.getCharacter().unlockForm("shikai"); data.getCharacter().unlockForm("bankai");
            data.getCharacter().setMastery("zanpakuto", "shikai", 100); data.getCharacter().setActiveForm("zanpakuto", definition.form());
            p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(com.bleachmod.common.ZanpakutoCarry.item(definition.identity())));
            try {
                float before = data.getResources().getCurrentReiatsu();
                TechniqueService.executeSlot(p, data, definition.slot());
                h.assertTrue(Math.abs(data.getResources().getCurrentReiatsu() - (before - definition.cost())) < .001, "Cast cost differs from definition: " + definition);
                int cooldown = switch (definition.slot()) {
                    case 1 -> data.getStatus().getTechniqueSlot1CooldownTicks(); case 2 -> data.getStatus().getTechniqueSlot2CooldownTicks();
                    case 3 -> data.getStatus().getTechniqueSlot3CooldownTicks(); default -> data.getStatus().getTechniqueSlot4CooldownTicks();
                };
                h.assertTrue(cooldown == definition.cooldownTicks(), "Cooldown differs from definition: " + definition);
                if (definition.drainPerTick() > 0) {
                    if ("nozarashi".equals(definition.identity())) com.bleachmod.common.technique.NozarashiTechniqueService.tick(p, data);
                    else TechniqueService.tickIgnition(p, data);
                    h.assertTrue(Math.abs(data.getResources().getCurrentReiatsu() - (before - definition.drainPerTick())) < .001, "Continuous drain differs from definition");
                }
                RyujinTechniqueService.cancel(p);
                data.getStatus().setCooldownsDisabled(true); data.getResources().setCostsDisabled(true);
                data.getResources().setCurrentReiatsu(0); TechniqueService.executeSlot(p, data, definition.slot());
                h.assertTrue(data.getResources().getCurrentReiatsu() == 0, "Definition bypassed free-cost override");
                h.assertTrue(data.getStatus().getTechniqueSlot1CooldownTicks() == 0 && data.getStatus().getTechniqueSlot2CooldownTicks() == 0
                        && data.getStatus().getTechniqueSlot3CooldownTicks() == 0 && data.getStatus().getTechniqueSlot4CooldownTicks() == 0, "Definition bypassed cooldown override");
            } finally { RyujinTechniqueService.cancel(p); }
        }
        h.succeed();
    }
}
