/*
 * Copyright Friday August 05 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.test;

import bottomtextdanny.braincell.base.Axis2D;
import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.libraries.mod.BCStaticData;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model.apply.BCEntityModel;
import bottomtextdanny.braincell.libraries.model.ik.BCIKHandler;
import bottomtextdanny.braincell.libraries.model_animation.ModelAnimator;
import bottomtextdanny.braincell.libraries.model_animation.ik.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.concurrent.locks.Lock;

import static bottomtextdanny.braincell.base.BCMath.*;

public class IKModel extends BCEntityModel<IKEntity> {
	public BCJoint bottom;
	public BCJoint middle;
	public BCJoint middletop;
	public BCJoint top;
	public BCJoint tip;
	public CCDIKSystem<IKEntity> system;

	public IKModel() {
		texWidth = 32;
		texHeight = 32;

		bottom = new BCJoint(this);
		bottom.setPosCore(0.0F, 0.0F, 0.0F);
		bottom.uvOffset(8, 10).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

		middle = new BCJoint(this);
		middle.setPosCore(0.0F, -8.0F, 0.0F);
		bottom.addChild(middle);
		middle.uvOffset(0, 10).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

		middletop = new BCJoint(this);
		middletop.setPosCore(0.0F, -8.0F, 0.0F);
		middle.addChild(middletop);
		middletop.uvOffset(8, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

		top = new BCJoint(this);
		top.setPosCore(0.0F, -8.0F, 0.0F);
		middletop.addChild(top);
		top.uvOffset(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

		tip = new BCJoint(this);
		tip.setPosCore(0.0F, -8.0F, 0.0F);
		top.addChild(tip);

		system = new CCDIKSystem<>(5,
			CCDIKLink.<IKEntity>builder(bottom, en -> en.bottom)
				.zConstraint(CCDIKAxisConstraint.LOCK)
				.createCCDIKLink(),
			CCDIKLink.<IKEntity>builder(middle, en -> en.middle)
				.xConstraint(new LimitedAxisConstraint(RAD * -90.0F, RAD * 90.0F))
				.zConstraint(CCDIKAxisConstraint.LOCK)
				.yConstraint(CCDIKAxisConstraint.LOCK)
				.createCCDIKLink(),
			CCDIKLink.<IKEntity>builder(middletop, en -> en.middleTop)
				.xConstraint(new LimitedAxisConstraint(RAD * -90.0F, RAD * 90.0F))
				.zConstraint(CCDIKAxisConstraint.LOCK)
				.yConstraint(CCDIKAxisConstraint.LOCK)
				.createCCDIKLink(),
			CCDIKLink.<IKEntity>builder(top, en -> en.top)
				.xConstraint(new LimitedAxisConstraint(RAD * -90.0F, RAD * 90.0F))
				.zConstraint(CCDIKAxisConstraint.LOCK)
				.yConstraint(CCDIKAxisConstraint.LOCK)
				.createCCDIKLink(),
			CCDIKLink.<IKEntity>builder(tip, en -> en.tip)
				.xConstraint(CCDIKAxisConstraint.LOCK)
				.zConstraint(CCDIKAxisConstraint.LOCK)
				.yConstraint(CCDIKAxisConstraint.LOCK)
				.createCCDIKLink());

		addReseter(system);
	}

	@Override
	public void handleRotations(IKEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		ModelAnimator animator = new ModelAnimator(this, ageInTicks % 40);

//		animator.setupKeyframe(10);
//		animator.move(system, 0, 0, 40);
//		animator.apply();
//		animator.setupKeyframe(10);
//		animator.move(system, 0, 0, 0);
//		animator.apply();
//		animator.setupKeyframe(10);
//		animator.move(system, 0, 0, -40);
//		animator.apply();
//		animator.setupKeyframe(10);
//		animator.move(system, 0, 0, 0);
//		animator.apply();

		system.add(Mth.sin(((BCStaticData.partialTick() + entity.tickCount) * 0.2F) * 0.2F)  * -19, 20 +  Mth.cos(((BCStaticData.partialTick() + entity.tickCount) * 0.2F) * 0.2F)  * -19, Mth.cos(((BCStaticData.partialTick() + entity.tickCount) * 0.4F) * 0.2F) * -6);

		system.drag(entity);
		system.setToModel(entity);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bottom.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
