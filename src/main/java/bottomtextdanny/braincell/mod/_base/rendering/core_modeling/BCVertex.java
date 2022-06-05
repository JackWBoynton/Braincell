package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.mod._base.animation.AnimatableModelComponent;
import bottomtextdanny.braincell.mod._base.animation.PosMutator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class BCVertex implements ModelSectionReseter, AnimatableModelComponent<PosMutator> {
    private final float defaultX, defaultY, defaultZ;
    public float x, y, z;

    public BCVertex(float x, float y, float z) {
        this.x = this.defaultX = x;
        this.y = this.defaultY = y;
        this.z = this.defaultZ = z;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    @Override
    public String name() {
        return "unknown";
    }

    @Override
    public PosMutator newAnimationData() {
        return new PosMutator();
    }

    @Override
    public void animationTransitionerPrevious(PosMutator previous, PosMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        progression = 1 - progression;
        this.x += progression * previous.getPosX();
        this.y += progression * previous.getPosY();
        this.z += progression * previous.getPosZ();
    }

    @Override
    public void animationTransitionerCurrent(PosMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        this.x += progression * current.getPosX();
        this.y += progression * current.getPosY();
        this.z += progression * current.getPosZ();
    }

    @Override
    public void reset(BCModel model) {
        this.x = this.defaultX;
        this.y = this.defaultY;
        this.z = this.defaultZ;
    }
}
