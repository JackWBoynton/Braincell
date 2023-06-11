package bottomtextdanny.braincell.mod._base.opengl;

import com.mojang.blaze3d.platform.TextureUtil;
import java.nio.ByteBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL43;

@OnlyIn(Dist.CLIENT)
public class TextureBuffer {
   private final int texId;
   private final int width;
   private final int height;

   public TextureBuffer(int width, int height) {
      this.width = width;
      this.height = height;
      this.texId = TextureUtil.generateTextureId();
      GL43.glBindTexture(3553, this.texId);
      GL43.glTexImage2D(3553, 0, 34836, width, height, 0, 6408, 5126, (ByteBuffer)null);
      GL43.glTexParameteri(3553, 10242, 33071);
      GL43.glTexParameteri(3553, 10243, 33071);
      GL43.glTexParameteri(3553, 10240, 9728);
      GL43.glTexParameteri(3553, 10241, 9728);
   }

   public void bind(int unit) {
      GL43.glBindImageTexture(unit, this.texId, 0, false, 0, 35002, 34836);
   }

   public int getId() {
      return this.texId;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }
}
