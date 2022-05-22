package bottomtextdanny.braincell.mod;

import bottomtextdanny.braincell.mod._base.Module;
import bottomtextdanny.braincell.mod._base.SideConfig;

public final class BraincellModules {
    //*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    public static final Module PLAYER_MODEL_HOOKS = Module.makeSimple(SideConfig.CLIENT);
    public static final Module MOUSE_HOOKS = Module.makeSimple(SideConfig.CLIENT);
    public static final Module ENTITY_HURT_CALL_OUT = Module.makeSimple(SideConfig.BOTH);
    //*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
}
