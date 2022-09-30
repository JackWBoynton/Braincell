package bottomtextdanny.braincell.libraries.psyche.input;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.libraries.psyche.targeting.MobMatchPredicate;
import com.google.common.util.concurrent.Runnables;
import bottomtextdanny.braincell.libraries.psyche.CombatHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public final class ActionInputKey<E> {
    public static final ActionInputKey<Runnable> SET_TARGET_CALL = new ActionInputKey<>("set_target", Runnables::doNothing);
    public static final ActionInputKey<IntScheduler> BLINDTRACKING_TIMER = new ActionInputKey<>("blindtracking_timer", () -> null);
    public static final ActionInputKey<MobMatchPredicate<LivingEntity>> SEE_TARGET_PREDICATE = new ActionInputKey<>("see_target_predicate", () -> (m, t) -> m.getSensing().hasLineOfSight(t));
    public static final ActionInputKey<MobMatchPredicate<LivingEntity>> TARGET_VALIDATOR = new ActionInputKey<>("target_validator", () -> (m, t) -> {
        return CombatHelper.isValidAttackTarget(t);
    });
    private static int counter;
    public final int id;
    public final ResourceLocation name;
    public final Supplier<E> defaultValue;

    public ActionInputKey(String name, Supplier<E> defaultValue) {
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
