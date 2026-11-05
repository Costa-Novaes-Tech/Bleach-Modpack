package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Presentation-only spiritual stage for Byakuya Kuchiki's Bankai: Senbonzakura Kageyoshi.
 * Provides:
 *   1. Terrain-conforming dark spiritual floor with discrete indigo undertones, rosy reflections,
 *      progressive 0..35 formation, and sparse surface petals/trails (keeping center legible).
 *   2. Curved 3D spatial dark backdrop and multi-layer artistic sakura petals in depth,
 *      preserving negative space behind the player and subtitles, with no visible panel edges.
 *
 * Pure visual geometry: never modifies blocks, collision shapes, time of day, weather, or entities.
 */
public final class SenbonStage {
    public static final int ENTRY_END = 35;
    public static final int SUSTAIN_END = 224;
    public static final int STAGE_END = 280;
    public static final int DURATION = 290;

    public static final double DROP_X = -0.26;
    public static final double DROP_Z = 0.44;
    private static final int FLOOR_MIN_X = -15, FLOOR_MAX_X = 15;
    private static final int FLOOR_MIN_Z = -32, FLOOR_MAX_Z = 22;

    private SenbonStage() { }

    public static double smoothstep(double edge0, double edge1, double x) {
        if (edge1 <= edge0) return x >= edge1 ? 1.0 : 0.0;
        double t = Mth.clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }

    /**
     * Master stage envelope:
     *   0..35:    Progressive entry (0.0 -> 1.0), fully formed at tick 35 when katana touches surface.
     *   35..224:  Sustained full presence (1.0).
     *   224..280: Coordinated dissolution (1.0 -> 0.0), revealing the surrounding world before tick 290.
     */
    public static double stageWeight(double age) {
        if (!Double.isFinite(age) || age <= 0.0 || age >= STAGE_END) return 0.0;
        if (age < ENTRY_END) {
            return smoothstep(0.0, ENTRY_END, age);
        } else if (age <= SUSTAIN_END) {
            return 1.0;
        } else {
            return 1.0 - smoothstep(SUSTAIN_END, STAGE_END, age);
        }
    }

    /**
     * Backdrop envelope dissolves slightly ahead of the floor (220..276) so the horizon opens first.
     */
    public static double backdropWeight(double age) {
        if (!Double.isFinite(age) || age <= 0.0 || age >= 276.0) return 0.0;
        if (age < ENTRY_END) {
            return smoothstep(0.0, ENTRY_END, age);
        } else if (age <= 220.0) {
            return 1.0;
        } else {
            return 1.0 - smoothstep(220.0, 276.0, age);
        }
    }

    /**
     * Subtle intensification of background and surface petal motion during blade dispersion (224..276),
     * tapering off as petals lose density during the final camera settle.
     */
    public static double dispersionBoost(double age) {
        if (!Double.isFinite(age) || age <= SUSTAIN_END || age >= STAGE_END) return 0.0;
        return smoothstep(SUSTAIN_END, 248.0, age) * (1.0 - smoothstep(258.0, STAGE_END, age));
    }

    /**
     * C1-continuous integrated phase for slow, elegant petal drift that subtly accelerates during dispersion.
     */
    public static double petalPhase(double age) {
        double clamped = Mth.clamp(age, 0.0, DURATION);
        double base = clamped * 0.026;
        if (clamped <= SUSTAIN_END) return base;
        double extra = clamped - SUSTAIN_END;
        double ramp = smoothstep(SUSTAIN_END, 260.0, clamped);
        return base + extra * 0.038 * ramp;
    }

    public static List<BenihimeGeometry.Quad> floor(Vec3 origin, Vec3 forward, double age) {
        return floor(origin, forward, age, BenihimeEntranceGeometry.FLAT);
    }

