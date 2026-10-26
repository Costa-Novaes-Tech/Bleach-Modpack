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
    public static final double RETURN_START = 244.0;
    public static final double RETURN_SETTLE = 286.0;

    public record Frame(Vec3 eye, Vec3 focus, float fov) { }

    private record Key(double tick, Vec3 eye, Vec3 focus, float fov) { }

    private static Key key(double t, double x, double y, double z, double fx, double fy, double fz, float fov) {
        return new Key(t, new Vec3(x, y, z), new Vec3(fx, fy, fz), fov);
    }

    private static final Key[] KEYS = {
            // 0..10: Plano médio frontal mostrando rosto, tronco, mão principal e katana vertical única
            key(0.0,   -0.32, 1.48, 2.35,   -0.10, 1.16, 0.22,   48.0F),
            key(10.0,  -0.32, 1.48, 2.35,   -0.10, 1.16, 0.22,   48.0F),

            // 10..35: Acompanhamento controlado da soltura e queda preservando rosto/tronco e conexão mão-espada-solo
            key(35.0,  -0.56, 1.40, 2.70,   -0.14, 0.86, 0.26,   50.0F),

            // 35..76: Tomada oblíqua da espada afundando e das ondulações concêntricas de Reishi
            key(76.0,  -0.92, 1.62, 3.45,   -0.12, 0.82, 0.24,   54.0F),

            // 76..112: Contrapicado acompanhando a ascensão das 20 lâminas gigantescas emergindo do solo
            key(112.0,  0.00, 0.78, 5.20,    0.00, 9.50, -2.00,  62.0F),

            // 112..160: Revelação do corredor simétrico e profundidade, descendo suavemente até Byakuya
            key(136.0,  0.00, 1.12, 5.20,    0.00, 6.20, -1.00,  58.0F),
            key(160.0,  0.00, 1.45, 5.10,    0.00, 1.35, 0.00,   50.0F),

            // 160..216: Composição frontal nobre e estável durante a invocação do nome
            key(216.0,  0.00, 1.42, 4.45,    0.00, 1.35, 0.00,   46.5F),

            // 216..244: Abertura antecipada do enquadramento enquanto ainda existe palco, preparando o destino da câmera
            key(RETURN_START, 0.00, 1.82, 5.65, 0.00, 1.35, -0.35, 54.0F)
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
