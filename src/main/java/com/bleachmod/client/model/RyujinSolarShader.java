package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;

/** Unlit solar material with alpha discard and fog; never changes world lighting. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
public final class RyujinSolarShader {
    private static ShaderInstance solar;
    private static boolean checkedOculus;
    private static Object iris;
    private static java.lang.reflect.Method isShaderPackInUse;
    private RyujinSolarShader(){ }
    public static ShaderInstance get(){return solar!=null?solar:GameRenderer.getRendertypeEyesShader();}
    public static String status(){
        return shaderPackActive()?(compositeAfterLevel()?"shader-pack-post":"shader-pack-entity")
                :solar==null?"fallback-eyes":"solar-unlit";
    }
    public static boolean compositeAfterLevel(){
        return Boolean.getBoolean("bleachmod.ryujinShaderComposite") && shaderPackActive();
    }
    static boolean usePackMaterial(){return shaderPackActive()&&!compositeAfterLevel();}
    public static boolean shaderPackActive(){
        if(!checkedOculus){
            checkedOculus=true;
            if(ModList.get().isLoaded("oculus")){
                try{
                    var api=Class.forName("net.irisshaders.iris.api.v0.IrisApi");
                    iris=api.getMethod("getInstance").invoke(null);
                    isShaderPackInUse=api.getMethod("isShaderPackInUse");
                }catch(ReflectiveOperationException|LinkageError failure){
                    LogUtils.getLogger().warn("Oculus shader pack state unavailable",failure);
                }
            }
        }
        if(isShaderPackInUse==null)return false;
        try{
            return (boolean)isShaderPackInUse.invoke(iris);
        }catch(ReflectiveOperationException failure){
            isShaderPackInUse=null;
            LogUtils.getLogger().warn("Oculus shader pack state unavailable",failure);
            return false;
        }
    }
    @SubscribeEvent public static void register(RegisterShadersEvent event){
        try{
            event.registerShader(new ShaderInstance(event.getResourceProvider(),Reference.id("ryujin_solar"),DefaultVertexFormat.NEW_ENTITY),shader->{
                solar=shader;LogUtils.getLogger().info("Ryujin solar shader ready: unlit with alpha discard");
            });
        }catch(java.io.IOException|RuntimeException error){
            solar=null;LogUtils.getLogger().warn("Ryujin solar shader unavailable; keeping unlit fallback",error);
        }
    }
}
