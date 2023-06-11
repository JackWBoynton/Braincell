package bottomtextdanny.braincell.mod._base.blitty.uv_data;

import bottomtextdanny.braincell.mod._base.BCStaticData;
import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import bottomtextdanny.braincell.mod._base.blitty.BlittyStartCallout;

public class Spriter implements BlittyUVPosTransformer, BlittyStartCallout {
   public int advanceX;
   public int advanceY;
   private int frameTicket;
   private final int movements;
   private final int framesPerSprite;
   private int subSpriteCounter;
   private boolean going;

   public Spriter(int advanceX, int advanceY, int sprites, int framesPerSprite) {
      this.advanceX = advanceX;
      this.advanceY = advanceY;
      this.movements = sprites - 1;
      this.framesPerSprite = framesPerSprite;
      this.subSpriteCounter = framesPerSprite * sprites + 1;
   }

   public void start(Blitty blitty) {
      int current = BCStaticData.frameCounter();
      if (this.frameTicket != current) {
         this.frameTicket = BCStaticData.frameCounter();
         this.updateFrame();
      }

   }

   public void transform(BlittyUV pos) {
      if (this.going) {
         float prog = (float)this.progression();
         pos.x += (float)this.advanceX * prog * pos.width % (float)pos.image.width();
         pos.y += (float)this.advanceY * prog * pos.height % (float)pos.image.height();
      }

   }

   protected void updateFrame() {
      if (this.subSpriteCounter < this.framesPerSprite * this.movements) {
         this.going = true;
         ++this.subSpriteCounter;
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