    /**
     * Builds the terrain-conforming dark spiritual floor.
     * Adjacent quads share identical corner heights so there are no cracks, overlapping plates,
     * z-fighting, or floating-board appearance on uneven terrain.
     */
    public static List<BenihimeGeometry.Quad> floor(Vec3 origin, Vec3 forward, double age, BenihimeEntranceGeometry.Ground ground) {
        double weight = stageWeight(age);
        if (weight <= 0.001) return List.of();

        Vec3 f = flatForward(forward);
        Vec3 left = SenbonGeometry.left(f);
        double[][] vertexHeights = buildVertexHeightGrid(ground, weight);

        // Progressive radial expansion from katana drop point (0.32, 0.35) during ticks 0..35
        double expandProgress = smoothstep(0.0, ENTRY_END, age);
        double maxReach = Mth.lerp(expandProgress, 5.0, 38.0);

        // Katana contact & sinking reflection intensity (peaks around ticks 32..90)
        double contactGlow = smoothstep(18.0, 35.0, age) * (1.0 - 0.55 * smoothstep(95.0, 175.0, age));
        // Monumental blade row reflection intensity (ticks 76..264)
        double corridorGlow = smoothstep(76.0, 115.0, age) * (1.0 - smoothstep(224.0, 264.0, age));

        int cols = FLOOR_MAX_X - FLOOR_MIN_X;
        int rows = FLOOR_MAX_Z - FLOOR_MIN_Z;
        var out = new ArrayList<BenihimeGeometry.Quad>(cols * rows);

        for (int ix = 0; ix < cols; ix++) {
            int x = FLOOR_MIN_X + ix;
            double cx = x + 0.5;
            for (int iz = 0; iz < rows; iz++) {
                int z = FLOOR_MIN_Z + iz;
                double cz = z + 0.5;

                double dxDrop = cx - DROP_X;
                double dzDrop = cz - DROP_Z;
                double distDrop = Math.sqrt(dxDrop * dxDrop + dzDrop * dzDrop);
                if (distDrop > maxReach + 1.5) continue;

                double radialReveal = 1.0 - smoothstep(maxReach - 2.2, maxReach + 0.8, distDrop);
                if (radialReveal <= 0.005) continue;

                // Soft elliptical & rectangular perimeter feathering
                double normX = cx / 15.0;
                double normZ = (cz + 5.0) / 27.0;
                double ellipse = Math.sqrt(normX * normX + normZ * normZ);
                double edgeFeather = 1.0 - smoothstep(0.75, 1.02, ellipse);
                double boxEdge = Math.min(
                        Math.min(x - FLOOR_MIN_X + 1, FLOOR_MAX_X - x),
                        Math.min(z - FLOOR_MIN_Z + 1, FLOOR_MAX_Z - z)) / 3.0;
                double edge = Math.min(edgeFeather, Mth.clamp(boxEdge, 0.0, 1.0));
                if (edge <= 0.005) continue;

                // Subtle indigo undertow + toned-down rose reflections
                double undertow = 0.5 + 0.5 * Math.sin(cx * 0.42 + cz * 0.28 - age * 0.055);
                double spot = Math.exp(-(cx * cx + cz * cz) / 5.2);
                double dropWell = Math.exp(-(dxDrop * dxDrop + dzDrop * dzDrop) / 4.8) * contactGlow;
                double rippleReflection = 0.0;
                if (age >= 30.0 && age < 175.0 && distDrop < 7.5) {
                    double wave = Math.sin(distDrop * 2.6 - (age - 30.0) * 0.22);
                    if (wave > 0.0) {
                        rippleReflection = wave * wave * Math.exp(-distDrop * 0.28) * contactGlow;
                    }
                }

                // Reflections beneath the two rows of 20 colossal blades (X = +/- 4.5, Z in [-16, 18.2])
                double bladeRowDist = Math.min(Math.abs(cx - 4.5), Math.abs(cx + 4.5));
                double bladeZMask = (cz >= -17.0 && cz <= 19.0) ? 1.0 : 0.0;
                double bladeReflection = Math.exp(-(bladeRowDist * bladeRowDist) / 1.65) * bladeZMask * corridorGlow;

                int r = (int) Mth.clamp(7 + undertow * 7 + spot * 22 + dropWell * 36 + rippleReflection * 52 + bladeReflection * 34, 0, 255);
                int g = (int) Mth.clamp(5 + undertow * 5 + spot * 16 + dropWell * 15 + rippleReflection * 22 + bladeReflection * 16, 0, 255);
                int b = (int) Mth.clamp(14 + undertow * 18 + spot * 34 + dropWell * 34 + rippleReflection * 44 + bladeReflection * 32, 0, 255);
                int alpha = (int) Mth.clamp(242.0 * weight * edge * radialReveal, 0.0, 245.0);
                if (alpha <= 2) continue;

                int color = (r << 24) | (g << 16) | (b << 8) | alpha;

                double h00 = vertexHeights[ix][iz];
                double h10 = vertexHeights[ix + 1][iz];
                double h11 = vertexHeights[ix + 1][iz + 1];
                double h01 = vertexHeights[ix][iz + 1];

                Vec3 p00 = worldPoint(origin, left, f, x, h00, z);
                Vec3 p10 = worldPoint(origin, left, f, x + 1, h10, z);
                Vec3 p11 = worldPoint(origin, left, f, x + 1, h11, z + 1);
                Vec3 p01 = worldPoint(origin, left, f, x, h01, z + 1);

                out.add(new BenihimeGeometry.Quad(p00, p10, p11, p01, color));
            }
        }
        return List.copyOf(out);
    }

