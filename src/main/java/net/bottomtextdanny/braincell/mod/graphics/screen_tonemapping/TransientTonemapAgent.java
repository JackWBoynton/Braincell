package net.bottomtextdanny.braincell.mod.graphics.screen_tonemapping;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class TransientTonemapAgent extends TonemapAgent {
    public final int lifetime;
    private int lifeTicks;

    public TransientTonemapAgent(int lifetime) {
        super();
        this.lifetime = lifetime;
    }

    @Override
    public void tick() {
        this.lifeTicks++;
    }

    public boolean removeIf() {
        return this.lifeTicks >= this.lifetime;
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public static TransientTonemapAgent createBlink(int lifetime) {
        return new TransientTonemapAgent(lifetime) {

            @Override
            public void render(float partialTick) {
                float blackness = -((this.lifetime - (getLifeTicks() + partialTick)) / this.lifetime) * 0.8F;
                getChannelModifier().set(blackness, blackness, blackness);
            }
        };
    }
}
