package bottomtextdanny.braincell.mod._base.opengl.enums;

import java.util.function.IntSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum ShaderType implements IntSupplier {
   FRAGMENT(35632),
   VERTEX(35633),
   GEOMETRY(36313),
   COMPUTATION(37305);

   int glInt;

   private ShaderType(int glInt) {
      this.glInt = glInt;
   }

   public int getAsInt() {
      return this.glInt;
   }

   public boolean compare(int other) {
      return this.glInt == other;
   }

   // $FF: synthetic method
   private static ShaderType[] $values() {
      return new ShaderType[]{FRAGMENT, VERTEX, GEOMETRY, COMPUTATION};
   }
}
