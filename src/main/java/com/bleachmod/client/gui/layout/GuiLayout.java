package com.bleachmod.client.gui.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

/**
 * A HUD, overlay or screen described by data (assets/bleachmod/gui_layouts/*.json).
 *
 * <p>All element coordinates are in <b>art pixels</b>: the pixels of the PNG as seen
 * in any image editor. {@code density} says how many art pixels fit in one GUI unit,
 * so a 480px wide panel with density 2 occupies 240 GUI units on screen.
 */
public record GuiLayout(String id, Anchor anchor, Anchor pivot, int offsetX, int offsetY,
                        int width, int height, float density, float scale, float maxWidthFraction,
                        float maxHeightFraction, boolean debug, List<Responsive> responsive,
                        List<Element> elements) {

    /** Overrides the placement when the scaled GUI is at most {@code maxScreenWidth} wide. */
    public record Responsive(int maxScreenWidth, Anchor anchor, Anchor pivot, int offsetX, int offsetY) {
    }

    public enum Anchor {
        TOP_LEFT(0, 0), TOP_CENTER(.5F, 0), TOP_RIGHT(1, 0),
        CENTER_LEFT(0, .5F), CENTER(.5F, .5F), CENTER_RIGHT(1, .5F),
        BOTTOM_LEFT(0, 1), BOTTOM_CENTER(.5F, 1), BOTTOM_RIGHT(1, 1);

        public final float fx;
        public final float fy;

        Anchor(float fx, float fy) {
            this.fx = fx;
            this.fy = fy;
        }
    }

    public enum Align { LEFT, CENTER, RIGHT }

    public enum VAlign { TOP, MIDDLE, BOTTOM }

    public enum Direction { RIGHT, LEFT, UP, DOWN }

    /** Fields every element shares. {@code visibleIf} is a flag name, optionally prefixed by '!'. */
    public record Common(String id, int x, int y, String visibleIf) {
    }

    public sealed interface Element permits Image, Bar, Text, Rect, Button, Entity {
        Common common();
    }

    /**
     * Picks one cell of a texture that holds several states side by side.
     * {@code names} lists the cells left to right; {@code stateIf} maps a state name to the
     * flag that turns it on (checked in order). {@code cell} forces a fixed cell index.
     */
    public record States(List<String> names, Map<String, String> stateIf, int cell) {
        public static final States NONE = new States(List.of(), Map.of(), -1);

        public int count() {
            return Math.max(1, names.size());
        }
    }

    /**
     * Text shared by {@code text} elements and button labels. {@code letterSpacing} (art
     * pixels, may be negative) tightens italic fonts whose glyph boxes include the slant.
     */
    public record Label(String text, String translate, List<String> args, ResourceLocation font,
                        boolean bold, boolean uppercase, int color, Map<String, Integer> colorIf,
                        boolean shadow, float scale, float letterSpacing) {
    }

    /**
     * A texture drawn 1:1 in art pixels. {@code w}/{@code h} of 0 mean "texture (cell) size";
     * {@code uv} selects a region, {@code slice} enables nine-slice, {@code tint} multiplies
     * colors/alpha and {@code slideValue}/{@code slideRange} move it down by value×range
     * (scroll bar thumbs).
     */
    public record Image(Common common, ResourceLocation texture, int w, int h, int[] uv, int[] slice,
                        States states, int tint, String slideValue, int slideRange) implements Element {
    }

    /**
     * A meter revealed from one side. The box may be a parallelogram: the top row
     * starts {@code skew} pixels to the right and the bottom row ends
     * {@code skewEnd} pixels before the right edge (negative values lean the other way).
     */
    public record Bar(Common common, String value, int w, int h, int skew, int skewEnd,
                      Direction direction, ResourceLocation texture, int color,
                      Map<String, Integer> colorIf, int background) implements Element {
    }

    /**
     * Text. {@code maxWidth} (art pixels) shrinks long single lines, or is the wrap width
     * when {@code wrap} is set; {@code maxLines} cuts wrapped text.
     */
    public record Text(Common common, Label label, Align align, VAlign valign, int maxWidth,
                       boolean wrap, int lineHeight, int maxLines) implements Element {
    }

    public record Rect(Common common, int w, int h, int color, Map<String, Integer> colorIf) implements Element {
    }

    /**
     * A clickable texture. States usually are normal|hover|pressed|disabled; a disabled
     * button uses "disabled" or, if missing, "locked". The screen receives {@code action}.
     */
    public record Button(Common common, ResourceLocation texture, int w, int h, States states,
                         String enabledIf, String action, Label label, int labelX, int labelY,
                         int disabledColor, String tooltip) implements Element {
    }

    /** A living entity (the player) rendered like the inventory preview, feet at pos. */
    public record Entity(Common common, String source, int w, int h, int scale, String yawValue) implements Element {
    }
}