    /**
     * Builds sparse low-lying sakura petals and soft luminous trails just above the dark floor,
     * preserving a clean, readable central zone around the player and katana drop point.
     */
    public static List<BenihimeGeometry.Quad> surfaceDetails(Vec3 origin, Vec3 forward, double age, BenihimeEntranceGeometry.Ground ground) {
        double weight = stageWeight(age);
        if (weight <= 0.01) return List.of();

        Vec3 f = flatForward(forward);
        Vec3 left = SenbonGeometry.left(f);
        double phase = petalPhase(age);
        double boost = dispersionBoost(age);
        double expandProgress = smoothstep(8.0, ENTRY_END, age);
        if (expandProgress <= 0.01) return List.of();

        var out = new ArrayList<BenihimeGeometry.Quad>(240);

        // 1. Soft Surface Trails (18 sweeping translucent current ribbons outside the central clear zone)
        int trailCount = 18;
        for (int i = 0; i < trailCount; i++) {
            double side = (i % 2 == 0) ? 1.0 : -1.0;
            int idx = i / 2;
            double baseZ = -16.0 + idx * 3.8 + Math.sin(i * 1.7 + phase * 0.8) * 1.4;
            double baseX = side * (2.3 + (idx % 3) * 1.95);
            double length = 2.4 + (i % 3) * 0.85;
            int segments = 6;

            Vec3 prevL = null, prevR = null;
            int prevColor = 0;
            for (int s = 0; s <= segments; s++) {
                double u = s / (double) segments;
                double z = baseZ + (u - 0.5) * length;
                double waveX = Math.sin(u * Math.PI * 1.5 + phase * 1.4 + i) * 0.28;
                double x = baseX + waveX;

                // Keep central player & katana drop zone legible
                if (x * x + z * z < 3.2) continue;

                double h = safeGround(ground, x, z) * weight + 0.042 + boost * 0.04 * u;
                double width = 0.045 * Math.sin(u * Math.PI);
                Vec3 center = worldPoint(origin, left, f, x, h, z);
                Vec3 curL = center.add(left.scale(-width));
                Vec3 curR = center.add(left.scale(width));

                double taper = Math.sin(u * Math.PI);
                int alpha = (int) (88.0 * weight * expandProgress * taper * (1.0 + 0.25 * boost));
                int r = 225, g = 115 + (i % 3) * 18, b = 180;
                int color = (r << 24) | (g << 16) | (b << 8) | Mth.clamp(alpha, 0, 255);

                if (prevL != null && alpha > 2) {
                    out.add(new BenihimeGeometry.Quad(prevL, prevR, curR, curL, (prevColor & 0xFFFFFF00) | alpha));
                }
                prevL = curL;
                prevR = curR;
                prevColor = color;
            }
        }

        // 2. Sparse Low-Lying Sakura Petals (44 delicate sculpted petals gliding near the surface)
        int petalCount = 44;
        for (int i = 0; i < petalCount; i++) {
            double golden = i * 2.399963229728653;
            double radius = 2.1 + (i / (double) petalCount) * 8.8;
            double angle = golden + phase * (0.35 + (i % 5) * 0.06);
            double x = Math.cos(angle) * radius * 0.92;
            double z = Math.sin(angle) * radius * 1.35 - 2.0;

            // Keep center around player (0,0) and katana drop (DROP_X, DROP_Z) completely clear
            double distCenterSq = x * x + z * z;
            double distDropSq = (x - DROP_X) * (x - DROP_X) + (z - DROP_Z) * (z - DROP_Z);
            if (distCenterSq < 3.4 || distDropSq < 2.8) continue;

            double bob = 0.065 + 0.045 * Math.sin(phase * 2.2 + i * 0.9) + boost * (0.12 + (i % 4) * 0.08);
            double h = safeGround(ground, x, z) * weight + bob;
            double size = 0.13 + (i % 4) * 0.032;

            Vec3 center = worldPoint(origin, left, f, x, h, z);
            double rot = angle + phase * 1.6 + i;
            Vec3 ax = left.scale(Math.cos(rot)).add(f.scale(Math.sin(rot))).add(0, 0.18 * Math.sin(rot), 0).normalize();
            Vec3 az = left.scale(-Math.sin(rot)).add(f.scale(Math.cos(rot))).normalize();

            int alpha = (int) (185.0 * weight * expandProgress);
            int r = 250;
            int g = 155 + (i % 4) * 18;
            int b = 205 + (i % 3) * 12;
            addSakuraPetal(out, center, ax, az, size, r, g, b, alpha);
        }

        return List.copyOf(out);
    }

