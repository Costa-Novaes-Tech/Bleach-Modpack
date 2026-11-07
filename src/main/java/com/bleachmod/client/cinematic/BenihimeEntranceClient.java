package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.BenihimeEntranceGeometry;
import com.bleachmod.common.visual.BenihimeStage;
import com.mojang.blaze3d.vertex.*;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.ClipContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;
import java.io.File;
import static net.minecraft.commands.Commands.*;
import com.mojang.brigadier.arguments.*;

/** Shared renderer for the synchronized entrance and the explicitly dev-only inspection bench. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT)
public final class BenihimeEntranceClient {
    private record Scene(long start,Vec3 anchor,Vec3 forward,Vec3 actorForward,boolean bench){ }
    private static final Map<Integer,Scene> SCENES=new HashMap<>();
    private static net.minecraft.client.multiplayer.ClientLevel world;
    private static CameraType previous;
    private static java.lang.reflect.Method cameraPosition;
    private static boolean cameraEnabled=true;
    private static double paused=-1;
    private static String angle="front";
    private static int recording,lastCapture=-1;
    private static long sequence;
    private static File evidence;
    private static String pendingCapture;
    private static int benchTicks,captureSerial;
    private static int inspection=-1;
    private static boolean autoInspect;
    private static com.bleachmod.common.visual.BenihimeFollowMotion.Pose benchFollow;
    private static double followTime;
    private static final class Types extends RenderType {
        private Types(){super("unused",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,8192,false,true,()->{},()->{});}
        private static final RenderType SCENE=create("benihime_entrance",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,262144,false,false,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setCullState(NO_CULL).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(false));
        private static final RenderType FLOOR=create("benihime_stage",DefaultVertexFormat.POSITION_COLOR,VertexFormat.Mode.QUADS,65536,false,true,
                CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    }
    private BenihimeEntranceClient(){ }
    public static void init(){PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("benihime_entrance"),4500,Body::new);}
    private static boolean dev(){return !net.minecraftforge.fml.loading.FMLEnvironment.production;}
    public static boolean active(int id){return world==Minecraft.getInstance().level&&SCENES.containsKey(id);}
    public static void accept(int id,CompoundTag tag){
        var mc=Minecraft.getInstance();if(mc.level==null)return;if(world!=mc.level){clear();world=mc.level;}
        int age=tag.getInt("elapsed");
        if(tag.getInt("duration")!=560||age<0||age>=560){finish(id);return;}
        var anchor=new Vec3(tag.getDouble("x"),tag.getDouble("y"),tag.getDouble("z"));float yaw=tag.contains("stageYaw")?tag.getFloat("stageYaw"):tag.getFloat("yaw");
        if(!Double.isFinite(anchor.lengthSqr())||!Float.isFinite(yaw))return;
        boolean fresh=!SCENES.containsKey(id);
        if(fresh&&SCENES.size()>=8){
            if(mc.player==null||id!=mc.player.getId())return;
            SCENES.keySet().stream().filter(other->other!=id).findFirst().ifPresent(SCENES::remove);
        }
        SCENES.put(id,new Scene(mc.level.getGameTime()-age,anchor,Vec3.directionFromRotation(0,yaw),Vec3.directionFromRotation(0,tag.getFloat("yaw")),false));
        if(fresh&&mc.player!=null&&mc.player.getId()==id&&age<20)cameraEnabled=true;
    }
    private static double age(Scene s,float partial){return s.bench?(paused>=0?paused:benchTicks+partial):world.getGameTime()-s.start+partial;}
    private static void finish(int id){var s=SCENES.remove(id);BenihimeEntranceAudio.stop(id);if(s!=null&&!s.bench&&age(s,0)>=520)com.bleachmod.client.particle.BenihimeRenderer.beginFollowing(id,s.anchor,s.forward);}
    private static Scene local(){var mc=Minecraft.getInstance();return mc.player==null?null:SCENES.get(mc.player.getId());}
    private static boolean following(Scene s){return s!=null&&s.bench&&paused<0&&benchTicks>=560;}
    public static boolean benchFollowing(){return following(local());}
    public static boolean localEntranceActive(){return local()!=null&&!local().bench;}
    public static void captureFollowing(String label){if(dev()&&recording==2)pendingCapture=label;}
    public static void endRecording(){if(dev()){recording=0;pendingCapture=null;}}
    private static boolean cameraActive(){var mc=Minecraft.getInstance();return local()!=null&&!following(local())&&(local().bench||com.bleachmod.client.ClientVisualConfig.BENIHIME_CAMERA.get())&&cameraEnabled&&!CinematicDirector.localActive()&&mc.screen==null&&mc.getCameraEntity()==mc.player;}
    private static void restore(){var mc=Minecraft.getInstance();if(previous!=null&&mc.options.getCameraType()==CameraType.THIRD_PERSON_BACK)mc.options.setCameraType(previous);previous=null;}
    private static void clear(){restore();BenihimeEntranceAudio.clear();SCENES.clear();world=null;paused=-1;recording=0;lastCapture=-1;pendingCapture=null;benchTicks=0;captureSerial=0;inspection=-1;autoInspect=false;benchFollow=null;}
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e){clear();}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();
        if(world!=mc.level){clear();world=mc.level;}if(mc.level==null||mc.player==null)return;
        for(var id:List.copyOf(SCENES.keySet())){var p=mc.level.getEntity(id);var s=SCENES.get(id);if(p==null||!p.isAlive()||(!s.bench&&(age(s,0)>=560||p.position().distanceToSqr(s.anchor)>512*512)))finish(id);}
        var s=local();
        BenihimeEntranceAudio.retain(SCENES.keySet());
        for(var entry:SCENES.entrySet())if(!entry.getValue().bench){
            boolean owner=entry.getKey()==mc.player.getId();var scene=entry.getValue();
            var actor=mc.level.getEntity(entry.getKey());Vec3 voicePosition=actor==null?scene.anchor.add(0,1.3,0):actor.position().add(0,1.3,0);
            BenihimeEntranceAudio.update(entry.getKey(),age(scene,0),voicePosition,scene.anchor.add(scene.forward.scale(-10)).add(0,4,0),owner,
                    !owner||(cameraEnabled&&mc.screen==null&&!CinematicDirector.localActive()));
        }
        if(s==null){restore();return;}
        if(s.bench&&paused<0&&!mc.isPaused())benchTicks++;
        if(!s.bench&&(mc.player.hurtTime>0||mc.options.keyUp.isDown()||mc.options.keyDown.isDown()||mc.options.keyLeft.isDown()||mc.options.keyRight.isDown()||mc.options.keyAttack.isDown()||mc.options.keyUse.isDown()||mc.options.keyJump.isDown()||CinematicDirector.localActive()))cameraEnabled=false;
        if(cameraActive()){
            if(previous==null){previous=mc.options.getCameraType();mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);}
            else if(mc.options.getCameraType()!=CameraType.THIRD_PERSON_BACK){cameraEnabled=false;restore();}
        }else restore();
        if(s.bench&&paused<0&&age(s,0)>=560){recording=0;if(autoInspect){inspection=0;autoInspect=false;}}
        if(s.bench&&inspection>=0){
            int[] marks={120,172,210,280,320,400,480};String[] views={"front","hand","hand","side","side","front","front"};
            int index=inspection/20;
            if(index>=marks.length){inspection=-1;paused=-1;benchTicks=560;angle="front";}
            else{paused=marks[index];angle=views[index];if(inspection%20==8)pendingCapture="B"+String.format(Locale.ROOT,"%02d",index+1);inspection++;}
        }
    }
    @SubscribeEvent public static void render(RenderLevelStageEvent e){
        if(e.getStage()!=RenderLevelStageEvent.Stage.AFTER_PARTICLES||world!=Minecraft.getInstance().level||SCENES.isEmpty())return;
        var mc=Minecraft.getInstance();var buffer=mc.renderBuffers().bufferSource();var matrix=e.getPoseStack().last().pose();
        var floor=buffer.getBuffer(Types.FLOOR);
        for(var scene:SCENES.values())if(!following(scene)&&mc.player.distanceToSqr(scene.anchor)<96*96){
            for(var q:BenihimeStage.floor(scene.anchor.subtract(e.getCamera().getPosition()),scene.forward,age(scene,e.getPartialTick())))
                for(var v:List.of(q.a(),q.b(),q.c(),q.d())){int c=q.color();floor.vertex(matrix,(float)v.x,(float)v.y,(float)v.z).color(c>>>24,(c>>>16)&255,(c>>>8)&255,c&255).endVertex();}
        }
        buffer.endBatch(Types.FLOOR);
        var vertices=buffer.getBuffer(Types.SCENE);
        for(var entry:SCENES.entrySet()){
            var s=entry.getValue();if(mc.player.distanceToSqr(s.anchor)>96*96)continue;
            var origin=s.anchor.subtract(e.getCamera().getPosition());var forward=s.forward;boolean idle=following(s);
            if(idle){double now=benchTicks+e.getPartialTick();if(benchFollow==null){benchFollow=new com.bleachmod.common.visual.BenihimeFollowMotion.Pose(s.anchor,(float)Math.toDegrees(Math.atan2(-s.forward.x,s.forward.z)));followTime=now;}benchFollow=com.bleachmod.common.visual.BenihimeFollowMotion.advance(benchFollow,mc.player.getPosition(e.getPartialTick()),mc.player.getYRot(),now-followTime);followTime=now;origin=benchFollow.anchor().subtract(e.getCamera().getPosition());forward=benchFollow.forward();}
            var terrain=BenihimeTerrain.sample(origin.add(e.getCamera().getPosition()),forward);
            double plane=idle?0:BenihimeStage.floorWeight(age(s,e.getPartialTick()));
            BenihimeEntranceGeometry.Ground ground=(x,z)->terrain.height(x,z)*(1-plane);
            var geometry=idle?BenihimeEntranceGeometry.buildIdle(origin,forward,age(s,e.getPartialTick())-560,0,ground):BenihimeEntranceGeometry.build(origin,forward,Math.min(560,age(s,e.getPartialTick())),ground);
            for(var q:geometry)for(var v:List.of(q.a(),q.b(),q.c(),q.d())){
                int c=q.color();vertices.vertex(matrix,(float)v.x,(float)v.y,(float)v.z).color(c>>>24,(c>>>16)&255,(c>>>8)&255,c&255).endVertex();
            }
        }
        buffer.endBatch(Types.SCENE);
    }
    private record Shot(Vec3 eye,Vec3 focus,float fov){ }
    private static Shot shot(Scene s,double age){
        Vec3 a=s.anchor,f=s.forward,right=new Vec3(-f.z,0,f.x);Vec3 eye,focus;float fov=55;
        if(s.bench&&angle.equals("hand")){eye=a.add(f.scale(-7)).add(right.scale(-4)).add(0,5,0);focus=a.add(f.scale(-12)).add(0,4.6,0);fov=45;}
        else if(s.bench&&angle.equals("side")){eye=a.add(right.scale(19)).add(f.scale(-5)).add(0,6,0);focus=a.add(f.scale(-10)).add(0,4.8,0);}
        else if(age<72&&!s.bench){double t=Mth.clamp(age/50,0,1);double face=com.bleachmod.common.visual.BenihimePlayerPose.faceHeight(age);eye=a.add(s.actorForward.scale(2.3-.7*t)).add(0,face+.12,0);focus=a.add(0,face,0);fov=42;}
        else if(s.bench){double t=1;double lateral=2*t*(1-com.bleachmod.common.visual.BenihimeEntranceTimeline.progress(age,340,450));eye=a.add(f.scale(3+5*t)).add(right.scale(lateral)).add(0,1.5+1.6*t,0);focus=a.add(f.scale(-8*t)).add(0,1+2.5*t,0);fov=58;}
        else{
            var frame=com.bleachmod.common.visual.BenihimeEntranceCamera.sample(Math.max(108,age));
            Vec3 across=com.bleachmod.common.visual.SenbonGeometry.left(f),behind=f.scale(-1);
            eye=a.add(across.scale(frame.eye().x)).add(0,frame.eye().y,0).add(behind.scale(frame.eye().z));
            focus=a.add(across.scale(frame.focus().x)).add(0,frame.focus().y,0).add(behind.scale(frame.focus().z));
            fov=frame.fov();
            if(age<108){
                double t=com.bleachmod.common.visual.BenihimeEntranceTimeline.progress(age,72,108);
                double faceHeight=com.bleachmod.common.visual.BenihimePlayerPose.faceHeight(age);
                eye=a.add(s.actorForward.scale(1.6)).add(0,faceHeight+.12,0).lerp(eye,t);
                focus=a.add(0,faceHeight,0).lerp(focus,t);
                fov=(float)Mth.lerp(t,42,fov);
            }
        }
        if(!s.bench&&age>528){
            double t=Mth.clamp((age-528)/32,0,1);t=t*t*(3-2*t);var normal=returnShot();
            Vec3 from=focus.subtract(eye).normalize(),to=normal.focus.subtract(normal.eye).normalize();
            double yaw=Math.toDegrees(Math.atan2(-from.x,from.z)),targetYaw=Math.toDegrees(Math.atan2(-to.x,to.z));
            double pitch=-Math.toDegrees(Math.asin(Mth.clamp(from.y,-1,1))),targetPitch=-Math.toDegrees(Math.asin(Mth.clamp(to.y,-1,1)));
            eye=previous==CameraType.FIRST_PERSON?BenihimeStage.returnEye(eye,normal.eye,mcLook(),t):eye.lerp(normal.eye,t);
            // Interpolate orientation, not the two focus points: opposing views must not collapse through a zero look vector.
            Vec3 direction=Vec3.directionFromRotation((float)Mth.lerp(t,pitch,targetPitch),(float)(yaw+Mth.wrapDegrees(targetYaw-yaw)*t));
            focus=eye.add(direction.scale(8));fov=(float)Mth.lerp(t,fov,normal.fov);
        }
        return new Shot(eye,focus,fov);
    }
    private static Vec3 mcLook(){return Minecraft.getInstance().player.getLookAngle();}
    /** The final first-person approach must not render the inside/front of the local head. */
    @SubscribeEvent public static void hideHeadOnReturn(RenderPlayerEvent.Pre e){
        var mc=Minecraft.getInstance();var s=local();
        if(e.getEntity()!=mc.player||s==null||s.bench||previous!=CameraType.FIRST_PERSON||!cameraActive()||age(s,e.getPartialTick())<528)return;
        if(mc.gameRenderer.getMainCamera().getPosition().distanceToSqr(mc.player.getEyePosition())<1.8)e.setCanceled(true);
    }
    /** Return to the perspective that preceded the scene, including its look pitch. */
    private static Shot returnShot(){
        var mc=Minecraft.getInstance();Vec3 eye=mc.player.getEyePosition(),look=mc.player.getLookAngle();
        CameraType mode=previous==null?CameraType.FIRST_PERSON:previous;
        Vec3 position=mode==CameraType.FIRST_PERSON?eye:eye.add(look.scale(mode==CameraType.THIRD_PERSON_FRONT?4:-4));
        if(mode!=CameraType.FIRST_PERSON){
            var hit=world.clip(new ClipContext(eye,position,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
            if(hit.getType()!=HitResult.Type.MISS)position=hit.getLocation().add(eye.subtract(position).normalize().scale(.2));
        }
        Vec3 direction=mode==CameraType.THIRD_PERSON_FRONT?look.scale(-1):look;
        return new Shot(position,position.add(direction.scale(4)),mc.options.fov().get().floatValue());
    }
    @SubscribeEvent(priority=net.minecraftforge.eventbus.api.EventPriority.LOW) public static void camera(ViewportEvent.ComputeCameraAngles e){
        if(!cameraActive())return;var mc=Minecraft.getInstance();var s=local();var sh=shot(s,age(s,(float)e.getPartialTick()));Vec3 eye=sh.eye;
        Vec3 safeOrigin=mc.player.getEyePosition();
        var hit=world.clip(new ClipContext(safeOrigin,eye,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
        if(hit.getType()!=HitResult.Type.MISS)eye=hit.getLocation().add(safeOrigin.subtract(eye).normalize().scale(.2));
        if(eye.distanceToSqr(sh.focus)<.25){cameraEnabled=false;restore();return;}
        try{if(cameraPosition==null)cameraPosition=net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(Camera.class,"m_90581_",Vec3.class);cameraPosition.invoke(e.getCamera(),eye);}
        catch(ReflectiveOperationException|RuntimeException failure){com.mojang.logging.LogUtils.getLogger().warn("Benihime camera restored",failure);cameraEnabled=false;restore();return;}
        Vec3 d=sh.focus.subtract(eye);e.setYaw((float)Math.toDegrees(Math.atan2(-d.x,d.z)));e.setPitch((float)-Math.toDegrees(Math.atan2(d.y,Math.sqrt(d.x*d.x+d.z*d.z))));e.setRoll(0);
    }
    @SubscribeEvent public static void fov(ViewportEvent.ComputeFov e){if(cameraActive()&&e.usedConfiguredFov())e.setFOV(shot(local(),age(local(),(float)e.getPartialTick())).fov);}
    @SubscribeEvent public static void hud(RenderGuiOverlayEvent.Pre e){if(cameraActive())e.setCanceled(true);}
    @SubscribeEvent public static void overlay(RenderGuiEvent.Post e){
        var mc=Minecraft.getInstance();
        if(mc.player!=null&&mc.level==world&&mc.screen==null&&com.bleachmod.client.ClientVisualConfig.BENIHIME_PENUMBRA.get()){
            double shade=0;Scene brightest=null;
            for(var scene:SCENES.values()){
                double age=age(scene,e.getPartialTick());
                if(age>=560)continue;
                Vec3 delta=scene.anchor.add(scene.forward.scale(-9)).add(0,5,0).subtract(mc.gameRenderer.getMainCamera().getPosition());
                Vec3 direction=delta.normalize();
                double facing=viewDirection(e.getPartialTick()).dot(direction);
                double distance=Mth.clamp((72-delta.length())/32,0,1);
                double value=distance*Mth.clamp((facing-.3)/.6,0,1)*BenihimeStage.darkness(age);
                if(value>shade){shade=value;brightest=scene;}
            }
            if(shade>0&&brightest!=null)spotlight(e,brightest,shade);
        }
        if(!cameraActive())return;var g=e.getGuiGraphics();int h=(int)(g.guiHeight()*.06);g.fill(0,0,g.guiWidth(),h,0xff000000);g.fill(0,g.guiHeight()-h,g.guiWidth(),g.guiHeight(),0xff000000);
        if(local().bench)g.drawString(Minecraft.getInstance().font,"BENIHIME QA / CLIENTE FORGE REAL / tick "+(int)age(local(),e.getPartialTick())+" / "+angle,8,5,0xffffff,false);
        double t=age(local(),e.getPartialTick());
        if(t>28&&t<65)g.drawCenteredString(Minecraft.getInstance().font,"Bankai",g.guiWidth()/2,g.guiHeight()-h-22,0xeee2df);
        if(t>461&&t<545)g.drawCenteredString(Minecraft.getInstance().font,"Kannonbiraki Benihime Aratame",g.guiWidth()/2,g.guiHeight()-h-22,0xeee2df);
    }
    /** Smooth screen-space penumbra with a protected ellipse over the actual player projection. */
    private static void spotlight(RenderGuiEvent.Post e,Scene scene,double shade){
        var mc=Minecraft.getInstance();var g=e.getGuiGraphics();double w=g.guiWidth(),h=g.guiHeight();
        var camera=mc.gameRenderer.getMainCamera();Vec3 actor=scene.anchor;
        for(var entry:SCENES.entrySet())if(entry.getValue()==scene){var entity=world.getEntity(entry.getKey());if(entity!=null)actor=entity.getPosition(e.getPartialTick());break;}
        Vec3 relative=actor.add(0,.95,0).subtract(camera.getPosition()),look=viewDirection(e.getPartialTick());
        Vec3 right=look.cross(new Vec3(0,1,0)).normalize(),up=right.cross(look).normalize();
        double depth=relative.dot(look),tan=Math.tan(Math.toRadians(cameraActive()?shot(local(),age(local(),e.getPartialTick())).fov:mc.options.fov().get())/2);
        double scale=h/(2*tan*Math.max(.1,depth));
        double cx=w*.5+relative.dot(right)*scale,cy=h*.5-relative.dot(up)*scale,rx=Math.max(12,.85*scale),ry=Math.max(20,1.5*scale);
        var vertices=g.bufferSource().getBuffer(RenderType.gui());var matrix=g.pose().last().pose();
        for(int y=0;y<h;y+=12)for(int x=0;x<w;x+=12){
            int x2=(int)Math.min(w,x+12),y2=(int)Math.min(h,y+12);
            for(int[] p:new int[][]{{x,y},{x,y2},{x2,y2},{x2,y}}){
                double mask=depth>.1?BenihimeStage.mask(p[0],p[1],cx,cy,rx,ry):1;
                vertices.vertex(matrix,p[0],p[1],0).color(0,0,5,(int)(255*shade*mask)).endVertex();
            }
        }
        g.flush();
    }
    private static Vec3 viewDirection(float partial){
        var camera=Minecraft.getInstance().gameRenderer.getMainCamera();
        if(cameraActive())return shot(local(),age(local(),partial)).focus.subtract(camera.getPosition()).normalize();
        return new Vec3(camera.getLookVector());
    }
    private static final class Body implements IAnimation{
        final AbstractClientPlayer player;Body(AbstractClientPlayer p){player=p;}
        @Override public boolean isActive(){return active(player.getId())&&!following(SCENES.get(player.getId()))&&!CinematicDirector.localActive()&&(player!=Minecraft.getInstance().player||cameraEnabled);}
        @Override public void setupAnim(float partial){ }
        @Override public FirstPersonMode getFirstPersonMode(float partial){return FirstPersonMode.VANILLA;}
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){
            var s=SCENES.get(player.getId());if(s==null)return value;double t=age(s,partial),w=com.bleachmod.common.visual.BenihimePlayerPose.weight(t);
            float x=value.getX(),y=value.getY(),z=value.getZ();
            boolean leftMain=player.getMainArm()==net.minecraft.world.entity.HumanoidArm.LEFT;
            boolean main=part.equals(leftMain?"leftArm":"rightArm"),support=part.equals(leftMain?"rightArm":"leftArm");
            boolean item=part.equals(leftMain?"leftItem":"rightItem");
            if(support&&!player.getOffhandItem().isEmpty())return value;
            var limb=main?com.bleachmod.common.visual.BenihimePlayerPose.mainArm(leftMain):com.bleachmod.common.visual.BenihimePlayerPose.supportArm(leftMain);
            if(type==TransformType.POSITION&&part.equals("body"))y=(float)(y*(1-w))+com.bleachmod.common.visual.BenihimePlayerPose.bodyOffset(t);
            if(type==TransformType.BEND&&(part.equals("rightLeg")||part.equals("leftLeg")))return new Vec3f(0,0,0);
            if(type==TransformType.BEND&&(main||support))return new Vec3f(0,(float)Mth.lerp(w,y,limb.bend()),0);
            if(type==TransformType.POSITION&&item){var target=com.bleachmod.common.visual.BenihimePlayerPose.itemPosition(leftMain);return new Vec3f((float)Mth.lerp(w,x,target.x),(float)Mth.lerp(w,y,target.y),(float)Mth.lerp(w,z,target.z));}
            if(type==TransformType.ROTATION){
                if(part.equals("rightLeg")||part.equals("leftLeg")||part.equals("body")||part.equals("head"))return new Vec3f((float)(x*(1-w)),(float)(y*(1-w)),(float)(z*(1-w)));
                if(main||support||item){var target=item?com.bleachmod.common.visual.BenihimePlayerPose.itemRotation(leftMain):limb.rotation();return new Vec3f((float)Mth.lerp(w,x,target.x),(float)Mth.lerp(w,y,target.y),(float)Mth.lerp(w,z,target.z));}
            }
            return new Vec3f(x,y,z);
        }
    }
    @SubscribeEvent public static void commands(RegisterClientCommandsEvent e){
        if(!dev())return;
        e.getDispatcher().register(literal("benihimeqa").requires(s->Minecraft.getInstance().hasSingleplayerServer()&&Minecraft.getInstance().player!=null&&Minecraft.getInstance().player.hasPermissions(2))
            .then(literal("play").executes(c->{bench();return 1;}))
            .then(literal("record").executes(c->{bench();recording=1;autoInspect=Boolean.getBoolean("bleachmod.benihimeQa");return 1;}))
            .then(literal("release").executes(c->{release();return 1;}))
            .then(literal("seek").then(argument("tick",IntegerArgumentType.integer(0,559)).executes(c->{ensureBench();paused=IntegerArgumentType.getInteger(c,"tick");return 1;})))
            .then(literal("angle").then(argument("view",StringArgumentType.word()).suggests((c,b)->net.minecraft.commands.SharedSuggestionProvider.suggest(List.of("front","side","hand"),b)).executes(c->{String v=StringArgumentType.getString(c,"view");if(!List.of("front","side","hand").contains(v))return 0;angle=v;return 1;})))
            .then(literal("capture").executes(c->{pendingCapture="manual";return 1;}))
            .then(literal("stop").executes(c->{clear();return 1;})));
    }
    private static void ensureBench(){if(local()==null||!local().bench)bench();}
    private static void release(){
        var mc=Minecraft.getInstance();if(!dev()||mc.player==null||!mc.hasSingleplayerServer()||!mc.player.hasPermissions(2))return;
        var uuid=mc.player.getUUID();clear();world=mc.level;cameraEnabled=true;sequence=System.currentTimeMillis();evidence=new File(mc.gameDirectory,"benihime-qa/"+sequence);recording=2;
        mc.getSingleplayerServer().execute(()->{
            var p=mc.getSingleplayerServer().getPlayerList().getPlayer(uuid);if(p==null)return;
            com.bleachmod.common.data.PlayerCapability.get(p).ifPresent(d->{
                if(!p.getServer().getWorldData().getLevelName().startsWith("Benihime-QA-68-"))return;
                com.bleachmod.server.events.FormModeHandler.descend(p,d);
                d.getCharacter().setSelectedForm("zanpakuto","bankai");d.getResources().setCurrentReiatsu(d.getResources().getMaxReiatsu());
                com.bleachmod.server.events.FormModeHandler.instantTransform(p,d);
            });
        });
    }
    private static void bench(){
        var mc=Minecraft.getInstance();if(!dev()||mc.level==null||mc.player==null||!mc.hasSingleplayerServer())return;
        clear();world=mc.level;var forward=Vec3.directionFromRotation(0,mc.player.getYRot());SCENES.put(mc.player.getId(),new Scene(world.getGameTime(),mc.player.position(),forward,forward,true));
        cameraEnabled=true;paused=-1;angle="front";sequence=System.currentTimeMillis();evidence=new File(mc.gameDirectory,"benihime-qa/"+sequence);lastCapture=-1;
    }
    private static void capture(String label){
        var mc=Minecraft.getInstance();if(!dev()||evidence==null||mc.player==null||world!=mc.level)return;
        var current=local();if(current==null&&recording!=2)return;
        double capturedAge=current==null?600:age(current,0);
        String name=String.format(Locale.ROOT,"%s-%05d-t%05d-%s.png",label,captureSerial++,Math.max(0,(int)capturedAge),angle);
        try{
            var anchor=current==null?mc.player.position():current.anchor;java.nio.file.Files.createDirectories(evidence.toPath());
            Screenshot.grab(evidence,name,mc.getMainRenderTarget(),msg->com.mojang.logging.LogUtils.getLogger().info("Benihime QA screenshot {}: {}",name,msg.getString()));
            String line=String.format(Locale.ROOT,"{\"file\":\"screenshots/%s\",\"gameTick\":%.3f,\"wallMs\":%d,\"phase\":\"%s\",\"view\":\"%s\",\"fpsObserved\":%d,\"clientDayTime\":%d,\"width\":%d,\"height\":%d,\"origin\":[%.4f,%.4f,%.4f],\"scenario\":\"%s\",\"source\":\"Minecraft Screenshot.grab framebuffer\"}%n",name,capturedAge,System.currentTimeMillis(),current==null?"FOLLOW":com.bleachmod.common.visual.BenihimeEntranceTimeline.phase(capturedAge),angle,mc.getFps(),world.getDayTime(),mc.getWindow().getWidth(),mc.getWindow().getHeight(),anchor.x,anchor.y,anchor.z,current!=null&&current.bench?"local-bench":"server-release");
            java.nio.file.Files.writeString(evidence.toPath().resolve("frames.jsonl"),line,java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);
            if(captureSerial==1){
                var meta=new com.google.gson.JsonObject();meta.addProperty("world",mc.getSingleplayerServer()==null?"remote":mc.getSingleplayerServer().getWorldData().getLevelName());
                meta.addProperty("dayTime",world.getDayTime());meta.addProperty("renderer",org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER));
                meta.addProperty("cpuThreads",Runtime.getRuntime().availableProcessors());meta.addProperty("maxHeapBytes",Runtime.getRuntime().maxMemory());
                meta.addProperty("renderDistance",mc.options.renderDistance().get());meta.addProperty("captureSamplingTicks",2);
                java.nio.file.Files.writeString(evidence.toPath().resolve("environment.json"),meta.toString());
            }
        }catch(java.io.IOException failure){com.mojang.logging.LogUtils.getLogger().warn("Cannot write Benihime QA frame metadata",failure);}
    }
    @SubscribeEvent public static void captureFrame(TickEvent.RenderTickEvent e){
        if(e.phase!=TickEvent.Phase.END||Minecraft.getInstance().screen!=null)return;
        if(pendingCapture!=null){capture(pendingCapture);pendingCapture=null;}
        if(recording==0||local()==null||following(local()))return;
        int at=(int)age(local(),0);if(at<560&&at/2!=lastCapture){lastCapture=at/2;capture("frame");}
    }
}
