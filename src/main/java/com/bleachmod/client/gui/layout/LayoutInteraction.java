package com.bleachmod.client.gui.layout;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mouse state for one rendered frame of an interactive layout. The renderer records
 * where each button and entity landed (in GUI coordinates); the screen uses those
 * regions on the next click, so hit testing always matches what is on screen.
 */
public final class LayoutInteraction {
    /** A clickable region. {@code action} is null for non-button regions such as entities. */
    public record Hit(String id, String action, float x0, float y0, float x1, float y1,
                      boolean enabled, Component tooltip) {
        public boolean contains(double x, double y) {
            return x >= x0 && x < x1 && y >= y0 && y < y1;
        }
    }

    private final List<Hit> hits = new ArrayList<>();
    private final List<Hit> building = new ArrayList<>();
    private double mouseX;
    private double mouseY;
    private String pressedId;

    /** Starts a frame: the regions recorded during this render replace the previous ones. */
    public void begin(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        building.clear();
    }

    public void end() {
        hits.clear();
        hits.addAll(building);
    }

    void record(Hit hit) {
        building.add(hit);
    }

    public boolean hovering(float x0, float y0, float x1, float y1) {
        return mouseX >= x0 && mouseX < x1 && mouseY >= y0 && mouseY < y1;
    }

    /** Topmost region under the point (later elements are drawn on top). */
    public Hit hitAt(double x, double y) {
        for (int i = hits.size() - 1; i >= 0; i--)
            if (hits.get(i).contains(x, y)) return hits.get(i);
        return null;
    }

    /** Tooltip of the region under the mouse, if any. */
    public Component tooltip() {
        Hit hit = hitAt(mouseX, mouseY);
        return hit == null ? null : hit.tooltip();
    }

    public String pressedId() {
        return pressedId;
    }

    public void press(String id) {
        pressedId = id;
    }
}
