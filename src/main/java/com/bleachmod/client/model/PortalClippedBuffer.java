package com.bleachmod.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

/** Clips textured model quads at the Garganta plane, preserving UV, light and normals. */
final class PortalClippedBuffer implements VertexConsumer {
    private record Vertex(float x, float y, float z, float r, float g, float b, float a,
                          float u, float v, int overlay, int light, float nx, float ny, float nz) {
        Vertex mix(Vertex q, float t) {
            return new Vertex(lerp(x,q.x,t),lerp(y,q.y,t),lerp(z,q.z,t),lerp(r,q.r,t),lerp(g,q.g,t),
                    lerp(b,q.b,t),lerp(a,q.a,t),lerp(u,q.u,t),lerp(v,q.v,t),overlay,light,
                    lerp(nx,q.nx,t),lerp(ny,q.ny,t),lerp(nz,q.nz,t));
        }
        private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
        void write(VertexConsumer out) {
            out.vertex(x,y,z).color((int)r,(int)g,(int)b,(int)a).uv(u,v)
                    .overlayCoords(overlay & 65535, overlay >>> 16).uv2(light & 65535, light >>> 16)
                    .normal(nx,ny,nz).endVertex();
        }
    }
    private final VertexConsumer out;
    private final Vector3f normal;
    private final float distance;
    private final List<Vertex> quad = new ArrayList<>(4);
    private float x,y,z,r=255,g=255,b=255,a=255,u,v,nx,ny,nz;
    private int overlay,light;
    PortalClippedBuffer(VertexConsumer out, Vector3f point, Vector3f normal) {
        this.out = out; this.normal = normal; this.distance = normal.dot(point);
    }
    private float side(Vertex p) { return normal.x*p.x + normal.y*p.y + normal.z*p.z - distance; }
    @Override public VertexConsumer vertex(double x, double y, double z) { this.x=(float)x;this.y=(float)y;this.z=(float)z;return this; }
    @Override public VertexConsumer color(int r,int g,int b,int a) {this.r=r;this.g=g;this.b=b;this.a=a;return this;}
    @Override public VertexConsumer uv(float u,float v) {this.u=u;this.v=v;return this;}
    @Override public VertexConsumer overlayCoords(int u,int v) {overlay=u|(v<<16);return this;}
    @Override public VertexConsumer uv2(int u,int v) {light=u|(v<<16);return this;}
    @Override public VertexConsumer normal(float x,float y,float z) {nx=x;ny=y;nz=z;return this;}
    @Override public void defaultColor(int r,int g,int b,int a) {color(r,g,b,a);}
    @Override public void unsetDefaultColor() { }
    @Override public void endVertex() {
        quad.add(new Vertex(x,y,z,r,g,b,a,u,v,overlay,light,nx,ny,nz));
        if (quad.size() != 4) return;
        if (quad.stream().allMatch(p -> side(p)>=0)) {
            quad.forEach(p -> p.write(out));quad.clear();return;
        }
        List<Vertex> polygon = new ArrayList<>(6);
        Vertex previous = quad.get(3);
        float before = side(previous);
        for (Vertex current : quad) {
            float after = side(current);
            if ((before >= 0) != (after >= 0)) polygon.add(previous.mix(current, before / (before - after)));
            if (after >= 0) polygon.add(current);
            previous=current;before=after;
        }
        // A clipped quad has at most five vertices. Triangle fans use degenerate quads,
        // matching the entity render type's QUADS mode without changing global GL state.
        for (int i=1;i+1<polygon.size();i++) {
            polygon.get(0).write(out);polygon.get(i).write(out);
            polygon.get(i+1).write(out);polygon.get(i+1).write(out);
        }
        quad.clear();
    }
}
