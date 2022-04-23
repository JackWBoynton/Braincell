package net.bottomtextdanny.braincell.mod._base;

import net.bottomtextdanny.braincell.BraincellHelper;
import net.bottomtextdanny.braincell.mod._mod.client_sided.BCClientToken;

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
