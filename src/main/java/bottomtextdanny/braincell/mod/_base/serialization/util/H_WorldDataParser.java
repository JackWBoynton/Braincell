package bottomtextdanny.braincell.mod._base.serialization.util;

import bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import bottomtextdanny.braincell.mod._base.serialization.SimpleSerializer;
import bottomtextdanny.braincell.mod._base.serialization.WorldDataSerializer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class H_WorldDataParser {
   public static void writeDataToPacket(FriendlyByteBuf stream, SerializerMark serializer, Object object, Level level) {
      if (serializer instanceof SimpleSerializer simple) {
         simple.writePacketStream(stream, object);
      } else {
         if (!(serializer instanceof WorldDataSerializer)) {
            throw writeUnsupportedSerializerException(serializer.getClass(), serializer.key());
         }

         WorldDataSerializer worldData = (WorldDataSerializer)serializer;
         worldData.writePacketStream(stream, level, object);
      }

   }

   public static Object readDataFromPacket(FriendlyByteBuf stream, SerializerMark serializer, Level level) {
      if (serializer instanceof SimpleSerializer simple) {
         return simple.readPacketStream(stream);
      } else if (serializer instanceof WorldDataSerializer worldData) {
         return worldData.readPacketStream(stream, level);
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
   public static Object readDataFromNBT(CompoundTag nbt, SerializerMark serializer, String storageKey, ServerLevel level) {
      if (serializer instanceof SimpleSerializer simple) {
         return simple.readNBT(nbt, storageKey);
      } else if (serializer instanceof WorldDataSerializer worldData) {
         return worldData.readNBT(nbt, level, storageKey);
      } else {
         throw readUnsupportedSerializerException(serializer.getClass(), serializer.key());
      }
   }

   public static UnsupportedOperationException readUnsupportedSerializerException(Class serializerClass, ResourceLocation serializerKey) {
      String message = "Parser can only read world image serializers and simple image serializers, tried to read: " + serializerClass.getSimpleName() + ", for serializer: " + serializerKey.toString() + '.';
      return new UnsupportedOperationException(message);
   }

   public static UnsupportedOperationException writeUnsupportedSerializerException(Class serializerClass, ResourceLocation serializerKey) {
      String message = "Parser can only write from world image serializers and simple image serializers, tried to write from: " + serializerClass.getSimpleName() + ", for serializer: " + serializerKey.toString() + '.';
      return new UnsupportedOperationException(message);
   }
}
