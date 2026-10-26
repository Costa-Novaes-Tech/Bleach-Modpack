package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.visual.SenbonGeometry;
import com.bleachmod.init.ModItems;
import com.bleachmod.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

/**
 * Dedicated blade-bound petal trail visuals for Senbonzakura Better Combat fencing.
 * Replaces the legacy static ARC emitter with continuous, hand-to-tip blade sampling:
 * - Combo 0 (Diagonal): Discontinuous ribbon following blade top-right to bottom-left with subtle white-pink tip highlight.
 * - Combo 1 (Return): Short horizontal band at chest height sweeping left to right with gentle ambient wind dispersal.
 * - Combo 2 (Thrust): Narrow linear stream along the tip vector with brief longitudinal contact dispersion.
 *
 * Honors performance budgets, particle quality scaling, first-person attenuation, and distance culling.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class SenbonCombatVisuals {
    private static final int SWING_DURATION = 14;
    private static final int ACTIVE_START = 2;
    private static final int ACTIVE_END = 9;

    private record ActiveSwing(int combo, long startTick, Vec3 lastHandPos, Vec3 lastTipPos) { }

    private static final Map<Integer, ActiveSwing> SWINGS = new LinkedHashMap<>();
    private static final Map<Integer, Vec3> PREVIOUS_POSITIONS = new HashMap<>();
    private static ClientLevel currentLevel;
    private static long lastClientTick = Long.MIN_VALUE;

    private SenbonCombatVisuals() { }

    public static void clear() {
        SWINGS.clear();
        PREVIOUS_POSITIONS.clear();
        currentLevel = null;
        lastClientTick = Long.MIN_VALUE;
    }

    /**
     * Registers a new melee swing for an attacker. Deduplicated per tick.
     */
    public static void startMelee(int ownerId, int combo) {
        var mc = Minecraft.getInstance();
        if (mc.level == null || combo < 0 || combo > 2) return;
        long now = mc.level.getGameTime();
        var existing = SWINGS.get(ownerId);
        if (existing != null && existing.startTick() == now && existing.combo() == combo) return;

        if (SWINGS.size() >= 24 && !SWINGS.containsKey(ownerId)) {
            SWINGS.remove(SWINGS.keySet().iterator().next());
        }
        SWINGS.put(ownerId, new ActiveSwing(combo, now, null, null));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            clear();
            return;
        }
        if (currentLevel != mc.level) {
            clear();
            currentLevel = mc.level;
        }
        if (mc.isPaused() || lastClientTick == mc.level.getGameTime()) return;
        lastClientTick = mc.level.getGameTime();

        // Expire finished swings
        SWINGS.values().removeIf(s -> lastClientTick - s.startTick() >= SWING_DURATION);
        PREVIOUS_POSITIONS.keySet().removeIf(id -> mc.level.getEntity(id) == null);

        if (SWINGS.isEmpty()) return;

        int particleBudget = 192;
        int scale = switch (mc.options.particles().get()) {
            case MINIMAL -> 4;
            case DECREASED -> 2;
            default -> 1;
        };

        for (var player : mc.level.players()) {
            if (!player.getMainHandItem().is(ModItems.SENBONZAKURA.get())) continue;
            int ownerId = player.getId();
            var swing = SWINGS.get(ownerId);
            if (swing == null) continue;

            if (mc.player.distanceToSqr(player) > 48 * 48) continue;
            if (!player.isAlive() || player.isSpectator() || player.isSleeping()) {
                SWINGS.remove(ownerId);
                continue;
            }

            // Interrupt if guarding or dodging
            boolean interrupted = PlayerCapability.get(player).map(d ->
                    d.getGuard().shownActive() || d.getGuard().shownBroken() || d.getStatus().isDodging()
            ).orElse(false);
            if (interrupted) {
                SWINGS.remove(ownerId);
                continue;
            }

            // Teleport detection (> 4 blocks jump cuts the trail immediately)
            Vec3 currentPos = player.position();
            Vec3 prevPos = PREVIOUS_POSITIONS.put(ownerId, currentPos);
            if (prevPos != null && prevPos.distanceToSqr(currentPos) > 16.0) {
                SWINGS.remove(ownerId);
                continue;
            }

            int age = (int) (lastClientTick - swing.startTick());
            if (age < ACTIVE_START || age > ACTIVE_END) continue;

            // Normalized progress within active window (0.0 to 1.0)
            float progress = (float) (age - ACTIVE_START) / (float) (ACTIVE_END - ACTIVE_START);
            progress = Math.max(0.0F, Math.min(1.0F, progress));

            float sign = player.getMainArm() == HumanoidArm.RIGHT ? 1.0F : -1.0F;
            Vec3 forward = SenbonGeometry.forward(player.getYRot());
            Vec3 left = SenbonGeometry.left(forward);
            Vec3 dominantRight = left.scale(-sign);

            // Calculate blade hand and tip based on our reconstructed choreographies
            Vec3 localHand, localTip;
            switch (swing.combo()) {
                case 0 -> { // Diagonal cut: top-dominant to low-opposite across chest
                    double t = progress;
                    localHand = new Vec3(
                            (0.35 - 0.65 * t) * sign,
                            1.45 - 0.50 * t,
                            0.20 + 0.10 * t
                    );
                    localTip = new Vec3(
                            (0.70 - 1.50 * t) * sign,
                            2.10 - 1.45 * t,
                            0.50 + 0.30 * t
                    );
                }
                case 1 -> { // Return cut: horizontal sweep from low-opposite to dominant chest
                    double t = progress;
                    localHand = new Vec3(
                            (-0.32 + 0.70 * t) * sign,
                            0.95 + 0.20 * t,
                            0.30 - 0.10 * t
                    );
                    localTip = new Vec3(
                            (-0.85 + 1.70 * t) * sign,
                            1.05 + 0.15 * t,
                            0.70 - 0.10 * t
                    );
                }
                default -> { // Thrust: linear forward extension from ribs chamber
                    double t = progress;
                    localHand = new Vec3(
                            (0.25 - 0.05 * t) * sign,
                            1.10 + 0.05 * t,
                            -0.05 + 0.60 * t
                    );
                    localTip = new Vec3(
                            (0.25 - 0.05 * t) * sign,
                            1.10 + 0.05 * t,
                            0.70 + 1.15 * t
                    );
                }
            }

            // Transform local bone offsets into world coordinates
            Vec3 handWorld = currentPos
                    .add(dominantRight.scale(localHand.x))
                    .add(0, localHand.y, 0)
                    .add(forward.scale(localHand.z));
            Vec3 tipWorld = currentPos
                    .add(dominantRight.scale(localTip.x))
                    .add(0, localTip.y, 0)
                    .add(forward.scale(localTip.z));

            // First-person attenuation (50% reduction for self in first-person to preserve screen clarity)
            boolean isFirstPersonSelf = (player == mc.player && mc.options.getCameraType().isFirstPerson());
            int targetCount = Math.max(1, (12 / scale) / (isFirstPersonSelf ? 2 : 1));

            // Ambient wind drift
            Vec3 wind = new Vec3(0.015 + Math.sin(lastClientTick * 0.05) * 0.008, 0.004, 0.008);

            // Sample points along blade: base, 1/3, 2/3, tip
            for (int i = 0; i < targetCount && particleBudget > 0; i++) {
                double fraction = (i + Math.random() * 0.5) / (double) targetCount;
                Vec3 p = handWorld.lerp(tipWorld, fraction);

                // Interpole with previous frame to prevent gaps at lower FPS
                if (swing.lastHandPos() != null && swing.lastTipPos() != null) {
                    Vec3 prevP = swing.lastHandPos().lerp(swing.lastTipPos(), fraction);
                    p = prevP.lerp(p, 0.5 + Math.random() * 0.5);
                }

                Vec3 velocity = switch (swing.combo()) {
                    case 0 -> dominantRight.scale(-0.03).add(0, -0.02, 0).add(forward.scale(0.01)); // Diagonal slice
                    case 1 -> dominantRight.scale(0.035).add(0, 0.005, 0).add(forward.scale(0.005)); // Horizontal sweep
                    default -> forward.scale(0.05).add(dominantRight.scale((Math.random() - 0.5) * 0.01)); // Linear thrust
                };
                velocity = velocity.add(wind);

                // Tip highlight on diagonal, longitudinal contact burst on thrust
                ParticleOptions particle = ModParticles.SENBON_PETAL.get();
                if (swing.combo() == 0 && fraction > 0.85 && Math.random() < 0.25) {
                    particle = ParticleTypes.END_ROD;
                } else if (swing.combo() == 2 && progress > 0.7F && fraction > 0.90) {
                    velocity = velocity.add(forward.scale(0.04));
                }

                mc.level.addParticle(particle, p.x, p.y, p.z, velocity.x, velocity.y, velocity.z);
                particleBudget--;
            }

            // Update last known blade positions for smooth frame-to-frame ribbons
            SWINGS.put(ownerId, new ActiveSwing(swing.combo(), swing.startTick(), handWorld, tipWorld));
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        clear();
    }

    @SubscribeEvent
    public static void onUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            clear();
        }
    }
}