    /**
     * Builds the curved 3D spatial dark backdrop and multi-layer artistic sakura petal composition.
     * Dimensioned for the 22-block monumental blades and contrapicado camera shots,
     * with feathered outer edges and preserved negative space behind the player and subtitles.
     */
    public static List<BenihimeGeometry.Quad> backdrop(Vec3 origin, Vec3 forward, double age) {
        double weight = backdropWeight(age);
        if (weight <= 0.001) return List.of();

        Vec3 f = flatForward(forward);
        Vec3 left = SenbonGeometry.left(f);
        Vec3 back = f.scale(-1.0); // Points down the corridor behind the player (-Z)
        Vec3 up = new Vec3(0.0, 1.0, 0.0);

        double phase = petalPhase(age);
        double boost = dispersionBoost(age);

        var out = new ArrayList<BenihimeGeometry.Quad>(920);

        // =========================================================================
        // 1. Curved 3D Spatial Sanctuary Dome / Sky-Wall (No visible panel edges)
        // =========================================================================
        int azSteps = 26;
        int elSteps = 16;
        double maxAzimuth = Math.PI * 0.85; // +/- 153 degrees wrap-around
        double minY = -2.2;
        double maxY = 40.0;
        double baseRadius = 30.0;

        for (int ia = 0; ia < azSteps; ia++) {
            double u0 = (ia / (double) azSteps) * 2.0 - 1.0;
            double u1 = ((ia + 1) / (double) azSteps) * 2.0 - 1.0;
            double theta0 = u0 * maxAzimuth;
            double theta1 = u1 * maxAzimuth;
            double uMid = 0.5 * (u0 + u1);

            // Smooth lateral feathering so side edges never appear as a hard line
            double azFeather = 1.0 - smoothstep(0.74, 1.0, Math.abs(uMid));
            if (azFeather <= 0.005) continue;

            for (int ie = 0; ie < elSteps; ie++) {
                double v0 = ie / (double) elSteps;
                double v1 = (ie + 1) / (double) elSteps;
                double vMid = 0.5 * (v0 + v1);

                double y0 = Mth.lerp(v0, minY, maxY);
                double y1 = Mth.lerp(v1, minY, maxY);

                // Curve the sanctuary vault overhead above Y=12 so contrapicado shots look into a closed dark vault
                double dome0 = smoothstep(0.30, 1.0, v0);
                double dome1 = smoothstep(0.30, 1.0, v1);
                double r0 = baseRadius * (1.0 - 0.88 * dome0 * dome0);
                double r1 = baseRadius * (1.0 - 0.88 * dome1 * dome1);

                // Vertical feathering only at the sub-floor base; keep the overhead zenith solid!
                double topFeather = 1.0 - 0.12 * smoothstep(0.88, 1.0, vMid);
                double bottomFeather = smoothstep(0.0, 0.05, vMid);
                double cellWeight = weight * azFeather * topFeather * bottomFeather;
                if (cellWeight <= 0.005) continue;

                // Negative space preservation behind the player & subtitles (central lower corridor)
                double centerFocus = Math.exp(-(uMid * uMid) / 0.06) * (1.0 - smoothstep(0.05, 0.32, vMid));
                double aurora = (0.5 + 0.5 * Math.sin(uMid * 4.5 + vMid * 3.2 - phase * 0.6)) * (1.0 - centerFocus);
                double roseMist = Math.exp(-Math.pow(vMid - 0.34, 2) / 0.08) * (1.0 - 0.75 * centerFocus);
                // Subtle indigo-violet silhouette backlight directly behind player's head & torso for crisp contrast
                double silhouetteGlow = Math.exp(-(uMid * uMid) / 0.024) * Math.exp(-Math.pow(vMid - 0.11, 2) / 0.0055);

                int r = (int) Mth.clamp(5 + aurora * 10 + roseMist * (16 + boost * 10) + silhouetteGlow * 22, 0, 255);
                int g = (int) Mth.clamp(4 + aurora * 6 + roseMist * 6 + silhouetteGlow * 16, 0, 255);
                int b = (int) Mth.clamp(11 + aurora * 22 + roseMist * (26 + boost * 8) + silhouetteGlow * 38, 0, 255);
                int alpha = (int) Mth.clamp(238.0 * cellWeight, 0.0, 244.0);
                if (alpha <= 2) continue;

                int color = (r << 24) | (g << 16) | (b << 8) | alpha;

                Vec3 p00 = domePoint(origin, left, back, theta0, y0, r0);
                Vec3 p10 = domePoint(origin, left, back, theta1, y0, r0);
                Vec3 p11 = domePoint(origin, left, back, theta1, y1, r1);
                Vec3 p01 = domePoint(origin, left, back, theta0, y1, r1);

                out.add(new BenihimeGeometry.Quad(p00, p10, p11, p01, color));
            }
        }

        // =========================================================================
        // 2. Gossamer Spiritual Mist Ribbons in Mid-Depth (8 sweeping arcs)
        // =========================================================================
        for (int i = 0; i < 8; i++) {
            double side = (i % 2 == 0) ? 1.0 : -1.0;
            int idx = i / 2;
            double baseTheta = side * (0.28 + idx * 0.22);
            double radius = 24.5 - idx * 1.4;
            int segs = 8;

            Vec3 prevA = null, prevB = null;
            for (int s = 0; s <= segs; s++) {
                double t = s / (double) segs;
                double y = 2.0 + t * 24.0;
                double theta = baseTheta + side * Math.sin(t * Math.PI + phase * 0.45 + i) * 0.14;
                double r = radius * (1.0 - 0.25 * smoothstep(0.4, 1.0, t));
                Vec3 center = domePoint(origin, left, back, theta, y, r);
                Vec3 tangent = left.scale(Math.cos(theta)).add(f.scale(Math.sin(theta))).normalize();
                double halfW = (0.65 + 0.45 * Math.sin(t * Math.PI)) * (1.0 + 0.15 * boost);

                Vec3 curA = center.add(tangent.scale(-halfW));
                Vec3 curB = center.add(tangent.scale(halfW));

                double taper = Math.sin(t * Math.PI);
                int alpha = (int) (42.0 * weight * taper * (1.0 + 0.35 * boost));
                if (prevA != null && alpha > 2) {
                    int col = (165 << 24) | (62 << 16) | (128 << 8) | Mth.clamp(alpha, 0, 255);
                    out.add(new BenihimeGeometry.Quad(prevA, prevB, curB, curA, col));
                }
                prevA = curA;
                prevB = curB;
            }
        }

        // =========================================================================
        // 3. Far Layer: Giant Soft Sakura Petals (18 monumental background blooms)
        // =========================================================================
        int farCount = 18;
        for (int i = 0; i < farCount; i++) {
            double side = (i % 2 == 0) ? 1.0 : -1.0;
            int idx = i / 2;
            // Distribute in sweeping arcs flanking the corridor & arching overhead, avoiding center negative space
            double baseTheta = side * (0.22 + (idx % 5) * 0.19) + Math.sin(phase * 0.35 + i) * 0.06;
            double baseY = 4.5 + idx * 3.1 + ((phase * 1.8 + i * 2.7) % 9.0);
            if (baseY > 31.0) baseY -= 26.5;

            // Negative space mask behind player & subtitles (|theta| < 0.20 and Y < 7.0)
            double negSpaceMask = negativeSpaceMask(baseTheta, baseY);
            if (negSpaceMask <= 0.05) continue;

            double domeFactor = smoothstep(12.0, 36.0, baseY);
            double radius = (27.5 - (i % 3) * 0.8) * (1.0 - 0.72 * domeFactor * domeFactor);
            Vec3 center = domePoint(origin, left, back, baseTheta, baseY, radius);

            double rot = i * 1.15 + phase * 0.42 * side;
            Vec3 tangent = left.scale(Math.cos(baseTheta)).add(f.scale(Math.sin(baseTheta))).normalize();
            Vec3 ax = tangent.scale(Math.cos(rot)).add(up.scale(Math.sin(rot))).normalize();
            Vec3 ay = tangent.scale(-Math.sin(rot)).add(up.scale(Math.cos(rot))).normalize();

            double size = 2.1 + (i % 4) * 0.48;
            int haloAlpha = (int) (34.0 * weight * negSpaceMask * (1.0 + 0.25 * boost));
            int bodyAlpha = (int) (68.0 * weight * negSpaceMask * (1.0 + 0.25 * boost));
            int coreAlpha = (int) (96.0 * weight * negSpaceMask * (1.0 + 0.30 * boost));

            // Soft outer halo + sculpted sakura body + luminous core
            addSakuraPetal(out, center.add(back.scale(0.15)), ax, ay, size * 1.38, 125, 42, 98, haloAlpha);
            addSakuraPetal(out, center, ax, ay, size, 198, 78, 146, bodyAlpha);
            addSakuraPetal(out, center.subtract(back.scale(0.12)), ax, ay, size * 0.58, 242, 138, 190, coreAlpha);
        }

        // =========================================================================
        // 4. Mid Layer: Medium Flowing Sakura Petals in Depth (36 petals)
        // =========================================================================
        int midCount = 36;
        for (int i = 0; i < midCount; i++) {
            double side = (i % 2 == 0) ? 1.0 : -1.0;
            double golden = i * 2.399963229728653;
            double theta = side * (0.14 + ((i * 0.17) % 0.92)) + Math.sin(phase * 0.65 + golden) * 0.08;
            double y = 2.2 + ((i * 1.85 + phase * 3.2) % 26.5);

            double negSpaceMask = negativeSpaceMask(theta, y);
            if (negSpaceMask <= 0.05) continue;

            double domeFactor = smoothstep(12.0, 34.0, y);
            double radius = (20.5 + (i % 5) * 0.9) * (1.0 - 0.65 * domeFactor * domeFactor);
            Vec3 center = domePoint(origin, left, back, theta, y, radius);

            double rot = golden + phase * 0.85 * side;
            Vec3 tangent = left.scale(Math.cos(theta)).add(f.scale(Math.sin(theta))).normalize();
            Vec3 ax = tangent.scale(Math.cos(rot)).add(up.scale(Math.sin(rot))).normalize();
            Vec3 ay = tangent.scale(-Math.sin(rot)).add(up.scale(Math.cos(rot))).add(back.scale(0.25 * Math.sin(rot))).normalize();

            double size = 0.82 + (i % 4) * 0.18;
            int alpha = (int) (122.0 * weight * negSpaceMask * (1.0 + 0.25 * boost));
            int r = 238 + (i % 3) * 6;
            int g = 128 + (i % 4) * 16;
            int b = 182 + (i % 3) * 14;
            addSakuraPetal(out, center, ax, ay, size, r, g, b, alpha);
        }

        // =========================================================================
        // 5. Near-Corridor Layer: Smaller Crisp Depth Petals (44 petals)
        // =========================================================================
        int nearCount = 44;
        for (int i = 0; i < nearCount; i++) {
            double side = (i % 2 == 0) ? 1.0 : -1.0;
            double golden = i * 1.61803398875;
            double theta = side * (0.16 + ((i * 0.13) % 0.85)) + Math.cos(phase * 0.95 + i) * 0.09;
            double y = 1.6 + ((i * 1.45 + phase * 4.6) % 23.5);

            double negSpaceMask = negativeSpaceMask(theta, y);
            if (negSpaceMask <= 0.05) continue;

            double domeFactor = smoothstep(11.0, 30.0, y);
            double radius = (13.8 + (i % 5) * 0.85) * (1.0 - 0.55 * domeFactor * domeFactor);
            Vec3 center = domePoint(origin, left, back, theta, y, radius);

            double rot = golden * 3.14 + phase * 1.35 * side;
            Vec3 tangent = left.scale(Math.cos(theta)).add(f.scale(Math.sin(theta))).normalize();
            Vec3 ax = tangent.scale(Math.cos(rot)).add(up.scale(Math.sin(rot))).normalize();
            Vec3 ay = tangent.scale(-Math.sin(rot)).add(up.scale(Math.cos(rot))).normalize();

            double size = 0.34 + (i % 4) * 0.075;
            int alpha = (int) (165.0 * weight * negSpaceMask * (1.0 + 0.20 * boost));
            int r = 255;
            int g = 178 + (i % 4) * 14;
            int b = 216 + (i % 3) * 11;
            addSakuraPetal(out, center, ax, ay, size, r, g, b, alpha);
        }

        return List.copyOf(out);
    }

