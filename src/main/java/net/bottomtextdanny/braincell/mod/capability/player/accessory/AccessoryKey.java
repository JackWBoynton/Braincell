package net.bottomtextdanny.braincell.mod.capability.player.accessory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.base.pair.Pair;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AccessoryKey<E extends IAccessory> {
    private static Map<AccessoryKey<?>, AccessoryAttributeModifierData> ATTRIBUTE_DATA;
    private static final ObjectOpenHashSet<AccessoryKey<?>> EMPTY_LOOKUP = Util.make(() -> {
        ObjectOpenHashSet<AccessoryKey<?>> table = new ObjectOpenHashSet<>(16);
        table.add(BCAccessoryKeys.EMPTY);
        table.add(BCAccessoryKeys.STACK_EMPTY);
        return table;
    });
    private static List<AccessoryKey<?>> ACCESSORIES_BY_ID = Lists.newLinkedList();
    private static Map<ResourceLocation, AccessoryKey<?>> ACCESSORIES_BY_LOCATION = Maps.newHashMap();
    private static boolean initialized;
    private final ResourceLocation key;
    private final AccessoryFactory<E> accessoryFactory;
    private int id;

    private AccessoryKey(AccessoryFactory<E> accessoryFactory, ResourceLocation key) {
        this.accessoryFactory = accessoryFactory;
        this.key = key;
    }

    public static <E extends IAccessory> AccessoryKey<E> createKey(ResourceLocation location, AccessoryFactory<E> supplier) {
        if (Braincell.common().hasPassedInitialization()) throw new UnsupportedOperationException("Can't create new accessory keys after mod initialization.");
        AccessoryKey<E> key = new AccessoryKey<>(supplier, location);
        key.id = ACCESSORIES_BY_ID.size();
        ACCESSORIES_BY_ID.add(key);
        ACCESSORIES_BY_LOCATION.put(location, key);
        return key;
    }

    public ResourceLocation getLocation() {
        return this.key;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessoryKey<?> that = (AccessoryKey<?>) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public E create(Player player) {
        return this.accessoryFactory.make(this, player);
    }

    public static List<AccessoryKey<?>> getAccessoriesById() {
        return ACCESSORIES_BY_ID;
    }

    public static Map<ResourceLocation, AccessoryKey<?>> getAccessoriesByLocation() {
        return ACCESSORIES_BY_LOCATION;
    }

    public static Map<AccessoryKey<?>, AccessoryAttributeModifierData> getAttributeData() {
        return ATTRIBUTE_DATA;
    }

    public static boolean isEmpty(AccessoryKey<?> key) {
        return EMPTY_LOOKUP.contains(key);
    }

    public static void build() {
        if (!initialized) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> {
                EmptyAccessoryKeyCollectorEvent collectorEvent = new EmptyAccessoryKeyCollectorEvent();
                MinecraftForge.EVENT_BUS.post(collectorEvent);
                EMPTY_LOOKUP.addAll(collectorEvent.getCollection());
            });
            ImmutableMap.Builder<AccessoryKey<?>, AccessoryAttributeModifierData> dataBuilder = ImmutableMap.builder();
            ACCESSORIES_BY_ID = ImmutableList.copyOf(ACCESSORIES_BY_ID);
            ACCESSORIES_BY_LOCATION = ImmutableMap.copyOf(ACCESSORIES_BY_LOCATION);
            initialized = true;

            ACCESSORIES_BY_ID.forEach(key -> {
                if (key.create(null) instanceof AttributeModifierAccessory accessory) {
                    LinkedList<Pair<ModifierType, Double>> modifierList = Lists.newLinkedList();
                    LinkedList<Pair<MiniAttribute, Float>> lesserModifierList = Lists.newLinkedList();
                    accessory.populateModifierData(modifierList, lesserModifierList);
                    AccessoryAttributeModifierData data = AccessoryAttributeModifierData.create(modifierList, lesserModifierList);
                    dataBuilder.put(key, data);
                }
            });

            ATTRIBUTE_DATA = dataBuilder.build();
        } else {
            throw new UnsupportedOperationException("Accessories are already initialized!");
        }
    }
}
