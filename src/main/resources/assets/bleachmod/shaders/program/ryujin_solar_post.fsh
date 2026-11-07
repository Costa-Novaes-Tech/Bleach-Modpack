#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D SceneDepth;
uniform vec4 SolarMask;
uniform vec4 SolarTiming;
in vec2 texCoord;
out vec4 fragColor;

vec3 hotSample(vec2 uv) {
    vec3 color = texture(DiffuseSampler, uv).rgb;
    float hot = smoothstep(0.68, 0.98, color.r) * smoothstep(0.08, 0.29, color.r - color.b);
    return color * hot * step(SolarTiming.w - 0.001, texture(SceneDepth, uv).r);
}

void main() {
    vec2 local = (texCoord - SolarMask.xy) / SolarMask.zw;
    float ring = length(local);
    float depth = texture(SceneDepth, texCoord).r;
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    if (ring > 0.68 && ring < 1.53 && depth > SolarTiming.w - 0.001) {
        float edge = smoothstep(0.68, 0.92, ring) * (1.0 - smoothstep(1.10, 1.53, ring));
        if (SolarTiming.y > 0.0) {
            float wave = sin(local.y * 31.0 + local.x * 19.0 - SolarTiming.x * 0.29);
            vec2 shift = vec2(wave, sin(local.x * 27.0 + SolarTiming.x * 0.19) * 0.32);
            vec2 uv = texCoord + shift * 0.007 * edge * SolarTiming.y;
            if (texture(SceneDepth, uv).r > SolarTiming.w - 0.001) {
                color = mix(color, texture(DiffuseSampler, uv).rgb, 0.65);
            }
        }
        if (SolarTiming.z > 0.0) {
            vec2 span = SolarMask.zw * 0.115;
            vec3 glow = hotSample(texCoord + vec2(span.x, 0.0)) + hotSample(texCoord - vec2(span.x, 0.0))
                      + hotSample(texCoord + vec2(0.0, span.y)) + hotSample(texCoord - vec2(0.0, span.y));
            color += glow * (0.095 * edge * SolarTiming.z);
        }
    }
    fragColor = vec4(color, 1.0);
}
