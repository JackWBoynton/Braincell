package bottomtextdanny.braincell.mod._mod.common_sided;

import bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import bottomtextdanny.braincell.mod._base.serialization.builtin.BlockPosSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.BooleanSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.BuiltinSerializers;
import bottomtextdanny.braincell.mod._base.serialization.builtin.ByteSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.DirectionSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.DoubleSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.FloatSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.IntegerSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.ItemSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.ItemStackSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.LongSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.ParticleOptionsSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.ResourceLocationSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.ShortSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.StringSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.UUIDSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.Vec2Serializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.Vec3Serializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.world.EntityReferenceSerializer;
import bottomtextdanny.braincell.mod._base.serialization.builtin.world.EntitySerializer;
import bottomtextdanny.braincell.mod._mod.common_sided.events.SerializerLookupBuildingEvent;
import bottomtextdanny.braincell.mod.serialization.BCSerializers;
import bottomtextdanny.braincell.mod.serialization.IntSchedulerSerializer;
import bottomtextdanny.braincell.mod.serialization.RangedIntSchedulerSerializer;
import bottomtextdanny.braincell.mod.serialization.entity.VariableIntSchedulerSerializer;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.mutable.MutableInt;

public interface SerializerLookup {
   int getIdFromSerializer(SerializerMark var1);

   SerializerMark getSerializerFromId(int var1);

   static SerializerLookup createUnbuilt() {
      return new NotBuilt();
   }

   public static class NotBuilt implements SerializerLookup {
      public static final UnsupportedOperationException UNBUILT_LOOKUP_EX = new UnsupportedOperationException("Cannot execute search operations on unbuilt lookup.");
      private final ImmutableMap.Builder serializerLookupBuilder = ImmutableMap.builder();
      private final ImmutableBiMap.Builder idLookupBuilder = ImmutableBiMap.builder();

      private NotBuilt() {
      }

      private static void bootstrap(ImmutableMap.Builder serializerLookupBuilder, ImmutableBiMap.Builder idLookupBuilder, MutableInt idCounter) {
         serializerLookupBuilder.put(BooleanSerializer.REF, BuiltinSerializers.BOOLEAN);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.BOOLEAN);
         serializerLookupBuilder.put(ByteSerializer.REF, BuiltinSerializers.BYTE);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.BYTE);
         serializerLookupBuilder.put(ShortSerializer.REF, BuiltinSerializers.SHORT);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.SHORT);
         serializerLookupBuilder.put(IntegerSerializer.REF, BuiltinSerializers.INTEGER);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.INTEGER);
         serializerLookupBuilder.put(FloatSerializer.REF, BuiltinSerializers.FLOAT);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.FLOAT);
         serializerLookupBuilder.put(DoubleSerializer.REF, BuiltinSerializers.DOUBLE);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.DOUBLE);
         serializerLookupBuilder.put(LongSerializer.REF, BuiltinSerializers.LONG);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.LONG);
         serializerLookupBuilder.put(StringSerializer.REF, BuiltinSerializers.STRING);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.STRING);
         serializerLookupBuilder.put(DirectionSerializer.REF, BuiltinSerializers.DIRECTION);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.DIRECTION);
         serializerLookupBuilder.put(UUIDSerializer.REF, BuiltinSerializers.UUID);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.UUID);
         serializerLookupBuilder.put(Vec2Serializer.REF, BuiltinSerializers.VEC2);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.VEC2);
         serializerLookupBuilder.put(Vec3Serializer.REF, BuiltinSerializers.VEC3);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.VEC3);
         serializerLookupBuilder.put(BlockPosSerializer.REF, BuiltinSerializers.BLOCK_POS);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.BLOCK_POS);
         serializerLookupBuilder.put(ItemStackSerializer.REF, BuiltinSerializers.ITEM_STACK);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.ITEM_STACK);
         serializerLookupBuilder.put(ItemSerializer.REF, BuiltinSerializers.ITEM);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.ITEM);
         serializerLookupBuilder.put(ResourceLocationSerializer.REF, BuiltinSerializers.RESOURCE_LOCATION);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.RESOURCE_LOCATION);
         serializerLookupBuilder.put(ParticleOptionsSerializer.REF, BuiltinSerializers.PARTICLE_OPTIONS);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.PARTICLE_OPTIONS);
         serializerLookupBuilder.put(EntitySerializer.REF, BuiltinSerializers.ENTITY);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.ENTITY);
         serializerLookupBuilder.put(EntityReferenceSerializer.REF, BuiltinSerializers.ENTITY_REFERENCE);
         idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.ENTITY_REFERENCE);
      }

      public Built build() {
         MutableInt idCounter = new MutableInt(0);
         bootstrap(this.serializerLookupBuilder, this.idLookupBuilder, idCounter);
         this.serializerLookupBuilder.put(IntSchedulerSerializer.REF, BCSerializers.INT_SCHEDULER);
         this.idLookupBuilder.put(idCounter.getAndIncrement(), BCSerializers.INT_SCHEDULER);
         this.serializerLookupBuilder.put(RangedIntSchedulerSerializer.REF, BCSerializers.RANGED_INT_SCHEDULER);
         this.idLookupBuilder.put(idCounter.getAndIncrement(), BCSerializers.RANGED_INT_SCHEDULER);
         this.serializerLookupBuilder.put(VariableIntSchedulerSerializer.REF, BCSerializers.VARIABLE_INT_SCHEDULER);
         this.idLookupBuilder.put(idCounter.getAndIncrement(), BCSerializers.VARIABLE_INT_SCHEDULER);
         MinecraftForge.EVENT_BUS.post(new SerializerLookupBuildingEvent(this.serializerLookupBuilder, this.idLookupBuilder, idCounter));
         return new Built(this.serializerLookupBuilder.build(), this.idLookupBuilder.build());
      }

      public int getIdFromSerializer(SerializerMark serializer) {
         throw UNBUILT_LOOKUP_EX;
      }

      public SerializerMark getSerializerFromId(int id) {
         throw UNBUILT_LOOKUP_EX;
      }
   }

   public static class Built implements SerializerLookup {
      private final Map serializerLookupMap;
      private final BiMap serializerIdLookupBimap;

      private Built(Map serializerLookup, BiMap serializerIdBimap) {
         this.serializerLookupMap = serializerLookup;
         this.serializerIdLookupBimap = serializerIdBimap;
      }

      public int getIdFromSerializer(SerializerMark serializer) {
         return (Integer)this.serializerIdLookupBimap.inverse().get(serializer);
      }

      public SerializerMark getSerializerFromId(int id) {
         return (SerializerMark)this.serializerIdLookupBimap.get(id);
      }
   }
}
