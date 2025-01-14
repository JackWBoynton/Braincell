package bottomtextdanny.braincell.libraries.accessory;

import bottomtextdanny.braincell.libraries.serialization.PacketData;
import bottomtextdanny.braincell.tables.BCAccessoryKeys;
import com.mojang.blaze3d.vertex.PoseStack;
import bottomtextdanny.braincell.base.ObjectFetcher;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import java.util.Random;

public class StackAccessory implements IAccessory {
	public static final StackAccessory EMPTY = new StackAccessory(BCAccessoryKeys.STACK_EMPTY.get(), null);
	protected final AccessoryKey<?> key;
	protected final Player player;
	protected ItemStack globalStack;
	protected Item item;
	protected InteractionHand hand;
	protected Random random = new Random();
	
	public StackAccessory(AccessoryKey<?> key, Player player) {
		this.key = key;
		this.player = player;
	}

	public void prepare(ItemStack itemStack, InteractionHand side) {
		this.item = itemStack.getItem();
		this.globalStack = itemStack;
		this.hand = side;
	}

	public ItemStack getStack() {
		return this.globalStack;
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public InteractionHand getHand() {
		return this.hand;
	}

	@Override
	public void tick() {}
	
	@OnlyIn(Dist.CLIENT)
	public void accessoryClientManager(int flag, ObjectFetcher fetcher) {}

	public void accessoryServerManager(int flag, ObjectFetcher fetcher) {}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void frameTick(LocalPlayer cPlayer, PoseStack poseStack, float partialTicks) {}

	@Override
	public void triggerClientAction(int flag, PacketDistributor.PacketTarget target, PacketData<?>... data) {
		if (this.player.level instanceof ServerLevel serverPlayer) {
			new MSGHandAccessoryClientMessenger(this.player.getId(), (byte)this.hand.ordinal(), flag, data, serverPlayer).sendTo(target);
		}
	}

	@Override
	public void triggerClientActionToTracking(int flag, PacketData<?>... data) {
		triggerClientAction(flag, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.player), data);
	}

	public void triggerClientActionSpecific(int flag, ServerPlayer player, PacketData<?>... data) {
		triggerClientAction(flag, PacketDistributor.PLAYER.with(() -> player), data);
	}

	@Override
	public void triggerClientActionToTracking(int flag) {
		triggerClientAction(flag, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.player), (PacketData<?>) null);
	}
	
	@Override
	public void triggerServerAction(int flag, PacketData<?>... data) {
		if (this.player.level.isClientSide()) {
			new MSGHandAccessoryServerMessenger((byte)this.hand.ordinal(), flag, data).sendToServer();
		}
	}
	
	@Override
	public void triggerServerAction(int flag) {
		triggerServerAction(flag, (PacketData<?>) null);
	}

	@Override
	public AccessoryKey<?> getKey() {
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void keyHandler(Options settings) {
	}
	
	@Override
	public void read(CompoundTag nbt) {}
	
	@Override
	public CompoundTag write() {
		return new CompoundTag();
	}
}
