package bottomtextdanny.braincell.mod.entity.modules.additional_motion;

import bottomtextdanny.braincell.mod.entity.modules.EntityModule;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ExtraMotionModule extends EntityModule {
   private final List customMotions;
   private Vec3 additionalMotion;

   public ExtraMotionModule(Entity entity) {
      super(entity);
      this.additionalMotion = Vec3.ZERO;
      this.customMotions = Lists.newLinkedList();
   }

   public void travelHook() {
      double x = 0.0;
      double y = 0.0;
      double z = 0.0;
      Iterator var7 = this.customMotions.iterator();

      while(var7.hasNext()) {
         ExternalMotion m = (ExternalMotion)var7.next();
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
