package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries.network.BCEntityPacket;
import bottomtextdanny.braincell.BCPacketInitialization;
import bottomtextdanny.braincell.libraries.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGEntityAnimation extends BCEntityPacket<MSGEntityAnimation, Entity> {
	private final int handlerIndex, animationIndex;
	
	public MSGEntityAnimation(int entityId, int moduleIndex, int animationIndex) {
		super(entityId);
		this.handlerIndex = moduleIndex;
		this.animationIndex = animationIndex;
	}
	
	@Override
	public void serialize(FriendlyByteBuf stream) {
		super.serialize(stream);
		stream.writeInt(this.handlerIndex);
		stream.writeInt(this.animationIndex);
	}
	
	@Override
	public MSGEntityAnimation deserialize(FriendlyByteBuf stream) {
		return new MSGEntityAnimation(stream.readInt(), stream.readInt(), stream.readInt());
	}
	
	@Override
	public void postDeserialization(NetworkEvent.Context ctx, Level world) {
		Connection.doClientSide(() -> {
			if (getEntityAsReceptor(world) instanceof BaseAnimatableProvider<?> provider
				&& provider.operateAnimatableModule()) {
				provider.animatableModule().animationHandlerList().get(this.handlerIndex).play((Entity) provider, provider.animatableModule().animationManager().getAnimation(this.animationIndex));
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
