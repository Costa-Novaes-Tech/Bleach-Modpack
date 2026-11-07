"""Make unretouched crops of Blockbench frames for human visual review."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
root=Path(__file__).resolve().parent
v6=root/'qa/activation-scene-v6'
v7=root/'qa/activation-scene-v7'
font=ImageFont.truetype('C:/Windows/Fonts/arial.ttf',20)

def sheet(items, output, cols, crop=(290,290,610,690), scale=2):
    cw,ch=crop[2]-crop[0],crop[3]-crop[1]
    w,h=cw*scale,ch*scale
    rows=(len(items)+cols-1)//cols
    canvas=Image.new('RGB',(w*cols,(h+38)*rows),'#101012')
    draw=ImageDraw.Draw(canvas)
    for i,(label,path) in enumerate(items):
        x,y=(i%cols)*w,(i//cols)*(h+38)
        frame=Image.open(path).convert('RGB').crop(crop).resize((w,h),Image.Resampling.NEAREST)
        canvas.paste(frame,(x,y+38))
        draw.text((x+10,y+8),label,font=font,fill='#f5c98a')
    canvas.save(output)
    print(output)

sheet([('v6 - 5,8 s',v6/'06-preview_animation-10.png'),
       ('v7 - 5,8 s',v7/'08-preview_animation-6.png')],
      v7/'v6-v7-position-comparison.png',2)
sheet([('5,8 s',v7/'08-preview_animation-6.png'),
       ('6,15 s',v7/'08-preview_animation-8.png'),
       ('6,45 s',v7/'08-preview_animation-10.png'),
       ('6,8 s',v7/'08-preview_animation-12.png'),
       ('7,1 s',v7/'08-preview_animation-14.png'),
       ('7,3 s',v7/'08-preview_animation-16.png')],
      v7/'v7-absorption-sequence.png',3)
