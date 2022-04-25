package net.bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;

public interface SearchNearestPredicate {

    LivingEntity search(Mob mob, ServerLevel world, RangeTest rangeTest, Lazy<AABB> searchArea, TargetPredicate post);
}
