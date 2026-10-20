package com.bleachmod.common.visual;

import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;
import static com.bleachmod.common.visual.BenihimeEntranceTimeline.progress;

/**
 * Native, deterministic vertical slice of study 67. Local +Z points behind the player.
 * Opaque curtain, fixed-length arm IK, articulated grasp and two alternating floor plants.
 * This is authored procedural geometry, not a GeckoLib export or a cloth physics solver.
 */
public final class BenihimeEntranceGeometry {
    private static final Vec3 UP = new Vec3(0, 1, 0);
    private static final int SKIN = 0xE8D3C1FF, HAIR = 0x18161FFF;
    private static final int RED = 0x91192BFF, LINING = 0x5B1022FF, GOLD = 0xC08A80FF;
    public static final double UPPER_ARM = 3.15, FOREARM = 3.30;
    private BenihimeEntranceGeometry() { }

    /** Local-space rig anchors are also available to the deterministic QA laboratory. */
    public record Arm(Vec3 shoulder, Vec3 elbow, Vec3 wrist) { }
    public record Rig(Vec3 waist, Vec3 chest, Vec3 neck, Vec3 head, Arm left, Arm right,
                      double rise, double grip) { }

    public static double curtainGap(double ticks, double height) {
        double crack = progress(ticks, 139, 151);
        double local = .55 + .45 * Math.exp(-Math.pow((height - 4.6) / 2.3, 2));
        double gap = -.045 * (1 - crack) + .36 * crack
                + 2.5 * progress(ticks, 176, 236) * local;
        return gap + (5.8 - gap) * progress(ticks, 236, 320);
    }
    public static Vec3 hemAnchor(double ticks) {
        return new Vec3(-curtainGap(ticks, 4.6), 4.6, 12.1 - .65 * grip(ticks));
    }
    private static double grip(double ticks) {
        return progress(ticks, 158, 174) * (1 - progress(ticks, 236, 256));
    }
    private static Vec3 v(double x, double y, double z) { return new Vec3(x, y, z); }
    private static Vec3 key(double ticks, double[] times, Vec3... points) {
        if (ticks <= times[0]) return points[0];
        for (int i = 1; i < times.length; i++)
            if (ticks <= times[i]) return points[i - 1].lerp(points[i], progress(ticks, times[i - 1], times[i]));
        return points[points.length - 1];
    }
    @FunctionalInterface public interface Ground { double height(double x, double z); }
    public static final Ground FLAT = (x,z) -> 0;
    public static Rig sample(double ticks) { return sample(ticks,FLAT); }
    public static Rig sample(double ticks, Ground ground) {
        ticks = BenihimeEntranceTimeline.clamp(ticks);
        double seconds = ticks / 20;
        // Shift root only as each opposite arm takes weight. Floor targets below stay world-locked.
        double offset = 8 - 4 * progress(ticks, 226, 260)
                - 2 * progress(ticks, 280, 308) - 2 * progress(ticks, 316, 340);
        double rise = progress(ticks, 376, 446);
        double weightShift = .18 * Math.sin(Math.PI * progress(ticks, 280, 308))
                - .18 * Math.sin(Math.PI * progress(ticks, 316, 340));
        double breathe = Math.sin(seconds * 1.1) * .018;
        Vec3 shift = v(weightShift, breathe, offset);
        Vec3 waist = v(0, 2.25 + .12 * progress(ticks, 376, 406), 11.1).add(shift);
        Vec3 chest = v(0, 4.5, 9.2).lerp(v(0, 7.45, 11), rise).add(shift);
        Vec3 neck = v(0, 5.35, 8.4).lerp(v(0, 8.62, 11), progress(ticks, 382, 448)).add(shift);
        Vec3 head = v(0, 6.15, 8.1).lerp(v(0, 9.47, 10.9), progress(ticks, 388, 450)).add(shift);
        Vec3 leftWrist;
        if (ticks < 240) {
            Vec3 approach = key(ticks, new double[]{0, 141, 158}, v(0, 4.65, 15.4), v(0, 4.65, 15.4), v(.14, 4.65, 13.05));
            leftWrist = approach.lerp(hemAnchor(ticks).add(.55, .05, .95), progress(ticks, 158, 173));
        } else {
            leftWrist = key(ticks, new double[]{240, 260, 280, 308, 344, 360},
                    hemAnchor(240).add(.55, .05, .95), v(-3.3, 2.1, 10.2),
                    v(-3.1, .38, 9.5), v(-3.1, .38, 7.5), v(-3.1, .38, 7.5), v(-3.1, .38, 6.6));
            // Clear floor on the moving half of the stride; fixed endpoints remain planted.
            if (ticks > 280 && ticks < 308) leftWrist = leftWrist.add(0, .42 * Math.sin(Math.PI * progress(ticks, 280, 308)), 0);
            if (ticks > 344 && ticks < 360) leftWrist = leftWrist.add(0, .22 * Math.sin(Math.PI * progress(ticks, 344, 360)), 0);
        }
        Vec3 rightWrist = key(ticks, new double[]{0, 226, 248, 266, 280, 314, 334},
                v(.2, 3.4, 18), v(.2, 3.4, 18), v(1.5, 3.9, 13.2), v(3.9, 1.1, 9.4),
                v(3.1, .38, 8.6), v(3.1, .38, 8.6), v(3.1, .38, 6.6));
        if (ticks > 314 && ticks < 334) rightWrist = rightWrist.add(0, .45 * Math.sin(Math.PI * progress(ticks, 314, 334)), 0);
        // Stagger releases from the floor; avoid the mirrored, rigid arm lift of the old study.
        leftWrist = leftWrist.lerp(v(-2.85, 6.0, 6.3), progress(ticks, 391, 444));
        rightWrist = rightWrist.lerp(v(2.85, 6.0, 6.3), progress(ticks, 404, 454));
        double idle = progress(ticks, 454, 490);
        leftWrist = leftWrist.add(.06 * Math.sin(seconds * .7) * idle, .05 * Math.sin(seconds) * idle, 0);
        rightWrist = rightWrist.add(.06 * Math.sin(seconds * .7 + 1) * idle, .05 * Math.sin(seconds + .8) * idle, 0);
        // Resolve bounded local ground before the arm IK: pelvis and torso share the load.
        double stage = safeGround(ground,0,11.1);
        double lw = progress(ticks,260,280)*(1-progress(ticks,391,444));
        double rw = progress(ticks,266,280)*(1-progress(ticks,404,454));
        double lg = safeGround(ground,leftWrist.x,leftWrist.z), rg = safeGround(ground,rightWrist.x,rightWrist.z);
        leftWrist=leftWrist.add(0,stage*(1-lw)+lg*lw,0);
        rightWrist=rightWrist.add(0,stage*(1-rw)+rg*rw,0);
        double torsoLift=stage+(lg-stage)*lw*.35+(rg-stage)*rw*.35;
        Vec3 lift=v(0,torsoLift,0);waist=waist.add(lift);chest=chest.add(lift);neck=neck.add(lift);head=head.add(lift);
        Vec3 leftShoulder = chest.add(-1.78, .2, 0), rightShoulder = chest.add(1.78, .2, 0);
        leftWrist=reachable(leftShoulder,leftWrist);rightWrist=reachable(rightShoulder,rightWrist);
        return new Rig(waist, chest, neck, head,
                new Arm(leftShoulder, elbow(leftShoulder, leftWrist, -1, rise), leftWrist),
                new Arm(rightShoulder, elbow(rightShoulder, rightWrist, 1, rise), rightWrist), rise, grip(ticks));
    }
    private static double safeGround(Ground ground,double x,double z){
        double y=ground.height(x,z);return Double.isFinite(y)?Math.max(-2,Math.min(2,y)):0;
    }
    private static Vec3 reachable(Vec3 shoulder,Vec3 wrist){
        Vec3 d=wrist.subtract(shoulder);double length=d.length();
        double limit=UPPER_ARM+FOREARM-.025;
        return length>limit?shoulder.add(d.scale(limit/length)):wrist;
    }
    private static Vec3 elbow(Vec3 shoulder, Vec3 wrist, int side, double rise) {
        Vec3 delta = wrist.subtract(shoulder);
        double distance = Math.max(.00001, delta.length());
        Vec3 direction = delta.scale(1 / distance);
        double along = (UPPER_ARM * UPPER_ARM - FOREARM * FOREARM + distance * distance) / (2 * distance);
        // Preserve the reaching/support pose until the rise. The settled upper arms hang
        // down beside the torso while the forearms lift forward to hold the threads.
        Vec3 bend = v(side * .65, .2, .9).lerp(v(side * .22, -1, .25), rise);
        bend = bend.subtract(direction.scale(bend.dot(direction))).normalize();
        return shoulder.add(direction.scale(along)).add(bend.scale(Math.sqrt(Math.max(0, UPPER_ARM * UPPER_ARM - along * along))));
    }

