package com.bleachmod.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

/** Inventory-style entity render with a fixed, viewer-facing pose turned by a yaw. */
public final class EntityPreview {
    private EntityPreview() {
    }

    /** Draws {@code entity} with its feet at (x, y); the entity's own rotations are restored afterwards. */
    public static void render(GuiGraphics graphics, int x, int y, int scale, float yaw, LivingEntity entity) {
        float body = entity.yBodyRot, bodyO = entity.yBodyRotO, rot = entity.getYRot(), pitch = entity.getXRot();
        float head = entity.yHeadRot, headO = entity.yHeadRotO;
        float facing = 180.0F + yaw;
        entity.yBodyRot = entity.yBodyRotO = facing;
        entity.setYRot(facing);
        entity.setXRot(0.0F);
        entity.yHeadRot = entity.yHeadRotO = facing;
        InventoryScreen.renderEntityInInventory(graphics, x, y, scale, new Quaternionf().rotateZ((float) Math.PI), new Quaternionf(), entity);
        entity.yBodyRot = body;
        entity.yBodyRotO = bodyO;
        entity.setYRot(rot);
        entity.setXRot(pitch);
        entity.yHeadRot = head;
        entity.yHeadRotO = headO;
    }
}
