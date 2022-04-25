package net.bottomtextdanny.braincell.mod._base.animation;

import com.google.common.collect.Maps;
import net.bottomtextdanny.braincell.base.Easing;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;
import net.bottomtextdanny.braincell.mod.rendering.modeling.BCEntityModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class EntityModelAnimator {
    public final BCEntityModel<?> model;
    private final Map<AnimatableModelComponent<?>, Object> modMapPing = Maps.newHashMap();
    private final Map<AnimatableModelComponent<?>, Object> modMapPong = Maps.newHashMap();
    private boolean useFirst;
    private float keyframe;
    private float prevKeyframe;
    private float timer;
    private float mult = 1.0F;

    public EntityModelAnimator(BCEntityModel<?> model, float timer) {
        this.model = model;
        this.timer = timer;
    }

    public EntityModelAnimator multiplier(float mult) {
        this.mult = mult;
        return this;
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
        this.getTransform(box).addRotation(x, y, z);

    }

    public void move(BCJoint box, float x, float y, float z) {
        this.getTransform(box).addOffset(x, -y, z);
    }

    public void move(AnimatableModelComponent<PosMutator> vertex, float x, float y, float z) {
        this.getTransform(vertex).setPos(x, y, z);
    }

    public void scale(BCJoint box, float x, float y, float z) {
        this.getTransform(box).addScale(x, y, z);
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
            float rawProg = easing.progression(uneasedProg);
            float prog = rawProg * this.mult;
            float invProg = (1.0F - rawProg) * this.mult;
            getPrevious().forEach((box, t) -> {
                box._animationTransitionerPrevious(t, this.mult, prog, invProg);
            });

            getActual().forEach((box, t) -> {
                box._animationTransitionerCurrent(t, this.mult, prog, invProg);
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

        if (animationTick >= this.prevKeyframe && animationTick < this.keyframe) {
            float uneasedProg = (animationTick - this.prevKeyframe) / (this.keyframe - this.prevKeyframe);
            float prog = easing.progression(uneasedProg);

            getPrevious().forEach((box, t) -> {
                box._animationTransitionerPrevious(t, this.mult, prog, 1.0F);
            });

            getActual().forEach((box, t) -> {
                box._animationTransitionerCurrent(t, this.mult, prog, 1.0F);
            });
        }
    }

    public void reset() {
        this.prevKeyframe = 0.0F;
        this.keyframe = 0.0F;
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
