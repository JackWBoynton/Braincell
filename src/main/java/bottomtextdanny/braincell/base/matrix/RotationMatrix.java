package bottomtextdanny.braincell.base.matrix;

import com.mojang.math.Vector3f;
import bottomtextdanny.braincell.base.BCMath;
import net.minecraft.world.phys.Vec3;

//#https://en.wikipedia.org/wiki/Rotation_matrix
public class RotationMatrix {
    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
    public float m20;
    public float m21;
    public float m22;

    public RotationMatrix() {
    }

    public final void rotX(float angle) {
        float sinAngle;
        float cosAngle;

        sinAngle = BCMath.sin(angle);
        cosAngle = BCMath.cos(angle);

        this.m00 = 1.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;

        this.m10 = 0.0F;
        this.m11 = cosAngle;
        this.m12 = -sinAngle;

        this.m20 = 0.0F;
        this.m21 = sinAngle;
        this.m22 = cosAngle;
    }

    public final void rotY(float angle) {
        float sinAngle;
        float cosAngle;

        sinAngle = BCMath.sin(angle);
        cosAngle = BCMath.cos(angle);

        this.m00 = cosAngle;
        this.m01 = 0.0F;
        this.m02 = sinAngle;

        this.m10 = 0.0F;
        this.m11 = 1.0F;
        this.m12 = 0.0F;

        this.m20 = -sinAngle;
        this.m21 = 0.0F;
        this.m22 = cosAngle;
    }

    public final void rotZ(float angle) {
        float sinAngle;
        float cosAngle;

        sinAngle = BCMath.sin(angle);
        cosAngle = BCMath.cos(angle);

        this.m00 = cosAngle;
        this.m01 = -sinAngle;
        this.m02 = 0.0F;

        this.m10 = sinAngle;
        this.m11 = cosAngle;
        this.m12 = 0.0F;

        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
    }

    public Vec3 getTransform(Vec3 vec0) {
        return new Vec3(
                this.m00 * vec0.x() + this.m01 * vec0.y() + this.m02 * vec0.z(),
                this.m10 * vec0.x() + this.m11 * vec0.y() + this.m12 * vec0.z(),
                this.m20 * vec0.x() + this.m21 * vec0.y() + this.m22 * vec0.z());
    }

    public Vector3f getTransform(Vector3f vec0) {
        return new Vector3f(
                this.m00 * vec0.x() + this.m01 * vec0.y() + this.m02 * vec0.z(),
                this.m10 * vec0.x() + this.m11 * vec0.y() + this.m12 * vec0.z(),
                this.m20 * vec0.x() + this.m21 * vec0.y() + this.m22 * vec0.z());
    }

    public void transform(Vector3f vec0) {
        vec0.set(
                this.m00 * vec0.x() + this.m01 * vec0.y() + this.m02 * vec0.z(),
                this.m10 * vec0.x() + this.m11 * vec0.y() + this.m12 * vec0.z(),
                this.m20 * vec0.x() + this.m21 * vec0.y() + this.m22 * vec0.z());
    }
}
