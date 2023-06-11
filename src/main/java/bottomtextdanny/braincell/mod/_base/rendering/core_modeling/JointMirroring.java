package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import bottomtextdanny.braincell.mod._base.animation.JointMutator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum JointMirroring {
   XY((vec, x, y, z) -> {
      vec.addOffset(x, y, -z);
   }, (vec, x, y, z) -> {
      vec.addRotation(-x, -y, z);
   }),
   XZ((vec, x, y, z) -> {
      vec.addOffset(x, -y, z);
   }, (vec, x, y, z) -> {
      vec.addRotation(-x, y, -z);
   }),
   YZ((vec, x, y, z) -> {
      vec.addOffset(-x, y, z);
   }, (vec, x, y, z) -> {
      vec.addRotation(x, -y, -z);
   });

   private final VectorTransformer offsetTransformer;
   private final VectorTransformer rotationTransformer;

   private JointMirroring(VectorTransformer transformer, VectorTransformer rotationTransformer) {
      this.offsetTransformer = transformer;
      this.rotationTransformer = rotationTransformer;
   }

   public void addByOff(JointMutator data, float x, float y, float z) {
      this.offsetTransformer.transform(data, x, y, z);
   }

   public void addByRot(JointMutator data, float x, float y, float z) {
      this.rotationTransformer.transform(data, x, y, z);
   }

   // $FF: synthetic method
   private static JointMirroring[] $values() {
      return new JointMirroring[]{XY, XZ, YZ};
   }

   @FunctionalInterface
   public interface VectorTransformer {
      void transform(JointMutator var1, float var2, float var3, float var4);
   }
}