    /** origin is player feet; forward is flattened internally. No world/client state is read. */
    public static List<BenihimeGeometry.Quad> build(Vec3 origin, Vec3 forward, double ticks) {
        return build(origin,forward,ticks,FLAT);
    }
    public static List<BenihimeGeometry.Quad> build(Vec3 origin, Vec3 forward, double ticks, Ground ground) {
        Vec3 flat = v(forward.x, 0, forward.z);
        flat = flat.lengthSqr() > .00001 ? flat.normalize() : v(0, 0, 1);
        Builder b = new Builder(origin, SenbonGeometry.left(flat), flat.scale(-1));
        b.ground=ground;
        ticks = BenihimeEntranceTimeline.clamp(ticks);
        b.curtain(ticks);
        // Delayed materialisation behind an already assembled opaque curtain, never during rise.
        if (ticks >= 108) b.figure(ticks, sample(ticks,ground));
        return List.copyOf(b.faces);
    }

    /** Completed silhouette with restrained secondary movement; idle tick zero matches build(560). */
    public static List<BenihimeGeometry.Quad> buildIdle(Vec3 origin, Vec3 forward, double ticks, int variant) {
        return buildIdle(origin,forward,ticks,variant,FLAT);
    }
    public static List<BenihimeGeometry.Quad> buildIdle(Vec3 origin, Vec3 forward, double ticks, int variant, Ground ground) {
        double elapsed = Double.isFinite(ticks) ? Math.max(0, ticks) : 0;
        Rig pose = sample(BenihimeEntranceTimeline.DURATION,ground);
        double seconds = (BenihimeEntranceTimeline.DURATION + elapsed) / 20;
        double vertical = .018 * (Math.sin(seconds * 1.1) - Math.sin(28 * 1.1));
        Vec3 shift = v(0, vertical, 0);
        Vec3 leftWrist = pose.left().wrist().add(.06 * (Math.sin(seconds * .7) - Math.sin(28 * .7)),
                .05 * (Math.sin(seconds) - Math.sin(28)), 0);
        Vec3 rightWrist = pose.right().wrist().add(.06 * (Math.sin(seconds * .7 + 1) - Math.sin(28 * .7 + 1)),
                .05 * (Math.sin(seconds + .8) - Math.sin(28 + .8)), 0);
        if (variant > 0) {
            double gesture = .22 * Math.sin(elapsed / 25);
            leftWrist = leftWrist.add(-gesture, gesture * .5, -gesture);
            rightWrist = rightWrist.add(gesture, gesture * .5, -gesture);
        }
        Vec3 leftShoulder = pose.left().shoulder().add(shift), rightShoulder = pose.right().shoulder().add(shift);
        Rig idle = new Rig(pose.waist().add(shift), pose.chest().add(shift), pose.neck().add(shift), pose.head().add(shift),
                new Arm(leftShoulder, elbow(leftShoulder, reachable(leftShoulder,leftWrist), -1, 1), reachable(leftShoulder,leftWrist)),
                new Arm(rightShoulder, elbow(rightShoulder, reachable(rightShoulder,rightWrist), 1, 1), reachable(rightShoulder,rightWrist)), 1, 0);
        Vec3 flat = v(forward.x, 0, forward.z);
        flat = flat.lengthSqr() > .00001 ? flat.normalize() : v(0, 0, 1);
        Builder b = new Builder(origin, SenbonGeometry.left(flat), flat.scale(-1));
        b.ground=ground;
        b.figure(BenihimeEntranceTimeline.DURATION + elapsed, idle);
        return List.copyOf(b.faces);
    }