    /**
     * Preserves clean negative space directly behind the player silhouette and subtitles
     * (central corridor axis |theta| < 0.22 rad, height Y < 6.5 blocks).
     */
    public static double negativeSpaceMask(double theta, double y) {
        double horizClear = smoothstep(0.11, 0.26, Math.abs(theta));
        double vertClear = smoothstep(4.2, 7.2, y);
        return Math.max(horizClear, vertClear);
    }

    /**
     * Adds an articulated 2-quad sakura petal with a classic tapered base and notched/cleft apex.
     */
    private static void addSakuraPetal(List<BenihimeGeometry.Quad> out, Vec3 center, Vec3 axisLong, Vec3 axisWide,
                                       double size, int r, int g, int b, int alpha) {
        if (alpha <= 2) return;
        int colMain = (Mth.clamp(r, 0, 255) << 24)
                | (Mth.clamp(g, 0, 255) << 16)
                | (Mth.clamp(b, 0, 255) << 8)
                | Mth.clamp(alpha, 0, 255);
        int colTip = (Mth.clamp(r + 12, 0, 255) << 24)
                | (Mth.clamp(g + 22, 0, 255) << 16)
                | (Mth.clamp(b + 16, 0, 255) << 8)
                | Mth.clamp((int) (alpha * 0.88), 0, 255);

        Vec3 base = center.subtract(axisLong.scale(size * 0.85));
        Vec3 midLeft = center.subtract(axisLong.scale(size * 0.05)).subtract(axisWide.scale(size * 0.58));
        Vec3 midRight = center.subtract(axisLong.scale(size * 0.05)).add(axisWide.scale(size * 0.58));
        Vec3 notchCenter = center.add(axisLong.scale(size * 0.52));
        Vec3 tipLeft = center.add(axisLong.scale(size * 0.85)).subtract(axisWide.scale(size * 0.28));
        Vec3 tipRight = center.add(axisLong.scale(size * 0.85)).add(axisWide.scale(size * 0.28));

        // Left lobe (base -> midLeft -> tipLeft -> notchCenter)
        out.add(new BenihimeGeometry.Quad(base, midLeft, tipLeft, notchCenter, colMain));
        // Right lobe (base -> notchCenter -> tipRight -> midRight)
        out.add(new BenihimeGeometry.Quad(base, notchCenter, tipRight, midRight, colTip));
    }

