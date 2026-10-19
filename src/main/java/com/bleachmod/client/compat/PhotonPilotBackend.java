package com.bleachmod.client.compat;

import com.lowdragmc.photon.client.fx.*;
import com.lowdragmc.photon.client.gameobject.emitter.particle.*;
import com.lowdragmc.photon.client.gameobject.emitter.data.number.*;
import com.lowdragmc.photon.client.gameobject.emitter.data.material.TextureMaterial;
import com.lowdragmc.photon.client.gameobject.emitter.data.shape.Box;
import com.lowdragmc.photon.gui.editor.FXProject;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/** Authored API pilot: two finite emitters, no camera, no damage, no external FX copied. */
final class PhotonPilotBackend {
    private record Running(FXRuntime runtime,net.minecraft.client.multiplayer.ClientLevel world,long until){ }
    private static final List<Running> ACTIVE=new ArrayList<>();
    private PhotonPilotBackend(){ }
    private static ParticleEmitter emitter(String name,boolean ash,float width,float height,boolean minimal){
        var emitter=new ParticleEmitter();emitter.setName(name);var c=emitter.config;
        c.setLooping(false);c.setDuration(ash?18:14);c.setStartDelay(NumberFunction.constant(ash?10:0));c.setStartLifetime(NumberFunction.constant(ash?22:12));
        c.setMaxParticles(128);c.setStartSpeed(NumberFunction.constant(ash?1.8:1.2));c.setSimulationSpace(ParticleConfig.Space.World);
        c.setStartSize(new NumberFunction3(ash?.06:.12,ash?.06:.12,ash?.06:.12));c.setStartColor(NumberFunction.color(ash?0xff49423b:0xffffa020));
        c.emission.setEmissionRate(NumberFunction.constant(minimal?1:4));c.shape.setShape(new Box());c.shape.setScale(new NumberFunction3(Math.min(2,width),Math.min(4,height),Math.min(2,width)));
        c.material.setMaterial(new TextureMaterial(ResourceLocation.fromNamespaceAndPath("minecraft","textures/particle/generic_0.png")));
        c.material.setCull(false);c.renderer.setBloomEffect(false); // Explicitly avoid a global post-process in this first pilot.
        return emitter;
    }
    private static FX definition(float width,float height,boolean minimal){var fx=new FX();fx.getMainFX().objects().add(emitter("higashi_embers",false,width,height,minimal));fx.getMainFX().objects().add(emitter("higashi_ash",true,width,height,minimal));return fx;}
    static boolean start(Vec3 at,float width,float height){var mc=Minecraft.getInstance();tick();if(mc.level==null||mc.player==null||mc.player.distanceToSqr(at)>80*80||ACTIVE.size()>=4)return false;
        var fx=definition(width,height,mc.options.particles().get()==net.minecraft.client.ParticleStatus.MINIMAL);var effect=new FXEffect(fx,mc.level){
            @Override public void start(){runtime=fx.createRuntime();runtime.root.updatePos(new Vector3f((float)at.x,(float)(at.y+height*.5),(float)at.z));runtime.emmit(this);}
        };effect.start();ACTIVE.add(new Running(effect.getRuntime(),mc.level,mc.level.getGameTime()+52));return true;
    }
    static void tick(){var mc=Minecraft.getInstance();var it=ACTIVE.iterator();while(it.hasNext()){var e=it.next();if(mc.level!=e.world||mc.level.getGameTime()>=e.until||!com.bleachmod.client.ClientVisualConfig.PHOTON_PILOT.get()){e.runtime.destroy(true);it.remove();}}}
    static void clear(){ACTIVE.forEach(e->e.runtime.destroy(true));ACTIVE.clear();}
    static Path export() throws IOException {
        var folder=Minecraft.getInstance().gameDirectory.toPath().resolve("photon-pilot");Files.createDirectories(folder);var fx=definition(.7F,1.8F,false);var tag=new CompoundTag();tag.putInt("_version",1);tag.put("fx",fx.serializeNBT());
        try(var out=Files.newOutputStream(folder.resolve("higashi-incineration.fx"))){NbtIo.writeCompressed(tag,out);}
        var project=new FXProject().newEmptyProject();project.getFx().deserializeNBT(fx.serializeNBT());NbtIo.write(project.serializeNBT(),folder.resolve("higashi-incineration.fxproj").toFile());return folder.toAbsolutePath();
    }
}
