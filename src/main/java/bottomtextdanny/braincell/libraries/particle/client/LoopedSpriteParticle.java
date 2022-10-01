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
public abstract class LoopedSpriteParticle extends BaseSpriteParticle {
    protected int loopTime = 10;

    protected LoopedSpriteParticle(ClientLevel world, double x, double y, double z, ParticleAction start, ParticleAction ticker) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D, start, ticker);

    }

    protected LoopedSpriteParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleAction start, ParticleAction ticker) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, start, ticker);
    }

    @Override
    protected void handleSprite(float lifetime) {
        float space = (age % loopTime) / (float)loopTime;
        int size = sprites.size();
        float o = frameSpace[frameSpace.length - 1];

        for (float v : frameSpace) {
            if (space <= v) {
                setLocalSprite((int)(o * size));
                break;
            }
            o = v;
        }
    }
}
