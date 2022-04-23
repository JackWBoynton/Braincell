package net.bottomtextdanny.braincell.mod._base.plotter;

import net.bottomtextdanny.braincell.base.function.IntTriConsumer;

import java.util.function.Consumer;

public final class BoxCoordIterable {

    public static Consumer<IntTriConsumer> layer(IntBox box,
                                                 int layerOffset,
                                                 boolean top,
                                                 boolean bottom) {
        return (iterator) -> {
            int x1 = box.x1 - layerOffset;
            int x2 = box.x2 + layerOffset;
            int y1 = box.y1 - layerOffset;
            int y2 = box.y2 + layerOffset;
            int z1 = box.z1 - layerOffset;
            int z2 = box.z2 + layerOffset;
            //y #InvasiveXZ
            if (top) {
                if (bottom) {
                    for (int x = x1; x <= x2; x++) {
                        for (int z = z1; z <= z2; z++) {
                            iterator.accept(x, y1, z);
                            iterator.accept(x, y2, z);
                        }
                    }
                } else {
                    for (int x = x1; x <= x2; x++) {
                        for (int z = z1; z <= z2; z++) {
                            iterator.accept(x, y2, z);
                        }
                    }
                }
            } else if (bottom) {
                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        iterator.accept(x, y1, z);
                    }
                }
            }
            y1++;
            y2--;
            //x #InvasiveZ
            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    iterator.accept(x, y, z1);
                    iterator.accept(x, y, z2);
                }
            }
            z1++;
            z2--;
            //z
            for (int z = z1; z <= z2; z++) {
                for (int y = y1; y <= y2; y++) {
                    iterator.accept(x1, y, z);
                    iterator.accept(x2, y, z);
                }
            }
        };
    }
}
