package net.bottomtextdanny.braincell.mod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.atomic.AtomicReference;

@OnlyIn(Dist.CLIENT)
@Deprecated
public class ImageData extends ResourceLocation {
    private static final AtomicReference<ImageData> current = new AtomicReference<>();
    public final int width;
    public final int height;

    protected ImageData(String[] data, int width, int height) {
        super(data);
        this.width = width;
        this.height = height;
    }

    public ImageData(String key, int width, int height) {
        super(key);
        this.width = width;
        this.height = height;
    }

    public ImageData(String namespace, String path, int width, int height) {
        super(namespace, path);
        this.width = width;
        this.height = height;
    }

    public boolean isCurrent() {
        return this == current.get();
    }

    public void use() {
        RenderSystem.setShaderTexture(0, this);
        current.set(this);
    }
}
