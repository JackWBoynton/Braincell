package bottomtextdanny.braincell.mod._base.opengl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

@OnlyIn(Dist.CLIENT)
public final class BCFramebuffer {
   private final int id;
   public int width;
   public int height;
   private final int depthTex;
   private final int colorTex;

   public BCFramebuffer(int textureWidth, int textureHeight, boolean depth) {
      this.width = textureWidth;
      this.height = textureHeight;
      this.id = GL30.glGenFramebuffers();
      GL30.glBindFramebuffer(36160, this.id);
      this.colorTex = TextureUtil.generateTextureId();
      GL11.glBindTexture(3553, this.colorTex);
      GL11.glTexImage2D(3553, 0, 6408, this.width, this.height, 0, 6408, 5121, (IntBuffer)null);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL30.glFramebufferTexture2D(36160, 36064, 3553, this.colorTex, 0);
      if (depth) {
         this.depthTex = TextureUtil.generateTextureId();
         GL11.glBindTexture(3553, this.depthTex);
         GL11.glTexImage2D(3553, 0, 6402, this.width, this.height, 0, 6402, 5126, (IntBuffer)null);
         GlStateManager._texParameter(3553, 10241, 9728);
         GlStateManager._texParameter(3553, 10240, 9728);
         GlStateManager._texParameter(3553, 10242, 10496);
         GlStateManager._texParameter(3553, 10243, 10496);
         GlStateManager._texParameter(3553, 34892, 0);
         GL30.glFramebufferTexture2D(36160, 36096, 3553, this.depthTex, 0);
      } else {
         this.depthTex = -1;
      }

      if (GL30.glCheckFramebufferStatus(36160) != 36053) {
         System.out.println("fbo failed");
      }

      GL30.glBindFramebuffer(36160, Minecraft.getInstance().getMainRenderTarget().frameBufferId);
   }

   public void bind(int type) {
      GlStateManager._glBindFramebuffer(type, this.id);
   }

   public int getId() {
      return this.id;
   }

   public int getColorID() {
      return this.colorTex;
   }

   public int getDepthID() {
      return this.depthTex;
   }
}
