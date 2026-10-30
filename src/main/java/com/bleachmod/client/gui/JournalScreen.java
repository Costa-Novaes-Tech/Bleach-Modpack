package com.bleachmod.client.gui;

import com.bleachmod.client.gui.layout.LayoutContext;
import com.bleachmod.client.gui.layout.LayoutScreen;
import com.bleachmod.common.data.*;
import com.bleachmod.common.network.NetworkHandler;
import com.bleachmod.common.network.c2s.*;
import com.bleachmod.common.quest.*;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Locale;

/**
 * Mission journal. Layout: gui_layouts/journal.json. Shows {@link #ROWS} quests at a time
 * (arrows move one row, « » one page) and details of the selected quest on the parchment.
 */
public final class JournalScreen extends LayoutScreen {
    static final int ROWS = 5;
    static final int OBJECTIVES = 3;
    static final int REWARDS = 2;

    private List<Quest> quests = List.of();
    private String selected;
    private int first;

    public JournalScreen() {
        super(Component.translatable("screen.bleachmod.journal"), "journal");
    }

    private PlayerData data() {
        return minecraft == null || minecraft.player == null ? null : PlayerCapability.get(minecraft.player).orElse(null);
    }

    private Quest quest() {
        return selected == null ? null : QuestRegistry.getQuest(selected);
    }

