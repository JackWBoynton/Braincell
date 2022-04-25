package net.bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class SearchNearestPredicates {

    public static SearchNearestPredicate nearestPlayer() {
        return (mob, level, radius, searchArea, post) -> {
            double d0 = -1.0D;
            Player player = null;
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

    public static SearchNearestPredicate nearestLiving() {
        return (mob, level, radius, searchArea, post) -> {
            double[] d0 = {-1.0D};
            LivingEntity[] t = {null};

            level.getEntities().get(searchArea.get(), e -> {
                if (e instanceof LivingEntity le) {
                    double d1 = radius.test(le.getX(), le.getY(), le.getZ());
                    if (d1 > 0 && (d0[0] == -1.0D || d1 < d0[0])) {
                        if (post.test(mob, le)) {
                            d0[0] = d1;
                            t[0] = le;
                        }
                    }
                }
            });

            return t[0];
        };
    }
}
