package com.bleachmod.gametest;

import com.bleachmod.common.BasicAttackStamina;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.technique.MasteryPracticeService;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.gametest.*;
import java.util.List;
import java.util.UUID;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class SwordEffortGameTests {
    private static FakePlayer player(GameTestHelper h) {
        var p = new FakePlayer(h.getLevel(), new GameProfile(UUID.randomUUID(), "SwordEffort"));
        PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability")).initializeShinigami();
        return p;
    }
    @GameTest(template = "empty")
    public static void manualAttackChargesOnceWhileTechniqueDamageIsFreeOfStamina(GameTestHelper h) {
        var p = player(h); var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var z = EntityType.ZOMBIE.create(h.getLevel());
        float energy = d.getResources().getCurrentReiatsu();
        for (var item : List.of(ModItems.ASAUCHI.get(), ModItems.RYUJIN_JAKKA.get(), ModItems.HYORINMARU.get())) {
            p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(item));
            d.getResources().fillStamina();
            MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(p, z));
            h.assertTrue(d.getResources().getStamina() == 96, "Manual attack did not charge exactly once");
            MasteryPracticeService.hurt(p, z, p.damageSources().playerAttack(p), 1, 2);
            h.assertTrue(d.getResources().getStamina() == 96, "Technique damage charged physical stamina");
        }
        for (int i = 0; i < 30; i++) {
            var event = new AttackEntityEvent(p, z); BasicAttackStamina.attack(event);
            h.assertTrue(!event.isCanceled(), "Exhaustion blocked basic attack");
        }
        h.assertTrue(d.getResources().getStamina() == 0 && d.getResources().getCurrentReiatsu() == energy, "Wrong resource accounting");
        h.succeed();
    }
    @GameTest(template = "empty")
    public static void canceledNonSwordAndCreativeAttacksDoNotCharge(GameTestHelper h) {
        var p = player(h); var d = PlayerCapability.get(p).orElseThrow(() -> new IllegalStateException("Missing capability"));
        var z = EntityType.ZOMBIE.create(h.getLevel());
        p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(ModItems.ASAUCHI.get()));
        var canceled = new AttackEntityEvent(p, z); canceled.setCanceled(true); BasicAttackStamina.attack(canceled);
        p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
        BasicAttackStamina.attack(new AttackEntityEvent(p, z));
        p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(ModItems.ASAUCHI.get()));
        p.setGameMode(net.minecraft.world.level.GameType.CREATIVE); BasicAttackStamina.attack(new AttackEntityEvent(p, z));
        h.assertTrue(d.getResources().getStamina() == 100, "Excluded attack consumed stamina"); h.succeed();
    }
    @GameTest(template = "empty")
    public static void zanpakutosAndLegacyDamagedStacksNeverWearOrBreak(GameTestHelper h) {
        var p = player(h); var z = EntityType.ZOMBIE.create(h.getLevel());
        for (var item : List.of(ModItems.ASAUCHI.get(), ModItems.RYUJIN_JAKKA.get(), ModItems.HYORINMARU.get())) {
            var stack = new ItemStack(item); stack.getOrCreateTag().putInt("Damage", 249);
            stack.getOrCreateTag().putString("Preserved", "identity");
            stack = ItemStack.of(stack.save(new CompoundTag()));
            h.assertTrue(!stack.isDamageableItem() && stack.getMaxDamage() == 0 && !item.isBarVisible(stack), "Legacy sword retained durability");
            for (int i = 0; i < 300; i++) item.hurtEnemy(stack, z, p);
            stack.hurtAndBreak(10000, p, ignored -> { throw new AssertionError("Zanpakuto broke"); });
            h.assertTrue(stack.getCount() == 1 && "identity".equals(stack.getTag().getString("Preserved")), "Sword or identity data lost");
        }
        h.assertTrue(new ItemStack(Items.IRON_SWORD).isDamageableItem(), "Vanilla durability changed"); h.succeed();
    }
}
