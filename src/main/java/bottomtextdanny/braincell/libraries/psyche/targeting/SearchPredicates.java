package bottomtextdanny.braincell.libraries.psyche.targeting;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;

public final class SearchPredicates {

    public static SearchPredicate<Player> nearestPlayer() {
        return (mob, level, radius, searchArea, post) -> {
            Player player = null;
            double d0 = -1.0D;

            for(Player player1 : level.players()) {
                double d1 = radius.test(player1.getX(), player1.getY(), player1.getZ());
                if (d1 > 0.0D && (d0 == -1.0D || d1 < d0)) {
                    if (post.test(mob, player1)) {
                        d0 = d1;
                        player = player1;
                    }
                }
            }

            return player;
        };
    }

    public static SearchPredicate<LivingEntity> nearestLiving() {
        return (mob, level, radius, searchArea, post) -> {
            LivingEntity[] t = {null};
            double[] d0 = {-1.0D};

            level.getEntities().get(EntityTypeTest.forClass(LivingEntity.class), searchArea.get(), le -> {
                double d1 = radius.test(le.getX(), le.getY(), le.getZ());
                if (d1 > 0 && (d0[0] == -1.0D || d1 < d0[0])) {
                    if (post.test(mob, le)) {
                        d0[0] = d1;
                        t[0] = le;
                    }
                }
            });

            return t[0];
        };
    }

    public static SearchPredicate<Entity> nearestEntity() {
        return (mob, level, radius, searchArea, post) -> {
            Entity[] t = {null};
            double[] d0 = {-1.0D};

            level.getEntities().get(EntityTypeTest.forClass(Entity.class), searchArea.get(), le -> {
                double d1 = radius.test(le.getX(), le.getY(), le.getZ());
                if (d1 > 0 && (d0[0] == -1.0D || d1 < d0[0])) {
                    if (post.test(mob, le)) {
                        d0[0] = d1;
                        t[0] = le;
                    }
                }
            });

            return t[0];
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> SearchPredicate<T> nearestEntityOfType(Class<T> clazz) {
        return (mob, level, radius, searchArea, post) -> {
            Entity[] t = {null};
            double[] d0 = {-1.0D};

            level.getEntities().get(EntityTypeTest.forClass(clazz), searchArea.get(), entity -> {
                double d1 = radius.test(entity.getX(), entity.getY(), entity.getZ());

                if (d1 > 0 && (d0[0] == -1.0D || d1 < d0[0])) {
                    if (post.test(mob, entity)) {
                        d0[0] = d1;
                        t[0] = entity;
                    }
                }
            });

            return (T)t[0];
        };
    }

    private SearchPredicates() {}
}
