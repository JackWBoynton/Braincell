package bottomtextdanny.braincell.libraries.model_animation;

import bottomtextdanny.braincell.base.Easing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class JointMutator {
    private float offsetX, offsetY, offsetZ;
    private float rotationX, rotationY, rotationZ;
    public float metaRotX, metaRotY, metaRotZ;
    private float scaleX, scaleY, scaleZ;
    private Easing offsetEasing = Easing.LINEAR,
            rotationEasing = Easing.LINEAR,
            scaleEasing = Easing.LINEAR;

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
        return offsetEasing;
    }

    public Easing getRotationEasing() {
        return rotationEasing;
    }

    public Easing getScaleEasing() {
        return scaleEasing;
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

    public void setMetaRot(float x, float y, float z) {
        this.metaRotX = (float)Math.toRadians(x);
        this.metaRotY = (float)Math.toRadians(y);
        this.metaRotZ = (float)Math.toRadians(z);
    }

    public void addMetaRot(float x, float y, float z) {
        this.metaRotX += (float)Math.toRadians(x);
        this.metaRotY += (float)Math.toRadians(y);
        this.metaRotZ += (float)Math.toRadians(z);
    }

    public void setRotation(float x, float y, float z) {
        this.rotationX = (float)Math.toRadians(x);
        this.rotationY = (float)Math.toRadians(y);
        this.rotationZ = (float)Math.toRadians(z);
    }

    public void addRotation(float x, float y, float z) {
        this.rotationX += (float)Math.toRadians(x);
        this.rotationY += (float)Math.toRadians(y);
        this.rotationZ += (float)Math.toRadians(z);
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
        if (offsetEasing != null)
            this.offsetEasing = offsetEasing;
    }

    public void setRotationEasing(Easing rotationEasing) {
        if (rotationEasing != null)
            this.rotationEasing = rotationEasing;
    }

    public void setScaleEasing(Easing scaleEasing) {
        if (scaleEasing != null)
            this.scaleEasing = scaleEasing;
    }
}
