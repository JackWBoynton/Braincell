package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.StepDataType;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class BCStepDataTypes {
    public static final ResourceKey<Registry<StepDataType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, "step_data_type"));
    private static final DeferredRegister<StepDataType<?>> DEFERRED_REGISTERER = DeferredRegister.create(REGISTRY_KEY, Braincell.ID);
    public static final BCRegistry<StepDataType<?>> ENTRIES = new BCRegistry<>();
    public static final RegistryHelper<StepDataType<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

    public static final Wrap<StepDataType<BlockState>> BLOCK_STATE = make("blockstate", Blocks.STONE::defaultBlockState);
    public static final Wrap<StepDataType<Vec3>> VEC3 = make("vec3", () -> Vec3.ZERO);
    public static final Wrap<StepDataType<Boolean>> BOOL = make("bool", () -> false);
    public static final Wrap<StepDataType<TagKey<?>>> TAG_KEY = make("tag_key", () -> null);
    public static final Wrap<StepDataType<Number>> NUMBER = make("number", () -> 0);

    private static <T> Wrap<StepDataType<T>> make(String name, Supplier<T> defaultValue) {
        return HELPER.defer(name, () -> new StepDataType<>(defaultValue));
    }

    public static void registerRegistry() {
        DEFERRED_REGISTERER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
