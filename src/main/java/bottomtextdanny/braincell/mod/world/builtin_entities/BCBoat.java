package bottomtextdanny.braincell.mod.world.builtin_entities;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import bottomtextdanny.braincell.mod._base.serialization.builtin.BuiltinSerializers;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManager;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.entity.serialization.EntityData;
import bottomtextdanny.braincell.mod.entity.serialization.EntityDataReference;
import bottomtextdanny.braincell.mod.entity.serialization.RawEntityDataReference;
import bottomtextdanny.braincell.mod.world.builtin_items.BCBoatItem;
import bottomtextdanny.braincell.mod.world.entity_utilities.EntityClientMessenger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

public class BCBoat extends Boat implements BCDataManagerProvider, EntityClientMessenger {
	public static final int UPDATE_TEXTURE_CALL = 0;
	public static final EntityDataReference<Item> DATA_REF = BCDataManager.attribute(BCBoat.class,
			RawEntityDataReference.of(
					BuiltinSerializers.ITEM,
					() -> Items.AIR,
					"boat_item"
			));
	private final EntityData<Item> boatItem;
	private final BCDataManager deDataManager;

	
	public BCBoat(EntityType<? extends Boat> type, Level world) {
		super(type, world);
		this.deDataManager = new BCDataManager(this);
		this.boatItem = bcDataManager().addSyncedData(EntityData.of(DATA_REF));
	}

	public void setBoatItem(BCBoatItem item) {
        this.boatItem.set(item);
	}

	@Override
	public void clientCallOutHandler(int flag, ObjectFetcher fetcher) {
		if (flag == UPDATE_TEXTURE_CALL) {
			setClientBoatItem(fetcher.get(0, Integer.class));
		}
	}

	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
		this.lastYd = this.getDeltaMovement().y;
		if (!this.isPassenger()) {
			if (onGroundIn) {
				if (this.fallDistance > 3.0F) {
					if (this.status != Status.ON_LAND) {
						this.fallDistance = 0.0F;
						return;
					}

					this.causeFallDamage(this.fallDistance, 1.0F, DamageSource.FALL);
					if (!this.level.isClientSide && !this.isRemoved()) {
						this.remove(RemovalReason.KILLED);
						if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							ItemEntity plankEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(getBCBoatType().materialItem.get(), 3));
							ItemEntity stickEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.STICK, 2));

							plankEntity.setDefaultPickUpDelay();
							stickEntity.setDefaultPickUpDelay();

							if (captureDrops() != null) {
								captureDrops().add(plankEntity);
								captureDrops().add(stickEntity);
							}
							else {
								this.level.addFreshEntity(plankEntity);
								this.level.addFreshEntity(stickEntity);
							}
						}
					}
				}
				this.fallDistance = 0.0F;
			} else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0D) {
				this.fallDistance = (float)((double)this.fallDistance - y);
			}

		}
	}

	protected void setLastYd(double newLastYd) {
		this.lastYd = newLastYd;
	}

	protected double getLastYd() {
		return this.lastYd;
	}

	public void updateClientTexture() {
		if (!this.level.isClientSide()) {
			sendClientMsg(UPDATE_TEXTURE_CALL, PacketDistributor.TRACKING_ENTITY.with(() -> this), WorldPacketData.of(BuiltinSerializers.INTEGER, Item.getId(this.boatItem.get())));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void setClientBoatItem(int transientId) {
		this.boatItem.set(Registry.ITEM.byId(transientId));
	}

	public BCBoatType getBCBoatType() {
		return this.boatItem.get() instanceof BCBoatItem boatItem ? boatItem.getType() : BCBoatType.INVALID;
	}

	@Override
	public Item getDropItem() {
		return getBCBoatType().materialItem.get();
	}
	
	@Override
	public BCDataManager bcDataManager() {
		return this.deDataManager;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
