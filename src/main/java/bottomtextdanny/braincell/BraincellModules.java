package bottomtextdanny.braincell;

import bottomtextdanny.braincell.libraries.mod.Module;
import bottomtextdanny.braincell.libraries.mod.SideConfig;

public final class BraincellModules {
    //*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    public static final Module PLAYER_MODEL_HOOKS = Module.makeSimple(SideConfig.CLIENT);
    public static final Module MOUSE_HOOKS = Module.makeSimple(SideConfig.CLIENT);
    public static final Module ENTITY_HURT_CALL_OUT = Module.makeSimple(SideConfig.BOTH);
    //*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
}
