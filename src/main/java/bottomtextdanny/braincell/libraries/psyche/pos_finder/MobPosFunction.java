/*
 * Copyright Friday September 09 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

public interface MobPosFunction<T, R> {

	R test(Mob mob, BlockPos blockPos, T extra);
}
