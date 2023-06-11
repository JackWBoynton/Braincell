package bottomtextdanny.braincell.mod._base.opengl;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL43;

@OnlyIn(Dist.CLIENT)
public class ShaderBuffer {
   private final int id = GL43.glGenBuffers();

   public ShaderBuffer(int space) {
      GL43.glBindBuffer(37074, this.id);
      GL43.glBufferData(37074, new int[space], 35048);
      GL43.glBindBuffer(37074, 0);
   }

   public void resetData() {
      GL43.glClearBufferData(37074, 33321, 6403, 5124, (int[])null);
   }

   public int getId() {
      return this.id;
   }
}
