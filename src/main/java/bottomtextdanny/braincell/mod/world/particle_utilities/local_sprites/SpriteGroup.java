package bottomtextdanny.braincell.mod.world.particle_utilities.local_sprites;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface SpriteGroup {
   TextureAtlasSprite fetch(int var1);

   int size();
}
