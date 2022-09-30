package bottomtextdanny.braincell.libraries.model;

import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.libraries.mod.BCStaticData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface BCModel {
    float RAD = BCMath.FRAD;

    default float getPartialTick() {
        return BCStaticData.partialTick();
    }

    //BB positiveOutcome
    default void setRotationAngle(BCJoint modelRenderer, float x, float y, float z) {
        modelRenderer.defaultAngleX = x;
        modelRenderer.defaultAngleY = y;
        modelRenderer.defaultAngleZ = z;
    }

    default void setRotationAngleDegrees(BCJoint modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x * RAD;
        modelRenderer.yRot = y * RAD;
        modelRenderer.zRot = z * RAD;
    }

    List<BCJoint> getJoints();

    boolean addJoint(BCJoint joint);

    int getTexWidth();

    int getTexHeight();

    void runDefaultState();

    void addReseter(ModelSectionReseter model);
}
