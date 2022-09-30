package bottomtextdanny.braincell.libraries.mod;

import bottomtextdanny.braincell.BraincellHelper;
import bottomtextdanny.braincell.in.BCClientToken;

public final class BCStaticData {
    private static float partialTick;
    private static int framesPassed;

    public static float partialTick() {
        return partialTick;
    }

    public static int frameCounter() {
        return framesPassed;
    }

    public static void setPartialTick(float value, BCClientToken token) {
        if (token == null) throw BraincellHelper.INVALID_TOKEN_EX;
        partialTick = value;
        framesPassed++;
    }
}
