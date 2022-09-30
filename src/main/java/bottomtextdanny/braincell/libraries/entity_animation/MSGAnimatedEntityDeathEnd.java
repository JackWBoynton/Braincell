package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries.network.BCEntityPacket;
import bottomtextdanny.braincell.BCPacketInitialization;
import bottomtextdanny.braincell.libraries.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGAnimatedEntityDeathEnd extends BCEntityPacket<MSGAnimatedEntityDeathEnd, Entity> {

	public MSGAnimatedEntityDeathEnd(int entityId) {
		super(entityId);
	}

	@Override
	public void serialize(FriendlyByteBuf stream) {
		super.serialize(stream);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public MSGAnimatedEntityDeathEnd deserialize(FriendlyByteBuf stream) {
		return new MSGAnimatedEntityDeathEnd(stream.readInt());
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void postDeserialization(NetworkEvent.Context ctx, Level world) {
		Connection.doClientSide(() -> {
			Entity entity = getEntityAsReceptor(world);
			if (entity instanceof LivingAnimatableProvider provider && provider.operateAnimatableModule()) {
				provider.onDeathAnimationEndClient();
			}
		});
	}
	
	@Override
	public LogicalSide side() {
		return LogicalSide.CLIENT;
	}

	@Override
	public SimpleChannel mainChannel() {
		return BCPacketInitialization.CHANNEL;
	}
}
