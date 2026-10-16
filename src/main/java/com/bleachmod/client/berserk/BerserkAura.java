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
    public static final int STRANDS = 2, STEPS = 2;
    public static final double HEIGHT = 2.3, RADIUS = .95;
    /** First person: keep the spiral below the chest and wider, so it frames the view instead of covering it. */
    public static final double FIRST_PERSON_TOP = 1.0, FIRST_PERSON_RADIUS = 1.35;
    private BerserkAura() { }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        var mc = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END || mc.level == null || mc.player == null || mc.isPaused()) return;
        for (AbstractClientPlayer player : mc.level.players())
            if (player.distanceToSqr(mc.player) < 64 * 64 && BerserkSkinLayer.active(player)) spiral(player);
    }

    private static void spiral(AbstractClientPlayer player) {
        var mc = Minecraft.getInstance(); var level = player.level(); var random = level.random;
        boolean ownView = player == mc.player && mc.options.getCameraType().isFirstPerson();
        double top = ownView ? FIRST_PERSON_TOP : HEIGHT, baseRadius = ownView ? FIRST_PERSON_RADIUS : RADIUS;
        double x = player.getX(), y = player.getY(), z = player.getZ();
        for (int strand = 0; strand < STRANDS; strand++) for (int step = 0; step < STEPS; step++) {
            double phase = player.tickCount + step / (double) STEPS;
            double height = (phase * .09 + strand * top / STRANDS) % top;
            double angle = phase * .5 + strand * Math.PI * 2 / STRANDS;
            double radius = baseRadius * (1 - height / HEIGHT * .3);
            level.addParticle(step == 0 ? BerserkVisuals.BLOOD_LARGE : BerserkVisuals.BLOOD,
                    x + Math.cos(angle) * radius, y + height, z + Math.sin(angle) * radius, 0, ownView ? 0 : .015, 0);
        }
        if (player.tickCount % 4 == 0) level.addParticle(BerserkVisuals.EMBER, x + (random.nextDouble() - .5) * 1.6,
                y + random.nextDouble() * top, z + (random.nextDouble() - .5) * 1.6, 0, ownView ? 0 : .04, 0);
        if (!ownView && player.tickCount % 8 == 0) level.addParticle(ParticleTypes.SMOKE, x + (random.nextDouble() - .5),
                y + 2 + random.nextDouble() * .4, z + (random.nextDouble() - .5), 0, .02, 0);
        if (player.tickCount % 20 == 0) for (int i = 0; i < 8; i++) { // pulse ring at the feet
            double angle = i * Math.PI / 4;
            level.addParticle(BerserkVisuals.BLOOD_DARK, x + Math.cos(angle) * 1.3, y + .1, z + Math.sin(angle) * 1.3, 0, 0, 0);
        }
    }
}
