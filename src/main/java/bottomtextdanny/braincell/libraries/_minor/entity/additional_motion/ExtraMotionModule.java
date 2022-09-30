package bottomtextdanny.braincell.libraries._minor.entity.additional_motion;

import bottomtextdanny.braincell.libraries._minor.entity.EntityModule;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExtraMotionModule {
    private final List<ExternalMotion> customMotions;
    private Vec3 additionalMotion = Vec3.ZERO;

    public ExtraMotionModule(Entity entity) {
        this.customMotions = Lists.newLinkedList();
    }

    public void travelHook() {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;

        for (ExternalMotion m : this.customMotions) {
            x += m.getAcceleratedMotion().x();
            y += m.getAcceleratedMotion().y();
            z += m.getAcceleratedMotion().z();
            m.tick();
        }

        this.additionalMotion = new Vec3(x, y, z);
    }

    public final void addMotion(ExternalMotion motion) {
        this.customMotions.add(motion);
    }

    public Vec3 getAdditionalMotion() {
        return this.additionalMotion;
    }
}
