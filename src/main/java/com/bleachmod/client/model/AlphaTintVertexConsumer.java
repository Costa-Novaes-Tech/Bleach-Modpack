package com.bleachmod.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Wraps a parent VertexConsumer to apply continuous alpha transparency and charred ash darkening
 * to entity quads without replacing or overriding any layer's texture, shader, or UVs.
 */
final class AlphaTintVertexConsumer implements VertexConsumer {
    private final VertexConsumer out;
    private final float alpha;
    private final float tint;

    AlphaTintVertexConsumer(VertexConsumer out, float alpha, float tint) {
        this.out = out;
        this.alpha = alpha;
        this.tint = tint;
    }

    @Override public VertexConsumer vertex(double x, double y, double z) { out.vertex(x, y, z); return this; }
    @Override public VertexConsumer color(int r, int g, int b, int a) {
        out.color((int)(r * tint), (int)(g * tint), (int)(b * tint), (int)(a * alpha));
        return this;
    }
    @Override public VertexConsumer uv(float u, float v) { out.uv(u, v); return this; }
    @Override public VertexConsumer overlayCoords(int u, int v) { out.overlayCoords(u, v); return this; }
    @Override public VertexConsumer uv2(int u, int v) { out.uv2(u, v); return this; }
    @Override public VertexConsumer normal(float x, float y, float z) { out.normal(x, y, z); return this; }
    @Override public void defaultColor(int r, int g, int b, int a) { out.defaultColor(r, g, b, a); }
    @Override public void unsetDefaultColor() { out.unsetDefaultColor(); }
    @Override public void endVertex() { out.endVertex(); }
}
