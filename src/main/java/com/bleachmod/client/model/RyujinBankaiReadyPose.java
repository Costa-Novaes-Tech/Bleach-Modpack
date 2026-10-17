package com.bleachmod.client.model;

import com.bleachmod.Reference;
import com.bleachmod.common.data.PlayerCapability;
import com.bleachmod.init.ModItems;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;

/** Quiet final Yamamoto-inspired stance. Attacks/guard/casts override this low layer. */
public final class RyujinBankaiReadyPose {
    private RyujinBankaiReadyPose(){ }
    public static void init(){PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Reference.id("ryujin_ready"),10,Pose::new);}
    public static float[] sword(float sign){return new float[]{-.25F,-1.2F*sign,.03F*sign};}
    private static final class Pose implements IAnimation {
        private final AbstractClientPlayer player;Pose(AbstractClientPlayer p){player=p;}
        @Override public boolean isActive(){return player.isAlive() && !player.isSpectator() && !player.isUsingItem() && !player.isSwimming() && !player.isFallFlying() && !player.isSprinting()
                && player.getMainHandItem().is(ModItems.RYUJIN_JAKKA.get()) && PlayerCapability.get(player).map(d->d.getCharacter().getZanpakutoIdentity().equals("ryujin_jakka") && d.getCharacter().getActiveForm().equals("bankai")).orElse(false);}
        @Override public void setupAnim(float partial){ }
        @Override public Vec3f get3DTransform(String part,TransformType type,float partial,Vec3f value){float sign=player.getMainArm()==HumanoidArm.RIGHT?1:-1;
            if(type==TransformType.ROTATION && part.equals(sign>0?"rightArm":"leftArm")){var a=sword(sign);return new Vec3f(a[0],a[1],a[2]);}
            if(type==TransformType.ROTATION && part.equals(sign>0?"rightItem":"leftItem"))return Vec3f.ZERO;
            return value;
        }
    }
}
