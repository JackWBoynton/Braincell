package bottomtextdanny.braincell.mod._base.serialization.util;

import bottomtextdanny.braincell.mod._base.serialization.EntityDataSerializer;
import bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import bottomtextdanny.braincell.mod._base.serialization.SimpleSerializer;
import bottomtextdanny.braincell.mod._base.serialization.WorldDataSerializer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class H_EntityDataParser {
   public static void writeDataToPacket(FriendlyByteBuf stream, SerializerMark serializer, Object object, Level level) {
      if (serializer instanceof SimpleSerializer simple) {
         simple.writePacketStream(stream, object);
      } else if (serializer instanceof WorldDataSerializer worldData) {
         worldData.writePacketStream(stream, level, object);
      } else {
         if (!(serializer instanceof EntityDataSerializer)) {
            throw writeUnsupportedSerializerException(serializer.getClass(), serializer.key());
         }

         EntityDataSerializer entityData = (EntityDataSerializer)serializer;
         entityData.writePacketStream(stream, level, object);
      }

   }

   public static Object readDataFromPacket(FriendlyByteBuf stream, SerializerMark serializer, Object baseObj, Level level) {
      if (serializer instanceof SimpleSerializer simple) {
         return simple.readPacketStream(stream);
      } else if (serializer instanceof WorldDataSerializer worldData) {
         return worldData.readPacketStream(stream, level);
      } else if (serializer instanceof EntityDataSerializer entityData) {
         return entityData.readPacketStream(stream, baseObj, level);
      } else {
         throw readUnsupportedSerializerException(serializer.getClass(), serializer.key());
      }
   }

   public static void writeDataToNBT(CompoundTag nbt, SerializerMark serializer, String storageKey, Object object, ServerLevel level) {
      if (serializer instanceof SimpleSerializer simple) {
         simple.writeNBT(nbt, object, storageKey);
      } else {
         if (!(serializer instanceof WorldDataSerializer)) {
            throw writeUnsupportedSerializerException(serializer.getClass(), serializer.key());
         }

         WorldDataSerializer worldData = (WorldDataSerializer)serializer;
         worldData.writeNBT(nbt, object, level, storageKey);
      }

   }

   @Nullable
   public static Object readDataFromNBT(CompoundTag nbt, SerializerMark serializer, Object baseObj, String storageKey, ServerLevel level) {
      if (serializer instanceof SimpleSerializer simple) {
         return simple.readNBT(nbt, storageKey);
      } else if (serializer instanceof WorldDataSerializer worldData) {
         return worldData.readNBT(nbt, level, storageKey);
      } else if (serializer instanceof EntityDataSerializer entityData) {
         return entityData.readNBT(nbt, baseObj, level, storageKey);
      } else {
         throw readUnsupportedSerializerException(serializer.getClass(), serializer.key());
      }
   }

   public static UnsupportedOperationException readUnsupportedSerializerException(Class serializerClass, ResourceLocation serializerKey) {
      String message = "Parser can only read entity image, world image and simple image serializers, tried to read: " + serializerClass.getSimpleName() + ", for serializer: " + serializerKey.toString() + '.';
      return new UnsupportedOperationException(message);
   }

   public static UnsupportedOperationException writeUnsupportedSerializerException(Class serializerClass, ResourceLocation serializerKey) {
      String message = "Parser can only write from entity image, world image and simple image serializers, tried to write from: " + serializerClass.getSimpleName() + ", for serializer: " + serializerKey.toString() + '.';
      return new UnsupportedOperationException(message);
   }
}
