package com.bleachmod.gametest;

import com.bleachmod.common.visual.BenihimePlayerPose;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Independent forward-kinematic checks of the native, unbent Minecraft/PlayerAnimator pipeline.
 * The base profile has no BendyLib: arm and held-item BEND transforms must not be assumed.
 * Units are model pixels throughout: player floor Y=24, positive Y points down.
 * These checks validate numerical grip/orientation, not skin pixels or cinematic quality.
 */
@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class BenihimeStandingGripGameTests {
    private static final double EPSILON = 0.0001; // six-decimal authored angles/positions
    private static final double MAIN_HANDLE_Y = 3.5;
    private static final double GRIP_Z = -Math.sqrt(70.9375);
    private static Vec3 mainTarget(boolean left) { return new Vec3(left ? -.75 : .75, 3, GRIP_Z); }
    private static Vec3 supportTarget(boolean left) { return new Vec3(left ? -.75 : .75, 6, GRIP_Z); }
    private BenihimeStandingGripGameTests() { }

    @GameTest(template="empty", batch="benihime_standing_grip")
    public static void bothDominantHandsShareOneHandleWithBladePointingDown(GameTestHelper helper) {
        var assets = assets();
        for (boolean left : new boolean[]{false, true}) {
            helper.assertTrue(Math.abs(BenihimePlayerPose.mainArm(left).bend()) < 1e-12
                    && Math.abs(BenihimePlayerPose.supportArm(left).bend()) < 1e-12,
                    "Native standing grip depends on absent/global BendyLib support");
            var item = itemFrame(left, assets, BenihimePlayerPose.itemRotation(left), BenihimePlayerPose.itemPosition(left), false);
            Vec3 main = hand(left, BenihimePlayerPose.mainArm(left));
            Vec3 support = hand(!left, BenihimePlayerPose.supportArm(left));
            double supportY = MAIN_HANDLE_Y + (supportTarget(left).y - mainTarget(left).y) / assets.scale.y;
            Vec3 mainOnHandle = item.apply(new Vec3(0, MAIN_HANDLE_Y - 8, 0));
            Vec3 supportOnHandle = item.apply(new Vec3(0, supportY - 8, 0));
            String side = left ? "left dominant" : "right dominant";
            helper.assertTrue(main.distanceTo(mainTarget(left)) < EPSILON, "Main palm misses chest grip: " + side);
            helper.assertTrue(support.distanceTo(supportTarget(left)) < EPSILON, "Support palm misses lower grip: " + side);
            helper.assertTrue(main.distanceTo(mainOnHandle) < EPSILON && support.distanceTo(supportOnHandle) < EPSILON,
                    "Hands do not grasp the same rendered handle: " + side);
            helper.assertTrue(MAIN_HANDLE_Y > assets.handleMin && supportY < assets.handleMax,
                    "A hand lies outside wooden handle bounds: " + side);
            Vec3 direction = item.rotation.apply(new Vec3(0, 1, 0)).normalize();
            helper.assertTrue(direction.y > .999999 && Math.abs(direction.x) < EPSILON && Math.abs(direction.z) < EPSILON,
                    "Blade fails to point vertically towards model floor: " + side);
            Vec3 tip = item.apply(new Vec3(0, assets.tipY - 8, 0));
            helper.assertTrue(tip.y > support.y && tip.y < 24 - .1,
                    "Blade tip rises above hands or penetrates reference floor: " + side + " tipY=" + tip.y);
            helper.assertTrue(Math.abs(tip.x - mainTarget(left).x) < EPSILON && Math.abs(tip.z - GRIP_Z) < EPSILON,
                    "Blade axis drifts from shared handle: " + side);
        }
        helper.succeed();
    }

    @GameTest(template="empty", batch="benihime_standing_grip")
    public static void gripChecksRejectUncorrectedOffsetsAndAnInvertedBlade(GameTestHelper helper) {
        var assets = assets();
        for (boolean left : new boolean[]{false, true}) {
            // Negative controls ensure the shared-grip/orientation predicates reject the old defaults.
            var noCorrection = itemFrame(left, assets, Vec3.ZERO, Vec3.ZERO, false);
            helper.assertTrue(noCorrection.apply(new Vec3(0, MAIN_HANDLE_Y - 8, 0)).distanceTo(mainTarget(left)) > 1,
                    "Grip predicate tolerates vanilla uncorrected item position");
            helper.assertTrue(noCorrection.rotation.apply(new Vec3(0, 1, 0)).normalize().y < .99,
                    "Orientation predicate accepts the original unsolved item basis");
            var inverted = itemFrame(left, assets, BenihimePlayerPose.itemRotation(left), BenihimePlayerPose.itemPosition(left), true);
            helper.assertTrue(inverted.rotation.apply(new Vec3(0, 1, 0)).normalize().y < -.999999,
                    "Orientation check cannot distinguish point-down from point-up");
        }
        helper.succeed();
    }

    private record Assets(Vec3 rightRotation, Vec3 leftRotation, Vec3 rightTranslation, Vec3 leftTranslation,
                          Vec3 scale, double elementAngle, double handleMin, double handleMax, double tipY) { }
    private static Assets assets() {
        JsonObject model = resource("benihime_bankai");
        String parent = model.get("parent").getAsString();
        if (!parent.startsWith("bleachmod:item/")) throw new IllegalStateException("Unexpected Benihime parent: " + parent);
        var display = resource(parent.substring("bleachmod:item/".length())).getAsJsonObject("display");
        var right = display.getAsJsonObject("thirdperson_righthand");
        var left = display.getAsJsonObject("thirdperson_lefthand");
        double tip = Double.NEGATIVE_INFINITY, handleMin = 0, handleMax = 0, angle = Double.NaN;
        for (var entry : model.getAsJsonArray("elements")) {
            var element = entry.getAsJsonObject();
            var rotation = element.getAsJsonObject("rotation");
            double elementAngle = rotation.get("angle").getAsDouble();
            if (!rotation.get("axis").getAsString().equals("z") || vector(rotation,"origin").distanceTo(new Vec3(8,8,8)) > 1e-9
                    || Double.isFinite(angle) && Math.abs(angle - elementAngle) > 1e-9)
                throw new IllegalStateException("Benihime element basis changed; re-evaluate cinematic grip");
            angle = elementAngle;
            Vec3 from = vector(element, "from"), to = vector(element, "to");
            tip = Math.max(tip, to.y);
            if (element.getAsJsonObject("faces").getAsJsonObject("north").get("texture").getAsString().equals("#wood")) {
                handleMin = from.y; handleMax = to.y;
            }
        }
        if (!Double.isFinite(angle) || handleMax <= handleMin) throw new IllegalStateException("Missing Benihime handle/model");
        Vec3 scale = vector(right,"scale");
        if (scale.distanceTo(vector(left,"scale")) > 1e-9) throw new IllegalStateException("Asymmetric hand scales require separate grip calibration");
        return new Assets(vector(right,"rotation"), vector(left,"rotation"), vector(right,"translation"),
                vector(left,"translation"), scale, angle, handleMin, handleMax, tip);
    }
    private static JsonObject resource(String name) {
        String path = "/assets/bleachmod/models/item/" + name + ".json";
        var stream = BenihimeStandingGripGameTests.class.getResourceAsStream(path);
        if (stream == null) throw new IllegalStateException("Missing item resource " + path);
        try (var reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (java.io.IOException failure) { throw new IllegalStateException("Cannot read " + path, failure); }
    }
    private static Vec3 vector(JsonObject object, String key) {
        var values = object.getAsJsonArray(key);
        return new Vec3(values.get(0).getAsDouble(), values.get(1).getAsDouble(), values.get(2).getAsDouble());
    }
    private static Mat armRotation(BenihimePlayerPose.Limb arm) {
        return Mat.z(arm.rotation().z).mul(Mat.y(arm.rotation().y)).mul(Mat.x(arm.rotation().x));
    }
    private static Vec3 shoulder(boolean left) { return new Vec3(left ? 5 : -5, 2, 0); }
    private static Vec3 hand(boolean left, BenihimePlayerPose.Limb arm) {
        // translateToHand + vanilla(-90X,180Y,T[sign,2,-10]) at the palm centre.
        // No elbow pivot or bend matrix: that branch is disabled without BendyLib.
        Vec3 palm = new Vec3(left ? 1 : -1,10,-2);
        return shoulder(left).add(armRotation(arm).apply(palm));
    }
    private record Frame(Mat rotation, Vec3 translation) {
        Vec3 apply(Vec3 point) { return translation.add(rotation.apply(point)); }
    }
    private static Frame itemFrame(boolean left, Assets assets, Vec3 itemRotation, Vec3 itemPosition, boolean invertBlade) {
        var arm = BenihimePlayerPose.mainArm(left);
        Mat root = armRotation(arm);
        Vec3 origin = shoulder(left);
        Mat a = root.mul(Mat.x(-Math.PI/2)).mul(Mat.y(Math.PI));
        Mat c = Mat.z(itemRotation.z).mul(Mat.y(itemRotation.y)).mul(Mat.x(itemRotation.x));
        Vec3 displayRotation = left ? assets.leftRotation.multiply(1,-1,-1) : assets.rightRotation;
        Mat d = Mat.x(Math.toRadians(displayRotation.x)).mul(Mat.y(Math.toRadians(displayRotation.y)))
                .mul(Mat.z(Math.toRadians(displayRotation.z)));
        Vec3 displayTranslation = left ? assets.leftTranslation.multiply(-1,1,1) : assets.rightTranslation;
        Vec3 vanillaTranslation = new Vec3(left ? -1 : 1,2,-10);
        Vec3 translation = origin.add(a.apply(vanillaTranslation.add(itemPosition).add(c.apply(displayTranslation))));
        Mat element = Mat.z(Math.toRadians(assets.elementAngle) + (invertBlade ? Math.PI : 0));
        Mat rotation = a.mul(c).mul(d).mul(Mat.scale(assets.scale)).mul(element);
        return new Frame(rotation, translation);
    }

    /** Tiny row-major matrix implementation avoids client-only renderer/JOML dependencies on server. */
    private record Mat(double[] m) {
        static Mat x(double a) { double c=Math.cos(a),s=Math.sin(a);return new Mat(new double[]{1,0,0,0,c,-s,0,s,c}); }
        static Mat y(double a) { double c=Math.cos(a),s=Math.sin(a);return new Mat(new double[]{c,0,s,0,1,0,-s,0,c}); }
        static Mat z(double a) { double c=Math.cos(a),s=Math.sin(a);return new Mat(new double[]{c,-s,0,s,c,0,0,0,1}); }
        static Mat scale(Vec3 s) { return new Mat(new double[]{s.x,0,0,0,s.y,0,0,0,s.z}); }
        Mat mul(Mat b) {
            double[] result = new double[9];
            for(int row=0;row<3;row++)for(int col=0;col<3;col++)for(int k=0;k<3;k++)result[row*3+col]+=m[row*3+k]*b.m[k*3+col];
            return new Mat(result);
        }
        Vec3 apply(Vec3 v) { return new Vec3(m[0]*v.x+m[1]*v.y+m[2]*v.z,m[3]*v.x+m[4]*v.y+m[5]*v.z,m[6]*v.x+m[7]*v.y+m[8]*v.z); }
    }
}
