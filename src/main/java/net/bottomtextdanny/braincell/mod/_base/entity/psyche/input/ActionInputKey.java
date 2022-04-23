package net.bottomtextdanny.braincell.mod._base.entity.psyche.input;

import com.google.common.util.concurrent.Runnables;
import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod.entity.psyche.MarkedTimer;
import net.bottomtextdanny.braincell.mod.entity.targeting.TargetPredicate;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class ActionInputKey<E> {
    public static final ActionInputKey<Runnable> SET_TARGET_CALL = new ActionInputKey<>("set_target", Runnables::doNothing);
    public static final ActionInputKey<Supplier<MarkedTimer>> MARKED_UNSEEN = new ActionInputKey<>("marked_unseen", () -> null);
    public static final ActionInputKey<TargetPredicate> SEE_TARGET_PREDICATE = new ActionInputKey<>("see_target_predicate", () -> (m, t) -> m.getSensing().hasLineOfSight(t));
    private static int counter;
    public final int id;
    public final ResourceLocation name;
    public final Supplier<E> defaultValue;

    public ActionInputKey(String name, Supplier<E> defaultValue ) {
        this.defaultValue = defaultValue;
        this.id = counter;
        counter++;
        this.name = new ResourceLocation(Braincell.ID, name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ActionInputKey<?> that = (ActionInputKey<?>) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
