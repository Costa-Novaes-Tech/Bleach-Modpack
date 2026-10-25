package com.bleachmod.gametest;

import com.bleachmod.common.visual.BenihimeEntranceGeometry;
import com.bleachmod.common.visual.SenbonAtmosphere;
import com.bleachmod.common.visual.SenbonStage;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.List;

@GameTestHolder("bleachmod")
@PrefixGameTestTemplate(false)
public final class SenbonStageGameTests {
    @GameTest(template = "empty", batch = "senbon_stage")
    public static void stageFloorAndBackdropRespectEnvelopeNegativeSpaceAndTerrain(GameTestHelper h) {
        Vec3 origin = new Vec3(10, 70, -4);
        Vec3 forward = new Vec3(0, 0, 1);

        h.assertTrue(SenbonStage.stageWeight(0) == 0.0
                && SenbonStage.stageWeight(35) == 1.0
                && SenbonStage.stageWeight(228) == 1.0
                && SenbonStage.stageWeight(290) == 0.0, "SenbonStage weight envelope violated");
        h.assertTrue(SenbonAtmosphere.darkness(0) == 0.0
                && SenbonAtmosphere.darkness(35) == 1.0
                && SenbonAtmosphere.darkness(228) == 1.0
                && SenbonAtmosphere.darkness(290) == 0.0, "SenbonAtmosphere darkness envelope violated");

        h.assertTrue(SenbonStage.floor(origin, forward, 0).isEmpty()
                && SenbonStage.floor(origin, forward, 290).isEmpty()
                && SenbonStage.backdrop(origin, forward, 0).isEmpty()
                && SenbonStage.backdrop(origin, forward, 290).isEmpty(), "SenbonStage geometry persists outside scene boundaries");

        var earlyFloor = SenbonStage.floor(origin, forward, 12);
        var fullFloor = SenbonStage.floor(origin, forward, 35);
        h.assertTrue(!earlyFloor.isEmpty() && fullFloor.size() > earlyFloor.size(),
                "SenbonStage floor did not expand progressively between ticks 0 and 35");

        BenihimeEntranceGeometry.Ground stepped = (x, z) -> (Math.abs(x) > 4 ? 0.5 : (z < -5 ? 3.5 : 0.0));
        var terrainFloor = SenbonStage.floor(origin, forward, 120, stepped);
        boolean adaptedStep = false;
        for (var q : terrainFloor) {
            for (var v : List.of(q.a(), q.b(), q.c(), q.d())) {
                h.assertTrue(Double.isFinite(v.lengthSqr()) && v.y <= origin.y + 0.6 && v.y >= origin.y - 1.3,
                        "SenbonStage floor climbed vertical wall or produced NaN");
                if (v.y > origin.y + 0.35) adaptedStep = true;
            }
        }
        h.assertTrue(adaptedStep, "SenbonStage floor did not adapt to stepped terrain relief");

        var details = SenbonStage.surfaceDetails(origin, forward, 120, BenihimeEntranceGeometry.FLAT);
        h.assertTrue(!details.isEmpty(), "SenbonStage surface petals/trails missing");
        for (var q : details) {
            for (var v : List.of(q.a(), q.b(), q.c(), q.d())) {
                double dx = v.x - origin.x, dz = v.z - origin.z;
                h.assertTrue(dx * dx + dz * dz > 1.2, "SenbonStage surface detail intruded into central legibility zone");
            }
        }

        h.assertTrue(SenbonStage.negativeSpaceMask(0.0, 2.5) == 0.0
                && SenbonStage.negativeSpaceMask(0.45, 2.5) > 0.9
                && SenbonStage.negativeSpaceMask(0.0, 10.0) > 0.9,
                "SenbonStage backdrop negative space mask failed to protect silhouette/subtitles");

        var backdrop = SenbonStage.backdrop(origin, forward, 120);
        h.assertTrue(backdrop.size() > 300, "SenbonStage backdrop geometry missing");

        double phaseBefore = SenbonStage.petalPhase(227.99);
        double phaseAfter = SenbonStage.petalPhase(228.01);
        h.assertTrue(Math.abs(phaseAfter - phaseBefore) < 0.01 && SenbonStage.dispersionBoost(245) > 0.5,
                "SenbonStage dispersion petal phase jumped discontinuously");

        h.succeed();
    }
}
