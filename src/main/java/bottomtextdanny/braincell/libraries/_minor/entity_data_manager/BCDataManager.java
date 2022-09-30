package bottomtextdanny.braincell.libraries._minor.entity_data_manager;

import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.Persistent;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.EntityDataReference;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.RawEntityDataReference;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization._Persistent;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BCDataManager {
    public static final int REFERENCE_SPLIT = 8;
    public static final Map<Class<? extends Entity>, List<EntityDataReference<?>>> REFERENCES =
            new ConcurrentHashMap<>(REFERENCE_SPLIT);
    private final List<_Persistent<?>> syncedDataList;
    private final List<_Persistent<?>> nonSyncedDataList;
    private final Entity provider;

    public BCDataManager(Entity provider) {
        super();
        this.provider = provider;
        this.syncedDataList = Lists.newArrayList();
        this.nonSyncedDataList = Lists.newArrayList();
    }

    public static <T> EntityDataReference<T> attribute(Class<? extends Entity> accessor,
                                                       RawEntityDataReference<T> rawRef) {
        List<EntityDataReference<?>> list;
        if (REFERENCES.containsKey(accessor)) {
            list = REFERENCES.get(accessor);
        } else {
            list = Lists.newArrayList();
            REFERENCES.put(accessor, list);
        }
        int currSize = list.size();
        EntityDataReference<T> ref = rawRef.mark(accessor, currSize);
        list.add(ref);
        return ref;
    }

    public <T extends _Persistent<?>> T addNonSyncedData(T data) {
        this.nonSyncedDataList.add(data);
        return data;
    }

    public <T extends _Persistent<?>> T addSyncedData(T data) {
        this.syncedDataList.add(data);
        return data;
    }

    public void writeTag(CompoundTag tag) {
        if (this.provider.level instanceof ServerLevel level) {
            this.nonSyncedDataList.forEach(data -> {
                data.writeToNBT(tag, level);
            });
            this.syncedDataList.forEach(data -> {
                data.writeToNBT(tag, level);
            });
        } //else throw CANNOT_EXECUTE_NBT_OPERATIONS_ON_CLIENT_EX;

    }

    public void readTag(CompoundTag tag) {

            this.nonSyncedDataList.forEach(data -> {
                data.readFromNBT(tag, provider.level);
            });
            this.syncedDataList.forEach(data -> {
                data.readFromNBT(tag, provider.level);
            });
         //else throw CANNOT_EXECUTE_NBT_OPERATIONS_ON_CLIENT_EX;
    }

    public void writePacket(FriendlyByteBuf pct) {
        this.syncedDataList.forEach(data -> {
            data.writeToPacketStream(pct, this.provider.level);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void readPacket(FriendlyByteBuf pct) {
        this.syncedDataList.forEach(data -> {
            data.readFromPacketStream(pct, this.provider.level);
        });
    }
}
