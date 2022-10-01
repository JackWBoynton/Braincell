/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client;

import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.libraries.particle.ModularParticleData;
import bottomtextdanny.braincell.libraries.particle.client.local_sprites.SimpleSpriteGroup;
import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class TransluscentParticle extends LinearSpriteParticle {

    protected TransluscentParticle(ClientLevel world, double x, double y, double z,
								   double xSpeed, double ySpeed, double zSpeed,
								   ParticleAction start, ParticleAction ticker,
								   SpriteSet handleSpriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, start, ticker);

        setSpriteGroup(SimpleSpriteGroup.of(handleSpriteSet, 1));
        setLocalSprite(0);
        setLinearFrameSpace();
    }

    protected TransluscentParticle(ClientLevel world, double x, double y, double z,
								   ParticleAction start, ParticleAction ticker, SpriteSet handleSpriteSet) {
        super(world, x, y, z, 0.0, 0.0, 0.0, start, ticker);

        setSpriteGroup(SimpleSpriteGroup.of(handleSpriteSet, 1));
        setLocalSprite(0);
        setLinearFrameSpace();
    }

    @Override
    public void customInit() {
    }

    @Override
    public void tick() {
        super.tick();
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float idk) {
        return Mth.lerp(idk, sizeO, quadSize);
    }

    protected boolean isLit() {
        return glow == 15728880;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<ModularParticleData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet p_i50634_1_) {
            spriteSet = p_i50634_1_;
        }

        @Nullable
        @Override
        public Particle createParticle(ModularParticleData typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleAction ticker = typeIn.fetch(1);
            ParticleAction start = typeIn.fetch(0);
            return new TransluscentParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, start == null ? ParticleAction.NO : start, ticker == null ? ParticleAction.NO : ticker, spriteSet);
        }
    }
}
