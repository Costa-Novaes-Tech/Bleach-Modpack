package com.bleachmod.gametest;

import com.bleachmod.common.data.PlayerCapability;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

/** Forge removes a travelling player from the old level and revives it in the new one; the data must survive that. */
@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class DimensionTravelGameTests {
    @GameTest(template = "empty")
    public static void playerDataSurvivesLevelRemovalAndRevive(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "Traveller"));
        p.setPos(h.absoluteVec(new Vec3(2.5, 2, 2.5)));
        var before = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
        before.initializeShinigami(); before.getStatus().setTechniqueSlot1CooldownTicks(100);
        p.remove(Entity.RemovalReason.CHANGED_DIMENSION);
        h.assertTrue(!PlayerCapability.get(p).isPresent(), "Removed player still exposed its data");
        p.revive();
        var after = PlayerCapability.get(p).resolve().orElse(null);
        h.assertTrue(after == before, "Player data lost after a dimension change");
        h.assertTrue(after.getStatus().hasCreatedCharacter() && after.getStatus().getTechniqueSlot1CooldownTicks() == 100,
                "Character or cooldowns lost after a dimension change");
        p.remove(Entity.RemovalReason.CHANGED_DIMENSION); p.revive();
        h.assertTrue(PlayerCapability.get(p).resolve().orElse(null) == before, "Second trip lost the player data");
        h.succeed();
    }
}
