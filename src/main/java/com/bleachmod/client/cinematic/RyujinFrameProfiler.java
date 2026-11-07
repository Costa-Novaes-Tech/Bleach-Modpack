package com.bleachmod.client.cinematic;

import com.bleachmod.client.model.RyujinSunVisual;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;

/** Development QA only. Actual inter-frame cadence, with capture mode disclosed. */
final class RyujinFrameProfiler {
    private record Sample(int tick,double clientTick,double ms,int cards,int particles,int flames,int lights) { }
    private static final ArrayList<Sample> SAMPLES=new ArrayList<>();
    private static long previous;
    private static Object session;
    private static final java.util.Map<String,Integer> SCREENS=new java.util.LinkedHashMap<>();
    private RyujinFrameProfiler(){ }
    static void sample(Object trial,int tick){
        if(session!=trial){SAMPLES.clear();SCREENS.clear();previous=0;session=trial;}
        var screen=net.minecraft.client.Minecraft.getInstance().screen;
        if(tick>=0&&tick<=320)SCREENS.merge(screen==null?"none":screen.getClass().getName(),1,Integer::sum);
        long now=System.nanoTime();
        if(previous!=0&&tick>=0&&tick<=320)SAMPLES.add(new Sample(tick,RyujinBankaiEntranceClient.localPlaybackTick(),(now-previous)/1e6,
                RyujinSunVisual.cardCount(),RyujinSunVisual.particleCount(),RyujinSunVisual.flameCount(),RyujinSolarLight.count()));
        previous=now;
    }
    private static double percentile(double[] sorted,double p){return sorted.length==0?0:sorted[Math.min(sorted.length-1,(int)Math.ceil(p*sorted.length)-1)];}
    static void write(Path folder){
        if(SAMPLES.isEmpty())return;
        try{
            Files.createDirectories(folder);
            var lines=new StringBuilder("tick,client_tick,frame_ms,cards,owned_particles,owned_flames,solar_lights\n");
            for(var s:SAMPLES)lines.append(String.format(Locale.ROOT,"%d,%.3f,%.5f,%d,%d,%d,%d%n",s.tick,s.clientTick,s.ms,s.cards,s.particles,s.flames,s.lights));
            Files.writeString(folder.resolve("frame-times.csv"),lines);
            double[] steady=SAMPLES.stream().filter(s->s.tick>=112&&s.tick<176).mapToDouble(Sample::ms).sorted().toArray();
            var mc=net.minecraft.client.Minecraft.getInstance();
            var report=new com.google.gson.JsonObject();
            report.addProperty("scope","single-scene sustained sun ticks 112..175; inter-frame cadence, not GPU timer");
            report.addProperty("gpu",org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER));
            report.addProperty("width",mc.getWindow().getWidth());report.addProperty("height",mc.getWindow().getHeight());
            report.addProperty("vsync",mc.options.enableVsync().get());
            report.addProperty("fps_limit",mc.options.framerateLimit().get());
            report.addProperty("fire_detail",com.bleachmod.client.ClientVisualConfig.RYUJIN_FIRE_DETAIL.get().name());
            report.addProperty("samples",steady.length);
            report.addProperty("p50_ms",percentile(steady,.5));report.addProperty("p95_ms",percentile(steady,.95));report.addProperty("p99_ms",percentile(steady,.99));
            var phases=new com.google.gson.JsonObject();
            String[] names={"preparation","speech_ignition","explosion_formation","sustained_sun","absorption","sealed_blade","name_hold","return"};
            int[] bounds={0,40,70,112,176,202,220,270,300};
            for(int i=0;i<names.length;i++){
                int from=bounds[i],to=bounds[i+1];
                double[] times=SAMPLES.stream().filter(s->s.clientTick>=from&&s.clientTick<to).mapToDouble(Sample::ms).sorted().toArray();
                var phase=new com.google.gson.JsonObject();phase.addProperty("from_client_tick",from);phase.addProperty("until_client_tick_exclusive",to);
                phase.addProperty("samples",times.length);phase.addProperty("p50_ms",percentile(times,.5));phase.addProperty("p95_ms",percentile(times,.95));phase.addProperty("p99_ms",percentile(times,.99));
                phases.add(names[i],phase);
            }
            report.add("phases",phases);
            report.add("screen_samples",new com.google.gson.Gson().toJsonTree(SCREENS));
            report.addProperty("screenshot_capture",!Boolean.getBoolean("bleachmod.qaNoScreenshots"));
            report.addProperty("video_capture",Boolean.getBoolean("bleachmod.ryujinQaVideo"));
            report.addProperty("solar_pose_pilot",com.bleachmod.common.visual.RyujinSolarPose.pilot());
            report.addProperty("solar_shader",com.bleachmod.client.model.RyujinSolarShader.status());
            report.addProperty("visual_v14",com.bleachmod.common.visual.RyujinSealFlow.enabled());
            report.addProperty("light_backend",RyujinSolarLight.backend());
            report.addProperty("solar_light_enabled",com.bleachmod.client.ClientVisualConfig.RYUJIN_SOLAR_LIGHTS.get()&&!Boolean.getBoolean("bleachmod.ryujinLightOff"));
            report.addProperty("max_cards",SAMPLES.stream().mapToInt(Sample::cards).max().orElse(0));
            report.addProperty("max_owned_particles",SAMPLES.stream().mapToInt(Sample::particles).max().orElse(0));
            report.addProperty("max_lights",SAMPLES.stream().mapToInt(Sample::lights).max().orElse(0));
            var sealed=SAMPLES.stream().filter(s->s.tick>=203).toList();
            var ended=SAMPLES.stream().filter(s->s.tick>=302).toList();
            report.addProperty("post_seal_samples",sealed.size());
            report.addProperty("post_seal_clean",!sealed.isEmpty()&&sealed.stream().allMatch(s->s.cards==0&&s.flames==0&&s.lights==0));
            report.addProperty("post_end_samples",ended.size());
            report.addProperty("post_end_clean",!ended.isEmpty()&&ended.stream().allMatch(s->s.cards==0&&s.particles==0&&s.lights==0));
            // Keep the original strict trial-based result; disclose clock drift separately.
            var playbackSealed=SAMPLES.stream().filter(s->s.clientTick>=203||(s.clientTick<0&&s.tick>=203)).toList();
            report.addProperty("playback_post_seal_samples",playbackSealed.size());
            report.addProperty("playback_post_seal_clean",!playbackSealed.isEmpty()&&playbackSealed.stream().allMatch(s->s.cards==0&&s.flames==0&&s.lights==0));
            report.addProperty("max_trial_playback_delta_ticks",SAMPLES.stream().filter(s->s.clientTick>=0).mapToDouble(s->s.tick-s.clientTick).max().orElse(0));
            Files.writeString(folder.resolve("performance.json"),new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(report));
        }catch(java.io.IOException error){com.mojang.logging.LogUtils.getLogger().warn("Ryujin QA frame profiler",error);}
    }
}
