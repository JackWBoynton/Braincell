package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;

public interface SearchPredicate<T> {

    T search(Mob mob, ServerLevel world, RangeTest rangeTest, Lazy<AABB> searchArea, MobMatchPredicate<? super T> post);

    @SuppressWarnings("unchecked")
    default <U extends T> SearchPredicate<U> cast() {
        return (SearchPredicate<U>)this;
    }

    @SuppressWarnings("unchecked")
    default <U> SearchPredicate<U> hackyCast() {
        return (SearchPredicate<U>)this;
    }

    default <U extends T> SearchPredicate<U> cast(Class<U> clazz) {
        return cast();
    }
}
