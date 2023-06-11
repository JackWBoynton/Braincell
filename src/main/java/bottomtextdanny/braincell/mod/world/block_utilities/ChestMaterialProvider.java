package bottomtextdanny.braincell.mod.world.block_utilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ChestMaterialProvider {
   int SINGLE_SHIFT = 0;
   int LEFT_SHIFT = 1;
   int RIGHT_SHIFT = 2;

   int getChestMaterialSlot();

   void setChestMaterialSlot(int var1);

   @OnlyIn(Dist.CLIENT)
   ResourceLocation[] chestTexturePaths();
}
