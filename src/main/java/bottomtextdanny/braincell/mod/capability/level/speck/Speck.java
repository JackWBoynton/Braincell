package bottomtextdanny.braincell.mod.capability.level.speck;

import bottomtextdanny.braincell.mod.capability.CapabilityHelper;
import bottomtextdanny.braincell.mod.capability.level.BCLevelCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Speck {
   private static final int MAX_UNLOADED_TICKS = 400;
   protected final Level level;
   private Vec3 position;
   public double xo;
   public double yo;
   public double zo;
   private int areaUnloadedTicks;
   private int ticksExisted;
   private boolean removed;

   public Speck(Level level) {
      this.position = Vec3.ZERO;
      this.level = level;
   }

   public final void baseTick() {
      ++this.ticksExisted;
      if (!this.level.hasChunkAt(this.getBlockPosition())) {
         ++this.areaUnloadedTicks;
         if (this.areaUnloadedTicks > 400) {
            this.remove();
         }
      } else {
         this.areaUnloadedTicks = 0;
      }

      this.xo = this.position.x;
      this.yo = this.position.y;
      this.zo = this.position.z;
      this.tick();
   }

   protected void tick() {
   }

   public void remove() {
      this.removed = true;
   }

   public void addToLevel() {
      BCLevelCapability capability = (BCLevelCapability)CapabilityHelper.get(this.level, BCLevelCapability.TOKEN);
      capability.getSpeckManager().addSpeck(this);
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public BlockPos getBlockPosition() {
      return new BlockPos(this.position);
   }

   public void setPosition(Vec3 position) {
      this.position = position;
   }

   public double getPosX() {
      return this.position.x;
   }

   public double getPosY() {
      return this.position.y;
   }

   public double getPosZ() {
      return this.position.z;
   }

   public int getAreaUnloadedTicks() {
      return this.areaUnloadedTicks;
   }

   public int getTicksExisted() {
      return this.ticksExisted;
   }
}
