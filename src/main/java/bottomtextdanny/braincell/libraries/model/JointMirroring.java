package bottomtextdanny.braincell.libraries.model;

import bottomtextdanny.braincell.libraries.model_animation.JointMutator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum JointMirroring {
    XY((vec, x, y, z) -> vec.addOffset(x, y, -z),
        (vec, x, y, z) -> vec.addRotation(-x, -y, z),
        (vec, x, y, z) -> vec.addMetaRot(-x, -y, z)),
    XZ((vec, x, y, z) -> vec.addOffset(x, -y, z),
        (vec, x, y, z) -> vec.addRotation(-x, y, -z),
        (vec, x, y, z) -> vec.addMetaRot(-x, y, -z)),
    YZ((vec, x, y, z) -> vec.addOffset(-x, y, z),
        (vec, x, y, z) -> vec.addRotation(x, -y, -z),
        (vec, x, y, z) -> vec.addMetaRot(-x, y, -z));

    private final VectorTransformer offsetTransformer;
    private final VectorTransformer rotationTransformer;
    private final VectorTransformer metaRotTransformer;

    JointMirroring(VectorTransformer transformer, VectorTransformer rotationTransformer, VectorTransformer metaRotTransformer) {
        this.offsetTransformer = transformer;
        this.rotationTransformer = rotationTransformer;
        this.metaRotTransformer = metaRotTransformer;
    }

    public void addByOff(JointMutator data, float x, float y, float z) {
        offsetTransformer.transform(data, x, y, z);
    }

    public void addByRot(JointMutator data, float x, float y, float z) {
        rotationTransformer.transform(data, x, y, z);
    }

    public void addByMetaRot(JointMutator data, float x, float y, float z) {
        metaRotTransformer.transform(data, x, y, z);
    }

    @FunctionalInterface
    public interface VectorTransformer {
        void transform(JointMutator data, float x, float y, float z);
    }
}
