package com.bleachmod.client.hud;

import com.bleachmod.client.input.ModKeybinds;
import com.bleachmod.common.data.PlayerData;
import com.bleachmod.common.quincy.QuincyTechniques;
import com.bleachmod.common.technique.MasteryFeedback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** Same compact guide as zanpakuto kits, with independent Quincy mastery and no fictitious Bankai. */
public final class QuincyHud {
    private QuincyHud() { }
    public static void render(GuiGraphics g,int w,int h,PlayerData data) {
        var mc=Minecraft.getInstance();var q=data.getQuincy();boolean advanced=q.isVollstandigActive();
        float scale=Math.min(.85F,Math.min(w/500F,h/260F));int width=188,height=167;
        g.pose().pushPose();g.pose().scale(scale,scale,1);g.pose().translate(w/scale-width-8,Math.max(110,h/scale-height-66),0);
        g.fill(0,0,width,height,0xCC102333);g.fill(0,0,2,height,0xFF85D7F0);
        line(g,Component.translatable(advanced?"hud.bleachmod.quincy.vollstandig":"hud.bleachmod.quincy.title").getString(),6,0xDFF7FF);
        line(g,Component.translatable("hud.bleachmod.quincy.mastery",MasteryFeedback.value(advanced?q.getVollstandigMastery():q.getMastery())).getString(),19,0x89CDE8);
        var keys=new net.minecraft.client.KeyMapping[]{ModKeybinds.TECHNIQUE_SLOT_1,ModKeybinds.TECHNIQUE_SLOT_2,ModKeybinds.TECHNIQUE_SLOT_3,ModKeybinds.TECHNIQUE_SLOT_4};
        int[] cooldowns=data.getStatus().getHudCooldowns();
        for(var def:advanced?QuincyTechniques.ADVANCED:QuincyTechniques.ALL){int i=def.slot()-1;
            line(g,"["+keys[i].getTranslatedKeyMessage().getString()+"] "+Component.translatable(def.nameKey()).getString(),33+i*25,0xFFFFFF);
            line(g,Component.translatable("hud.bleachmod.mastery.technique",MasteryFeedback.value(advanced?q.getAdvancedMastery(def.slot()):q.getTechniqueMastery(def.slot()))).getString(),42+i*25,0x85AAC2);
            String cost=Component.translatable("hud.bleachmod.technique.cost",MasteryFeedback.value(def.cost()),MasteryFeedback.value(def.cooldown()/20.0)).getString();
            if(def.stamina()>0)cost+=" · "+(int)def.stamina()+" ST";
            if(advanced&&def.slot()==3)cost+=" · "+com.bleachmod.client.cinematic.QuincyVollstandigVisuals.steps(mc.player.getId())+"/3";
            if(cooldowns[i]>0)cost=Component.translatable("hud.bleachmod.quincy.cooldown",MasteryFeedback.value(cooldowns[i]/20.0)).getString();
            if(!advanced&&def.slot()==4&&q.isSprengerActive()){
                long age=mc.level.getGameTime()-q.getSprengerCreationTick();
                cost=Component.translatable(age<30?"hud.bleachmod.quincy.preparing":"hud.bleachmod.quincy.detonate",MasteryFeedback.value(Math.max(0,120-age)/20.0)).getString();
            }
            line(g,cost,51+i*25,0x85AAC2);
        }
        line(g,Component.translatable(advanced?"hud.bleachmod.quincy.volley":"hud.bleachmod.quincy.basic").getString(),140,0xD5BF78);
        line(g,Component.translatable(data.getStatus().areHudCooldownsDisabled()?"hud.bleachmod.mastery.test_mode":"hud.bleachmod.quincy.form_hint").getString(),153,0xF1B46B);
        g.pose().popPose();
    }
    private static void line(GuiGraphics g,String text,int y,int color){var font=Minecraft.getInstance().font;g.drawString(font,font.plainSubstrByWidth(text,172),8,y,color,false);}
}
