package com.bleachmod.client.hud;

import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.common.quest.PlayerQuestData;
import com.bleachmod.common.quest.Quest;
import com.bleachmod.common.quest.QuestProgress;
import com.bleachmod.common.quest.QuestService;
import com.bleachmod.common.quest.QuestType;
import com.bleachmod.common.quest.QuestWaypoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

/** Accepted quests, ordered by importance, with the tracked quest expanded at the top. */
public final class TrackedQuestHud {
    private static final int GOLD = 0xE8C547;
    private static final int MUTED = 0xBDB5CA;
    private static final int LINE_HEIGHT = 11;
    private static final int MAX_CARDS = 3;

    private TrackedQuestHud() {}

    public static final IGuiOverlay OVERLAY = (gui, graphics, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.options.renderDebug) return;
        PlayerCapability.get(mc.player).ifPresent(data ->
                render(mc, graphics, data.getPlayerQuestData(), width, height,
                        data.isDataLoaded() && data.getStatus().hasCreatedCharacter()
                                && "shinigami".equals(data.getCharacter().getRace())));
    };

    private static void render(Minecraft mc, GuiGraphics graphics, PlayerQuestData data, int screenWidth, int screenHeight, boolean hasZanpakutoGuide) {
        List<Quest> accepted = data.getAcceptedQuestsForHud().stream()
                .filter(q -> !q.isStory() || hasZanpakutoGuide).toList();
        if (accepted.isEmpty()) return;
        int width = Math.min(224, screenWidth / 2 - 12);
        var status = com.bleachmod.client.gui.layout.GuiLayouts.get("hud_status");
        if (status != null) {
            var placement = com.bleachmod.client.gui.layout.LayoutGeometry.place(status, screenWidth, screenHeight);
            width = Math.min(width, (int) (screenWidth - placement.x() - status.width() * placement.scale() - 18));
        }
        if (width < 100) return;
        int x = screenWidth - width - 6;
        int y = 8;
        int bottom = Math.min(screenHeight - 65, screenHeight * 2 / 3);
        if (hasZanpakutoGuide) bottom = Math.min(bottom, (int) ZanpakutoHud.topEdge(screenWidth, screenHeight) - 6);
        if (bottom < 80) return;
        graphics.drawString(mc.font, Component.translatable("hud.bleachmod.quests.title", accepted.size()), x + 6, y, MUTED, false);
        y += 14;
        int shown = 0;
        for (Quest quest : accepted) {
            if (shown == MAX_CARDS) break;
            boolean tracked = shown == 0;
            QuestProgress progress = data.getProgress(quest.getQuestKey());
            List<FormattedCharSequence> titles = mc.font.split(Component.translatable(quest.getTitle()), width - 16);
            int titleLines = Math.min(titles.size(), tracked && bottom >= 120 ? 2 : 1);
            List<FormattedCharSequence> objectives = objectiveLines(mc, quest, progress, width - 16);
            int objectiveLines = Math.min(objectives.size(), tracked && bottom >= 120 ? 2 : 1);
            boolean navigation = quest.isStory() || !quest.getQuestGiver().isBlank();
            QuestWaypoint waypoint = data.getWaypoint(quest.getQuestKey());
            boolean compact = bottom < 120;
            if (compact && navigation) objectiveLines = 0;
            int navHeight = navigation ? (tracked ? (compact ? 22 : 28) : 12) : 0;
            int cardHeight = (compact ? 18 : 22) + (titleLines + objectiveLines) * LINE_HEIGHT + navHeight;
            // Reserve room for overflow, keeping the tracker clear of the hotbar.
            if (y + cardHeight + 14 > bottom) break;
            graphics.fill(x, y, x + width, y + cardHeight, tracked ? 0xD9171321 : 0xB3171321);
            graphics.fill(x, y, x + 2, y + cardHeight, tracked ? 0xFFE8C547 : 0xFF71647F);
            String label = quest.getType() == QuestType.SAGA ? "hud.bleachmod.quests.story" : "hud.bleachmod.quests.side";
            Component heading = Component.translatable(label);
            if (tracked) heading = heading.copy().append(" · ").append(Component.translatable("hud.bleachmod.quests.tracked"));
            graphics.drawString(mc.font, heading, x + 8, y + 5, tracked ? GOLD : MUTED, false);
            int textY = y + 18;
            for (int i = 0; i < titleLines; i++) {
                graphics.drawString(mc.font, titles.get(i), x + 8, textY, 0xFFFFFF, false);
                textY += LINE_HEIGHT;
            }
            for (int i = 0; i < objectiveLines; i++) {
                graphics.drawString(mc.font, objectives.get(i), x + 8, textY, MUTED, false);
                textY += LINE_HEIGHT;
            }
            if (navigation) renderNavigation(mc, graphics, quest, waypoint, x + 8, textY, width - 16, tracked, navHeight);
            y += cardHeight + 4;
            shown++;
        }
        if (shown < accepted.size()) {
            graphics.drawString(mc.font, Component.translatable("hud.bleachmod.quests.more", accepted.size() - shown), x + 6, y, MUTED, false);
        }
    }

    private static void renderNavigation(Minecraft mc, GuiGraphics g, Quest quest, QuestWaypoint waypoint,
                                         int x, int y, int width, boolean tracked, int height) {
        Component name = Component.translatable("entity.bleachmod.npc_" + (quest.isStory() ? quest.getStory().targetNpc() : quest.getQuestGiver()));
        boolean sameDimension = waypoint != null && waypoint.dimension().equals(mc.player.level().dimension().location());
        Component text;
        if (waypoint == null) text = Component.translatable("hud.bleachmod.quests.location_unknown", name);
        else if (!sameDimension) {
            String id = waypoint.dimension().toString();
            Component dimension = switch (id) {
                case "minecraft:overworld" -> Component.translatable("hud.bleachmod.quests.overworld");
                case "minecraft:the_nether" -> Component.translatable("hud.bleachmod.quests.nether");
                case "minecraft:the_end" -> Component.translatable("hud.bleachmod.quests.end");
                default -> Component.literal(id);
            };
            text = Component.translatable("hud.bleachmod.quests.other_dimension", name, dimension);
        } else text = Component.translatable("hud.bleachmod.quests.distance", name,
                waypoint.distanceMetres(mc.player.getX(), mc.player.getY(), mc.player.getZ()));

        int inset = tracked && sameDimension ? 27 : 0;
        if (inset > 0) {
            double dx = waypoint.pos().getX() + .5 - mc.player.getX();
            double dz = waypoint.pos().getZ() + .5 - mc.player.getZ();
            compass(g, x + 10, y + height / 2, Math.toRadians(waypoint.relativeBearing(
                    mc.player.getX(), mc.player.getZ(), mc.player.getYRot())), dx * dx + dz * dz < 1);
        }
        var lines = mc.font.split(text, width - inset);
        int limit = Math.min(lines.size(), tracked && height >= 22 ? 2 : 1);
        int textY = y + Math.max(0, (height - limit * LINE_HEIGHT) / 2);
        for (int i = 0; i < limit; i++)
            g.drawString(mc.font, lines.get(i), x + inset, textY + i * LINE_HEIGHT, sameDimension ? GOLD : MUTED, false);
    }

    /** Fixed forward notch with a rotating destination needle; no inventory item required. */
    private static void compass(GuiGraphics g, int cx, int cy, double angle, boolean arrived) {
        for (int degrees = 0; degrees < 360; degrees += 12) {
            double radians = Math.toRadians(degrees);
            int x = cx + (int) Math.round(Math.sin(radians) * 9);
            int y = cy - (int) Math.round(Math.cos(radians) * 9);
            g.fill(x, y, x + 1, y + 1, 0xFF71647F);
        }
        g.fill(cx - 1, cy - 11, cx + 2, cy - 9, 0xFFFFFFFF);
        if (arrived) { g.fill(cx - 2, cy - 2, cx + 3, cy + 3, 0xFFE8C547); return; }
        int tipX = cx + (int) Math.round(Math.sin(angle) * 7);
        int tipY = cy - (int) Math.round(Math.cos(angle) * 7);
        line(g, cx, cy, tipX, tipY);
        for (double wing : new double[]{-.65, .65})
            line(g, tipX, tipY, tipX - (int) Math.round(Math.sin(angle + wing) * 4),
                    tipY + (int) Math.round(Math.cos(angle + wing) * 4));
    }

    private static void line(GuiGraphics g, int x1, int y1, int x2, int y2) {
        int steps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        for (int i = 0; i <= steps; i++) {
            double t = steps == 0 ? 0 : (double) i / steps;
            int x = (int) Math.round(x1 + (x2 - x1) * t), y = (int) Math.round(y1 + (y2 - y1) * t);
            g.fill(x, y, x + 1, y + 1, 0xFFE8C547);
        }
    }

    private static List<FormattedCharSequence> objectiveLines(Minecraft mc, Quest quest, QuestProgress progress, int width) {
        List<FormattedCharSequence> lines = new ArrayList<>();
        if (quest.isStory() && !com.bleachmod.common.quest.RukiaOpeningQuest.isOpening(quest) && !progress.isEncounterStarted()) {
            var pending = com.bleachmod.common.quest.StoryQuestService.pendingReason(quest);
            return mc.font.split(Component.translatable(pending != null ? "hud.bleachmod.quests.content_pending" : "hud.bleachmod.quests.begin"), width);
        }
        for (int i = 0; i < quest.getObjectives().size(); i++) {
            var objective = quest.getObjectives().get(i);
            int required = progress.getRequired(i, objective.getRequired());
            int done = progress.getObjectiveProgress(i);
            if (done >= required || (!com.bleachmod.common.quest.RukiaOpeningQuest.isOpening(quest) && !QuestService.isObjectiveActive(quest, progress, i))
                    || !com.bleachmod.common.quest.RukiaOpeningQuest.visibleObjective(quest, progress, i)) continue;
            lines.addAll(mc.font.split(Component.literal("[" + done + "/" + required + "] ").append(objective.describe()), width));
        }
        return lines;
    }
}
