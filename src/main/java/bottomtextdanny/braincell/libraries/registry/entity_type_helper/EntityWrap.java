package bottomtextdanny.braincell.libraries.registry.entity_type_helper;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.Objects;
import java.util.function.Supplier;

public class EntityWrap<T extends EntityType<?>> extends Wrap<T> {
	
	public EntityWrap(ResourceLocation name, Supplier<T> sup) {
		super(name, sup);
	}

	@Override
	public void solve() {
		super.solve();
		EggBuildData eggBuilderData = Braincell.common().getEntityCoreDataDeferror().getEggBuilder(this.key);

		if (eggBuilderData == null) return;

		ResourceLocation eggKey = new ResourceLocation(getModSolvingState().getModID(), eggBuilderData.name() + "_spawn_egg");
		BCSpawnEggItem.Builder eggBuilder = eggBuilderData.builder();

		Objects.requireNonNull(eggBuilderData.builder(), String.join("Attempted to register a null egg for entity type ", this.key.toString()));

		BCSpawnEggItem egg = eggBuilder.setTypeSupplier(() -> (EntityType<Mob>)this.obj).build();


		getModSolvingState().getRegistryDeferror(Registry.ITEM_REGISTRY).get().addDeferredRegistry(eggKey, () -> egg);
		this.getModSolvingState().doHooksForObject(Registry.ITEM, eggKey, egg);

		if (eggBuilder.getSort() != -1) {
			Braincell.common().getItemSortData().setSortValue(eggKey, eggBuilder.getSort());
		}
	}
}
