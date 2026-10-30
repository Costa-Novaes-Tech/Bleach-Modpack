package com.bleachmod.client.gui;

import com.bleachmod.common.data.*;
import com.bleachmod.common.quest.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import java.util.*;

/** Vanilla book proportions, with pages rebuilt from current quest snapshots on every render. */
public final class StoryBookScreen extends Screen {
    private static final ResourceLocation BOOK=new ResourceLocation("textures/gui/book.png");
    private final Screen parent;
    private int page;
    private Button prev,next;
    public StoryBookScreen(Screen parent) { super(Component.translatable("item.bleachmod.story_journal"));this.parent=parent; }
    public static void open() { net.minecraft.client.Minecraft.getInstance().setScreen(new StoryBookScreen(null)); }
    @Override protected void init() {
        int x=(width-192)/2,y=(height-192)/2;
        prev=addRenderableWidget(Button.builder(Component.literal("‹"),b->page=Math.max(0,page-1)).bounds(x+34,y+159,20,18).build());
        next=addRenderableWidget(Button.builder(Component.literal("›"),b->page++).bounds(x+134,y+159,20,18).build());
        addRenderableWidget(Button.builder(Component.translatable("screen.bleachmod.journal"),b->minecraft.setScreen(new JournalScreen())).bounds(x+57,y+194,98,20).build());
    }
    private List<List<FormattedCharSequence>> pages() {
        List<List<FormattedCharSequence>> result=new ArrayList<>();
        List<List<Component>> chapters=new ArrayList<>();
        List<Component> sections=new ArrayList<>();chapters.add(sections);
        var d=minecraft.player==null?null:PlayerCapability.get(minecraft.player).orElse(null);
        String stage=d==null?"none":d.getOrigin().stage().name().toLowerCase(Locale.ROOT);
        sections.add(Component.translatable("item.bleachmod.story_journal"));
        sections.add(Component.translatable("story.bleachmod.book."+stage));
        if(d!=null)for(var q:QuestRegistry.allQuests()) {
            String key=q.getQuestKey();var status=d.getPlayerQuestData().getStatus(key);
            if(status==QuestStatus.NOT_STARTED)continue;
            sections=new ArrayList<>();chapters.add(sections);
            sections.add(Component.translatable(q.getTitle()));
            sections.add(Component.translatable(q.getDescription()));
            sections.add(Component.translatable("story.bleachmod.book.status",Component.translatable("story.bleachmod.status."+status.name().toLowerCase(Locale.ROOT))));
            var p=d.getPlayerQuestData().getProgress(key);
            for(int i=0;i<q.getObjectives().size();i++) {
                var objective=q.getObjectives().get(i);
                sections.add(objective.describe());
                sections.add(Component.translatable("story.bleachmod.book.objective",i+1,p.getObjectiveProgress(i),p.getRequired(i,objective.getRequired())));
            }
        }
        if(d!=null&&com.bleachmod.Reference.RACE_SHINIGAMI.equals(d.getCharacter().getRace())) {
            List<Component> study=new ArrayList<>();chapters.add(study);
            study.add(Component.translatable("screen.bleachmod.academy"));
            for(String key:List.of("intro","zanjutsu","hakuda","hoho","kido","notice"))
                study.add(Component.translatable("screen.bleachmod.academy."+key));
        }
        for(var chapter:chapters) {
            List<FormattedCharSequence> current=new ArrayList<>();
            for(var section:chapter) {
                var lines=font.split(section,114);
                if(!current.isEmpty()&&lines.size()<=13&&current.size()+lines.size()>13) { result.add(current);current=new ArrayList<>(); }
                for(var line:lines) {
                    if(current.size()==13) { result.add(current);current=new ArrayList<>(); }
                    current.add(line);
                }
                if(current.size()<13)current.add(FormattedCharSequence.EMPTY);
            }
            if(!current.isEmpty())result.add(current);
        }
        return result;
    }
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partial) {
        renderBackground(g);int x=(width-192)/2,y=(height-192)/2;
        g.blit(BOOK,x,y,0,0,192,192,256,256);
        var pages=pages();page=Math.min(page,pages.size()-1);
        prev.active=page>0;next.active=page<pages.size()-1;
        int lineY=y+20;
        for(var line:pages.get(page)) { g.drawString(font,line,x+36,lineY,0x382C23,false);lineY+=10; }
        var number=Component.literal((page+1)+" / "+pages.size());
        g.drawCenteredString(font,number,x+96,y+164,0x75634F);
        super.render(g,mouseX,mouseY,partial);
    }
    @Override public void onClose() { minecraft.setScreen(parent); }
    @Override public boolean isPauseScreen() { return false; }
}
