package com.bleachmod.client.gui.layout;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The live values a layout can show: bar ratios, text placeholders and flags used by
 * {@code visible_if} / {@code color_if}. The HUD code fills one per frame.
 */
public final class LayoutContext {
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{([a-zA-Z0-9_.]+)}");

    private final Map<String, Float> values = new HashMap<>();
    private final Map<String, Object> texts = new HashMap<>();
    private final Set<String> flags = new HashSet<>();
    private final Map<String, Float> numbers = new HashMap<>();
    private final Map<String, net.minecraft.world.entity.LivingEntity> entities = new HashMap<>();

    /** A 0..1 ratio for bars. Also exposed as text "{key}" (0-100) for convenience. */
    public LayoutContext value(String key, float ratio) {
        float clamped = Float.isNaN(ratio) ? 0F : Math.max(0F, Math.min(1F, ratio));
        values.put(key, clamped);
        texts.putIfAbsent(key, Integer.toString(Math.round(clamped * 100F)));
        return this;
    }

    public LayoutContext text(String key, Object text) {
        texts.put(key, text);
        return this;
    }

    public LayoutContext flag(String key, boolean enabled) {
        if (enabled) flags.add(key);
        else flags.remove(key);
        return this;
    }

    /** A free number (no clamping), e.g. a rotation in degrees. */
    public LayoutContext number(String key, float value) {
        numbers.put(key, value);
        return this;
    }

    public float number(String key) {
        return numbers.getOrDefault(key, 0F);
    }

    public LayoutContext entity(String key, net.minecraft.world.entity.LivingEntity entity) {
        entities.put(key, entity);
        return this;
    }

    public net.minecraft.world.entity.LivingEntity entity(String key) {
        return entities.get(key);
    }

    public float value(String key) {
        return values.getOrDefault(key, 0F);
    }

    /** "flag" or "!flag"; null/empty is always visible. */
    public boolean test(String condition) {
        if (condition == null || condition.isEmpty()) return true;
        return condition.startsWith("!") ? !flags.contains(condition.substring(1)) : flags.contains(condition);
    }

    /** First matching color in {@code colorIf}, else the base color. */
    public int color(int base, Map<String, Integer> colorIf) {
        for (var entry : colorIf.entrySet())
            if (test(entry.getKey())) return entry.getValue();
        return base;
    }

    /**
     * Replaces {name} placeholders. A template that is exactly one placeholder keeps the
     * original object (a translatable Component stays translatable).
     */
    public Object resolve(String template) {
        Matcher whole = PLACEHOLDER.matcher(template);
        if (whole.matches()) return texts.getOrDefault(whole.group(1), template);
        Matcher m = PLACEHOLDER.matcher(template);
        StringBuilder out = new StringBuilder();
        while (m.find()) {
            Object value = texts.get(m.group(1));
            String replacement = value == null ? m.group() : value instanceof Component c ? c.getString() : value.toString();
            m.appendReplacement(out, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(out);
        return out.toString();
    }

    /** The finished component of a label: text, translation, font, bold and uppercase applied. */
    public Component component(GuiLayout.Label label) {
        Component base = component(label.text(), label.translate(), label.args());
        net.minecraft.network.chat.MutableComponent result = label.uppercase()
                ? Component.literal(base.getString().toUpperCase(java.util.Locale.ROOT)) : base.copy();
        net.minecraft.network.chat.Style style = result.getStyle();
        if (label.font() != null) style = style.withFont(label.font());
        if (label.bold()) style = style.withBold(true);
        return result.setStyle(style);
    }

    public Component component(String text, String translate, java.util.List<String> args) {
        if (translate != null) {
            Object[] resolved = args.stream().map(this::resolve).toArray();
            return Component.translatable(translate, resolved);
        }
        Object value = resolve(text);
        return value instanceof Component c ? c : Component.literal(value.toString());
    }
}
