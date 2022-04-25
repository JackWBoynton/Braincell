package net.bottomtextdanny.braincell.mod.network.stc;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.bottomtextdanny.braincell.mod._base.entity.modules.looped_walk.LoopedWalkProvider;
import net.bottomtextdanny.braincell.mod._base.network.BCEntityPacket;
import net.bottomtextdanny.braincell.mod._base.network.Connection;
import net.bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Consumer;

public class MSGTrivialEntityActions extends BCEntityPacket<MSGTrivialEntityActions, Entity> {
    public static int HIT_EVENT;
    public static final Int2ObjectArrayMap<Consumer<Entity>> EVENT_MAP = Util.make(() -> {
        Int2ObjectArrayMap<Consumer<Entity>> map = new Int2ObjectArrayMap<>();
        map.put(HIT_EVENT, entity -> {
            if (entity instanceof LoopedWalkProvider provider && provider.operateWalkModule()) {
                provider.loopedWalkModule().renderLimbSwingAmount += provider.hurtLoopLimbSwingFactor();
            }
        });
        return map;
    });

    private final int flag;

    public MSGTrivialEntityActions(int entityId, int flag) {
        super(entityId);
        this.flag = flag;
    }

    @Override
    public MSGTrivialEntityActions deserialize(FriendlyByteBuf stream) {
        return new MSGTrivialEntityActions(stream.readInt(), stream.readInt());
    }

    @Override
    public void serialize(FriendlyByteBuf stream) {
        super.serialize(stream);
        stream.writeInt(this.flag);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void postDeserialization(NetworkEvent.Context ctx, Level world) {
        super.postDeserialization(ctx, world);
        Connection.doClientSide(() -> {
            EVENT_MAP.get(this.flag).accept(getEntityAsReceptor(world));
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