    @Override
    protected void init() {
        PlayerData player = data();
        if (player != null && com.bleachmod.Reference.RACE_SHINIGAMI.equals(player.getCharacter().getRace())) {
            addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
                    Component.translatable("item.bleachmod.story_journal"),
                    button -> minecraft.setScreen(new StoryBookScreen(this)))
                    .bounds(width - 160, height - 24, 152, 20).build());
        }
        quests = List.copyOf(QuestRegistry.allQuests());
        if (quest() == null && !quests.isEmpty()) selected = quests.get(0).getQuestKey();
        clampScroll();
    }

    @Override
    public void tick() {
        List<Quest> current = List.copyOf(QuestRegistry.allQuests());
        if (!current.equals(quests)) {
            quests = current;
            first = 0;
            if (quest() == null) selected = quests.isEmpty() ? null : quests.get(0).getQuestKey();
        }
        clampScroll();
    }

    private void clampScroll() {
        first = Math.max(0, Math.min(first, Math.max(0, quests.size() - ROWS)));
    }

    private boolean pending(PlayerData d, Quest q) {
        QuestProgress p = d.getPlayerQuestData().getProgress(q.getQuestKey());
        if (p == null) return false;
        for (int i = 0; i < q.getRewards().size(); i++) if (!p.isRewardClaimed(i)) return true;
        return false;
    }

    @Override
    protected LayoutContext context() {
        LayoutContext ctx = new LayoutContext();
        PlayerData d = data();
        ctx.text("title", title);

        for (int i = 0; i < ROWS; i++) {
            int index = first + i;
            boolean visible = index < quests.size();
            ctx.flag("row" + i + "_visible", visible);
            if (!visible) continue;
            Quest item = quests.get(index);
            QuestStatus status = d == null ? QuestStatus.NOT_STARTED : d.getPlayerQuestData().getStatus(item.getQuestKey());
            ctx.text("row" + i + "_name", Component.translatable(item.getTitle()));
            ctx.flag("row" + i + "_selected", item.getQuestKey().equals(selected));
            ctx.flag("row" + i + "_done", status == QuestStatus.SUCCESS);
            ctx.flag("row" + i + "_locked", d != null && status == QuestStatus.NOT_STARTED
                    && !QuestAvailabilityChecker.isAvailable(d, item.getQuestKey()));
        }
        int scrollable = Math.max(0, quests.size() - ROWS);
        ctx.value("list_scroll", scrollable == 0 ? 0F : first / (float) scrollable);
        ctx.flag("can_up", first > 0);
        ctx.flag("can_down", first < scrollable);

        Quest q = quest();
        boolean valid = q != null && d != null;
        ctx.flag("has_quest", q != null);
        if (q != null) {
            QuestProgress p = d == null ? null : d.getPlayerQuestData().getProgress(selected);
            QuestStatus status = d == null ? QuestStatus.NOT_STARTED : d.getPlayerQuestData().getStatus(selected);
            ctx.text("quest_title", Component.translatable(q.getTitle()));
            ctx.text("quest_desc", Component.translatable(q.getDescription()));
            Component notice = null;
            if (p != null && !p.matchesDefinition(q)) notice = Component.translatable("message.bleachmod.quest.changed");
            else if (d != null && status == QuestStatus.NOT_STARTED && !QuestAvailabilityChecker.isAvailable(d, selected))
                notice = Component.translatable("screen.bleachmod.quest_locked");
            ctx.flag("has_notice", notice != null);
            if (notice != null) ctx.text("notice", notice);

            for (int i = 0; i < OBJECTIVES; i++) {
                boolean visible = i < q.getObjectives().size();
                ctx.flag("obj" + i + "_visible", visible);
                if (!visible) continue;
                var o = q.getObjectives().get(i);
                int done = p == null ? 0 : p.getObjectiveProgress(i);
                int required = p == null ? o.getRequired() : p.getRequired(i, o.getRequired());
                ctx.text("obj" + i + "_text", o.describe().copy().append(" [" + done + "/" + required + "]"));
                ctx.flag("obj" + i + "_done", done >= required);
                ctx.flag("obj" + i + "_failed", status == QuestStatus.FAILED && done < required);
            }
            for (int i = 0; i < REWARDS; i++) {
                boolean visible = i < q.getRewards().size();
                ctx.flag("rew" + i + "_visible", visible);
                if (visible) ctx.text("rew" + i + "_text", q.getRewards().get(i).describe().copy()
                        .append(p != null && p.isRewardClaimed(i) ? " ✓" : ""));
            }
            ctx.flag("quest_success", status == QuestStatus.SUCCESS);
        }

        QuestProgress p = valid ? d.getPlayerQuestData().getProgress(selected) : null;
        boolean compatible = p == null || p.matchesDefinition(q);
        ctx.flag("can_start", valid && compatible && q.getQuestGiver().isBlank() && QuestAvailabilityChecker.isAvailable(d, selected));
        ctx.flag("can_claim", valid && compatible && d.getPlayerQuestData().isCompleted(selected) && pending(d, q));
        ctx.flag("can_track", valid && d.getPlayerQuestData().isAccepted(selected));
        boolean tracked = valid && selected.equals(d.getPlayerQuestData().getTrackedQuestId());
        ctx.text("track_label", Component.translatable(tracked ? "screen.bleachmod.untrack" : "screen.bleachmod.journal.track"));
        boolean portuguese = minecraft != null && minecraft.getLanguageManager().getSelected().toLowerCase(Locale.ROOT).startsWith("pt");
        boolean success = q != null && d != null && d.getPlayerQuestData().getStatus(selected) == QuestStatus.SUCCESS;
        ctx.flag("stamp_pt", success && portuguese);
        ctx.flag("stamp_en", success && !portuguese);
        return ctx;
    }

    @Override
    protected void onAction(String action) {
        PlayerData d = data();
        if (action.startsWith("row:")) {
            int index = first + Integer.parseInt(action.substring(4));
            if (index < quests.size()) selected = quests.get(index).getQuestKey();
            return;
        }
        switch (action) {
            case "scroll_up" -> first--;
            case "scroll_down" -> first++;
            case "page_prev" -> first -= ROWS;
            case "page_next" -> first += ROWS;
            case "start" -> {
                if (quest() != null) NetworkHandler.sendToServer(new QuestActionC2S(QuestActionC2S.Action.START, selected, -1));
            }
            case "claim" -> {
                if (d != null && quest() != null && d.getPlayerQuestData().getProgress(selected) != null)
                    NetworkHandler.sendToServer(new ClaimQuestRewardC2S(selected, -1));
            }
            case "track" -> {
                if (d != null && quest() != null)
                    NetworkHandler.sendToServer(new SetTrackedQuestC2S(selected.equals(d.getPlayerQuestData().getTrackedQuestId()) ? "" : selected));
            }
            case "character" -> minecraft.setScreen(new StatusScreen());
            default -> {
            }
        }
        clampScroll();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        first -= (int) Math.signum(delta);
        clampScroll();
        return true;
    }
}
