package bottomtextdanny.braincell.mod._base.serialization.util;

import bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import bottomtextdanny.braincell.mod._base.serialization.SimpleSerializer;
import bottomtextdanny.braincell.mod.entity.serialization.EntityDataReference;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class H_DataParser {
   public static void writeDataToPacket(PacketWriteData data) {
      SerializerMark var2 = data.reference.serializer();
      if (var2 instanceof SimpleSerializer simple) {
         simple.writePacketStream(data.stream, data.object);
      } else {
         throw writeUnsupportedSerializerException(data.reference().serializer().getClass(), data.reference.serializer().key());
      }
   }

   public static Object readDataFromPacket(PacketReadData data) {
      SerializerMark var2 = data.reference.serializer();
      if (var2 instanceof SimpleSerializer simple) {
         return simple.readPacketStream(data.stream);
      } else {
         throw readUnsupportedSerializerException(data.reference().serializer().getClass(), data.reference.serializer().key());
      }
   }

   public static void writeDataToNBT(NBTWriteData data) {
      SerializerMark var2 = data.reference.serializer();
      if (var2 instanceof SimpleSerializer simple) {
         simple.writeNBT(data.nbt, data.object, data.reference.storageKey());
      } else {
         throw writeUnsupportedSerializerException(data.reference().serializer().getClass(), data.reference.serializer().key());
      }
   }

   @Nullable
   public static Object readDataFromNBT(NBTReadData data) {
      SerializerMark var2 = data.reference.serializer();
      if (var2 instanceof SimpleSerializer simple) {
         return simple.readNBT(data.nbt, data.reference.storageKey());
      } else {
         throw readUnsupportedSerializerException(data.reference().serializer().getClass(), data.reference.serializer().key());
      }
   }

   public static UnsupportedOperationException readUnsupportedSerializerException(Class serializerClass, ResourceLocation serializerKey) {
      String message = "Parser can only read simple image serializers, tried to read: " + serializerClass.getSimpleName() + ", for serializer: " + serializerKey.toString() + '.';
      return new UnsupportedOperationException(message);
   }

   public static UnsupportedOperationException writeUnsupportedSerializerException(Class serializerClass, ResourceLocation serializerKey) {
      String message = "Parser can only write from simple image serializers, tried to write from: " + serializerClass.getSimpleName() + ", for serializer: " + serializerKey.toString() + '.';
      return new UnsupportedOperationException(message);
   }

   public static record PacketWriteData(FriendlyByteBuf stream, EntityDataReference reference, Object object, Level level) {
      public PacketWriteData(FriendlyByteBuf stream, EntityDataReference reference, Object object, Level level) {
         this.stream = stream;
         this.reference = reference;
         this.object = object;
         this.level = level;
      }

      public FriendlyByteBuf stream() {
         return this.stream;
      }

      public EntityDataReference reference() {
         return this.reference;
      }

      public Object object() {
         return this.object;
      }

      public Level level() {
         return this.level;
      }
   }

   public static record PacketReadData(FriendlyByteBuf stream, EntityDataReference reference, Level level) {
      public PacketReadData(FriendlyByteBuf stream, EntityDataReference reference, Level level) {
         this.stream = stream;
         this.reference = reference;
         this.level = level;
      }

      public FriendlyByteBuf stream() {
         return this.stream;
      }

      public EntityDataReference reference() {
         return this.reference;
      }

      public Level level() {
         return this.level;
      }
   }

   public static record NBTWriteData(CompoundTag nbt, EntityDataReference reference, Object object, ServerLevel level) {
      public NBTWriteData(CompoundTag nbt, EntityDataReference reference, Object object, ServerLevel level) {
         this.nbt = nbt;
         this.reference = reference;
         this.object = object;
         this.level = level;
      }

      public CompoundTag nbt() {
         return this.nbt;
      }

      public EntityDataReference reference() {
         return this.reference;
      }

      public Object object() {
         return this.object;
      }

      public ServerLevel level() {
         return this.level;
      }
   }

   public static record NBTReadData(CompoundTag nbt, EntityDataReference reference, ServerLevel level) {
      public NBTReadData(CompoundTag nbt, EntityDataReference reference, ServerLevel level) {
         this.nbt = nbt;
         this.reference = reference;
         this.level = level;
      }

      public CompoundTag nbt() {
         return this.nbt;
      }

      public EntityDataReference reference() {
         return this.reference;
      }

      public ServerLevel level() {
         return this.level;
      }
   }
}
