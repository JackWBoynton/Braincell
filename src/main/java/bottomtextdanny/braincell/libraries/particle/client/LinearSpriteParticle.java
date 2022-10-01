/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client;

import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class LinearSpriteParticle extends BaseSpriteParticle {

    protected LinearSpriteParticle(ClientLevel world, double x, double y, double z, ParticleAction start, ParticleAction ticker) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D, start, ticker);
    }

    protected LinearSpriteParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleAction start, ParticleAction ticker) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, start, ticker);
    }

    @Override
    protected void handleSprite(float lifetime) {
        float space = age / lifetime;
        float[] frameSpace = this.frameSpace;
        for (int i = 0, frameSpaceLength = frameSpace.length; i < frameSpaceLength; i++) {
            float v = frameSpace[i];
            if (space <= v) {
                setLocalSprite(i);
                break;
            }
        }
    }
}
