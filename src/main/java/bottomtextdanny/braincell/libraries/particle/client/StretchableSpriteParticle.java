/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client;

import bottomtextdanny.braincell.libraries.particle.ExtraOptions;
import bottomtextdanny.braincell.libraries.particle.ModularParticleOptions;
import bottomtextdanny.braincell.libraries.particle.ModularParticleType;
import bottomtextdanny.braincell.libraries.particle.StretchOptions;
import bottomtextdanny.braincell.libraries.particle.client.local_sprites.SimpleSpriteGroup;
import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.floats.FloatLists;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class StretchableSpriteParticle extends BaseSpriteParticle<StretchOptions> {

    protected StretchableSpriteParticle(ModularParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleAction<?> start, ParticleAction<?> ticker, StretchOptions options, SpriteSet handleSpriteSet) {
        super(type, world, x, y, z, xSpeed, ySpeed, zSpeed, start, ticker, options);
        setSpriteGroup(SimpleSpriteGroup.of(handleSpriteSet, type.sprites));
        setLocalSprite(0);
    }

    @Override
    public void customInit() {}

    @Override
    protected void handleSprite(float lifetime) {
        float space = age / lifetime;
        FloatList frameSpace = options.frameSpace();

        for (int i = 0, frameSpaceLength = frameSpace.size(); i < frameSpaceLength; i++) {
            float v = frameSpace.getFloat(i);
            if (space <= v) {
                setLocalSprite(i);
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<ModularParticleOptions<StretchOptions>> {
        private FloatList linearFrameSpace;
        private final SpriteSet spriteSet;

        public Factory(SpriteSet set) {
            spriteSet = set;
        }

        @Nullable
        @Override
        public Particle createParticle(ModularParticleOptions<StretchOptions> options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (options.extra.frameSpace == null) {
                if (linearFrameSpace == null) linearFrameSpace = getLinearFrameSpace(options.getType().sprites);
                options.extra.frameSpace = linearFrameSpace;
            }
            return new StretchableSpriteParticle(options.getType(), level, x, y, z, xSpeed, ySpeed, zSpeed, options.start, options.tick, options.extra, spriteSet);
        }

        private static FloatList getLinearFrameSpace(int size) {
            FloatList list = new FloatArrayList();

            float inv = 1.0F / size;
            float stack = 0.0F;

            for (int i = 0; i < size; i++) {
                list.add(stack += inv);
            }

            return FloatLists.unmodifiable(list);
        }
    }
}
