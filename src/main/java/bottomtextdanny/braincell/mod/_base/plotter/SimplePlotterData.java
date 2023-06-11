package bottomtextdanny.braincell.mod._base.plotter;

import java.util.Objects;
import java.util.random.RandomGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class SimplePlotterData {
   private final Plotter plotter;
   private BlockState state;
   private final BlockState originalState;
   private final BlockPos blockPos;
   private final BlockPos localPos;
   private final RandomGenerator random;

   public SimplePlotterData(Plotter plotter, BlockState state, BlockState originalState, BlockPos blockPos, BlockPos localPos, RandomGenerator random) {
      this.plotter = plotter;
      this.state = state;
      this.originalState = originalState;
      this.blockPos = blockPos;
      this.localPos = localPos;
      this.random = random;
   }

   public Plotter plotter() {
      return this.plotter;
   }

   public BlockState state() {
      return this.state;
   }

   public void setState(BlockState state) {
      this.state = state;
   }

   public BlockState originalState() {
      return this.originalState;
   }

   public BlockPos blockPos() {
      return this.blockPos;
   }

   public BlockPos localPos() {
      return this.localPos;
   }

   public RandomGenerator random() {
      return this.random;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         SimplePlotterData that = (SimplePlotterData)obj;
         return Objects.equals(this.plotter, that.plotter) && Objects.equals(this.blockPos, that.blockPos) && Objects.equals(this.localPos, that.localPos) && Objects.equals(this.random, that.random);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.plotter, this.blockPos, this.localPos, this.random});
   }

   public String toString() {
      return "Data[plotter=" + this.plotter + ", blockPos=" + this.blockPos + ", localPos=" + this.localPos + ", random=" + this.random + "]";
   }
}
