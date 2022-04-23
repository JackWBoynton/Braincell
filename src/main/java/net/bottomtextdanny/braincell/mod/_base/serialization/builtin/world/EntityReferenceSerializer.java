package net.bottomtextdanny.braincell.mod._base.serialization.builtin.world;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.BraincellHelper;
import net.bottomtextdanny.braincell.mod._base.serialization.WorldDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityReferenceSerializer implements WorldDataSerializer<Entity> {
    public static final ResourceLocation REF = new ResourceLocation(Braincell.ID, "entity_reference");

    @Override
    public void writeNBT(CompoundTag nbt, Entity obj, ServerLevel level, String storage) {
        nbt.putUUID(storage, obj != null ? obj.getUUID() : BraincellHelper.EMPTY_UUID);
    }

    @Override
    public Entity readNBT(CompoundTag nbt, ServerLevel level, String storage) {
        Tag tag = nbt.get(storage);
        if (tag == null || tag.getType() != IntArrayTag.TYPE) return null;
        return level.getEntity(NbtUtils.loadUUID(tag));
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Level level, Entity obj) {
        stream.writeInt(obj != null ? obj.getId() : -1);
    }

    @Override
    public Entity readPacketStream(FriendlyByteBuf stream, Level level) {
        final int id = stream.readInt();
        if (id == -1) return null;
        else return level.getEntity(stream.readInt());
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
