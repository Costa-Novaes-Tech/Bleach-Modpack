"""Crop only for a legible comparison; no retouching of Blockbench previews."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
root = Path(__file__).resolve().parent
qa = root / 'qa' / 'activation-scene-v6-heat-uv-fix'
frames = [('Explosão 3,55 s', '03-preview_animation-2.png'),
          ('Sol 5,80 s', '03-preview_animation-4.png'),
          ('Absorção 6,60 s', '03-preview_animation-6.png'),
          ('Retorno 7,20 s', '03-preview_animation-8.png')]
scale = 2
crop = (300, 320, 600, 660)
w, h = (crop[2]-crop[0])*scale, (crop[3]-crop[1])*scale
sheet = Image.new('RGB', (w*2, (h+44)*2), '#131112')
draw = ImageDraw.Draw(sheet)
for i, (label, filename) in enumerate(frames):
    image = Image.open(qa / filename).convert('RGB').crop(crop).resize((w, h), Image.Resampling.NEAREST)
    x, y = (i%2)*w, (i//2)*(h+44)
    sheet.paste(image, (x, y+44))
    draw.text((x+12,y+12), label, fill='#f5c98a')
out = qa / 'review-contact-sheet-crop.png'
sheet.save(out)
print(out)
