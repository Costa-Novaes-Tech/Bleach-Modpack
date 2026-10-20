package com.bleachmod.entity;

/** Stable ids are persisted in worlds and used by the shared Hollow renderer. */
public enum HollowVariant {
    BRUTE("brute", 20, .20, 12, .55, 1.5F, 3.4F, 48),
    SWIFT("swift", 6, .38, 4, .05, .65F, 1.35F, 14),
    FLYING("flying", 14, .27, 7, .15, .95F, 2.15F, 28);

    public final String id;
    public final double health, speed, damage, knockbackResistance;
    public final float width, height;
    public final int attackIntervalTicks;

    HollowVariant(String id, double health, double speed, double damage,
                  double knockbackResistance, float width, float height, int attackIntervalTicks) {
        this.id = id;
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.knockbackResistance = knockbackResistance;
        this.width = width;
        this.height = height;
        this.attackIntervalTicks = attackIntervalTicks;
    }

    public static HollowVariant byId(String id) {
        for (var variant : values()) if (variant.id.equals(id)) return variant;
        return BRUTE;
    }
}
