package com.bleachmod.client.berserk;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.BerserkVisuals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Berserk Mode aura: three crimson strands spiralling up around the body every client tick, with embers
 * and dark smoke. Purely local particles (no network), shown to every viewer within 64 blocks.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class BerserkAura {
    public static final int STRANDS = 3, STEPS = 4;
    public static final double HEIGHT = 2.4, RADIUS = .95;
    private BerserkAura() { }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        var mc = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END || mc.level == null || mc.player == null || mc.isPaused()) return;
        for (AbstractClientPlayer player : mc.level.players())
            if (player.distanceToSqr(mc.player) < 64 * 64 && BerserkSkinLayer.active(player)) spiral(player);
    }

    private static void spiral(AbstractClientPlayer player) {
        var level = player.level(); var random = level.random;
        double x = player.getX(), y = player.getY(), z = player.getZ();
        for (int strand = 0; strand < STRANDS; strand++) for (int step = 0; step < STEPS; step++) {
            double phase = player.tickCount + step / (double) STEPS;
            double height = (phase * .09 + strand * HEIGHT / STRANDS) % HEIGHT;
            double angle = phase * .5 + strand * Math.PI * 2 / STRANDS;
            double radius = RADIUS * (1 - height / HEIGHT * .35);
            level.addParticle(BerserkVisuals.BLOOD_LARGE, x + Math.cos(angle) * radius, y + height, z + Math.sin(angle) * radius, 0, .02, 0);
            if (step == 0) level.addParticle(BerserkVisuals.BLOOD, x + Math.cos(angle) * radius * 1.15, y + height, z + Math.sin(angle) * radius * 1.15, 0, .05, 0);
        }
        if (player.tickCount % 2 == 0) level.addParticle(BerserkVisuals.EMBER, x + (random.nextDouble() - .5) * 1.4,
                y + random.nextDouble() * 2.2, z + (random.nextDouble() - .5) * 1.4, 0, .06, 0);
        if (player.tickCount % 4 == 0) level.addParticle(ParticleTypes.LARGE_SMOKE, x + (random.nextDouble() - .5),
                y + 2 + random.nextDouble() * .5, z + (random.nextDouble() - .5), 0, .03, 0);
        if (player.tickCount % 10 == 0) for (int i = 0; i < 12; i++) { // pulse ring at the feet
            double angle = i * Math.PI / 6;
            level.addParticle(BerserkVisuals.BLOOD_DARK, x + Math.cos(angle) * 1.2, y + .1, z + Math.sin(angle) * 1.2, 0, .01, 0);
        }
    }
}
