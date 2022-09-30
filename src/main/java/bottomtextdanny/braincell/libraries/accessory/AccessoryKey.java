package bottomtextdanny.braincell.libraries.accessory;

import bottomtextdanny.braincell.base.pair.Pair;
import bottomtextdanny.braincell.tables.BCAccessoryKeys;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import bottomtextdanny.braincell.Braincell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AccessoryKey<E extends IAccessory> {
    private final ResourceLocation key;
    private final AccessoryFactory<E> accessoryFactory;

    public AccessoryKey(AccessoryFactory<E> accessoryFactory, ResourceLocation key) {
        this.accessoryFactory = accessoryFactory;
        this.key = key;
    }

    public static <E extends IAccessory> AccessoryKey<E> createKey(ResourceLocation location, AccessoryFactory<E> supplier) {
        return new AccessoryKey<>(supplier, location);
    }

    public ResourceLocation getLocation() {
        return this.key;
    }

    public E create(Player player) {
        return this.accessoryFactory.make(this, player);
    }

    public boolean isEmpty() {
        return false;
    }
}
