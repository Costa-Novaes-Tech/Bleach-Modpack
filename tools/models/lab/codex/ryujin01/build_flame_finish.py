"""Generate original sprite sheets for the Ryujin eye and distant fire."""

from math import exp, pi, sin
from pathlib import Path

from PIL import Image


DEST = Path(__file__).resolve().parents[5] / "src/main/resources/assets/bleachmod/textures/effect"
TILE = (64, 80)
FRAMES = 8


def clamp(value):
    return min(1.0, max(0.0, value))


def row_value(rows, y):
    for (y0, x0, w0), (y1, x1, w1) in zip(rows, rows[1:]):
        if y0 <= y <= y1:
            t = (y - y0) / (y1 - y0)
            return x0 + (x1 - x0) * t, w0 + (w1 - w0) * t
    return 0.0, 0.0


def eye(x, y, phase):
    # The eye lies at v=.73. Its entire 2x1 texel rectangle remains inside
    # the broad, steady section. Flicker is concentrated above the brow.
    outline = ((.035, .26, 0), (.14, .37, .045), (.25, .40, .092),
               (.37, .46, .095), (.48, .46, .19), (.59, .49, .32),
               (.72, .49, .37), (.85, .49, .33), (.91, .48, .26),
               (.97, .41, .12), (.99, .38, 0))
    centre, width = row_value(outline, y)
    if width:
        flicker = clamp((.58 - y) / .50)
        centre += .085 * flicker * sin(phase + 5 * y)
        width += .021 * sin(phase + 19 * y) * flicker
    body = width - abs(x - centre) if width else -1
    for base, top, rise in ((.58, .76, .30), (.32, .12, .40)):
        t = (.69 - y) / rise
        if 0 <= t <= 1:
            axis = base + (top - base) * t + .025 * sin(phase + 5 * t) * t
            body = max(body, .135 * (1 - t) ** .7 - abs(x - axis))
    alpha = clamp(body * 47)
    heat = clamp(body * 10) * (.79 + .21 * clamp((y - .35) * 2))
    return 255, int(93 + 158 * heat), int(12 + 226 * heat ** 1.7), int(255 * alpha)


def wall(x, y, phase, variant):
    rise = 1 - y
    edge = clamp(x * 28) * clamp((1 - x) * 28) * clamp(y * 30)
    # Let the low glow dissolve against the ground across neighboring cards.
    footing = clamp((1 - y) * (9 + 2 * sin(x * 18 + variant)))
    edge *= footing * footing * (3 - 2 * footing)
    density = heat = 0.0
    for j in range(4):
        height = (.58, .97, .74, .62)[(j + variant) % 4]
        height += .05 * sin(phase + j * 2 + variant)
        t = rise / height
        if t >= 1:
            continue
        root = (.13, .37, .61, .84)[j]
        centre = root + .11 * t * sin(7 * t + variant * 2.1 + j * 3)
        centre += .075 * t * sin(phase + t * 5 + j + variant)
        width = (.15 + .03 * sin(j + variant)) * (1 - t) ** .72
        tongue = clamp((width - abs(x - centre)) * 18)
        density = max(density, tongue)
        heat = max(heat, tongue * (1 - .73 * t) * (.68 + .32 * sin(j * .9 + variant) ** 2))
    base = .62 * clamp((.30 - rise) / .30) * clamp(x * 7) * clamp((1 - x) * 7)
    density = max(base, density * (.67 + .33 * sin(15 * x - 10 * rise + phase) ** 2))
    return int(167 + 88 * heat), int(25 + 125 * heat), int(6 + 47 * heat ** 2), int(247 * density * edge)


def smoke(x, y, phase, variant):
    density = 0
    for j in range(5):
        centre_y = .19 + .15 * j
        centre_x = .43 + .17 * sin(j * 1.24 + variant) + .055 * sin(phase + j)
        spread = .21 - .017 * j
        density += exp(-((x - centre_x) / spread) ** 2 - ((y - centre_y) / .16) ** 2)
    edge = clamp(x * 7) * clamp((1 - x) * 7) * clamp(y * 9) * clamp((1 - y) * 6)
    breakup = .71 + .16 * sin(x * 21 + y * 27 + phase) + .13 * sin(x * 33 - y * 16 + variant)
    warm = clamp((y - .28) * 1.35)
    return int(49 + 46 * warm), int(46 + 20 * warm), int(49 + 4 * warm), int(205 * clamp(density * breakup) * edge)


def sheet(name, variants, sample):
    w, h = TILE
    atlas = Image.new("RGBA", (w * variants, h * FRAMES))
    for frame in range(FRAMES):
        phase = frame * 2 * pi / FRAMES
        for variant in range(variants):
            tile = Image.new("RGBA", (w * 2, h * 2))
            tile.putdata([sample((x + .5) / (w * 2), (y + .5) / (h * 2), phase, variant)
                          for y in range(h * 2) for x in range(w * 2)])
            atlas.paste(tile.resize(TILE, Image.Resampling.LANCZOS), (variant * w, frame * h))
    atlas.save(DEST / name)


if __name__ == "__main__":
    sheet("ryujin_eye_crown.png", 1, lambda x, y, phase, _: eye(x, y, phase))
    sheet("ryujin_far_flame_sheet.png", 4, wall)
    sheet("ryujin_backdrop_smoke.png", 4, smoke)
