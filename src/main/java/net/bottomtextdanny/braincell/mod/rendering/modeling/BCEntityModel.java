package net.bottomtextdanny.braincell.mod.rendering.modeling;

import com.google.common.collect.Lists;
import net.bottomtextdanny.braincell.base.BCMath;
import net.bottomtextdanny.braincell.mod._base.BCStaticData;
import net.bottomtextdanny.braincell.mod.entity.modules.looped_walk.LoopedWalkProvider;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCModel;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.ModelSectionReseter;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class BCEntityModel<E extends Entity> extends EntityModel<E> implements BCModel {
    private final List<ModelSectionReseter> reseters = Lists.newArrayList();
    private final List<BCJoint> joints = Lists.newArrayList();
    public int texWidth, texHeight;
    private E host;

    protected BCEntityModel() {
        super();
    }

    @Override
    public void setupAnim(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.host = entityIn;
        runDefaultState();

        this.handleRotations(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.handleKeyframedAnimations(entityIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
    }

    public void handleRotations(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {}

    public void handleKeyframedAnimations(E entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch) {}

    public static <D extends LoopedWalkProvider> float caculateLimbSwingEasing(D entity) {
        return entity.operateWalkModule() ? BCMath.loopLerp(BCStaticData.partialTick(), 1, entity.loopedWalkModule().prevLimbSwingLoop, entity.loopedWalkModule().limbSwingLoop) : 0.0F;
    }

    public static <D extends LoopedWalkProvider> float caculateLimbSwingAmountEasing(D entity) {
        return entity.operateWalkModule() ? Mth.lerp(BCStaticData.partialTick(), entity.loopedWalkModule().prevRenderLimbSwingAmount, entity.loopedWalkModule().renderLimbSwingAmount) : 0.0F;
    }

    @Override
    public void prepareMobModel(E entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.host = entityIn;
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    @Override
    public void runDefaultState() {
        this.reseters.forEach(r -> r.reset(this));
    }

    @Override
    public void addReseter(ModelSectionReseter model) {
        this.reseters.add(model);
    }

    public void addJoint(BCJoint joint) {
        this.joints.add(joint);
    }

    public void setOffset(BCJoint part, float x, float y, float z) {
        part.x = x;
        part.y = y;
        part.z = z;
    }

    public void setSize(BCJoint part, float x, float y, float z) {
        part.scaleX = x;
        part.scaleY = y;
        part.scaleZ = z;
    }

	public E getHost() {
		return this.host;
	}

    public List<BCJoint> getJoints() {
        return Collections.unmodifiableList(this.joints);
    }

    @Deprecated
	public float walkRotationHelper(float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        return invert ? (BCMath.cos(limbSwing * speed + desync * (float)Math.PI) * (-amount * RAD * 2) * limbSwingAmount + weight * limbSwingAmount * RAD) : (BCMath.cos(limbSwing * speed + desync * (float)Math.PI) * (amount * RAD * 2) * limbSwingAmount + weight * limbSwingAmount * RAD);
    }

    @Deprecated
    public float walkOffsetHelper(float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        return invert ? (BCMath.cos(limbSwing * speed + desync * (float)Math.PI) * (-amount * 2) * limbSwingAmount + weight * limbSwingAmount) : (BCMath.cos(limbSwing * speed + desync * (float)Math.PI) * (amount * 2) * limbSwingAmount + weight * limbSwingAmount);
    }

    /**
     * @param degrees will be transformed to radians
     * @param desync is how much the rotation will desynchronize, 1.0F will invert it completely
     * @param invert quick way to invert the rotation
     */

    @Deprecated
    public void walkRotateX(BCJoint part, float speed, float degrees, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.xRot += walkRotationHelper(speed, degrees, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkRotateY(BCJoint part, float speed, float degrees, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.yRot += walkRotationHelper(speed, degrees, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkRotateZ(BCJoint part, float speed, float degrees, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.zRot += walkRotationHelper(speed, degrees, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkOffsetX(BCJoint part, float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.x += walkOffsetHelper(speed, amount, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkOffsetY(BCJoint part, float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.y += walkOffsetHelper(speed, amount, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkOffsetZ(BCJoint part, float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.z += walkOffsetHelper(speed, amount, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkScaleX(BCJoint part, float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.scaleX += walkOffsetHelper(speed, amount, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkScaleY(BCJoint part, float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.scaleY += walkOffsetHelper(speed, amount, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Deprecated
    public void walkScaleZ(BCJoint part, float speed, float amount, float desync, float weight, float limbSwing, float limbSwingAmount, boolean invert) {
        part.scaleZ += walkOffsetHelper(speed, amount, desync, weight, limbSwing, limbSwingAmount, invert);
    }

    @Override
    public int getTexWidth() {
        return this.texWidth;
    }

    @Override
    public int getTexHeight() {
        return this.texHeight;
    }
}
