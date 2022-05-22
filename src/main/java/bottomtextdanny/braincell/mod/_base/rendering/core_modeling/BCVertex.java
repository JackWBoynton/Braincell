package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import bottomtextdanny.braincell.mod._base.animation.AnimatableModelComponent;
import bottomtextdanny.braincell.mod._base.animation.PosMutator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    public void animationTransitionerPrevious(PosMutator previous, float multiplier, float progression, float invertedProgression) {
        this.x += invertedProgression * previous.getPosX();
        this.y += invertedProgression * previous.getPosY();
        this.z += invertedProgression * previous.getPosZ();
    }

    @Override
    public void animationTransitionerCurrent(PosMutator current, float multiplier, float progression, float invertedProgression) {
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
