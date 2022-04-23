package net.bottomtextdanny.braincell.mod.entity.targeting;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;

public final class TargetPredicates {

    public static TargetPredicate byClass(Class<? extends LivingEntity> clazz) {
        return (m, t) -> t.getClass().isAssignableFrom(clazz);
    }

    public static TargetPredicate byClasses(Class<?>... classes) {
        return (m, t) -> Arrays.stream(classes).anyMatch(cl -> t.getClass().isAssignableFrom(cl));
    }

    public static TargetPredicate excludeByClass(Class<? extends LivingEntity> clazz) {
        return (m, t) -> !t.getClass().isAssignableFrom(clazz);
    }

    public static TargetPredicate excludeByClasses(Class<?>... classes) {
        return (m, t) -> Arrays.stream(classes).noneMatch(cl -> t.getClass().isAssignableFrom(cl));
    }

    public static TargetPredicate noCreativeOrSpectator() {
        return (m, t) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(t);
    }

    public static TargetPredicate canBeCollidedWith() {
        return (m, t) -> EntitySelector.CAN_BE_COLLIDED_WITH.test(t);
    }

    public static TargetPredicate notBeingRidden() {
        return (m, t) -> EntitySelector.ENTITY_NOT_BEING_RIDDEN.test(t);
    }

    public static TargetPredicate alive() {
        return (m, t) -> EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(t);
    }

    public static TargetPredicate notRiding(Entity mount) {
        return (m, t) -> EntitySelector.notRiding(mount).test(t);
    }

    public static TargetPredicate notRiding() {
        return (m, t) -> t.isPassenger();
    }

    public static TargetPredicate pushableBy(Entity pusher) {
        return (m, t) -> EntitySelector.pushableBy(pusher).test(t);
    }

    public static TargetPredicate withingDistance(double x, double y, double z, double distance) {
        return (m, t) -> EntitySelector.withinDistance(x, y, z, distance).test(t);
    }
}