    private static double[][] buildVertexHeightGrid(BenihimeEntranceGeometry.Ground ground, double weight) {
        int cols = FLOOR_MAX_X - FLOOR_MIN_X;
        int rows = FLOOR_MAX_Z - FLOOR_MIN_Z;
        double[][] raw = new double[cols + 1][rows + 1];
        for (int ix = 0; ix < cols; ix++) {
            int x = FLOOR_MIN_X + ix;
            double cx = x + 0.5;
            for (int iz = 0; iz < rows; iz++) {
                int z = FLOOR_MIN_Z + iz;
                double cz = z + 0.5;
                raw[ix][iz] = safeGround(ground, cx, cz);
            }
        }

        double[][] vertices = new double[cols + 1][rows + 1];
        for (int ix = 0; ix <= cols; ix++) {
            int x = FLOOR_MIN_X + ix;
            for (int iz = 0; iz <= rows; iz++) {
                int z = FLOOR_MIN_Z + iz;
                // Shared vertex height blends local neighborhood and slightly lifts over steps to prevent block poke-through
                double sum = 0.0;
                double maxLocal = -10.0;
                int count = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    int nx = Mth.clamp(ix + dx, 0, cols - 1);
                    for (int dz = -1; dz <= 1; dz++) {
                        int nz = Mth.clamp(iz + dz, 0, rows - 1);
                        double v = raw[nx][nz];
                        sum += v;
                        if (v > maxLocal) maxLocal = v;
                        count++;
                    }
                }
                double avg = sum / count;
                double terrainH = Mth.lerp(0.55, avg, maxLocal);
                // Ensure the shared vertex never dips below the maximum height of the 4 cells sharing this corner
                double maxCellCorner = -10.0;
                for (int cx = -1; cx <= 0; cx++) {
                    int nx = Mth.clamp(ix + cx, 0, cols - 1);
                    for (int cz = -1; cz <= 0; cz++) {
                        int nz = Mth.clamp(iz + cz, 0, rows - 1);
                        if (raw[nx][nz] > maxCellCorner) maxCellCorner = raw[nx][nz];
                    }
                }
                terrainH = Math.max(terrainH, maxCellCorner);

                // Keep the immediate katana sinking locus (DROP_X, DROP_Z) level so the sinking blade & ripples stay crisp
                double dxDrop = x - DROP_X;
                double dzDrop = z - DROP_Z;
                double dropFlatten = smoothstep(0.8, 2.4, Math.sqrt(dxDrop * dxDrop + dzDrop * dzDrop));

                // Small vertical offset above block collider to eliminate z-fighting
                double normX = x / 15.0;
                double normZ = (z + 5.0) / 27.0;
                double border = smoothstep(0.75, 1.0, Math.sqrt(normX * normX + normZ * normZ));
                double lift = Mth.lerp(border, 0.026, 0.010);

                vertices[ix][iz] = terrainH * dropFlatten * weight + lift;
            }
        }
        return vertices;
    }

    private static double safeGround(BenihimeEntranceGeometry.Ground ground, double x, double z) {
        if (ground == null) return 0.0;
        double y = ground.height(x, z);
        if (!Double.isFinite(y)) return 0.0;
        // Ignore tall vertical walls (> 1.15 blocks above anchor) so the floor stays on the ground instead of climbing walls
        if (y > 1.15) return 0.0;
        return Mth.clamp(y, -1.25, 1.05);
    }

    private static Vec3 flatForward(Vec3 forward) {
        Vec3 flat = new Vec3(forward.x, 0.0, forward.z);
        return flat.lengthSqr() > 1.0E-5 ? flat.normalize() : new Vec3(0.0, 0.0, 1.0);
    }

    private static Vec3 worldPoint(Vec3 origin, Vec3 left, Vec3 forward, double x, double y, double z) {
        return origin.add(left.scale(x)).add(0.0, y, 0.0).add(forward.scale(z));
    }

    private static Vec3 domePoint(Vec3 origin, Vec3 left, Vec3 back, double theta, double y, double radius) {
        double lx = Math.sin(theta) * radius;
        double bz = Math.cos(theta) * radius;
        return origin.add(left.scale(lx)).add(0.0, y, 0.0).add(back.scale(bz));
    }
}
