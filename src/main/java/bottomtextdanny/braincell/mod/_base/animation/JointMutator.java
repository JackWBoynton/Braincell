package bottomtextdanny.braincell.mod._base.animation;

import bottomtextdanny.braincell.base.Easing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class JointMutator {
   private float offsetX;
   private float offsetY;
   private float offsetZ;
   private float rotationX;
   private float rotationY;
   private float rotationZ;
   private float scaleX;
   private float scaleY;
   private float scaleZ;
   private Easing offsetEasing;
   private Easing rotationEasing;
   private Easing scaleEasing;

   public JointMutator() {
      this.offsetEasing = Easing.LINEAR;
      this.rotationEasing = Easing.LINEAR;
      this.scaleEasing = Easing.LINEAR;
   }

   public float getOffsetX() {
      return this.offsetX;
   }

   public float getOffsetY() {
      return this.offsetY;
   }

   public float getOffsetZ() {
      return this.offsetZ;
   }

   public float getRotationX() {
      return this.rotationX;
   }

   public float getRotationY() {
      return this.rotationY;
   }

   public float getRotationZ() {
      return this.rotationZ;
   }

   public float getScaleX() {
      return this.scaleX;
   }

   public float getScaleY() {
      return this.scaleY;
   }

   public float getScaleZ() {
      return this.scaleZ;
   }

   public Easing getOffsetEasing() {
      return this.offsetEasing;
   }

   public Easing getRotationEasing() {
      return this.rotationEasing;
   }

   public Easing getScaleEasing() {
      return this.scaleEasing;
   }

   public void setOffset(float x, float y, float z) {
      this.offsetX = x;
      this.offsetY = y;
      this.offsetZ = z;
   }

   public void addOffset(float x, float y, float z) {
      this.offsetX += x;
      this.offsetY += y;
      this.offsetZ += z;
   }

   public void setRotation(float x, float y, float z) {
      this.rotationX = (float)Math.toRadians((double)x);
      this.rotationY = (float)Math.toRadians((double)y);
      this.rotationZ = (float)Math.toRadians((double)z);
   }

   public void addRotation(float x, float y, float z) {
      this.rotationX += (float)Math.toRadians((double)x);
      this.rotationY += (float)Math.toRadians((double)y);
      this.rotationZ += (float)Math.toRadians((double)z);
   }

   public void setScale(float x, float y, float z) {
      this.scaleX = x;
      this.scaleY = y;
      this.scaleZ = z;
   }

   public void addScale(float x, float y, float z) {
      this.scaleX += x;
      this.scaleY += y;
      this.scaleZ += z;
   }

   public void setOffsetEasing(Easing offsetEasing) {
      if (offsetEasing != null) {
         this.offsetEasing = offsetEasing;
      }

   }

   public void setRotationEasing(Easing rotationEasing) {
      if (rotationEasing != null) {
         this.rotationEasing = rotationEasing;
      }

   }

   public void setScaleEasing(Easing scaleEasing) {
      if (scaleEasing != null) {
         this.scaleEasing = scaleEasing;
      }

   }
}
