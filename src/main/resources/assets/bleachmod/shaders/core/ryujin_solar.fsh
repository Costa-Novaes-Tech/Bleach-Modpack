#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;

in float solarDistance;
in vec4 solarColor;
in vec2 solarUV;

out vec4 fragColor;

void main() {
    vec4 energy = texture(Sampler0, solarUV) * solarColor * ColorModulator;
    if (energy.a < 0.1) discard;
    fragColor = energy * linear_fog_fade(solarDistance, FogStart, FogEnd);
}
