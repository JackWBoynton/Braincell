/*
 * Copyright Friday August 05 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.ik;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model.BCModel;
import bottomtextdanny.braincell.libraries.model.ModelSectionReseter;
import bottomtextdanny.braincell.libraries.model_animation.AnimatableModelComponent;
import bottomtextdanny.braincell.libraries.model_animation.PosMutator;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.logging.log4j.message.FormattedMessage;

import java.util.Map;

import static bottomtextdanny.braincell.base.BCMath.*;
import static net.minecraft.util.Mth.atan2;

public class CCDIKSystem<T> implements ModelSectionReseter, AnimatableModelComponent<PosMutator> {
	protected final CCDIKLink<T>[] links;
	protected Vector3f goal = new Vector3f();
	protected final int top;
	protected final int iterations;

	public CCDIKSystem(int frameIterations, CCDIKLink<T>... joints) {
		super();
		this.iterations = frameIterations;
		this.links = joints;
		this.top = this.links.length - 1;
	}

	public void drag(T object) {
		Vector3f goalVector = this.goal;
		if (Minecraft.getInstance().isPaused())return;
		int iterations = this.iterations;
		float xDif = 0.0F;
		float yDif = 0.0F;
		float zDif = 0.0F;
		float xRotO = 0.0F;
		float yRotO = 0.0F;
		float zRotO = 0.0F;

		for (int j = 0; j < iterations; j++) {
			for (int i = top - 1; i >= 0; i--) {
				CCDIKLink<T> link = this.links[i];
				CCDIKPartData data = link.dataGetter().apply(object);
				Vector3f relativeTopPos;
				Vector3f relativeGoalPos;

				xRotO = data.xRot;
				if (!link.xConstraint.skip()) {
					relativeTopPos = getLocalPos(object, i, this.top);
					relativeGoalPos = getLocalPos(object, i, goalVector);

					xDif = normalizeRad(radianAngleDiff(
						(float) atan2((float) relativeGoalPos.z(), relativeGoalPos.y()),
						(float) atan2((float) relativeTopPos.z(), relativeTopPos.y())
					));

					data.xRot = normalizeRad(data.xRot + xDif);
					data.xRot = link.xConstraint.apply(data.xRot, xDif, xRotO);
				}

				zRotO = data.zRot;
				if (!link.zConstraint.skip()) {
					relativeTopPos = getLocalPos(object, i, this.top);
					relativeGoalPos = getLocalPos(object, i, goalVector);

					zDif = normalizeRad(radianAngleDiff(
						(float) atan2((float) relativeGoalPos.y(), relativeGoalPos.x()),
						(float) atan2((float) relativeTopPos.y(), relativeTopPos.x())
					));

					data.zRot = normalizeRad(data.zRot + zDif);
					data.zRot = link.zConstraint.apply(data.zRot, xDif, xRotO);
				}

				yRotO = data.yRot;
				if (!link.yConstraint.skip()) {
					relativeTopPos = getLocalPos(object, i, this.top);
					relativeGoalPos = getLocalPos(object, i, goalVector);

					yDif = normalizeRad(radianAngleDiff(
						(float) atan2(relativeGoalPos.x(), relativeGoalPos.z()),
						(float) atan2(relativeTopPos.x(), relativeTopPos.z())
					));

					data.yRot = normalizeRad(data.yRot + yDif);
					data.yRot = link.yConstraint.apply(data.yRot, xDif, xRotO);
				}
				link.getConstraint().apply(data, xDif, yDif, zDif, xRotO, yRotO, zRotO);

//				if (i == 3) {
//					Minecraft.getInstance().player.chat(
//						new FormattedMessage("xDif {}, goal {}, top {}, radToGoal {}, radToTop {}", xDif, relativeGoalPos, relativeTopPos,
//							atan2((float) Math.sqrt(dot2(relativeGoalPos.x(), relativeGoalPos.z())), relativeGoalPos.y()),
//							atan2((float) Math.sqrt(dot2(relativeTopPos.x(), relativeTopPos.z())), relativeTopPos.y()))
//							.getFormattedMessage()
//					);
//				}
			}
		}
	}

	public Vector3f getLocalPos(T object,
								int index,
								Vector3f goal) {
		Vector3f trans = goal.copy();
		CCDIKLink<T> sLink = links[0];
		CCDIKPartData dataO = sLink.dataGetter().apply(object);
		BCJoint jointO = sLink.joint();
		for (int i = 1; i <= index; i++) {
			CCDIKLink<T> link = links[i];
			BCJoint joint = link.joint();
			Vector3f transformation = new Vector3f(
				joint.x * joint.scaleX,
				joint.y * joint.scaleY,
				joint.z * joint.scaleZ);
			transformation.transform(Vector3f.XP.rotation(dataO.xRot + jointO.xRot));
			transformation.transform(Vector3f.YP.rotation(dataO.yRot + jointO.yRot));
			transformation.transform(Vector3f.ZP.rotation(dataO.zRot + jointO.zRot));
			trans.sub(transformation);

			trans.transform(Vector3f.ZN.rotation(dataO.zRot + jointO.xRot));
			trans.transform(Vector3f.YN.rotation(dataO.yRot + jointO.yRot));
			trans.transform(Vector3f.XN.rotation(dataO.xRot + jointO.zRot));

			jointO = joint;
			dataO = link.dataGetter().apply(object);
		}

		return trans;
	}

	public Vector3f getLocalPos(T object,
								int startIndex,
								int index) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		CCDIKLink<T> sLink = links[startIndex];
		CCDIKPartData dataO = sLink.dataGetter().apply(object);
		BCJoint jointO = sLink.joint();
		for (int i = startIndex + 1; i <= index; i++) {
			CCDIKLink<T> link = links[i];

			BCJoint joint = link.joint();
			mat.multiply(Vector3f.ZP.rotation(dataO.zRot + jointO.zRot));
			mat.multiply(Vector3f.YP.rotation(dataO.yRot + jointO.yRot));
			mat.multiply(Vector3f.XP.rotation(dataO.xRot + jointO.xRot));
			mat.multiplyWithTranslation(
				joint.x * joint.scaleX,
				joint.y * joint.scaleY,
				joint.z * joint.scaleZ);

			jointO = joint;
			dataO = link.dataGetter().apply(object);
		}

		Vector4f vec = new Vector4f(0, 0, 0, 1);
		vec.transform(mat);
		return new Vector3f(vec.x(), vec.y(), vec.z());
	}

	public void setToModel(T object) {
		for (CCDIKLink<T> joint : this.links) {
			CCDIKPartData data = joint.dataGetter().apply(object);

			joint.joint().xRot += data.xRot;
			joint.joint().yRot += data.yRot;
			joint.joint().zRot += data.zRot;
		}
	}

	public void set(float x, float y, float z) {
		this.goal.set(x, -y, z);
	}

	public void add(float x, float y, float z) {
		this.goal.add(x, -y, z);
	}

	public float getGoalX() {
		return this.goal.x();
	}

	public float getGoalY() {
		return this.goal.y();
	}

	public float getGoalZ() {
		return this.goal.z();
	}

	@Override
	public String name() {
		return "ccdiksystem";
	}

	@Override
	public PosMutator newAnimationData() {
		return new PosMutator();
	}

	@Override
	public void animationTransitionerPrevious(PosMutator previous, PosMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
		progression = 1 - progression;
		this.goal.add(
			progression * previous.getPosX(),
			progression * -previous.getPosY(),
			progression * previous.getPosZ());
	}

	@Override
	public void animationTransitionerCurrent(PosMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
		this.goal.add(
			progression * current.getPosX(),
			progression * -current.getPosY(),
			progression * current.getPosZ());
	}

	@Override
	public void reset(BCModel model) {
		this.goal.set(0.0F, 0.0F, 0.0F);
		for (CCDIKLink<T> link : this.links)
			link.reset(model);
	}
}
