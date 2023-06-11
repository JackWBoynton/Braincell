package bottomtextdanny.braincell.mod.world.particle_utilities.local_sprites;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class CustomSpriteGroup implements SpriteGroup {
   private final TextureAtlasSprite[] spriteArray;

   public CustomSpriteGroup(TextureAtlasSprite[] spriteArray) {
      this.spriteArray = spriteArray;
   }

   public TextureAtlasSprite fetch(int index) {
      if (index < this.spriteArray.length && index >= 0) {
         return this.spriteArray[index];
      } else {
         throw new IllegalArgumentException("Tried to fetch sprite with index out of range, requested index:" + index + ", for bounds (inclusive): 0-" + (this.spriteArray.length - 1));
      }
   }

   public int size() {
      return this.spriteArray.length;
   }
}
