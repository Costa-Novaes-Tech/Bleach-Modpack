#version 150

#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;
uniform int FogShape;

out float solarDistance;
out vec4 solarColor;
out vec2 solarUV;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    solarDistance = fog_distance(ModelViewMat, IViewRotMat * Position, FogShape);
    solarColor = Color;
    solarUV = UV0;
}
