package bottomtextdanny.braincell.mod._base.animation;

import bottomtextdanny.braincell.base.Easing;
import com.google.common.collect.Maps;
import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;
import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class ModelAnimator {
    public final BCModel model;
    private final Map<AnimatableModelComponent<?>, Object> modMapPing = Maps.newIdentityHashMap();
    private final Map<AnimatableModelComponent<?>, Object> modMapPong = Maps.newIdentityHashMap();
    private final Map<Easing, Float> progressionMap = Maps.newIdentityHashMap();
    private boolean useFirst;
    private float keyframe;
    private float prevKeyframe;
    private float timer;
    private float mult = 1.0F;
    private boolean mirroring;

    public ModelAnimator(BCModel model, float timer) {
        this.model = model;
        this.timer = timer;
    }

    public ModelAnimator multiplier(float mult) {
        this.mult = mult;
        return this;
    }

    public void mirror(boolean state) {
        this.mirroring = state;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    private void pingPongTables() {
        this.useFirst = !this.useFirst;
    }

    private Map<AnimatableModelComponent<?>, Object> getActual() {
        return this.useFirst ? this.modMapPing : this.modMapPong;
    }

    private Map<AnimatableModelComponent<?>, Object> getPrevious() {
        return this.useFirst ? this.modMapPong : this.modMapPing;
    }

    @SuppressWarnings("unchecked")
    private <T> T getTransform(AnimatableModelComponent<T> part) {
        return (T)getActual().computeIfAbsent(part, t -> part.newAnimationData());
    }

    public void rotate(BCJoint box, float x, float y, float z) {
        rotate(box, x, y, z, null);
    }

    public void rotate(BCJoint box, float x, float y, float z, @Nullable Easing progression) {
        JointMutator transform;

        if (mirroring) {
            if (box.getMirrorJoint() != null)
                box = box.getMirrorJoint();

            transform = getTransform(box);

            if (box.getMirrorType() != null) {
                box.getMirrorType().addByRot(transform, x, y, z);
                transform.setRotationEasing(progression);
                return;
            }
        }

        transform = getTransform(box);

        transform.addRotation(x, y, z);
        transform.setRotationEasing(progression);
    }

    public void move(BCJoint box, float x, float y, float z) {
        move(box, x, y, z, null);
    }

    public void move(BCJoint box, float x, float y, float z, @Nullable Easing progression) {
        JointMutator transform;

        if (mirroring) {
            if (box.getMirrorJoint() != null)
                box = box.getMirrorJoint();

            transform = getTransform(box);

            if (box.getMirrorType() != null) {
                box.getMirrorType().addByOff(transform, x, -y, z);
                transform.setOffsetEasing(progression);
                return;
            }
        }

        transform = getTransform(box);

        getTransform(box).addOffset(x, -y, z);
        transform.setOffsetEasing(progression);
    }

    public void move(AnimatableModelComponent<PosMutator> vertex, float x, float y, float z) {
        this.getTransform(vertex).setPos(x, y, z);
    }

    public void scale(BCJoint box, float x, float y, float z) {
        scale(box, x, y, z, null);
    }

    public void scale(BCJoint box, float x, float y, float z, @Nullable Easing progression) {
        if (mirroring && box.getMirrorJoint() != null) {
            box = box.getMirrorJoint();
        }

        JointMutator transform = this.getTransform(box);

        transform.addScale(x, y, z);
        transform.setScaleEasing(progression);
    }

    public boolean setupKeyframe(float duration) {
        this.prevKeyframe = this.keyframe;
        this.keyframe += duration;
        return this.timer >= this.prevKeyframe;
    }

    public void emptyKeyframe(float duration, Easing easing) {
        setupKeyframe(duration);
        this.apply(easing);
    }
	
	public void emptyKeyframe(float duration) {
		setupKeyframe(duration);
		this.apply(Easing.LINEAR);
	}

    public void staticKeyframe(float duration) {
        setupKeyframe(duration);
        this.overlap(Easing.LINEAR);
    }

    public void apply(Easing easing) {
        if (this.timer >= this.prevKeyframe && this.timer < this.keyframe) {
            float uneasedProg = (this.timer - this.prevKeyframe) / (this.keyframe - this.prevKeyframe);
            float prog = easing.progression(uneasedProg);
            Map<AnimatableModelComponent<?>, Object> actual = getActual();
            Map<AnimatableModelComponent<?>, Object> previous = getPrevious();
            progressionMap.clear();

            previous.forEach((box, t) -> {
                box._animationTransitionerPrevious(t, getTransform(box), this.mult, prog, progressionMap);
            });

            actual.forEach((box, t) -> {
                box._animationTransitionerCurrent(t, this.mult, prog, progressionMap);
            });
        }

        getPrevious().clear();
        pingPongTables();
    }

    public void apply() {
        apply(Easing.LINEAR);
    }

    public void overlap(Easing easing) {
        float animationTick = this.timer;

        progressionMap.clear();

        if (animationTick >= this.prevKeyframe && animationTick < this.keyframe) {
            float uneasedProg = (animationTick - this.prevKeyframe) / (this.keyframe - this.prevKeyframe);
            float prog = easing.progression(uneasedProg);
            Map<AnimatableModelComponent<?>, Object> actual = getActual();
            Map<AnimatableModelComponent<?>, Object> previous = getPrevious();

            previous.forEach((box, t) -> {
                box._animationTransitionerPrevious(t, getTransform(box), this.mult, 0.0F, progressionMap);
            });

            actual.forEach((box, t) -> {
                box._animationTransitionerCurrent(t, this.mult, prog, progressionMap);
            });


        }
    }

    public void reset() {
        this.prevKeyframe = 0.0F;
        this.keyframe = 0.0F;
        progressionMap.clear();
        getActual().clear();
	    getPrevious().clear();
    }

    public float disable(float startTick, float staticTicks, float endTick, float timer) {
        if (timer < startTick) {
            float prog = timer / startTick;
            return 1.0F - prog;
        } else if (timer >= startTick && timer < startTick + staticTicks) {
            return 0.0F;
        } else if (timer >= startTick + staticTicks && timer < startTick + staticTicks + endTick) {
            return (timer - (startTick + staticTicks)) / endTick;
        }
        return 0.0F;
    }

    public float disable(float startTick, float staticTicks, float endTick) {
        if (this.timer < startTick) {
            float prog = this.timer / startTick;
            return 1.0F - prog;
        } else if (this.timer >= startTick && this.timer < startTick + staticTicks) {
            return 0.0F;
        } else if (this.timer >= startTick + staticTicks && this.timer < startTick + staticTicks + endTick) {
            return (this.timer - (startTick + staticTicks)) / endTick;
        }
        return 0.0F;
    }

    public void disableFloat(MutableFloat ref, float startTick, float staticTicks, float endTick) {
        if (this.timer < startTick) {
            float prog = this.timer / startTick;
            ref.setValue(1 - prog);
        } else if (this.timer >= startTick && this.timer < startTick + staticTicks) {
            ref.setValue(0);
        } else if (this.timer >= startTick + staticTicks && this.timer < startTick + staticTicks + endTick) {
            float prog = (this.timer - (startTick + staticTicks)) / endTick;
            ref.setValue(prog);
        }
    }
}
