package bottomtextdanny.braincell.libraries._minor.entity.motion_util;

import bottomtextdanny.braincell.libraries._minor.entity.ModuleProvider;

public interface MotionUtilProvider extends ModuleProvider {

    float inputMovementMultiplier();

    void setInputMovementMultiplier(float newFactor);
}
