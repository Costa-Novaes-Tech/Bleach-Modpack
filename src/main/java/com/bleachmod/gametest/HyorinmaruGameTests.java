package com.bleachmod.gametest;

import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.UUID;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class HyorinmaruGameTests {
    private static PlayerData data(FakePlayer p) { return PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Capability missing")); }
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "IceTest"));
        data(p).initializeShinigami(); data(p).getCharacter().bindZanpakuto("ryujin_jakka"); data(p).getCharacter().preserveLegacyTechniques();
        BlockPos pos = h.absolutePos(new BlockPos(1, 3, 1));
        p.setPos(pos.getX() + 0.5, h.getLevel().getMaxBuildHeight() - 64, pos.getZ() + 0.5); p.setYRot(0);
        p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HYORINMARU.get()));
        return p;
    }
    private static void release(FakePlayer p, String form) {
        var d = data(p); d.getCharacter().bindZanpakuto("hyorinmaru"); d.getCharacter().preserveLegacyTechniques();
        d.getSkills().get("zanpakuto").setLevel(2);
        d.getCharacter().unlockForm("shikai"); d.getCharacter().unlockForm("bankai");
        d.getCharacter().setMastery("zanpakuto", "shikai", 100);
        d.getCharacter().setActiveForm("zanpakuto", form);
    }
    private static Zombie zombie(GameTestHelper h, FakePlayer p, int ahead) {
        var z = EntityType.ZOMBIE.create(h.getLevel()); z.setNoAi(true); z.setNoGravity(true);
        z.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.IRON_HELMET));
        z.setPos(p.getX(), p.getY(), p.getZ() + ahead);
        h.getLevel().getChunkAt(z.blockPosition()); h.getLevel().addFreshEntity(z); return z;
    }
    @GameTest(template = "empty")
    public static void iceRequiresIdentityMatchingItemAndOwnUnlock(GameTestHelper h) {
        var p = player(h); var d = data(p);
        TechniqueService.executeSlot(p, d, 1);
        h.assertTrue(d.getResources().getCurrentReiatsu() == 100 && !d.getStatus().isIgnitionActive(), "Wrong sword activated Ryujin");
        d.getCharacter().bindZanpakuto("hyorinmaru"); d.getCharacter().preserveLegacyTechniques(); d.getCharacter().setActiveForm("zanpakuto", "shikai");
        TechniqueService.executeSlot(p, d, 1);
        h.assertTrue(d.getResources().getCurrentReiatsu() == 100, "Locked Shikai spent energy");
        release(p, "shikai"); p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RYUJIN_JAKKA.get()));
        TechniqueService.executeSlot(p, d, 1);
        h.assertTrue(d.getResources().getCurrentReiatsu() == 100, "Mismatched identity spent energy"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void bindingIsOperatorOnlyAndPreservesCooldowns(GameTestHelper h) {
        var p = player(h); var d = data(p); d.getStatus().setTechniqueSlot2CooldownTicks(80);
        var dispatcher = p.getServer().getCommands().getDispatcher();
        boolean rejected = false;
        try { dispatcher.execute("bleachdev zanpakuto bind @s hyorinmaru", p.createCommandSourceStack().withPermission(0)); }
        catch (com.mojang.brigadier.exceptions.CommandSyntaxException expected) { rejected = true; }
        h.assertTrue(rejected && d.getCharacter().getZanpakutoIdentity().equals("ryujin_jakka"), "Unauthorized bind accepted");
        try { dispatcher.execute("bleachdev zanpakuto bind @s hyorinmaru", p.createCommandSourceStack().withPermission(2)); }
        catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) { throw new IllegalStateException(e); }
        h.assertTrue(d.getCharacter().getZanpakutoIdentity().equals("hyorinmaru") && d.getCharacter().getActiveForm().equals("sealed"), "Operator bind failed");
        h.assertTrue(d.getStatus().getTechniqueSlot2CooldownTicks() == 80, "Bind bypassed cooldown"); h.succeed();
    }

    @GameTest(template = "empty")
    public static void iceStrikeDamagesSlowsAndCooldownPreventsSecondDebit(GameTestHelper h) {
        var p = player(h); release(p, "shikai"); var z = zombie(h, p, 3);
        h.runAfterDelay(5, () -> TechniqueService.executeSlot(p, data(p), 1));
        h.runAfterDelay(10, () -> { try {
            h.assertTrue(z.getHealth() < 20 && z.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Traveling ice dragon missed or lacked slow");
            h.assertTrue(data(p).getResources().getCurrentReiatsu() == 85, "Wrong Shikai cost");
            TechniqueService.executeSlot(p, data(p), 1);
            h.assertTrue(data(p).getResources().getCurrentReiatsu() == 85, "Cooldown debited again");
            data(p).getStatus().setCooldownsDisabled(true); data(p).getResources().setCostsDisabled(true);
            data(p).getResources().setCurrentReiatsu(0); TechniqueService.executeSlot(p, data(p), 2);
            h.assertTrue(data(p).getResources().getCurrentReiatsu() == 0 && data(p).getStatus().getTechniqueSlot2CooldownTicks() == 0, "Ice kit ignored free test controls");
        } finally { RyujinTechniqueService.cancel(p); z.discard(); } h.succeed(); });
    }

    @GameTest(template = "empty", batch = "zone_legacy", timeoutTicks = 150)
    public static void iceZoneExpiresAndNeverAffectsAllies(GameTestHelper h) {
        var p = player(h); release(p, "bankai"); var ally = zombie(h, p, 2); var enemy = zombie(h, p, 3);
        var scoreboard = h.getLevel().getScoreboard(); var team = scoreboard.addPlayerTeam("ice" + UUID.randomUUID().toString().substring(0,8));
        scoreboard.addPlayerToTeam(p.getScoreboardName(), team); scoreboard.addPlayerToTeam(ally.getScoreboardName(), team);
        h.onEachTick(() -> HyorinmaruTechniqueService.tick(p, data(p)));
        h.runAfterDelay(5, () -> TechniqueService.executeSlot(p, data(p), 4));
        h.runAfterDelay(20, () -> { HyorinmaruTechniqueService.tick(p, data(p));
            h.assertTrue(ally.getHealth() == 20 && !ally.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Ice zone affected ally");
            h.assertTrue(enemy.getHealth() < 20 && enemy.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Ice zone missed enemy");
        });
        h.runAfterDelay(130, () -> { try {
            HyorinmaruTechniqueService.tick(p, data(p));
            h.assertTrue(!HyorinmaruTechniqueService.isActive(p), "Expired zone remains active");
            if (com.bleachmod.common.technique.BlizzardWeatherData.eligible(p.serverLevel(), p.blockPosition())) {
                h.assertTrue(com.bleachmod.common.technique.BlizzardWeatherData.get(p.serverLevel()).isManaged(), "Zone expiration cancelled ongoing Bankai weather");
            }
        } finally {
            // Leave Bankai first: onEachTick may still run the caster after cleanup and must release, not renew, the weather lease.
            data(p).getCharacter().setActiveForm("zanpakuto", "sealed");
            scoreboard.removePlayerTeam(team); ally.discard(); enemy.discard(); RyujinTechniqueService.cancel(p);
        } h.succeed(); });
    }
}
