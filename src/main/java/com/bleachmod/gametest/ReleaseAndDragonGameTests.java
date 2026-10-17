package com.bleachmod.gametest;

import com.bleachmod.common.*;
import com.bleachmod.common.data.*;
import com.bleachmod.common.technique.*;
import com.bleachmod.entity.IceDragonEntity;
import com.bleachmod.init.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class ReleaseAndDragonGameTests {
    @GameTest(template="empty",batch="dragon_assets")
    public static void geckoModelAndMolangAnimationLoadWithRealAdapters(GameTestHelper h) {
        try(var model=new java.io.InputStreamReader(Objects.requireNonNull(ReleaseAndDragonGameTests.class.getResourceAsStream("/assets/bleachmod/geo/ice_dragon.geo.json")),java.nio.charset.StandardCharsets.UTF_8);
            var animation=new java.io.InputStreamReader(Objects.requireNonNull(ReleaseAndDragonGameTests.class.getResourceAsStream("/assets/bleachmod/animations/ice_dragon.animation.json")),java.nio.charset.StandardCharsets.UTF_8)) {
            var gson=software.bernie.geckolib.util.JsonUtil.GEO_GSON;
            var modelJson=com.google.gson.JsonParser.parseReader(model).getAsJsonObject();
            var geometry=gson.fromJson(modelJson,software.bernie.geckolib.loading.json.raw.Model.class);
            double minZ=Double.POSITIVE_INFINITY,maxZ=Double.NEGATIVE_INFINITY;int jawDetails=0;
            for(var element:modelJson.getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject().getAsJsonArray("bones")){
                var bone=element.getAsJsonObject();var cubes=bone.getAsJsonArray("cubes");
                if(cubes==null)continue;
                if(bone.get("name").getAsString().equals("jaw"))jawDetails=cubes.size();
                for(var elementCube:cubes){var cube=elementCube.getAsJsonObject();double z=cube.getAsJsonArray("origin").get(2).getAsDouble();minZ=Math.min(minZ,z);maxZ=Math.max(maxZ,z+cube.getAsJsonArray("size").get(2).getAsDouble());}
            }
            h.assertTrue((maxZ-minZ)/16*IceDragonEntity.BANKAI_VISUAL_SCALE>EntityType.ENDER_DRAGON.getWidth() && jawDetails>=10,"Bankai silhouette or articulated jaw lost its imposing scale/detail");
            var root=com.google.gson.JsonParser.parseReader(animation).getAsJsonObject();
            var clips=gson.fromJson(root.getAsJsonObject("animations"),software.bernie.geckolib.loading.object.BakedAnimations.class);
            h.assertTrue(geometry.minecraftGeometry()[0].bones().length>=18 && clips.getAnimation("animation.ice_dragon.flight")!=null,"Dragon geometry or animation failed to load");
        }catch(java.io.IOException e){throw new IllegalStateException(e);}h.succeed();
    }
    private static PlayerData data(FakePlayer p){return PlayerCapability.get(p).orElseThrow(()->new IllegalStateException("Missing capability"));}
    private static FakePlayer player(GameTestHelper h,String identity){
        var p=new FakePlayer(h.getLevel(),new GameProfile(UUID.randomUUID(),"ReleasePilot"));
        var pos=h.absoluteVec(new Vec3(2.5,2,2.5));p.setPos(pos.x,h.getLevel().getMaxBuildHeight()-90,pos.z);p.setYRot(0);p.setOnGround(true);p.setNoGravity(true);
        h.getLevel().getChunkAt(p.blockPosition());var d=data(p);d.initializeShinigami();d.getCharacter().bindZanpakuto(identity);d.getCharacter().preserveLegacyTechniques();
        d.getSkills().get("zanpakuto").setLevel(2);d.getCharacter().unlockForm("shikai");d.getCharacter().unlockForm("bankai");
        d.getCharacter().setMastery("zanpakuto","shikai",100);d.getCharacter().setMastery("zanpakuto","bankai",100);
        p.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(identity.equals("hyorinmaru")?ModItems.HYORINMARU.get():ModItems.RYUJIN_JAKKA.get()));return p;
    }
    private static Zombie target(GameTestHelper h,FakePlayer p,double x,double z){var t=EntityType.ZOMBIE.create(h.getLevel());t.setPos(p.position().add(x,0,z));t.setNoAi(true);t.setNoGravity(true);t.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.IRON_HELMET));
        t.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(80);t.setHealth(80);h.getLevel().addFreshEntity(t);return t;}
    @GameTest(template="empty",batch="release_commit",timeoutTicks=90)
    public static void releaseCommitsOnceAndBlocksOtherActions(GameTestHelper h){
        var p=player(h,"ryujin_jakka");var d=data(p);var t=target(h,p,0,2);
        d.getCharacter().setSelectedForm("zanpakuto","bankai");
        com.bleachmod.server.events.FormModeHandler.attemptTransform(p,d);
        h.assertTrue(ReleaseAnimationService.busy(p) && d.getCharacter().getActiveForm().equals("sealed"),"Release must begin before form commit");
        h.assertTrue(!GuardService.eligible(p,d) && !DodgeService.request(p,d,1,0) && !TechniqueProgression.authorize(p,d,1),"Action escaped release lock");
        p.attack(t);h.assertTrue(t.getHealth()==80 && d.getResources().getStamina()==100,"Physical hit escaped release lock");
        h.onEachTick(()->ReleaseAnimationService.tick(p));
        h.runAfterDelay(20,()->{var view=ReleaseAnimationService.snapshot(p);h.assertTrue(view.getInt("elapsed")>=20 && view.getInt("duration")==60 && d.getCharacter().getActiveForm().equals("sealed"),"Late observer snapshot or pre-commit form incorrect");});
        final float[] balance={0};
        h.runAfterDelay(45,()->{h.assertTrue(d.getCharacter().getActiveForm().equals("bankai"),"Release did not commit");balance[0]=d.getResources().getCurrentReiatsu();h.assertTrue(balance[0]<100,"Release did not charge");});
        h.runAfterDelay(65,()->{try{h.assertTrue(!ReleaseAnimationService.busy(p) && d.getResources().getCurrentReiatsu()==balance[0],"Release charged twice or leaked");}finally{ReleaseAnimationService.cancel(p);t.discard();RyujinTechniqueService.cancel(p);}h.succeed();});
    }
    @GameTest(template="empty",batch="release_cancel",timeoutTicks=40)
    public static void interruptedReleaseKeepsFormAndBalance(GameTestHelper h){var p=player(h,"hyorinmaru");var d=data(p);d.getCharacter().setSelectedForm("zanpakuto","shikai");
        com.bleachmod.server.events.FormModeHandler.attemptTransform(p,d);h.assertTrue(ReleaseAnimationService.busy(p),"Release not started");
        p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);ReleaseAnimationService.tick(p);
        h.assertTrue(!ReleaseAnimationService.busy(p) && d.getCharacter().getActiveForm().equals("sealed") && d.getResources().getCurrentReiatsu()==100,"Cancelled release charged or transformed");h.succeed();}
    @GameTest(template="empty",batch="triple_dragon",timeoutTicks=55)
    public static void threeDragonsHitSeparateBranchesWithoutTripleHittingOverlap(GameTestHelper h){
        var p=player(h,"hyorinmaru");var d=data(p);d.getCharacter().setActiveForm("zanpakuto","bankai");
        var center=target(h,p,0,8);var left=target(h,p,-3.4,7.3);var right=target(h,p,3.4,7.3);var overlap=target(h,p,0,1);
        TechniqueService.executeSlot(p,d,1);
        var dragons=h.getLevel().getEntitiesOfClass(IceDragonEntity.class,p.getBoundingBox().inflate(2));
        h.assertTrue(dragons.size()==3 && center.getHealth()==80,"Expected three traveling dragons");
        h.runAfterDelay(4,()->{h.assertTrue(overlap.getHealth()<80,"Near victim missed");float hp=overlap.getHealth();overlap.invulnerableTime=0;
            h.runAfterDelay(8,()->h.assertTrue(overlap.getHealth()==hp,"Overlapping branches applied extra damage"));});
        h.runAfterDelay(20,()->{try{h.assertTrue(center.getHealth()<80 && left.getHealth()<80 && right.getHealth()<80,"A dragon branch failed to hit");
            p.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);for(var dragon:dragons)dragon.tick();h.assertTrue(dragons.stream().allMatch(Entity::isRemoved),"Unequipping did not cancel dragons");
        }finally{HyorinmaruTechniqueService.cancel(p);for(var t:List.of(center,left,right,overlap))t.discard();}h.succeed();});
    }
    @GameTest(template="empty",batch="dragon_wall",timeoutTicks=45)
    public static void iceDragonStopsAtSolidWall(GameTestHelper h){var p=player(h,"hyorinmaru");var d=data(p);d.getCharacter().setActiveForm("zanpakuto","shikai");var t=target(h,p,0,8);
        var blocks=new ArrayList<net.minecraft.core.BlockPos>();
        for(int x=-2;x<=2;x++)for(int y=0;y<4;y++){var pos=p.blockPosition().offset(x,y,4);blocks.add(pos);h.getLevel().setBlock(pos,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);}
        TechniqueService.executeSlot(p,d,1);
        h.runAfterDelay(20,()->{try{h.assertTrue(t.getHealth()==80,"Dragon damaged through wall");}finally{HyorinmaruTechniqueService.cancel(p);t.discard();for(var pos:blocks)h.getLevel().setBlock(pos,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),3);}h.succeed();});
    }
}
