package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;

public class MobMatchPredicates {
    public static MobMatchPredicate<Object> byClass(Class<?> clazz) {
        return (m, t) -> t.getClass().isAssignableFrom(clazz);
    }

    public static MobMatchPredicate<Object> byClasses(Class<?>... classes) {
        return (m, t) -> {
            for (Class<?> cl : classes) {
                if (t.getClass().isAssignableFrom(cl)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static MobMatchPredicate<Object> excludeByClass(Class<? extends LivingEntity> clazz) {
        return (m, t) -> !t.getClass().isAssignableFrom(clazz);
    }

    public static MobMatchPredicate<Object> excludeByClasses(Class<?>... classes) {
        return (m, t) -> {
            for (Class<?> cl : classes) {
                if (t.getClass().isAssignableFrom(cl)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static MobMatchPredicate<Entity> noCreativeOrSpectator() {
        return (m, t) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(t);
    }

    public static MobMatchPredicate<Entity> canBeCollidedWith() {
        return (m, t) -> EntitySelector.CAN_BE_COLLIDED_WITH.test(t);
    }

    public static MobMatchPredicate<Entity> notBeingRidden() {
        return (m, t) -> EntitySelector.ENTITY_NOT_BEING_RIDDEN.test(t);
    }

    public static MobMatchPredicate<Entity> alive() {
        return (m, t) -> EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(t);
    }

    public static MobMatchPredicate<Entity> notRiding(Entity mount) {
        return (m, t) -> EntitySelector.notRiding(mount).test(t);
    }

    public static MobMatchPredicate<Entity> notRiding() {
        return (m, t) -> t.isPassenger();
    }

    public static MobMatchPredicate<Entity> pushableBy(Entity pusher) {
        return (m, t) -> EntitySelector.pushableBy(pusher).test(t);
    }

    public static MobMatchPredicate<Entity> withingDistance(double x, double y, double z, double distance) {
        return (m, t) -> EntitySelector.withinDistance(x, y, z, distance).test(t);
    }
}
