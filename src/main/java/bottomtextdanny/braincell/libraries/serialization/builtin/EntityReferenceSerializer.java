package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.entity.EntityReference;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class EntityReferenceSerializer implements DataSerializer<EntityReference> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "entity_ref");
    
    @Override
    public void writeNBT(CompoundTag nbt, EntityReference obj, String storage) {
        nbt.putInt(storage + "_rate", obj.searchRate);
        if (obj.getUuid() == null) {
            if (obj.getContained() != null) nbt.putUUID(storage, obj.getContained().getUUID());
            else nbt.putUUID(storage, EntityReference.DEF);
        } else nbt.putUUID(storage, obj.getUuid());
    }

    @Nullable
    @Override
    public EntityReference readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage) || !nbt.contains(storage + "_rate")) return null;
        return new EntityReference(nbt.getInt(storage + "_rate"), nbt.getUUID(storage));
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, EntityReference obj) {
        stream.writeVarInt(obj.searchRate);
        if (obj.getUuid() == null) {
            if (obj.getContained() != null) stream.writeUUID(obj.getContained().getUUID());
            else stream.writeUUID(EntityReference.DEF);
        } else stream.writeUUID(obj.getUuid());
    }

    @Override
    public EntityReference readPacketStream(FriendlyByteBuf stream) {
        return new EntityReference(stream.readVarInt(), stream.readUUID());
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
