package bottomtextdanny.braincell.mod.network.stc;

import bottomtextdanny.braincell.mod.entity.modules.animatable.LivingAnimatableProvider;
import bottomtextdanny.braincell.mod.network.BCEntityPacket;
import bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import bottomtextdanny.braincell.mod.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGAnimatedEntityDeathEnd extends BCEntityPacket {
   public MSGAnimatedEntityDeathEnd(int entityId) {
      super(entityId);
   }

   public void serialize(FriendlyByteBuf stream) {
      super.serialize(stream);
   }

   @OnlyIn(Dist.CLIENT)
   public MSGAnimatedEntityDeathEnd deserialize(FriendlyByteBuf stream) {
      return new MSGAnimatedEntityDeathEnd(stream.readInt());
   }

   @OnlyIn(Dist.CLIENT)
   public void postDeserialization(NetworkEvent.Context ctx, Level world) {
      Connection.doClientSide(() -> {
         Entity entity = this.getEntityAsReceptor(world);
         if (entity instanceof LivingAnimatableProvider provider) {
            if (provider.operateAnimatableModule()) {
               provider.onDeathAnimationEndClient();
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
