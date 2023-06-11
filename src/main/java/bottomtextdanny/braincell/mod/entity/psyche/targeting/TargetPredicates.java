package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import java.util.Arrays;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;

/** @deprecated */
@Deprecated
public final class TargetPredicates {
   public static TargetPredicate byClass(Class clazz) {
      return (m, t) -> {
         return t.getClass().isAssignableFrom(clazz);
      };
   }

   public static TargetPredicate byClasses(Class... classes) {
      return (m, t) -> {
         return Arrays.stream(classes).anyMatch((cl) -> {
            return t.getClass().isAssignableFrom(cl);
         });
      };
   }

   public static TargetPredicate excludeByClass(Class clazz) {
      return (m, t) -> {
         return !t.getClass().isAssignableFrom(clazz);
      };
   }

   public static TargetPredicate excludeByClasses(Class... classes) {
      return (m, t) -> {
         return Arrays.stream(classes).noneMatch((cl) -> {
            return t.getClass().isAssignableFrom(cl);
         });
      };
   }

   public static TargetPredicate noCreativeOrSpectator() {
      return (m, t) -> {
         return EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(t);
      };
   }

   public static TargetPredicate canBeCollidedWith() {
      return (m, t) -> {
         return EntitySelector.CAN_BE_COLLIDED_WITH.test(t);
      };
   }

   public static TargetPredicate notBeingRidden() {
      return (m, t) -> {
         return EntitySelector.ENTITY_NOT_BEING_RIDDEN.test(t);
      };
   }

   public static TargetPredicate alive() {
      return (m, t) -> {
         return EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(t);
      };
   }

   public static TargetPredicate notRiding(Entity mount) {
      return (m, t) -> {
         return EntitySelector.notRiding(mount).test(t);
      };
   }

   public static TargetPredicate notRiding() {
      return (m, t) -> {
         return t.isPassenger();
      };
   }

   public static TargetPredicate pushableBy(Entity pusher) {
      return (m, t) -> {
         return EntitySelector.pushableBy(pusher).test(t);
      };
   }

   public static TargetPredicate withingDistance(double x, double y, double z, double distance) {
      return (m, t) -> {
         return EntitySelector.withinDistance(x, y, z, distance).test(t);
      };
   }
}
