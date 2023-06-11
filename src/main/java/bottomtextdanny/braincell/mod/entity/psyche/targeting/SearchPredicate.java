package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.Lazy;

public interface SearchPredicate {
   Object search(Mob var1, ServerLevel var2, RangeTest var3, Lazy var4, MobMatchPredicate var5);

   default SearchPredicate cast() {
      return this;
   }

   default SearchPredicate hackyCast() {
      return this;
   }

   default SearchPredicate cast(Class clazz) {
      return this.cast();
   }
}
