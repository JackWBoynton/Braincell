package net.bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import static net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCModelingParameters.*;

public enum BCBoxPosition {
    LEFT_UP_FRONT(BOX_POS_LEFT, BOX_POS_UP, BOX_POS_FRONT),
    RIGHT_UP_FRONT(BOX_POS_RIGHT, BOX_POS_UP, BOX_POS_FRONT),
    LEFT_DOWN_FRONT(BOX_POS_LEFT, BOX_POS_DOWN, BOX_POS_FRONT),
    RIGHT_DOWN_FRONT(BOX_POS_RIGHT, BOX_POS_DOWN, BOX_POS_FRONT),
    LEFT_UP_BACK(BOX_POS_LEFT, BOX_POS_UP, BOX_POS_BACK),
    RIGHT_UP_BACK(BOX_POS_RIGHT, BOX_POS_UP, BOX_POS_BACK),
    LEFT_DOWN_BACK(BOX_POS_LEFT, BOX_POS_DOWN, BOX_POS_BACK),
    RIGHT_DOWN_BACK(BOX_POS_RIGHT, BOX_POS_DOWN, BOX_POS_BACK);

    public final int x, y, z;

    BCBoxPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
