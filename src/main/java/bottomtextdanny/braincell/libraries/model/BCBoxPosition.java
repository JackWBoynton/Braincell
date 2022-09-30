package bottomtextdanny.braincell.libraries.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum BCBoxPosition {
    LEFT_UP_FRONT(BCModelingParameters.BOX_POS_LEFT, BCModelingParameters.BOX_POS_UP, BCModelingParameters.BOX_POS_FRONT),
    RIGHT_UP_FRONT(BCModelingParameters.BOX_POS_RIGHT, BCModelingParameters.BOX_POS_UP, BCModelingParameters.BOX_POS_FRONT),
    LEFT_DOWN_FRONT(BCModelingParameters.BOX_POS_LEFT, BCModelingParameters.BOX_POS_DOWN, BCModelingParameters.BOX_POS_FRONT),
    RIGHT_DOWN_FRONT(BCModelingParameters.BOX_POS_RIGHT, BCModelingParameters.BOX_POS_DOWN, BCModelingParameters.BOX_POS_FRONT),
    LEFT_UP_BACK(BCModelingParameters.BOX_POS_LEFT, BCModelingParameters.BOX_POS_UP, BCModelingParameters.BOX_POS_BACK),
    RIGHT_UP_BACK(BCModelingParameters.BOX_POS_RIGHT, BCModelingParameters.BOX_POS_UP, BCModelingParameters.BOX_POS_BACK),
    LEFT_DOWN_BACK(BCModelingParameters.BOX_POS_LEFT, BCModelingParameters.BOX_POS_DOWN, BCModelingParameters.BOX_POS_BACK),
    RIGHT_DOWN_BACK(BCModelingParameters.BOX_POS_RIGHT, BCModelingParameters.BOX_POS_DOWN, BCModelingParameters.BOX_POS_BACK);

    public final int x, y, z;

    BCBoxPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
