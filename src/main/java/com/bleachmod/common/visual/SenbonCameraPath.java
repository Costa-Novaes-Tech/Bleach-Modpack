package com.bleachmod.common.visual;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Authoritative continuous camera path for Byakuya Kuchiki's Bankai: Senbonzakura Kageyoshi.
 * Coordinates are relative to caster: X lateral (left/right), Y vertical (up), Z longitudinal (corridor forward).
 *
 * Guarantees C0 position/focus continuity (matching boundary endpoints between all segments)
 * and C1 smooth velocity curve (ease without angular or positional jumps).
 */
public final class SenbonCameraPath {
    public static final double RETURN_START = 266.0;

    public record Frame(Vec3 eye, Vec3 focus, float fov) { }

    private record Key(double tick, Vec3 eye, Vec3 focus, float fov) { }

    private static Key key(double t, double x, double y, double z, double fx, double fy, double fz, float fov) {
        return new Key(t, new Vec3(x, y, z), new Vec3(fx, fy, fz), fov);
    }

    private static final Key[] KEYS = {
            // 0..10: Plano médio frontal mostrando rosto, mão e katana invertida
            key(0.0,   0.40, 1.45, 2.20,   0.18, 1.10, 0.20,   48.0F),
            key(10.0,  0.40, 1.45, 2.20,   0.18, 1.10, 0.20,   48.0F),

            // 10..35: Acompanhamento controlado da queda preservando conexão mão-espada-solo
            key(35.0,  0.75, 1.20, 2.40,   0.32, 0.30, 0.35,   50.0F),

            // 35..76: Tomada superior oblíqua da espada afundando e das ondulações concêntricas de Reishi
            key(76.0,  1.25, 1.65, 3.60,   0.25, 0.35, 0.25,   54.0F),

            // 76..112: Contrapicado acompanhando a ascensão das 20 lâminas gigantescas emergindo do solo
            key(112.0, 0.20, 0.75, 5.20,   0.00, 9.50, -2.00,  62.0F),

            // 112..160: Revelação do corredor simétrico e profundidade, descendo suavemente até Byakuya
            key(136.0, 0.10, 1.10, 5.20,   0.00, 6.50, -1.00,  58.0F),
            key(160.0, 0.00, 1.45, 5.20,   0.00, 1.35, 0.00,   50.0F),

            // 160..228: Composição frontal nobre e estável com aproximação discreta durante o nome
            key(228.0, 0.00, 1.42, 4.40,   0.00, 1.35, 0.00,   46.0F),

            // 228..266: Recuo moderado acompanhando a conversão das lâminas em tufões ciclônicos de pétalas
            key(RETURN_START, 0.00, 2.20, 7.60, 0.00, 1.35, 0.00, 58.0F)
    };

    private SenbonCameraPath() { }

    public static double ease(double t) {
        t = Mth.clamp(t, 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }

    public static Frame sample(double age) {
        if (age <= KEYS[0].tick) {
            return new Frame(KEYS[0].eye, KEYS[0].focus, KEYS[0].fov);
        }
        for (int i = 1; i < KEYS.length; i++) {
            Key a = KEYS[i - 1];
            Key b = KEYS[i];
            if (age <= b.tick) {
                double t = ease((age - a.tick) / (b.tick - a.tick));
                Vec3 eye = a.eye.lerp(b.eye, t);
                Vec3 focus = a.focus.lerp(b.focus, t);
                float fov = (float) Mth.lerp(t, a.fov, b.fov);
                return new Frame(eye, focus, fov);
            }
        }
        Key end = KEYS[KEYS.length - 1];
        return new Frame(end.eye, end.focus, end.fov);
    }

    /**
     * Blends look direction safely without trigonometric collapse when facing vectors cross.
     */
    public static Vec3 blendDirection(Vec3 from, Vec3 to, double t) {
        from = from.normalize();
        to = to.normalize();
        double yaw = Math.toDegrees(Math.atan2(-from.x, from.z));
        double targetYaw = Math.toDegrees(Math.atan2(-to.x, to.z));
        double pitch = -Math.toDegrees(Math.asin(Mth.clamp(from.y, -1.0, 1.0)));
        double targetPitch = -Math.toDegrees(Math.asin(Mth.clamp(to.y, -1.0, 1.0)));
        return Vec3.directionFromRotation(
                (float) Mth.lerp(t, pitch, targetPitch),
                (float) (yaw + Mth.wrapDegrees(targetYaw - yaw) * t)
        );
    }

    /**
     * Orbits around the player towards the rear target rather than cutting straight through the face.
     */
    public static Vec3 returnEye(Vec3 start, Vec3 center, Vec3 target, Vec3 look, double t) {
        t = Mth.clamp(t, 0.0, 1.0);
        if (t == 0.0) return start;
        if (t == 1.0) return target;
        Vec3 a = start.subtract(center);
        Vec3 b = target.subtract(center);
        if (b.horizontalDistance() < 0.01) {
            // First person approach directly to eye center
            return BenihimeStage.returnEye(start, target, look, t);
        }
        double angle = Math.atan2(a.z, a.x);
        double end = Math.atan2(b.z, b.x);
        double turn = Math.atan2(Math.sin(end - angle), Math.cos(end - angle));
        double radius = Mth.lerp(t, a.horizontalDistance(), b.horizontalDistance());
        return center.add(
                Math.cos(angle + turn * t) * radius,
                Mth.lerp(t, a.y, b.y),
                Math.sin(angle + turn * t) * radius
        );
    }
}
