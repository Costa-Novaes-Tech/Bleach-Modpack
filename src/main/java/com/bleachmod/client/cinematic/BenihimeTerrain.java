package com.bleachmod.client.cinematic;

import com.bleachmod.common.visual.BenihimeEntranceGeometry;
import com.bleachmod.common.visual.SenbonGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Client collision-shape sampling; no chunk loads, block changes or gameplay movement. */
public final class BenihimeTerrain {
    private BenihimeTerrain() { }
    public static BenihimeEntranceGeometry.Ground sample(Vec3 anchor, Vec3 forward) {
        var mc=Minecraft.getInstance();var level=mc.level;
        Vec3 flat=new Vec3(forward.x,0,forward.z).normalize(),left=SenbonGeometry.left(flat);
        return (x,z)->{
            if(level==null||mc.player==null)return 0;
            Vec3 point=anchor.add(left.scale(x)).subtract(flat.scale(z));
            if(!level.hasChunkAt(BlockPos.containing(point)))return 0;
            var hit=level.clip(new ClipContext(point.add(0,2.5,0),point.add(0,-2.5,0),
                    ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,mc.player));
            return hit.getType()==HitResult.Type.MISS?0:hit.getLocation().y-anchor.y;
        };
    }
}
