package bottomtextdanny.braincell.libraries.entity_variant;

import bottomtextdanny.braincell.libraries.entity_variant.client.VariantRenderingData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class Form<E extends LivingEntity> {
    @OnlyIn(Dist.CLIENT)
    private VariantRenderingData<E> rendering;

    public Form() {
        super();
    }

    public Component name() {
        return Component.literal("Unnamed Form");
    }

    public int primaryColor() {
        return -1;
    }

    public int secondaryColor() {
        return -1;
    }

    protected abstract VariantRenderingData<E> createRenderingHandler();

    @SuppressWarnings("unchecked")
    public final void applyAttributeBonusesRaw(Mob entityIn) {
        applyAttributeBonuses((E) entityIn);
    }

    public void applyAttributeBonuses(E entityIn) {}

    @Nullable
    public EntityDimensions boxSize() {
        return null;
    }

    @Nullable
    public ResourceLocation customLoot() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public VariantRenderingData<E> getRendering() {
        if (this.rendering == null)
            this.rendering = createRenderingHandler();
        return this.rendering;
    }
}
