package bottomtextdanny.braincell.libraries.serialization;

import bottomtextdanny.braincell.libraries.serialization.builtin.*;
import bottomtextdanny.braincell.events.SerializerLookupBuildingEvent;
import bottomtextdanny.braincell.tables.BuiltinSerializers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import bottomtextdanny.braincell.tables.BCDataSerializers;
import bottomtextdanny.braincell.libraries.serialization.builtin.IntSchedulerSerializer;
import bottomtextdanny.braincell.libraries.serialization.builtin.RangedIntSchedulerSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Map;

public interface SerializerLookup {

    int getIdFromSerializer(DataSerializer<?> serializer);
    
    DataSerializer<?> getSerializerFromId(int id);

    static SerializerLookup createUnbuilt() {
        return new NotBuilt();
    }

    class Built implements SerializerLookup {
        private final Map<ResourceLocation, DataSerializer<?>> serializerLookupMap;
        private final BiMap<Integer, DataSerializer<?>> serializerIdLookupBimap;

        private Built(
                Map<ResourceLocation, DataSerializer<?>> serializerLookup,
                BiMap<Integer, DataSerializer<?>> serializerIdBimap) {
            this.serializerLookupMap = serializerLookup;
            this.serializerIdLookupBimap = serializerIdBimap;
        }

        @Override
        public int getIdFromSerializer(DataSerializer<?> serializer) {
            return this.serializerIdLookupBimap.inverse().get(serializer);
        }

        @Override
        public DataSerializer<?> getSerializerFromId(int id) {
            return this.serializerIdLookupBimap.get(id);
        }
    }

    class NotBuilt implements SerializerLookup {
        public static final UnsupportedOperationException UNBUILT_LOOKUP_EX =
                new UnsupportedOperationException("Cannot execute search operations on unbuilt lookup.");
        private final ImmutableMap.Builder<ResourceLocation, DataSerializer<?>> serializerLookupBuilder =
                ImmutableMap.builder();
        private final ImmutableBiMap.Builder<Integer, DataSerializer<?>> idLookupBuilder =
                ImmutableBiMap.builder();

        private NotBuilt() {
            super();
        }

        private static void bootstrap(
                ImmutableMap.Builder<ResourceLocation, DataSerializer<?>> serializerLookupBuilder,
                ImmutableBiMap.Builder<Integer, DataSerializer<?>> idLookupBuilder,
                MutableInt idCounter) {
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
            serializerLookupBuilder.put(EntityReferenceSerializer.REF, BuiltinSerializers.ENTITY_REFERENCE);
            idLookupBuilder.put(idCounter.getAndIncrement(), BuiltinSerializers.ENTITY_REFERENCE);
        }

        public Built build() {
            final MutableInt idCounter = new MutableInt(0);
            bootstrap(this.serializerLookupBuilder, this.idLookupBuilder, idCounter);
            this.serializerLookupBuilder.put(IntSchedulerSerializer.REF, BCDataSerializers.INT_SCHEDULER);
            this.idLookupBuilder.put(idCounter.getAndIncrement(), BCDataSerializers.INT_SCHEDULER);
            this.serializerLookupBuilder.put(RangedIntSchedulerSerializer.REF, BCDataSerializers.RANGED_INT_SCHEDULER);
            this.idLookupBuilder.put(idCounter.getAndIncrement(), BCDataSerializers.RANGED_INT_SCHEDULER);
            MinecraftForge.EVENT_BUS.post(new SerializerLookupBuildingEvent(
                    this.serializerLookupBuilder, this.idLookupBuilder, idCounter));
            return new Built(this.serializerLookupBuilder.build(), this.idLookupBuilder.build());
        }


        @Override
        public int getIdFromSerializer(DataSerializer<?> serializer) {
            throw UNBUILT_LOOKUP_EX;
        }

        @Override
        public DataSerializer<?> getSerializerFromId(int id) {
            throw UNBUILT_LOOKUP_EX;
        }
    }
}
