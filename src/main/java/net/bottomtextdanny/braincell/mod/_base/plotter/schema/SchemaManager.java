package net.bottomtextdanny.braincell.mod._base.plotter.schema;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.base.BCStringUtil;
import net.bottomtextdanny.braincell.mod.PropertyMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.apache.commons.io.IOUtils;

import java.io.StringWriter;
import java.rmi.UnexpectedException;
import java.util.*;

public final class SchemaManager {
    public static final String M_BASE = "ba";
    public static final String M_ENTRIES = "en";
    public static final String M_ENTRY_NAME = "na";
    public static final String M_ENTRY_FLAGS = "fl";
    public static final String M_BLOCK_DATA = "bp";
    public static final String M_PROPERTY_ENTRIES = "prop";
    public static final String M_PROPERTY_ENTRIES_NAME = "id";
    public static final String M_PROPERTY_ENTRIES_VALUES = "val";
    private final Map<ResourceLocation, SchemaMemo> cache = new HashMap<>(1024);
    private final Key key = new Key();
    private MinecraftServer server;

    public SchemaManager() {
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
    }

    private void onServerStart(ServerStartingEvent event) {
        this.server = event.getServer();
        this.cache.forEach((rl, memo) -> {
            parseSchema(rl);
        });
    }

    public SchemaMemo parseSchema(ResourceLocation dir) {
        if (this.server == null) {
            Braincell.common().logger.error("couldn't parse schema because schema manager hasn't attached to a server yet.");
            return null;
        }

        if (!active()) {
            SchemaMemo memo = unbuiltMemo(dir);
            this.cache.put(dir, unbuiltMemo(dir));
            return memo;
        }
        if (this.cache.containsKey(dir) && this.cache.get(dir).isBuilt()) {
            return cache.get(dir);
        } else {
            Schema schema = Schema.DUMMY;
            String file = null;
            JsonObject json = null;

            try {
                StringWriter strBuf = new StringWriter();
                IOUtils.copy(this.server
                        .getResourceManager()
                        .getResource(dir)
                        .getInputStream(), strBuf, "UTF-8");
                file = strBuf.toString();
            } catch (Exception ex) {
                failedParsing(dir, "the file does not exist or cannot be read as text.");
                ex.printStackTrace();
            }

            if (file != null) {
                try {
                    json = GsonHelper.parse(file);
                } catch (Exception ignored) {
                    failedParsing(dir, "the file cannot be parsed as json.");
                }
            }

            if (json != null) {
                try {
                    JsonArray flagEntriesSerial = json.get(M_ENTRIES).getAsJsonArray();
                    JsonArray propertyEntriesSerial = json.get(M_PROPERTY_ENTRIES).getAsJsonArray();
                    JsonArray blockDataArraySerial = json.get(M_BLOCK_DATA).getAsJsonArray();

                    List<SchemaPropertyValueGetter<?>> propertyValueMatrix = Lists.newArrayList();
                    List<FlagsEntry> flagsEntries = Lists.newArrayList();
                    List<BlockPos> schemaBlockPositions = Lists.newArrayList();
                    IntList schemaIndices = new IntArrayList();
                    List<IntList> schemaPropertyValueIndices = Lists.newArrayList();

                    Iterator<JsonElement> propertyEntriesSerialIterator = propertyEntriesSerial.iterator();
                    Iterator<JsonElement> entriesSerialIterator = flagEntriesSerial.iterator();
                    Iterator<JsonElement> blockDataArraySerialIterator = blockDataArraySerial.iterator();

                    while(propertyEntriesSerialIterator.hasNext()) {
                        JsonObject entrySerial = propertyEntriesSerialIterator.next().getAsJsonObject();
                        String name = entrySerial.get(M_PROPERTY_ENTRIES_NAME).getAsString();
                        Property<?> property = PropertyMap.getProperty(name);
                        List<Object> values = Lists.newArrayList();

                        propertyValueMatrix.add(new SchemaPropertyValueGetter<>(property, Collections.unmodifiableList(values)));

                        for (JsonElement element : entrySerial.get(M_PROPERTY_ENTRIES_VALUES).getAsJsonArray()) {
                            String valueString = element.getAsString();
                            values.add(property.getValue(valueString).get());
                        }
                    }

                    while (entriesSerialIterator.hasNext()) {
                        JsonObject entrySerial = entriesSerialIterator.next().getAsJsonObject();
                        JsonArray flagsSerial = entrySerial.get(M_ENTRY_FLAGS).getAsJsonArray();
                        Iterator<JsonElement> flagsIterator = flagsSerial.iterator();
                        IntList flagList = new IntArrayList();
                        IntSet flagSet = new IntOpenHashSet();

                        while (flagsIterator.hasNext()) {
                            int flag = Math.abs(flagsIterator.next().getAsInt());
                            flagList.add(flag);
                            flagSet.add(flag);
                        }

                        flagsEntries.add(new FlagsEntry(
                                entrySerial.get(M_ENTRY_NAME).getAsString(),
                                flagList, flagSet));
                    }

                    while (blockDataArraySerialIterator.hasNext()) {
                        JsonArray blockDataSerial = blockDataArraySerialIterator.next().getAsJsonArray();
                        Iterator<JsonElement> blockDataIterator = blockDataSerial.iterator();
                        IntList propertyValueList = new IntArrayList();

                        int counter = 0;
                        while (blockDataIterator.hasNext()) {
                            int intValue = blockDataIterator.next().getAsInt();
                            if (counter > 3) {
                                propertyValueList.add(intValue);
                            }
                            counter++;
                        }

                        schemaPropertyValueIndices.add(propertyValueList);
                        schemaBlockPositions.add(new BlockPos(
                                blockDataSerial.get(0).getAsInt(),
                                blockDataSerial.get(1).getAsInt(),
                                blockDataSerial.get(2).getAsInt()));
                        schemaIndices.add(blockDataSerial.get(3).getAsInt());
                    }

                    schema = new Schema(Collections.unmodifiableList(propertyValueMatrix), flagsEntries, schemaBlockPositions, schemaPropertyValueIndices, schemaIndices);
                } catch (Exception ex) {
                    failedParsing(dir, "the json file has invalid schema metadata.");
                    ex.printStackTrace();
                }
            }

            SchemaMemo memo = unbuiltMemo(dir);
            cache.put(dir, memo);
            memo.setBuilt(schema, this.key);
            return memo;
        }
    }

    public static SchemaMemo parseStatically(ResourceLocation location) {
        return Braincell.common().getSchemaManager().parseSchema(location);
    }

    private boolean active() {
        return server != null;
    }

    private static SchemaMemo unbuiltMemo(ResourceLocation location) {
        return new SchemaMemo(location);
    }

    private static void failedParsing(ResourceLocation filename, String message) {
        new UnexpectedException(BCStringUtil.params("Couldn't deserialize schema file at {} because {}", filename, message)).printStackTrace();
    }

    public static class Key {
        private Key() {}
    }
}
