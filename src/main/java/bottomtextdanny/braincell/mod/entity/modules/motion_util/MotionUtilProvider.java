package bottomtextdanny.braincell.mod.entity.modules.motion_util;

import bottomtextdanny.braincell.mod.entity.modules.ModuleProvider;

public interface MotionUtilProvider extends ModuleProvider {
   float inputMovementMultiplier();

   void setInputMovementMultiplier(float var1);
}
