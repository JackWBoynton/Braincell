package bottomtextdanny.braincell.libraries.particle.local_sprites;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface SpriteGroup {

    TextureAtlasSprite fetch(int index);

    int size();
}
