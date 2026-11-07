package com.bleachmod.client.cinematic;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/** Disposable development QA only: records the game's framebuffer, never the desktop. */
final class RyujinQaVideoRecorder {
    private static final int FPS=20;
    private record Frame(int index,byte[] pixels) { }
    private static final class Recording {
        final Path folder;
        final int width,height;
        final long start=System.nanoTime(),wallStart=System.currentTimeMillis();
        final ArrayBlockingQueue<Frame> queue=new ArrayBlockingQueue<>(2);
        final Process encoder;
        final Thread writer;
        int accepted,dropped,repeated,lastIndex=-1;
        volatile String error="";
        Recording(Path folder,int width,int height)throws java.io.IOException{
            this.folder=folder;this.width=width;this.height=height;
            Files.createDirectories(folder);
            Path binary=Path.of(System.getProperty("bleachmod.qaFfmpeg","")).toAbsolutePath();
            if(!Files.isRegularFile(binary))throw new java.io.IOException("Set qaFfmpeg to the existing local FFmpeg executable");
            encoder=new ProcessBuilder(binary.toString(),"-nostdin","-hide_banner","-loglevel","warning",
                    "-f","rawvideo","-pixel_format","rgba","-video_size",width+"x"+height,
                    "-framerate",Integer.toString(FPS),"-i","pipe:0","-an","-c:v","libx264",
                    "-preset","ultrafast","-crf","21","-pix_fmt","yuv420p","-y",folder.resolve("scene-video.mp4").toString())
                    .redirectError(folder.resolve("video-encoder.log").toFile())
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD).start();
            writer=new Thread(this::write,"ryujin-qa-video");writer.setDaemon(true);writer.start();
        }
        private void write(){
            int written=0;byte[] previous=null;
            try(var stream=encoder.getOutputStream()){
                while(true){
                    var frame=queue.take();
                    byte[] fill=previous==null?frame.pixels:previous;
                    while(fill!=null&&written<frame.index){stream.write(fill);written++;repeated++;}
                    if(frame.pixels==null)break;
                    stream.write(frame.pixels);written++;previous=frame.pixels;
                }
            }catch(Exception failure){error=failure.toString();}
        }
        void capture(){
            int index=(int)((System.nanoTime()-start)*FPS/1_000_000_000L);
            if(index<=lastIndex||index>FPS*30||!error.isEmpty())return;
            lastIndex=index;
            if(queue.remainingCapacity()==0){dropped++;return;}
            try(var img=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){
                if(img.getWidth()!=width||img.getHeight()!=height){error="Framebuffer resized during recording";return;}
                byte[] bytes=new byte[width*height*4];
                ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().put(img.getPixelsRGBA());
                if(queue.offer(new Frame(index,bytes)))accepted++;else dropped++;
            }
        }
        void finish(){
            try{
                int end=(int)Math.min(FPS*30,(System.nanoTime()-start)*FPS/1_000_000_000L);
                if(!queue.offer(new Frame(end,null),2,TimeUnit.SECONDS))error="Encoder queue did not drain";
                writer.join(3000);
                if(writer.isAlive()){error="Encoder writer did not finish";encoder.destroy();}
                if(!encoder.waitFor(5,TimeUnit.SECONDS)){error="Encoder did not close";encoder.destroy();}
                var report=new JsonObject();
                report.addProperty("source","Minecraft framebuffer; no desktop capture, no microphone");
                report.addProperty("fps",FPS);report.addProperty("width",width);report.addProperty("height",height);
                report.addProperty("wall_start_ms",wallStart);report.addProperty("accepted_frames",accepted);
                report.addProperty("queue_drops",dropped);report.addProperty("repeated_frames",repeated);
                report.addProperty("note","Repeated frames preserve wall time when rendering/capture stalls. Not a performance benchmark.");
                report.addProperty("error",error);
                report.addProperty("encoder_exit",encoder.isAlive()?-1:encoder.exitValue());
                Files.writeString(folder.resolve("video-capture.json"),new GsonBuilder().setPrettyPrinting().create().toJson(report));
            }catch(Exception failure){LogUtils.getLogger().warn("Ryujin QA video finalization",failure);encoder.destroy();}
        }
    }
    private static Recording current;
    private static Object session;
    private static boolean failed;
    private RyujinQaVideoRecorder(){ }
    static void capture(Object trial,int age,Path folder){
        if(!Boolean.getBoolean("bleachmod.ryujinQaVideo")||age<0||age>320)return;
        if(session!=trial){finish();session=trial;failed=false;}
        if(failed)return;
        try{
            if(current==null){var target=Minecraft.getInstance().getMainRenderTarget();current=new Recording(folder,target.width,target.height);}
            current.capture();
        }catch(Exception error){failed=true;finish();LogUtils.getLogger().warn("Ryujin QA video capture disabled",error);}
    }
    static void finish(){if(current!=null){current.finish();current=null;}}
}
