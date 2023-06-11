package bottomtextdanny.braincell.mod.network.stc;

import bottomtextdanny.braincell.mod.entity.modules.animatable.AnimationHandler;
import bottomtextdanny.braincell.mod.entity.modules.animatable.BaseAnimatableProvider;
import bottomtextdanny.braincell.mod.network.BCEntityPacket;
import bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import bottomtextdanny.braincell.mod.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGEntityAnimation extends BCEntityPacket {
   private final int handlerIndex;
   private final int animationIndex;

   public MSGEntityAnimation(int entityId, int moduleIndex, int animationIndex) {
      super(entityId);
      this.handlerIndex = moduleIndex;
      this.animationIndex = animationIndex;
   }

   public void serialize(FriendlyByteBuf stream) {
      super.serialize(stream);
      stream.writeInt(this.handlerIndex);
      stream.writeInt(this.animationIndex);
   }

   public MSGEntityAnimation deserialize(FriendlyByteBuf stream) {
      return new MSGEntityAnimation(stream.readInt(), stream.readInt(), stream.readInt());
   }

   public void postDeserialization(NetworkEvent.Context ctx, Level world) {
      Connection.doClientSide(() -> {
         Entity entity = this.getEntityAsReceptor(world);
         if (entity instanceof BaseAnimatableProvider provider) {
            if (provider.operateAnimatableModule()) {
               ((AnimationHandler)provider.animatableModule().animationHandlerList().get(this.handlerIndex)).play(provider.animatableModule().animationManager().getAnimation(this.animationIndex));
            }
         }

      });
   }

   public LogicalSide side() {
      return LogicalSide.CLIENT;
   }

   public SimpleChannel mainChannel() {
      return BCPacketInitialization.CHANNEL;
   }
}
