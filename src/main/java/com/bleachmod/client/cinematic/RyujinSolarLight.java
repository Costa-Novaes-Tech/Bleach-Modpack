package com.bleachmod.client.cinematic;

import com.bleachmod.Reference;
import com.bleachmod.client.ClientVisualConfig;
import com.bleachmod.common.visual.RyujinBankaiPresentation;
import com.bleachmod.common.visual.RyujinBankaiTimeline;
import com.mojang.logging.LogUtils;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Optional Shimmer adapter. No Shimmer types or world-light mutation in the base mod. */
@Mod.EventBusSubscriber(modid=Reference.MOD_ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
public final class RyujinSolarLight {
    private static final Map<Integer,Object> LIGHTS=new HashMap<>();
    private static boolean initialized,failed;
    private static Object manager;
    private static Method add,setPos,setColor,remove,isRemoved;
    private static Field radius;
    private RyujinSolarLight(){ }
    private static boolean initialize(){
        if(failed||!ModList.get().isLoaded("shimmer"))return false;
        if(initialized)return true;
        try{
            Class<?> api=Class.forName("com.lowdragmc.shimmer.client.light.LightManager");
            Class<?> light=Class.forName("com.lowdragmc.shimmer.client.light.ColorPointLight");
            manager=api.getField("INSTANCE").get(null);
            add=api.getMethod("addLight",Vector3f.class,int.class,float.class,boolean.class);
            setPos=light.getMethod("setPos",float.class,float.class,float.class);
            setColor=light.getMethod("setColor",float.class,float.class,float.class,float.class);
            remove=light.getMethod("remove");isRemoved=light.getMethod("isRemoved");
            radius=light.getField("radius");initialized=true;
            LogUtils.getLogger().info("Ryujin solar light: Shimmer API ready");return true;
        }catch(ReflectiveOperationException|LinkageError e){fail(e);return false;}
    }
    private static void fail(Throwable error){
        if(!failed)LogUtils.getLogger().warn("Ryujin solar light unavailable; keeping base presentation",error);
        failed=true;clear();
    }
    public static String backend(){return failed?"shimmer-error":initialized?"shimmer":ModList.get().isLoaded("shimmer")?"shimmer-pending":"base-emissive";}
    public static int count(){return LIGHTS.size();}
    public static void remove(int owner){
        Object light=LIGHTS.remove(owner);
        if(light!=null&&remove!=null)try{remove.invoke(light);}catch(ReflectiveOperationException ignored){ }
    }
    public static void clear(){for(int owner:Set.copyOf(LIGHTS.keySet()))remove(owner);}
    public static void retain(Set<Integer> owners){for(int id:Set.copyOf(LIGHTS.keySet()))if(!owners.contains(id))remove(id);}
    public static void update(int owner,Vec3 anchor,Vec3 forward,double phase,RyujinBankaiPresentation.Blade blade){
        if(!ClientVisualConfig.RYUJIN_SOLAR_LIGHTS.get()||Boolean.getBoolean("bleachmod.ryujinLightOff")
                ||phase<20||phase>=52){remove(owner);return;}
        if(!initialize())return;
        try{
            double size=RyujinBankaiTimeline.sunRadius(phase)/9;
            if(size<.01){remove(owner);return;}
            Vec3 centre=RyujinBankaiPresentation.sunCenter(anchor,forward);
            if(com.bleachmod.common.visual.RyujinSolarPose.pilot())centre=com.bleachmod.common.visual.RyujinSolarPose.center(anchor,forward,phase,blade);
            else if(blade!=null)centre=centre.lerp(blade.hand().lerp(blade.tip(),.55),RyujinBankaiTimeline.progress(phase,32,52));
            Object light=LIGHTS.get(owner);
            if(light!=null&&(boolean)isRemoved.invoke(light)){LIGHTS.remove(owner);light=null;}
            if(light==null){
                light=add.invoke(manager,new Vector3f((float)centre.x,(float)centre.y,(float)centre.z),0xFFFF8A24,(float)(18*size),false);
                if(light==null)return;
                LIGHTS.put(owner,light);
            }
            setPos.invoke(light,(float)centre.x,(float)centre.y,(float)centre.z);
            radius.setFloat(light,(float)(18*size));
            setColor.invoke(light,1F,.42F,.08F,(float)Math.min(1,size));
        }catch(ReflectiveOperationException|LinkageError e){fail(e);}
    }
    @SubscribeEvent public static void reload(RegisterClientReloadListenersEvent event){
        event.registerReloadListener((ResourceManagerReloadListener)resources->{
            clear();
            com.bleachmod.client.model.RyujinSunVisual.clear();
            com.bleachmod.client.model.RyujinSolarPost.reset();
        });
    }
}
