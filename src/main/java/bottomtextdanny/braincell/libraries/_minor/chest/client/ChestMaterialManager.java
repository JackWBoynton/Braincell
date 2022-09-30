package bottomtextdanny.braincell.libraries._minor.chest.client;

import bottomtextdanny.braincell.libraries._minor.chest.ChestMaterialProvider;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class ChestMaterialManager {
    private final List<ResourceLocation> deferredChestLocations = Lists.newArrayList();
    private final List<Material> chestMaterials = Lists.newArrayList();

    public ChestMaterialManager() {
        super();
    }

    public void sendListeners() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((TextureStitchEvent.Pre event) -> {
            if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
                this.deferredChestLocations.forEach(event::addSprite);
            }
        });
    }

    public Material getChestMaterial(ChestMaterialProvider provider, ChestMaterial type) {
        return this.chestMaterials.get(provider.getChestMaterialSlot() + type.ordinal());
    }

    public void deferMaterialsForChest(ResourceLocation key, ChestMaterialProvider chestSpriteProvider) {
        ResourceLocation[] texturePaths = chestSpriteProvider.chestTexturePaths(key);
        chestSpriteProvider.setChestMaterialSlot(this.chestMaterials.size());
        generateChestBit(texturePaths[ChestMaterialProvider.SINGLE_SHIFT]);
        generateChestBit(texturePaths[ChestMaterialProvider.LEFT_SHIFT]);
        generateChestBit(texturePaths[ChestMaterialProvider.RIGHT_SHIFT]);
    }

    private void generateChestBit(ResourceLocation chestTexturePath) {
        this.deferredChestLocations.add(chestTexturePath);
        Material material = new Material(Sheets.CHEST_SHEET, chestTexturePath);
        this.chestMaterials.add(material);
    }
}
