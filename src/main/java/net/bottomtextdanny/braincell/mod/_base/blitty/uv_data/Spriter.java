package net.bottomtextdanny.braincell.mod._base.blitty.uv_data;

import net.bottomtextdanny.braincell.mod._base.blitty.Blitty;
import net.bottomtextdanny.braincell.mod._base.blitty.BlittyStartCallout;
import net.bottomtextdanny.braincell.mod._base.BCStaticData;

public class Spriter implements BlittyUVPosTransformer, BlittyStartCallout {
    public int advanceX;
    public int advanceY;
    private int frameTicket;
    private final int movements;
    private final int framesPerSprite;
    private int subSpriteCounter;
    private boolean going;

    public Spriter(int advanceX, int advanceY, int sprites, int framesPerSprite) {
        super();
        this.advanceX = advanceX;
        this.advanceY = advanceY;
        this.movements = sprites - 1;
        this.framesPerSprite = framesPerSprite;
        this.subSpriteCounter = framesPerSprite * sprites + 1;
    }

    @Override
    public void start(Blitty blitty) {
        final int current = BCStaticData.frameCounter();
        if (this.frameTicket != current) {
            this.frameTicket = BCStaticData.frameCounter();
            updateFrame();
        }
    }

    @Override
    public void transform(BlittyUV pos) {
        if (this.going) {
            final float prog = progression();
            pos.x += (this.advanceX * prog * pos.width) % pos.image.width();
            pos.y += (this.advanceY * prog * pos.height) % pos.image.height();
        }
    }

    protected void updateFrame() {
        if (this.subSpriteCounter < this.framesPerSprite * this.movements) {
            this.going = true;
            this.subSpriteCounter++;
        } else {
            this.going = false;
        }
    }

    public boolean isActive() {
        return this.going;
    }

    public int progression() {
        return this.subSpriteCounter / this.framesPerSprite;
    }

    public void reset() {
        this.subSpriteCounter = 0;
    }
}
