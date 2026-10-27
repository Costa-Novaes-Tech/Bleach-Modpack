package com.bleachmod.client.cinematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/** Samples the actual third-person held-item matrix, rather than inferring it from JSON angles. */
public final class HeldWeaponQaProbe {
    private static long lastTick=Long.MIN_VALUE;
    private static final String RUN="held-probe-"+System.currentTimeMillis()+".jsonl";
    private HeldWeaponQaProbe() { }
    public static void capture(LivingEntity entity,ItemStack item,ItemDisplayContext context,boolean left,PoseStack stack){
        if(net.minecraftforge.fml.loading.FMLEnvironment.production || !Boolean.getBoolean("bleachmod.benihimeQa")
                || !Boolean.getBoolean("bleachmod.higashiPoseQa"))return;
        var mc=Minecraft.getInstance();
        if(entity!=mc.player || mc.level==null || context!=ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                &&context!=ItemDisplayContext.THIRD_PERSON_LEFT_HAND)return;
        float age=com.bleachmod.client.model.HigashiExecutionVisuals.currentPoseAge();
        long tick=mc.level.getGameTime();if(age<0 || tick==lastTick)return;lastTick=tick;
        var row=new JsonObject();row.addProperty("worldTick",tick);row.addProperty("poseAge",age);
        row.addProperty("context",context.name());
        var matrix=new JsonArray();for(float f:stack.last().pose().get(new float[16]))matrix.add(f);row.add("heldMatrix",matrix);
        stack.pushPose();
        try{
            var model=mc.getItemRenderer().getModel(item,mc.level,entity,0);
            model.getTransforms().getTransform(context).apply(left,stack);stack.translate(-.5,-.5,-.5);
            row.add("gripScreen",screen(stack.last().pose(),point(8,3.5,8)));
            row.add("tipScreen",screen(stack.last().pose(),point(8.6,26.5,8)));
        }finally{stack.popPose();}
        try{Path p=mc.gameDirectory.toPath().resolve("cinematic-qa").resolve(RUN);Files.createDirectories(p.getParent());
            Files.writeString(p,row+System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }catch(java.io.IOException failure){com.mojang.logging.LogUtils.getLogger().warn("Held-item QA probe failed",failure);}
    }
    private static Vector4f point(double x,double y,double z){
        double a=-Math.PI/4,dx=x-8,dy=y-8;
        return new Vector4f((float)((8+dx*Math.cos(a)-dy*Math.sin(a))/16),
                (float)((8+dx*Math.sin(a)+dy*Math.cos(a))/16),(float)(z/16),1);
    }
    private static JsonArray screen(Matrix4f model,Vector4f point){
        point.mul(model).mul(RenderSystem.getProjectionMatrix());var out=new JsonArray();
        out.add(point.x/point.w);out.add(point.y/point.w);out.add(point.z/point.w);return out;
    }
}
