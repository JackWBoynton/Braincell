package net.bottomtextdanny.braincell.mod._base.registry;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.registry.managing.DeferrorType;
import net.bottomtextdanny.braincell.mod._base.registry.managing.Wrap;
import net.bottomtextdanny.braincell.mod.world.builtin_items.BCSpawnEggItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class EntityWrap<T extends EntityType<?>> extends Wrap<T> {
	private BCSpawnEggItem egg;
	
	public EntityWrap(ResourceLocation name, Supplier<T> sup) {
		super(name, sup);
	}

	@Override
	public void solve() {
		super.solve();
		Optional<BCSpawnEggItem.Builder> eggBuilderOp = Braincell.common().getEntityCoreDataDeferror().getEggBuilder(this.key);
		if (eggBuilderOp.isPresent()) {
			ResourceLocation eggKey = new ResourceLocation(getModSolvingState().getModID(), this.key.getPath() + "_spawn_egg");
			BCSpawnEggItem.Builder eggBuilder = eggBuilderOp.get();

			Objects.requireNonNull(eggBuilderOp.get(), String.join("Attempted to register a null egg for entity type ", this.key.toString()));
			this.egg = eggBuilder.setTypeSupplier(() -> (EntityType<Mob>)this.obj).build();
			getModSolvingState().getRegistryDeferror(DeferrorType.ITEM).get().addDeferredRegistry(() -> this.egg);
			this.getModSolvingState().doHooksForObject(DeferrorType.ITEM, this.egg);
			if (eggBuilder.getSort() != -1) {
				Braincell.common().getItemSortData().setSortValue(eggKey, eggBuilder.getSort());
			}
			this.egg.setRegistryName(eggKey);
		}
	}
}
