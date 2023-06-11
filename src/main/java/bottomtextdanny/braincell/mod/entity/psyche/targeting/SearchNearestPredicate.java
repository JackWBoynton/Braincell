package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.Lazy;

/** @deprecated */
@Deprecated
public interface SearchNearestPredicate extends SearchPredicate {
   LivingEntity search(Mob var1, ServerLevel var2, RangeTest var3, Lazy var4, MobMatchPredicate var5);
}
