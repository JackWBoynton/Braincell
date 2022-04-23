package net.bottomtextdanny.braincell.mod._base.blitty.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bottomtextdanny.braincell.mod._base.blitty.Blitty;
import net.bottomtextdanny.braincell.mod._base.blitty.BlittyConfig;

public interface BlittyRenderer {

    void render(Blitty blitty, PoseStack matrixStack, float posX, float posY, float posZ, BlittyConfig... configurations);

    static boolean modsColor(BlittyConfig... configurations) {
        if (configurations.length > 5) return true;
        for (BlittyConfig configuration : configurations) {
            if (configuration.usesColor()) {
                return true;
            }
        }
        return false;
    }

    static boolean modsPosition(BlittyConfig... configurations) {
        for (BlittyConfig configuration : configurations) {
            if (configuration.usesPos()) {
                return true;
            }
        }
        return false;
    }

    static boolean modsUV(BlittyConfig... configurations) {
        for (BlittyConfig configuration : configurations) {
            if (configuration.usesUV()) {
                return true;
            }
        }
        return false;
    }
}
