/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client;

import bottomtextdanny.braincell.libraries.model.ImpreciseRot;
import bottomtextdanny.braincell.libraries.particle.client.local_sprites.SpriteGroup;
import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class BaseSpriteParticle extends ModularTextureSheetParticle {
    protected SpriteGroup sprites;
    protected float[] frameSpace;
    private float[] frameSpaceDefault;
    protected boolean onCeiling;

    protected BaseSpriteParticle(ClientLevel world, double x, double y, double z, ParticleAction start, ParticleAction ticker) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D, start, ticker);
    }

    protected BaseSpriteParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleAction start, ParticleAction ticker) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, start, ticker);
    }

    public void tick() {
        xRotO = xRot;
        yRotO = yRot;
        oRoll = roll;
        sizeO = quadSize;
        xo = x;
        yo = y;
        zo = z;
        glow = -1;
        ticker.execute(this);

        move(xd, yd, zd);

        int lifetime = this.lifetime;

        if (quadSize > 0.005F && age++ < lifetime) {
            yd -= 0.04D * (double) gravity;

            float friction = this.friction;

            if (friction != 0.0F) {
                xd *= friction;
                yd *= friction;
                zd *= friction;
            }

            handleSprite((float) lifetime);
        } else {
            remove();
        }
    }

    protected abstract void handleSprite(float lifetime);

    public void resetFrames() {
        frameSpace = frameSpaceDefault.clone();
    }

    @Override
    public void move(double x, double y, double z) {
        double d0 = x;
        double d1 = y;
        double d2 = z;
        if (hasPhysics && (x != 0.0D || y != 0.0D || z != 0.0D)) {
	        Vec3 vector3d = Entity.collideBoundingBox(null, new Vec3(x, y, z), getBoundingBox(), level, List.of());
	        x = vector3d.x;
            y = vector3d.y;
            z = vector3d.z;
        }

        if (x != 0.0D || y != 0.0D || z != 0.0D) {
            setBoundingBox(getBoundingBox().move(x, y, z));
            setLocationFromBoundingbox();
        }

        onCeiling = d1 != y && d1 > 0.0D;

        onGround = d1 != y && d1 < 0.0D;
        if (d0 != x) {
            xd = 0.0D;
        }

        if (d2 != z) {
            zd = 0.0D;
        }
    }
    
    public void render(VertexConsumer buffer, Camera renderInfo, float tickOffset) {
        float f4 = Math.max(getQuadSize(tickOffset), 0.0F);

        PoseStack pose = new PoseStack();

        Vec3 vector3d = renderInfo.getPosition();
        float f = (float)(Mth.lerp(tickOffset, xo, x) - vector3d.x());
        float f1 = (float)(Mth.lerp(tickOffset, yo, y) - vector3d.y());
        float f2 = (float)(Mth.lerp(tickOffset, zo, z) - vector3d.z());
        pose.translate(f, f1, f2);
        if (lookToCamera) {
            if (yRotO != 0 || yRot != 0 || renderInfo.getYRot() != 0) {
                pose.mulPose(ImpreciseRot.yRotDeg(-Mth.lerp(tickOffset, yRotO, yRot) - renderInfo.getYRot()));
            }
            if (xRotO != 0 || xRot != 0 || renderInfo.getXRot() != 0) {
                pose.mulPose(ImpreciseRot.xRotDeg(Mth.lerp(tickOffset, xRotO, xRot) + renderInfo.getXRot()));
            }
        } else {
            if (yRotO != 0 || yRot != 0) {
                pose.mulPose(ImpreciseRot.yRotDeg(-Mth.lerp(tickOffset, yRotO, yRot)));
            }
            if (xRotO != 0 || xRot != 0) {
                pose.mulPose(ImpreciseRot.xRotDeg(Mth.lerp(tickOffset, xRotO, xRot)));
            }
        }
        if (oRoll != 0 && roll != 0) {
            pose.mulPose(ImpreciseRot.zRotDeg(Mth.lerp(tickOffset, oRoll, roll)));
        }

        preRender(pose, buffer, renderInfo, tickOffset);

        int j = getLightColor(tickOffset);

        Vector3f[] avector3f = {
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)};

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(pose.last().normal());
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float minU = getU0();
        float maxU = getU1();
        float minV = getV0();
        float maxV = getV1();

        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(maxU, maxV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(maxU, minV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(minU, minV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(minU, maxV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();

        if (!lookToCamera) {
            buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(minU, maxV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();
            buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(minU, minV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();
            buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(maxU, minV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();
            buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(maxU, maxV).color(rCol, gCol, bCol, alpha).uv2(j).endVertex();
        }
    }

    public void preRender(PoseStack poseStack, VertexConsumer buffer, Camera renderInfo, float partialTicks) {}

    public final void setSpriteGroup(SpriteGroup group) {
        sprites = group;
    }

    @Override
    public final void setSpriteFromAge(SpriteSet spriteSet) {
        super.setSpriteFromAge(spriteSet);
    }

    protected final void setLocalSprite(int index) {
        sprite = sprites.fetch(index);
    }
//
//    public void setTicksForEachFrame(int... ticks) {
//        int lifetime = this.lifetime;
//        int[] frameTicks = new int[lifetime];
//
//        for (int i = 0; i < ticks.length; i++) {
//            frameTicks[i] = ticks[i];
//            lifetime += ticks[i];
//        }
//
//        this.lifetime = lifetime;
//
//        this.ticksForEachFrameDefault = frameTicks;
//        resetFrames();
//    }

    public void setFrameSpace(float... portions) {
        int size = sprites.size();
        float[] frameSpace = new float[size];

        for (int i = 0; i < size; i++) {
            frameSpace[i] = portions[i];
        }

        frameSpaceDefault = frameSpace.clone();
        resetFrames();
    }


    public void setLinearFrameSpace() {
        int size = sprites.size();
        float[] frameSpace = new float[size];

        float inv = 1.0F / size;
        float stack = 0.0F;

        for (int i = 0; i < size; i++) {
            frameSpace[i] = stack += inv;
        }

        frameSpaceDefault = frameSpace.clone();
        resetFrames();
    }

    protected void setDefaultSize(float size) {
        defaultSize = size;
        quadSize = size;
    }

    public float[] getFrameSpace() {
        return frameSpace;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public float getDefaultSize() {
        return defaultSize;
    }

    @Override
    public boolean shouldCull() {
        return true;
    }
}
