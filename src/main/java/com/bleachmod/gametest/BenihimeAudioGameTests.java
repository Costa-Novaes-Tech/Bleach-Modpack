package com.bleachmod.gametest;

import com.bleachmod.Reference;
import com.bleachmod.common.visual.BenihimeAudioTimeline;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("bleachmod") @PrefixGameTestTemplate(false)
public final class BenihimeAudioGameTests {
    @GameTest(template="empty",batch="benihime_audio")
    public static void dialogueDoesNotReplayForLateObserversAndAllCuesHaveAssets(GameTestHelper h){
        var bankai=BenihimeAudioTimeline.Cue.BANKAI;
        h.assertTrue(BenihimeAudioTimeline.due(27,28,bankai)&&BenihimeAudioTimeline.due(27,35,bankai),"Dialogue is lost at its cue or after a short render stall");
        h.assertTrue(!BenihimeAudioTimeline.due(28,29,bankai)&&!BenihimeAudioTimeline.due(99,100,bankai)
                &&!BenihimeAudioTimeline.due(27,45,bankai)&&!BenihimeAudioTimeline.due(40,27,bankai),"Dialogue replays, starts late or plays after a clock rewind");
        int previous=-1;
        for(var cue:BenihimeAudioTimeline.Cue.values()){
            h.assertTrue(cue.tick>previous&&cue.tick<560,"Audio cue order escapes entrance timeline");previous=cue.tick;
            h.assertTrue(net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS.containsKey(Reference.id(cue.event)),"Unregistered sound event "+cue.event);
        }
        for(String asset:new String[]{"benihime_entrance","benihime_voice_bankai","benihime_voice_name","benihime_curtain","benihime_reveal","benihime_threads"}){
            try(var input=BenihimeAudioGameTests.class.getResourceAsStream("/assets/bleachmod/sounds/"+asset+".ogg")){
                h.assertTrue(input!=null&&new String(input.readNBytes(4),java.nio.charset.StandardCharsets.US_ASCII).equals("OggS"),"Missing/invalid OGG "+asset);
            }catch(java.io.IOException failure){throw new IllegalStateException(failure);}
        }
        h.assertTrue(BenihimeAudioGameTests.class.getResource("/META-INF/licenses/benihime-voice.txt")!=null,"Voice attribution missing from packaged resources");
        h.succeed();
    }
}
