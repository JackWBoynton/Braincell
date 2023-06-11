package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import java.util.Iterator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public final class SearchPredicates {
   public static SearchPredicate nearestPlayer() {
      return (mob, level, radius, searchArea, post) -> {
         Player player = null;
         double d0 = -1.0;
         Iterator var8 = level.players().iterator();

         while(true) {
            Player player1;
            double d1;
            do {
               do {
                  if (!var8.hasNext()) {
                     return player;
                  }

                  player1 = (Player)var8.next();
                  d1 = radius.test(player1.getX(), player1.getY(), player1.getZ());
               } while(!(d1 > 0.0));
            } while(d0 != -1.0 && !(d1 < d0));

            if (post.test(mob, player1)) {
               d0 = d1;
               player = player1;
            }
         }
      };
   }

   public static SearchPredicate nearestLiving() {
      return (mob, level, radius, searchArea, post) -> {
         LivingEntity[] t = new LivingEntity[]{null};
         double[] d0 = new double[]{-1.0};
         level.getEntities().get(EntityTypeTest.forClass(LivingEntity.class), (AABB)searchArea.get(), (le) -> {
            double d1 = radius.test(le.getX(), le.getY(), le.getZ());
            if (d1 > 0.0 && (d0[0] == -1.0 || d1 < d0[0]) && post.test(mob, le)) {
               d0[0] = d1;
               t[0] = le;
            }

         });
         return t[0];
      };
   }

   public static SearchPredicate nearestEntity() {
      return (mob, level, radius, searchArea, post) -> {
         Entity[] t = new Entity[]{null};
         double[] d0 = new double[]{-1.0};
         level.getEntities().get(EntityTypeTest.forClass(Entity.class), (AABB)searchArea.get(), (le) -> {
            double d1 = radius.test(le.getX(), le.getY(), le.getZ());
            if (d1 > 0.0 && (d0[0] == -1.0 || d1 < d0[0]) && post.test(mob, le)) {
               d0[0] = d1;
               t[0] = le;
            }

         });
         return t[0];
      };
   }

   public static SearchPredicate nearestEntityOfType(Class clazz) {
      return (mob, level, radius, searchArea, post) -> {
         Entity[] t = new Entity[]{null};
         double[] d0 = new double[]{-1.0};
         level.getEntities().get(EntityTypeTest.forClass(clazz), (AABB)searchArea.get(), (entity) -> {
            double d1 = radius.test(entity.getX(), entity.getY(), entity.getZ());
            if (d1 > 0.0 && (d0[0] == -1.0 || d1 < d0[0]) && post.test(mob, entity)) {
               d0[0] = d1;
               t[0] = entity;
            }

         });
         return t[0];
      };
   }

   private SearchPredicates() {
   }
}
