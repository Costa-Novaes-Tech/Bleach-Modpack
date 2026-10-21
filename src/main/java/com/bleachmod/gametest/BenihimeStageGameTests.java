package com.bleachmod.gametest;

import com.bleachmod.common.visual.BenihimeStage;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class BenihimeStageGameTests {
    @GameTest(template="empty",batch="benihime_stage")
    public static void lightProtectsPlayerAndSceneDisappearsWithoutPersistentFloor(GameTestHelper h){
        h.assertTrue(BenihimeStage.darkness(0)==0&&BenihimeStage.darkness(100)>.1
                &&BenihimeStage.darkness(300)>.6&&BenihimeStage.darkness(466)==0,"Dramatic light ignores rise/settle boundaries");
        h.assertTrue(BenihimeStage.mask(100,100,100,100,20,40)==0
                &&BenihimeStage.mask(200,100,100,100,20,40)==1,"Spotlight does not protect the actor");
        h.assertTrue(BenihimeStage.floor(Vec3.ZERO,new Vec3(0,0,1),0).isEmpty()
                &&BenihimeStage.floor(Vec3.ZERO,new Vec3(0,0,1),560).isEmpty(),"Floor persists outside the scene");
        var floor=BenihimeStage.floor(new Vec3(12,80,-3),new Vec3(.7,0,.7),300);
        h.assertTrue(floor.size()==468,"Floor geometry unexpectedly exploded");
        for(var q:floor)for(var p:java.util.List.of(q.a(),q.b(),q.c(),q.d()))
            h.assertTrue(Double.isFinite(p.lengthSqr())&&Math.abs(p.y-80.018)<1e-6,"Support plane is not flat");
        h.succeed();
    }
    @GameTest(template="empty",batch="benihime_stage")
    public static void placementAvoidsObstructedRearAxisAndKeepsStableUnobstructedYaw(GameTestHelper h){
        h.assertTrue(BenihimeStage.chooseYaw(120,f->0)==120,"Clear scene rotates needlessly");
        float yaw=BenihimeStage.chooseYaw(0,f->Math.abs(f.x)<.5?100:0);
        h.assertTrue(Math.abs(yaw)==50,"Scene did not choose the nearest less-obstructed rear direction");
        h.assertTrue(BenihimeStage.chooseYaw(25,f->Double.NaN)==25,"Invalid collision sample corrupts orientation");
        h.succeed();
    }
    @GameTest(template="empty",batch="benihime_stage")
    public static void returnCameraOrbitsBeforeEnteringThePlayerHead(GameTestHelper h){
        Vec3 start=new Vec3(0,3,8),eye=new Vec3(0,1.62,0),look=new Vec3(0,0,1),last=start;
        h.assertTrue(BenihimeStage.returnEye(start,eye,look,0).equals(start)
                &&BenihimeStage.returnEye(start,eye,look,1).equals(eye),"Camera return endpoints changed");
        for(int i=1;i<=200;i++){
            double t=i/200.;Vec3 p=BenihimeStage.returnEye(start,eye,look,t);
            h.assertTrue(Double.isFinite(p.lengthSqr())&&p.distanceTo(last)<.35,"Camera return jumps");
            if(t<=.7)h.assertTrue(p.subtract(eye).horizontalDistance()>2.19,"Camera crosses face before orbital leg ends");
            if(t>.7&&t<1)h.assertTrue(p.z<eye.z,"Final approach comes through the front of the face");
            last=p;
        }
        h.succeed();
    }
}
