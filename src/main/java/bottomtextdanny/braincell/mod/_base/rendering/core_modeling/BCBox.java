package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;

public class BCBox implements ModelSectionReseter {
   private static final int[] DIRECTION_TO_QUAD = new int[]{2, 3, 4, 5, 1, 0};
   protected final BCTexturedQuad[] quads = new BCTexturedQuad[6];
   protected BCVertex[] vertices;

   public BCBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, BCJoint joint) {
      float xl = x - deltaX;
      float xr = x + width + deltaX;
      float yu = y - deltaY;
      float yd = y + height + deltaY;
      float zf = z - deltaZ;
      float zb = z + depth + deltaZ;
      if (mirorIn) {
         float t = xr;
         xr = xl;
         xl = t;
      }

      BCVertex vertexLeftDownFront = new BCVertex(xl, yd, zf);
      BCVertex vertexRightDownFront = new BCVertex(xr, yd, zf);
      BCVertex vertexRightUpFront = new BCVertex(xr, yu, zf);
      BCVertex vertexLeftUpFront = new BCVertex(xl, yu, zf);
      BCVertex vertexLeftDownBack = new BCVertex(xl, yd, zb);
      BCVertex vertexRightDownBack = new BCVertex(xr, yd, zb);
      BCVertex vertexRightUpBack = new BCVertex(xr, yu, zb);
      BCVertex vertexLeftUpBack = new BCVertex(xl, yu, zb);
      this.vertices = new BCVertex[]{vertexLeftDownFront, vertexRightDownFront, vertexLeftUpFront, vertexRightUpFront, vertexLeftDownBack, vertexRightDownBack, vertexLeftUpBack, vertexRightUpBack};
      float f4 = (float)texOffX;
      float f5 = (float)texOffX + depth;
      float f6 = (float)texOffX + depth + width;
      float f7 = (float)texOffX + depth + width + width;
      float f8 = (float)texOffX + depth + width + depth;
      float f9 = (float)texOffX + depth + width + depth + width;
      float f10 = (float)texOffY;
      float f11 = (float)texOffY + depth;
      float f12 = (float)texOffY + depth + height;
      float texWidth = (float)joint.getModel().getTexWidth();
      float texHeight = (float)joint.getModel().getTexHeight();
      this.quads[2] = new BCTexturedQuad(new BCVertex[]{vertexRightUpBack, vertexLeftUpBack, vertexLeftUpFront, vertexRightUpFront}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
      this.quads[3] = new BCTexturedQuad(new BCVertex[]{vertexRightDownFront, vertexLeftDownFront, vertexLeftDownBack, vertexRightDownBack}, f6, f11, f7, f10, texWidth, texHeight, mirorIn, Direction.UP);
      this.quads[1] = new BCTexturedQuad(new BCVertex[]{vertexLeftUpFront, vertexLeftUpBack, vertexLeftDownBack, vertexLeftDownFront}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
      this.quads[4] = new BCTexturedQuad(new BCVertex[]{vertexRightUpFront, vertexLeftUpFront, vertexLeftDownFront, vertexRightDownFront}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
      this.quads[0] = new BCTexturedQuad(new BCVertex[]{vertexRightUpBack, vertexRightUpFront, vertexRightDownFront, vertexRightDownBack}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
      this.quads[5] = new BCTexturedQuad(new BCVertex[]{vertexLeftUpBack, vertexRightUpBack, vertexRightDownBack, vertexLeftDownBack}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
   }

   public void clearQuad(Direction direction) {
      this.quads[DIRECTION_TO_QUAD[direction.ordinal()]] = null;
   }

   @Nullable
   public BCTexturedQuad getQuad(Direction direction) {
      return this.quads[DIRECTION_TO_QUAD[direction.ordinal()]];
   }

   public BCVertex getPoint(BCBoxPosition position) {
      return this.vertices[position.ordinal()];
   }

   public void reset(BCModel model) {
      BCVertex[] var2 = this.vertices;
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         BCVertex vertex = var2[var4];
         vertex.reset(model);
      }

      BCTexturedQuad[] var6 = this.quads;
      var3 = var6.length;

      for(var4 = 0; var4 < var3; ++var4) {
         BCTexturedQuad quad = var6[var4];
         if (quad != null) {
            quad.reset(model);
         }
      }

   }
}
