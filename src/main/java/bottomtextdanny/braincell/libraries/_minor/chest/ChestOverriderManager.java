package bottomtextdanny.braincell.libraries._minor.chest;

import bottomtextdanny.braincell.Braincell;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.function.Supplier;

public final class ChestOverriderManager {
    private final Map<ResourceLocation, Supplier<ItemStack>> materialMap = Maps.newHashMap();

    public ChestOverriderManager() {
        super();
    }

    public void put(ResourceLocation key, Supplier<ItemStack> chestItemStack) {
        if (!Braincell.common().hasPassedInitialization()) this.materialMap.put(key, chestItemStack);
    }

    public boolean containsForId(ResourceLocation key) {
        return this.materialMap.containsKey(key);
    }

    public ItemStack getItemStack(ResourceLocation key) {
        return this.materialMap.get(key).get();
    }
}
