package net.bottomtextdanny.braincell.mod._base.animation;

import com.google.common.collect.Maps;
import net.bottomtextdanny.braincell.mod.rendering.modeling.BCEntityModel;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;
import net.bottomtextdanny.braincell.base.Easing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class EntityAnimator {
    public final BCEntityModel<?> model;
    private final Map<BCJoint, JointMutator> modMapPing = Maps.newIdentityHashMap();
    private final Map<BCJoint, JointMutator> modMapPong = Maps.newIdentityHashMap();
    private boolean switchState;
    private float keyframe;
    private float prevKeyframe;
    private float progression;

    public EntityAnimator(BCEntityModel<?> model) {
        this.model = model;
    }

    public void setProgression(float progression) {
        this.progression = progression;
    }

    public boolean setupKeyframe(float duration) {
        this.prevKeyframe = this.keyframe;
        this.keyframe += duration;
        return this.progression >= this.prevKeyframe;
    }

    public void emptyKeyframe(float duration, Easing easing) {
        setupKeyframe(duration);
        this.apply(easing, 1.0F);
    }
	
	public void emptyKeyframe(float duration) {
		setupKeyframe(duration);
		this.apply(Easing.LINEAR, 1.0F);
	}

    public void staticKeyframe(float duration) {
        setupKeyframe(duration);
        this.overlap(Easing.LINEAR, 1.0F);
    }

    public void apply(Easing easing, float mult) {

        if (this.progression >= this.prevKeyframe && this.progression < this.keyframe) {
            float uneasedProg = (this.progression - this.prevKeyframe) / (this.keyframe - this.prevKeyframe);
            float prog = easing.progression(uneasedProg);
            float invProg = 1.0F - prog;

            getPrevious().forEach((box, t) -> {
                box.xRot += invProg * t.getRotationX() * mult;
                box.yRot += invProg * t.getRotationY() * mult;
                box.zRot += invProg * t.getRotationZ() * mult;
                box.x += invProg * t.getOffsetX() * mult;
                box.y += invProg * t.getOffsetY() * mult;
                box.z += invProg * t.getOffsetZ() * mult;
                box.scaleX += invProg * t.getScaleX() * mult;
                box.scaleY += invProg * t.getScaleY() * mult;
                box.scaleZ += invProg * t.getScaleZ() * mult;
            });


            getActual().forEach((box, t) -> {
                box.xRot += prog * t.getRotationX() * mult;
                box.yRot += prog * t.getRotationY() * mult;
                box.zRot += prog * t.getRotationZ() * mult;
                box.x += prog * t.getOffsetX() * mult;
                box.y += prog * t.getOffsetY() * mult;
                box.z += prog * t.getOffsetZ() * mult;
                box.scaleX += prog * t.getScaleX() * mult;
                box.scaleY += prog * t.getScaleY() * mult;
                box.scaleZ += prog * t.getScaleZ() * mult;
            });
        }

        getPrevious().clear();
        pingPongTables();
    }

    public void apply() {
        apply(Easing.LINEAR, 1.0F);
    }

    public void overlap(Easing easing, float mult) {
        float animationTick = this.progression;

        if (animationTick >= this.prevKeyframe && animationTick < this.keyframe) {
            float uneasedProg = (animationTick - this.prevKeyframe) / (this.keyframe - this.prevKeyframe);
            float prog = easing.progression(uneasedProg);

            getPrevious().forEach((box, t) -> {
                box.xRot += t.getRotationX() * mult;
                box.yRot += t.getRotationY() * mult;
                box.zRot += t.getRotationZ() * mult;
                box.x += t.getOffsetX() * mult;
                box.y += t.getOffsetY() * mult;
                box.z += t.getOffsetZ() * mult;
                box.scaleX += t.getScaleX() * mult;
                box.scaleY += t.getScaleY() * mult;
                box.scaleZ += t.getScaleZ() * mult;
            });

            getActual().forEach((box, t) -> {
                box.xRot += prog * t.getRotationX() * mult;
                box.yRot += prog * t.getRotationY() * mult;
                box.zRot += prog * t.getRotationZ() * mult;
                box.x += prog * t.getOffsetX() * mult;
                box.y += prog * t.getOffsetY() * mult;
                box.z += prog * t.getOffsetZ() * mult;
                box.scaleX += prog * t.getScaleX() * mult;
                box.scaleY += prog * t.getScaleY() * mult;
                box.scaleZ += prog * t.getScaleZ() * mult;
            });
        }
    }

    public void overlap() {
        overlap(Easing.LINEAR, 1.0F);
    }

    public void cleanUp() {
        this.prevKeyframe = 0.0F;
        this.keyframe = 0.0F;
        getActual().clear();
	    getPrevious().clear();
    }

    public void rotate(BCJoint box, float x, float y, float z) {
        this.getTransform(box).addRotation(x, y, z);
    }

    public void move(BCJoint box, float x, float y, float z) {
        this.getTransform(box).addOffset(x, y, z);
    }

    public void scale(BCJoint box, float x, float y, float z) {
        this.getTransform(box).addScale(x, y, z);
    }

    private void pingPongTables() {
        this.switchState = !this.switchState;
    }

    private Map<BCJoint, JointMutator> getActual() {
        return this.switchState ? this.modMapPing : this.modMapPong;
    }

    private Map<BCJoint, JointMutator> getPrevious() {
        return this.switchState ? this.modMapPong : this.modMapPing;
    }

    private JointMutator getTransform(BCJoint part) {
        return getActual().computeIfAbsent(part, t -> new JointMutator());
    }
}
