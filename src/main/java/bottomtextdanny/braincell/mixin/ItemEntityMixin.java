package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.mixin_support.ItemEntityExtensor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements ItemEntityExtensor {
	private static final EntityDataAccessor<Integer> DATA_SHOWING_ITEM = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.INT);

	public ItemEntityMixin(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}
	
	@Inject(at = @At(value = "TAIL"), method = "defineSynchedData", remap = true)
	public void defineS(CallbackInfo ci) {
		this.getEntityData().define(DATA_SHOWING_ITEM, -1);
	}

	@Inject(at = @At(value = "HEAD"), method = "tryToMerge", remap = true, cancellable = true)
	public void mergeWith(ItemEntity other, CallbackInfo ci) {
		if (((ItemEntityExtensor)other).getShowingModel() != getShowingModel()) {
			ci.cancel();
		}
	}

	@Override
	public int getShowingModel() {
		return this.getEntityData().get(DATA_SHOWING_ITEM);
	}

	@Override
	public void setShowingModel(int item) {
		this.getEntityData().set(DATA_SHOWING_ITEM, item);
	}

	@Override
	public void setShowingModel(Enum<?> item) {
		this.getEntityData().set(DATA_SHOWING_ITEM, item.ordinal());
	}
}
