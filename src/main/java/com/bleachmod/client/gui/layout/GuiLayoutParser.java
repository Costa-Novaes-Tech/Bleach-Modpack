package com.bleachmod.client.gui.layout;

import com.bleachmod.client.gui.layout.GuiLayout.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Reads a {@link GuiLayout} from JSON. Every problem is reported with the element id
 * and field name, because these files are edited by hand while the game is running.
 */
public final class GuiLayoutParser {
    private GuiLayoutParser() {
    }

    public static final class LayoutException extends RuntimeException {
        public LayoutException(String message) {
            super(message);
        }
    }

    public static GuiLayout parse(String id, String json) {
        JsonElement root;
        try {
            root = JsonParser.parseString(json);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new LayoutException("JSON invalido: " + cause.getMessage());
        }
        if (!root.isJsonObject()) throw new LayoutException("o arquivo deve ser um objeto { ... }");
        return parse(id, root.getAsJsonObject());
    }

    public static GuiLayout parse(String id, JsonObject o) {
        Anchor anchor = anchor(o, "anchor", Anchor.TOP_LEFT, "layout");
        Anchor pivot = anchor(o, "pivot", anchor, "layout");
        int[] offset = ints(o, "offset", new int[]{0, 0}, 2, "layout");
        int[] size = ints(o, "size", null, 2, "layout");
        float density = num(o, "art_density", 1F, "layout");
        if (density <= 0) throw new LayoutException("layout: art_density deve ser maior que 0");

        List<Responsive> responsive = new ArrayList<>();
        if (o.has("responsive")) {
            for (JsonElement e : array(o, "responsive", "layout")) {
                JsonObject r = e.getAsJsonObject();
                Anchor ra = anchor(r, "anchor", anchor, "responsive");
                int[] ro = ints(r, "offset", offset, 2, "responsive");
                responsive.add(new Responsive((int) num(r, "max_screen_width", 0, "responsive"),
                        ra, anchor(r, "pivot", ra, "responsive"), ro[0], ro[1]));
            }
            responsive.sort((a, b) -> Integer.compare(a.maxScreenWidth(), b.maxScreenWidth()));
        }

        List<Element> elements = new ArrayList<>();
        JsonArray list = array(o, "elements", "layout");
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isJsonObject()) throw new LayoutException("elements[" + i + "] deve ser um objeto");
            JsonObject raw = list.get(i).getAsJsonObject();
            for (JsonObject expanded : repeat(raw, i)) elements.add(element(expanded, i));
        }
        return new GuiLayout(id, anchor, pivot, offset[0], offset[1], size[0], size[1], density,
                num(o, "scale", 1F, "layout"), num(o, "max_width_fraction", 0F, "layout"),
                num(o, "max_height_fraction", 0F, "layout"), bool(o, "debug", false),
                List.copyOf(responsive), List.copyOf(elements));
    }

    /**
     * "repeat": {"count": 5, "step": [0, 53], "start": 0} copies the element, replacing
     * {i} in every text field by the index and moving each copy by step.
     */
    private static List<JsonObject> repeat(JsonObject raw, int index) {
        if (!raw.has("repeat")) return List.of(raw);
        String where = raw.has("id") ? raw.get("id").getAsString() : "elements[" + index + "]";
        JsonObject spec = raw.getAsJsonObject("repeat");
        int count = (int) num(spec, "count", 1, where + ".repeat");
        int start = (int) num(spec, "start", 0, where + ".repeat");
        int[] step = ints(spec, "step", new int[]{0, 0}, 2, where + ".repeat");
        JsonObject template = raw.deepCopy();
        template.remove("repeat");
        String json = template.toString();
        List<JsonObject> copies = new ArrayList<>();
        for (int n = 0; n < count; n++) {
            int value = start + n;
            JsonObject copy = JsonParser.parseString(json.replace("{i}", Integer.toString(value))).getAsJsonObject();
            int[] pos = ints(copy, "pos", new int[]{0, 0}, 2, where);
            JsonArray moved = new JsonArray();
            moved.add(pos[0] + step[0] * n);
            moved.add(pos[1] + step[1] * n);
            copy.add("pos", moved);
            copies.add(copy);
        }
        return copies;
    }

    private static Element element(JsonObject o, int index) {
        String id = o.has("id") ? o.get("id").getAsString() : "elements[" + index + "]";
        String type = str(o, "type", null);
        if (type == null)
            throw new LayoutException(id + ": falta o campo \"type\" (image, bar, text, rect, button ou entity)");
        int[] pos = ints(o, "pos", new int[]{0, 0}, 2, id);
        Common common = new Common(id, pos[0], pos[1], str(o, "visible_if", null));
        return switch (type) {
            case "image" -> {
                int[] sz = ints(o, "size", new int[]{0, 0}, 2, id);
                yield new Image(common, texture(o, id, true), sz[0], sz[1],
                        optionalInts(o, "uv", 4, id), optionalInts(o, "slice", 4, id), states(o, id),
                        color(o, "tint", 0xFFFFFFFF, id), str(o, "slide_value", null),
                        (int) num(o, "slide_range", 0, id));
            }
            case "bar" -> {
                int[] sz = ints(o, "size", null, 2, id);
                int skew = (int) num(o, "skew", 0, id);
                yield new Bar(common, str(o, "value", id), sz[0], sz[1], skew,
                        (int) num(o, "skew_end", skew, id),
                        enumValue(Direction.class, o, "direction", Direction.RIGHT, id),
                        texture(o, id, false), color(o, "color", 0xFFFFFFFF, id), colorMap(o, id),
                        color(o, "background", 0, id));
            }
            case "text" -> new Text(common, label(o, id, true),
                    enumValue(Align.class, o, "align", Align.LEFT, id),
                    enumValue(VAlign.class, o, "valign", VAlign.TOP, id),
                    (int) num(o, "max_width", 0, id), bool(o, "wrap", false),
                    (int) num(o, "line_height", 0, id), (int) num(o, "max_lines", 0, id));
            case "rect" -> {
                int[] sz = ints(o, "size", null, 2, id);
                yield new Rect(common, sz[0], sz[1], color(o, "color", 0xFFFFFFFF, id), colorMap(o, id));
            }
            case "button" -> {
                int[] sz = ints(o, "size", new int[]{0, 0}, 2, id);
                int[] labelPos = ints(o, "label_pos", new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE}, 2, id);
                boolean hasLabel = o.has("text") || o.has("translate");
                yield new Button(common, texture(o, id, true), sz[0], sz[1], states(o, id),
                        str(o, "enabled_if", null), str(o, "action", id),
                        hasLabel ? label(o, id, false) : null, labelPos[0], labelPos[1],
                        color(o, "disabled_color", 0xFF8A8F99, id), str(o, "tooltip", null));
            }
            case "entity" -> {
                int[] sz = ints(o, "size", null, 2, id);
                yield new Entity(common, str(o, "source", "player"), sz[0], sz[1],
                        (int) num(o, "scale", 30, id), str(o, "yaw_value", null));
            }
            default -> throw new LayoutException(id + ": type \"" + type
                    + "\" desconhecido (use image, bar, text, rect, button ou entity)");
        };
    }

    private static Label label(JsonObject o, String id, boolean required) {
        List<String> args = new ArrayList<>();
        if (o.has("args")) for (JsonElement a : array(o, "args", id)) args.add(a.getAsString());
        String text = str(o, "text", null);
        String translate = str(o, "translate", null);
        if (required && text == null && translate == null)
            throw new LayoutException(id + ": texto precisa de \"text\" ou \"translate\"");
        ResourceLocation font = null;
        if (o.has("font")) {
            font = ResourceLocation.tryParse(o.get("font").getAsString());
            if (font == null) throw new LayoutException(id + ".font: id invalido");
        }
        return new Label(text, translate, List.copyOf(args), font, bool(o, "bold", false),
                bool(o, "uppercase", false), color(o, "color", 0xFFFFFFFF, id), colorMap(o, id),
                bool(o, "shadow", true), num(o, "scale", 1F, id), num(o, "letter_spacing", 0F, id));
    }

    private static States states(JsonObject o, String id) {
        List<String> names = new ArrayList<>();
        if (o.has("states")) for (JsonElement e : array(o, "states", id)) names.add(e.getAsString());
        Map<String, String> stateIf = new LinkedHashMap<>();
        if (o.has("state_if")) {
            for (var entry : o.getAsJsonObject("state_if").entrySet()) {
                if (!names.contains(entry.getKey()))
                    throw new LayoutException(id + ".state_if: estado \"" + entry.getKey() + "\" nao esta em \"states\"");
                stateIf.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        int cell = (int) num(o, "cell", -1, id);
        if (o.has("cell") && !o.has("cells") && names.isEmpty())
            throw new LayoutException(id + ": \"cell\" precisa de \"cells\" (quantas celulas a textura tem)");
        if (o.has("cells")) {
            int cells = (int) num(o, "cells", 1, id);
            names = new ArrayList<>();
            for (int i = 0; i < cells; i++) names.add("cell" + i);
            if (cell < 0 || cell >= cells) throw new LayoutException(id + ".cell: fora de 0.." + (cells - 1));
        }
        if (names.isEmpty() && stateIf.isEmpty() && cell < 0) return States.NONE;
        return new States(List.copyOf(names), Map.copyOf(stateIf), cell);
    }

    private static ResourceLocation texture(JsonObject o, String id, boolean required) {
        String value = str(o, "texture", null);
        if (value == null) {
            if (required) throw new LayoutException(id + ": falta \"texture\"");
            return null;
        }
        // Atalho: "hud/painel.png" vira bleachmod:textures/gui/hud/painel.png
        if (!value.contains(":")) value = "bleachmod:textures/gui/" + value;
        ResourceLocation location = ResourceLocation.tryParse(value);
        if (location == null) throw new LayoutException(id + ": texture invalida \"" + value + "\"");
        return location;
    }

    private static Map<String, Integer> colorMap(JsonObject o, String id) {
        if (!o.has("color_if")) return Map.of();
        Map<String, Integer> map = new LinkedHashMap<>();
        for (var entry : o.getAsJsonObject("color_if").entrySet())
            map.put(entry.getKey(), parseColor(entry.getValue().getAsString(), id + ".color_if." + entry.getKey()));
        return map;
    }

    static int color(JsonObject o, String key, int fallback, String id) {
        return o.has(key) ? parseColor(o.get(key).getAsString(), id + "." + key) : fallback;
    }

    /** "#RRGGBB" (opaque) or "#AARRGGBB". */
    static int parseColor(String raw, String where) {
        String hex = raw.startsWith("#") ? raw.substring(1) : raw;
        try {
            if (hex.length() == 6) return 0xFF000000 | Integer.parseUnsignedInt(hex, 16);
            if (hex.length() == 8) return (int) Long.parseLong(hex, 16);
        } catch (NumberFormatException ignored) {
        }
        throw new LayoutException(where + ": cor \"" + raw + "\" invalida (use #RRGGBB ou #AARRGGBB)");
    }

    private static Anchor anchor(JsonObject o, String key, Anchor fallback, String id) {
        return enumValue(Anchor.class, o, key, fallback, id);
    }

    private static <E extends Enum<E>> E enumValue(Class<E> type, JsonObject o, String key, E fallback, String id) {
        if (!o.has(key)) return fallback;
        String raw = o.get(key).getAsString();
        try {
            return Enum.valueOf(type, raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            StringBuilder options = new StringBuilder();
            for (E value : type.getEnumConstants()) {
                if (!options.isEmpty()) options.append(", ");
                options.append(value.name().toLowerCase(Locale.ROOT));
            }
            throw new LayoutException(id + "." + key + ": \"" + raw + "\" invalido (opcoes: " + options + ")");
        }
    }

    private static int[] ints(JsonObject o, String key, int[] fallback, int length, String id) {
        if (!o.has(key)) {
            if (fallback == null) throw new LayoutException(id + ": falta \"" + key + "\" ([" + length + " numeros])");
            return fallback;
        }
        JsonElement e = o.get(key);
        if (!e.isJsonArray() || e.getAsJsonArray().size() != length)
            throw new LayoutException(id + "." + key + ": esperado uma lista com " + length + " numeros");
        int[] result = new int[length];
        try {
            for (int i = 0; i < length; i++) result[i] = Math.round(e.getAsJsonArray().get(i).getAsFloat());
        } catch (RuntimeException ex) {
            throw new LayoutException(id + "." + key + ": esperado uma lista com " + length + " numeros");
        }
        return result;
    }

    private static int[] optionalInts(JsonObject o, String key, int length, String id) {
        return o.has(key) ? ints(o, key, null, length, id) : null;
    }

    private static JsonArray array(JsonObject o, String key, String id) {
        if (!o.has(key) || !o.get(key).isJsonArray()) throw new LayoutException(id + ": \"" + key + "\" deve ser uma lista [ ... ]");
        return o.getAsJsonArray(key);
    }

    private static float num(JsonObject o, String key, float fallback, String id) {
        if (!o.has(key)) return fallback;
        try {
            return o.get(key).getAsFloat();
        } catch (RuntimeException e) {
            throw new LayoutException(id + "." + key + ": esperado um numero");
        }
    }

    private static boolean bool(JsonObject o, String key, boolean fallback) {
        return o.has(key) ? o.get(key).getAsBoolean() : fallback;
    }

    private static String str(JsonObject o, String key, String fallback) {
        return o.has(key) ? o.get(key).getAsString() : fallback;
    }
}
