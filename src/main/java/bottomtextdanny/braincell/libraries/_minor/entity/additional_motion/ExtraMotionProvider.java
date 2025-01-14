package bottomtextdanny.braincell.libraries._minor.entity.additional_motion;

import bottomtextdanny.braincell.libraries._minor.entity.ModuleProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface ExtraMotionProvider extends ModuleProvider {

    ExtraMotionModule extraMotionModule();

    default boolean operateExtraMotionModule() {
        return extraMotionModule() != null;
    }

    default <M extends ExternalMotion> M addCustomMotion(M motion) {
        if (!operateExtraMotionModule()) throw new UnsupportedOperationException("Braincell: Tried to add custom motion on deactivated MotionUtil module, entity:" + ((Entity)this).getType().builtInRegistryHolder().key().toString());
        extraMotionModule().addMotion(motion);
        return motion;
    }

    //not handled by mixins!
    default Vec3 moveHook(Vec3 vectorParameter) {
        Entity entity = (Entity)this;
        if (operateExtraMotionModule() && (entity.isInWater() || entity.isInLava()) && vectorParameter == entity.getDeltaMovement()) {
            return vectorParameter.add(extraMotionModule().getAdditionalMotion());
        }
        return vectorParameter;
    }
}