    private static final class Builder {
        final List<BenihimeGeometry.Quad> faces = new ArrayList<>(6000);
        final Vec3 origin, across, behind;
        boolean subject;
        Ground ground=FLAT;
        Builder(Vec3 origin, Vec3 across, Vec3 behind) { this.origin = origin; this.across = across; this.behind = behind; }
        Vec3 world(Vec3 p) { return origin.add(across.scale(p.x)).add(0, p.y, 0).add(behind.scale(p.z)); }
        void quad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, int rgba) {
            double brightness = 1;
            if (subject) brightness = .105 + .895 * ease((14.4 - (a.z + b.z + c.z + d.z) / 4) / 2.7);
            // Deterministic facet shading supplies form to the untextured emissive native renderer.
            Vec3 normal = b.subtract(a).cross(d.subtract(a)).normalize();
            brightness *= .75 + .25 * Math.abs(normal.dot(v(.25, .85, -.45).normalize()));
            faces.add(new BenihimeGeometry.Quad(world(a), world(b), world(c), world(d), shade(rgba, brightness)));
        }
        void tube(Vec3 a, Vec3 b, double ra, double rb, int color, int segments) {
            Vec3 dir = b.subtract(a).normalize();
            if (dir.lengthSqr() < .1) return;
            Vec3 x = dir.cross(UP);
            if (x.lengthSqr() < .001) x = v(1, 0, 0); else x = x.normalize();
            Vec3 y = dir.cross(x).normalize();
            for (int i = 0; i < segments; i++) {
                double p = i * Math.PI * 2 / segments, q = (i + 1) * Math.PI * 2 / segments;
                Vec3 va = x.scale(Math.cos(p)).add(y.scale(Math.sin(p))), vb = x.scale(Math.cos(q)).add(y.scale(Math.sin(q)));
                quad(a.add(va.scale(ra)), a.add(vb.scale(ra)), b.add(vb.scale(rb)), b.add(va.scale(rb)), color);
            }
        }
        void path(List<Vec3> points, double radius, int color) {
            for (int i = 1; i < points.size(); i++) tube(points.get(i - 1), points.get(i), radius, radius, color, 6);
        }
        void oval(Vec3 center, double rx, double ry, double rz, int color, int slices, int rows) {
            for (int j = 0; j < rows; j++) for (int i = 0; i < slices; i++)
                quad(sphere(center, rx, ry, rz, i, j, slices, rows), sphere(center, rx, ry, rz, i + 1, j, slices, rows),
                        sphere(center, rx, ry, rz, i + 1, j + 1, slices, rows), sphere(center, rx, ry, rz, i, j + 1, slices, rows), color);
        }
        Vec3 sphere(Vec3 c, double rx, double ry, double rz, int i, int j, int n, int rows) {
            double a = i * Math.PI * 2 / n, p = -Math.PI / 2 + j * Math.PI / rows;
            return c.add(rx * Math.cos(p) * Math.cos(a), ry * Math.sin(p), rz * Math.cos(p) * Math.sin(a));
        }
        void curtain(double ticks) {
            if (ticks < 72) return;
            double grow = progress(ticks, 72, 102), gone = progress(ticks, 466, 526);
            if (gone >= 1) return;
            double height = 12.35 * grow;
            // Opaque back, ceiling and sides keep the cavity dark from oblique observing angles.
            quad(v(-8.3, 0, 21), v(8.3, 0, 21), v(8.3, height, 21), v(-8.3, height, 21), 0x030307FF);
            quad(v(-8.3, height, 12.1), v(8.3, height, 12.1), v(8.3, height, 21), v(-8.3, height, 21), 0x030307FF);
            for (int side : new int[]{-1, 1}) {
                quad(v(side * 8.25, 0, 12.1), v(side * 8.25, 0, 21), v(side * 8.25, height, 21), v(side * 8.25, height, 12.1), 0x05050AFF);
                for (int j = 0; j < 18; j++) for (int i = 0; i < 18; i++) {
                    Vec3 a = clothPoint(ticks, side, i / 18., j / 18., grow, gone);
                    Vec3 bb = clothPoint(ticks, side, (i + 1) / 18., j / 18., grow, gone);
                    Vec3 c = clothPoint(ticks, side, (i + 1) / 18., (j + 1) / 18., grow, gone);
                    Vec3 d = clothPoint(ticks, side, i / 18., (j + 1) / 18., grow, gone);
                    int tone = (int) (21 + 7 * Math.sin(i * 1.1 + j * .16));
                    quad(a, bb, c, d, (tone << 24) | ((tone - 1) << 16) | ((tone + 4) << 8) | 255);
                }
                List<Vec3> hem = new ArrayList<>();
                for (int j = 0; j <= 36; j++) hem.add(clothPoint(ticks, side, 0, j / 36., grow, gone));
                path(hem, .026, 0x352931FF);
            }
        }
        Vec3 clothPoint(double t, int side, double u, double h, double grow, double gone) {
            double y = h * 12 * grow, gap = curtainGap(t, y), touch = grip(t);
            double wave = .12 * Math.sin(u * Math.PI * 8 + h * 2) * Math.sin(h * Math.PI)
                    + .24 * touch * (1 - u) * Math.sin(u * Math.PI * 8 + h * 2 - t / 40) * Math.sin(h * Math.PI);
            // Wave vanishes at the shared hem anchor, giving fingers the same exact contact point.
            double pinch = -.65 * touch * Math.exp(-Math.pow((y - 4.6) / .9, 2)) * Math.pow(1 - u, 3);
            return v(side * (gap + (8 - gap) * u + gone * 8), y, 12.1 + wave * Math.sin(u * Math.PI) + pinch);
        }
        List<Vec3> ring(Vec3 center, double rx, double rz, double phase, int count) {
            List<Vec3> result = new ArrayList<>();
            for (int i = 0; i <= count; i++) {
                double a = i * Math.PI * 2 / count;
                result.add(center.add(rx * Math.cos(a) * (1 + .025 * Math.sin(a * 10 + phase)), 0, rz * Math.sin(a) + .05 * Math.sin(a * 8 + phase)));
            }
            return result;
        }
        void join(List<Vec3> a, List<Vec3> b, int color) {
            for (int i = 1; i < a.size(); i++) quad(a.get(i - 1), a.get(i), b.get(i), b.get(i - 1), i % 4 == 0 ? shade(color, .84) : color);
        }
        void figure(double t, Rig rig) {
            subject = true;
            Vec3 waist = rig.waist(), chest = rig.chest(), neck = rig.neck(), head = rig.head();
            double hemLeft=safeGround(ground,-5,waist.z),hemRight=safeGround(ground,5,waist.z);
            double hemFront=safeGround(ground,0,waist.z-4),hemBack=safeGround(ground,0,waist.z+4);
            for (int layer = 0; layer < 3; layer++) {
                double radius = 4.9 - layer * .37;
                List<Vec3> previous = null;
                for (int row = 0; row <= 6; row++) {
                    double h = row / 6.;
                    Vec3 center = waist.add(0, -2.14 + h * 2.26, -layer * .13);
                    List<Vec3> next = ring(center, radius * (1 - h) + 1.32 * h, (radius * (1 - h) + 1.32 * h) * .73, t / 40 - layer * .45, 28);
                    double drape=Math.pow(1-h,3);
                    double floor=waist.y-2.14;
                    next=next.stream().map(q->{
                        double across=Math.max(0,Math.min(1,(q.x+5)/10)),depth=Math.max(0,Math.min(1,(q.z-waist.z+4)/8));
                        double surface=(hemLeft*(1-across)+hemRight*across+hemFront*(1-depth)+hemBack*depth)*.5;
                        return q.add(0,(surface+.11-floor)*drape,0);
                    }).toList();
                    if (previous != null) join(previous, next, layer == 0 ? LINING : layer == 1 ? RED : 0xB52839FF);
                    if (row == 0 && layer > 0) path(next, .022, 0xC8555BFF);
                    previous = next;
                }
            }
            double[] fractions = {0, .22, .5, .8, 1}, widths = {1.32, 1.25, 1.52, 1.95, 1.78};
            List<Vec3> previous = null;
            for (int j = 0; j < fractions.length; j++) {
                List<Vec3> next = ring(waist.lerp(chest, fractions[j]), widths[j], j == 3 ? 1.08 : .87, t / 50, 28);
                if (previous != null) join(previous, next, RED);
                previous = next;
            }
            tube(chest, neck, .64, .34, SKIN, 12);
            for (int side : new int[]{-1, 1}) {
                Vec3 a = neck.add(side * .35, .06, -.3), b = waist.add(side * .28, .65, -1);
                quad(a, b, waist.add(side * .58, .3, -1.05), chest.add(side * 1.28, .22, -.9), 0xD0C0ABFF);
                Vec3 outer = neck.add(side * .54, .1, -.28), hem = waist.add(side * .68, .15, -1.09);
                quad(outer, hem, waist.add(side * 1.03, -.2, -1.02), chest.add(side * 1.6, .2, -.82), 0xB8283AFF);
                path(List.of(outer, hem, waist.add(side * 1.03, -.2, -1.02)), .027, 0xD35960FF);
                for (int i = 1; i <= 8; i++) {
                    Vec3 stitch = outer.lerp(hem, i / 9.);
                    tube(stitch.add(-.055, 0, -.025), stitch.add(.055, .07, -.03), .012, .012, GOLD, 5);
                }
            }
            oval(waist.add(0, .12, -.95), 1.37, .22, .16, LINING, 14, 4);
            faceAndHair(t, head, chest, waist);
            for (int side : new int[]{-1, 1}) {
                Arm arm = side == -1 ? rig.left() : rig.right();
                oval(arm.shoulder(), .49, .57, .49, SKIN, 12, 6);
                tube(arm.shoulder(), arm.elbow(), .43, .29, SKIN, 12);
                oval(arm.elbow(), .29, .31, .29, SKIN, 10, 5);
                // Inset articulated doll joint and fine seam instead of a faceted stick hinge.
                Vec3 hinge=arm.elbow().add(0,0,-.20);
                oval(hinge,.17,.18,.10,0xC39D94FF,12,6);
                tube(hinge.add(-.16,0,-.02),hinge.add(.16,0,-.02),.014,.014,0x855C5EFF,6);
                tube(arm.elbow(), arm.wrist(), .29, .20, SKIN, 12);
                hand(t, side, arm.wrist(), rig.rise());
            }
            subject = false;
        }
        void faceAndHair(double t, Vec3 head, Vec3 chest, Vec3 waist) {
            double[][] profile = {{-1.08,.14,.18},{-.9,.36,.36},{-.65,.54,.46},{-.32,.68,.54},{.04,.74,.59},{.38,.73,.59},{.70,.67,.55},{.96,.51,.43},{1.14,.15,.16}};
            List<Vec3> previous = null;
            for (double[] row : profile) {
                List<Vec3> next = ring(head.add(0, row[0], 0), row[1], row[2], 0, 24);
                if (previous != null) join(previous, next, SKIN);
                previous = next;
            }
            tube(head.add(0, .13, -.60), head.add(0, -.23, -.69), .055, .035, 0xD4B6A3FF, 8);
            for (int side : new int[]{-1, 1}) {
                path(List.of(head.add(side * .13, -.015, -.598), head.add(side * .34, -.04, -.55), head.add(side * .52, -.015, -.455)), .014, 0x3E2831FF);
                path(List.of(head.add(side * .16, .27, -.58), head.add(side * .34, .29, -.545), head.add(side * .51, .27, -.46)), .012, 0x4E3339FF);
            }
            path(List.of(head.add(-.14, -.50, -.50), head.add(0, -.51, -.53), head.add(.14, -.50, -.50)), .014, 0x894E52FF);
            oval(head.add(0, .18, .54), 1.02, 1.39, .65, HAIR, 18, 9);
            oval(head.add(0, 1.02, .10), .64, .55, .58, HAIR, 16, 8);
            quad(head.add(-.51, 1.10, -.43), head.add(.51, 1.10, -.43), head.add(.50, .78, -.48), head.add(-.50, .78, -.48), HAIR);
            for (int side : new int[]{-1, 1}) {
                path(List.of(head.add(side * .06, 1.09, -.15), head.add(side * .40, 1, -.46), head.add(side * .73, .68, -.42), head.add(side * .86, .13, -.29), head.add(side * .85, -.66, -.12)), .19, HAIR);
                for (int coil = 0; coil < 3; coil++) {
                    List<Vec3> loop = new ArrayList<>();
                    for (int i = 0; i <= 20; i++) {
                        double a = i * Math.PI / 10;
                        loop.add(head.add(side * (.73 + coil * .15 + (.35 + coil * .07) * Math.cos(a)), 1.05 + coil * .14 + (.42 + coil * .06) * Math.sin(a), .25 + coil * .10 + .16 * Math.cos(a)));
                    }
                    path(loop, .17, HAIR);
                }
                for (int lock = 0; lock < 5; lock++) {
                    double lag = .09 * Math.sin(t / 27 - lock * .4) * progress(t, 240, 320);
                    List<Vec3> strand = List.of(head.add(side * (.86 + .13 * lock), .65, .02 + .17 * lock),
                            head.add(side * (1 + .13 * lock), -.35, -.06), chest.add(side * (1.15 + .18 * lock), -.15, -.96),
                            waist.add(side * (1.24 + .21 * lock) + lag, -.78, -1.12));
                    path(strand, .11 + lock * .018, HAIR);
                    if (lock % 2 == 0) path(strand.stream().map(q -> q.add(0, .025, -.09)).toList(), .008, 0x432F3AFF);
                }
            }
        }
        Vec3 handPoint(Vec3 wrist, Vec3 local, double roll) {
            return wrist.add(local.x * Math.cos(roll) - local.y * Math.sin(roll), local.x * Math.sin(roll) + local.y * Math.cos(roll), local.z);
        }
        void hand(double t, int side, Vec3 wrist, double rise) {
            double roll = side == -1 ? -Math.PI / 2 * progress(t, 138, 162) * (1 - progress(t, 240, 280)) : .12 * progress(t, 226, 248) * (1 - progress(t, 248, 280));
            double g = side == -1 ? grip(t) : 0;
            Vec3 palm = handPoint(wrist, v(0, -.2, -.42), roll);
            // Palm ellipsoid follows the roll through a local surface transform.
            for (int j = 0; j < 5; j++) for (int i = 0; i < 12; i++) {
                Vec3[] points = new Vec3[4]; int[][] indices = {{i,j},{i+1,j},{i+1,j+1},{i,j+1}};
                for (int k = 0; k < 4; k++) points[k] = handPoint(wrist, sphere(v(0,-.2,-.42), .48,.17,.56,indices[k][0],indices[k][1],12,5),roll);
                quad(points[0],points[1],points[2],points[3],SKIN);
            }
            Vec3 node = hemAnchor(t);
            double groundY=safeGround(ground,wrist.x,wrist.z);
            for (int finger = 0; finger < 4; finger++) {
                double x = (finger - 1.5) * .2, length = new double[]{.88,.99,.93,.76}[finger];
                double flex = rise * .5 + .2 * progress(t, 136 + finger, 154 + finger) * (1 - progress(t, 158, 170));
                Vec3 root = v(x, -.21, -.75);
                Vec3 a = root.add(0, -length * .4 * Math.sin(flex), -length * .4 * Math.cos(flex));
                Vec3 b = a.add(0, -length * .34 * Math.sin(flex * 1.7), -length * .34 * Math.cos(flex * 1.7));
                Vec3 tip = b.add(0, -length * .25 * Math.sin(flex * 2.2), -length * .25 * Math.cos(flex * 2.2));
                // Pads settle on the reference floor while the palm bears weight.
                double grounded=ease((.9-(wrist.y-groundY))/.45)*(1-rise);
                a=a.lerp(v(a.x,-.29,a.z),grounded);b=b.lerp(v(b.x,-.32,b.z),grounded);tip=tip.lerp(v(tip.x,-.33,tip.z),grounded);
                double weight = g * progress(t, 158 + finger, 172 + finger);
                double y = 4.65 - x;
                Vec3[] natural = {root,a,b,tip}, target = {node.add(.335,y-node.y,.18),node.add(.22,y-node.y,-.14),node.add(-.10,y-node.y,-.28),node.add(.02,y-node.y,.02)};
                Vec3[] fingerPoints = new Vec3[4];
                for (int k = 0; k < 4; k++) fingerPoints[k] = handPoint(wrist,natural[k],roll).lerp(target[k],weight);
                for (int k = 1; k < 4; k++) tube(fingerPoints[k-1],fingerPoints[k],.085-k*.01,.075-k*.01,SKIN,8);
                for (int k = 0; k < 3; k++) oval(fingerPoints[k],.072,.06,.072,SKIN,8,4);
                tube(fingerPoints[3].add(0,.045,.01),fingerPoints[2].lerp(fingerPoints[3],.65).add(0,.05,0),.035,.039,0xF1DED1FF,6);
                if (t > 450) {
                    Vec3 end = v(side * .3 + x * 2, 1.28, 0), start = fingerPoints[3];
                    Vec3 partial = start.lerp(end, progress(t, 450 + finger * 2, 480 + finger * 2));
                    path(List.of(start,start.lerp(partial,.5).add(.04*Math.sin(t/20+finger),0,0),partial),.009,0xBA4053DD);
                }
            }
            Vec3 thumbRoot = handPoint(wrist,v(side*.39,-.22,-.29),roll);
            Vec3 thumbA = handPoint(wrist,v(side*.62,-.28,-.57),roll).lerp(node.add(.42,.30,.30),g);
            Vec3 thumbB = handPoint(wrist,v(side*.72,-.36-rise*.30,-.81),roll).lerp(node.add(.12,.27,.13),g);
            if(wrist.y-groundY<.9&&rise<.1)thumbB=thumbB.add(0,.04*ease((.9-(wrist.y-groundY))/.45),0);
            tube(thumbRoot,thumbA,.11,.085,SKIN,8);tube(thumbA,thumbB,.085,.055,SKIN,8);
            // Fine wrist seams reinforce the reconstructed, stitched identity.
            for (double line : new double[]{-.07,.06})
                tube(handPoint(wrist,v(-.22,.065,line),roll),handPoint(wrist,v(.22,.065,line),roll),.01,.01,0x855C5EFF,5);
        }
    }
    private static double ease(double t) { t = Math.max(0, Math.min(1, t)); return t * t * (3 - 2 * t); }
    private static int shade(int rgba, double value) {
        return ((int)((rgba >>> 24) * value) << 24) | ((int)(((rgba >>> 16) & 255) * value) << 16)
                | ((int)(((rgba >>> 8) & 255) * value) << 8) | (rgba & 255);
    }
}
