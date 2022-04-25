package net.bottomtextdanny.braincell.mod._base.animation;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.bottomtextdanny.braincell.base.BCStringUtil;
import net.bottomtextdanny.braincell.base.function.Lazy;
import net.bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInstruction;
import net.bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInstructionActor;
import net.bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInterpreter;
import net.bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInterpreterData;
import net.bottomtextdanny.braincell.mod.rendering.modeling.BCEntityModel;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.StringWriter;
import java.rmi.UnexpectedException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AnimationManager {
    private static final Gson JSON_ADAPTER = new GsonBuilder()
            .setLenient().setPrettyPrinting().create();
    private static final Map<String, AnimationInstructionActor> INSTRUCTION_PARSER = Util.make(() -> {
        Map<String, AnimationInstructionActor> map = Maps.newHashMap();

        map.put("rotation", AnimationInstructionActor.ROTATE);
        map.put("position", AnimationInstructionActor.POSITION);
        map.put("scale", AnimationInstructionActor.SCALE);

        return Collections.unmodifiableMap(map);
    });
    private final Map<ResourceLocation, AnimationInterpreterData> cache;

    public AnimationManager() {
        super();
        this.cache = Maps.newHashMapWithExpectedSize(512);
    }

    public AnimationInterpreter makeInterpreter(ResourceLocation location, BCEntityModel<?> model) {
        return new AnimationInterpreter(model, Lazy.of(() -> {

            if (cache.containsKey(location))
                return cache.get(location);

            AnimationInterpreterData newBuiltData = parseData(location);

            if (newBuiltData == null) {
                return AnimationInterpreterData.DUMMY;
            }

            this.cache.put(location, newBuiltData);

            return newBuiltData;
        }));
    }

    @Nullable
    private AnimationInterpreterData parseData(ResourceLocation location) {
        String extracted = resourceInput(new ResourceLocation(location.getNamespace(), "bc_animations/" + location.getPath() + ".json"));

        if (extracted == null) return null;
        JsonObject jsonEquivalent;

        try {
            jsonEquivalent = JSON_ADAPTER.fromJson(extracted, JsonObject.class);
        } catch (Exception ex) {
            failedParsing(location, "the file cannot be read as a json structure.");
            ex.printStackTrace();
            return null;
        }

        if (!jsonEquivalent.isJsonObject()) return null;
        AnimationInterpreterData data = null;

        try {
            data = parseJsonData(jsonEquivalent.getAsJsonArray("animation"));
        } catch(Exception ex) {
            failedParsing(location, "the file cannot be read as animation metadata.");
        }

        return data;
    }

    private AnimationInterpreterData parseJsonData(JsonArray animationMetadata) {
        Map<String, Integer> jointIdentifiersMap = Maps.newHashMap();
        List<Map<Float, List<AnimationInstruction>>> runners = Lists.newArrayList();

        for (JsonElement runnerElement : animationMetadata) {
            JsonObject runnerJson = runnerElement.getAsJsonObject();

            Map<Float, List<AnimationInstruction>> timedInstructions = parseRunner(str -> {
                jointIdentifiersMap.putIfAbsent(str, jointIdentifiersMap.size());
            }, jointIdentifiersMap, runnerJson);

            if (timedInstructions != null)
                runners.add(timedInstructions);
        }

        List<String> jointIdentifiers = jointIdentifiersMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .toList();

        return new AnimationInterpreterData(jointIdentifiers, runners);
    }

    private Map<Float, List<AnimationInstruction>> parseRunner(Consumer<String> nameTester,
                                                               Map<String, Integer> identifiersIndexed,
                                                               JsonObject runnerMetadata) {
        Map<Float, List<AnimationInstruction>> map = Maps.newLinkedHashMap();

        for (Map.Entry<String, JsonElement> entry : runnerMetadata.entrySet()) {
            List<AnimationInstruction> instructions = Lists.newArrayList();
            String frame = entry.getKey();

            for (JsonElement instructionsJson : entry.getValue().getAsJsonArray()) {
                instructions.add(parseInstructions(nameTester, identifiersIndexed, instructionsJson.getAsJsonObject()));
            }

            map.putIfAbsent(Float.parseFloat(frame), instructions);
        }

        return map;
    }

    private AnimationInstruction parseInstructions(Consumer<String> nameTester,
                                                   Map<String, Integer> identifiersIndexed,
                                                   JsonObject instructionsMetadata) {
        List<Map.Entry<String, JsonElement>> elements = instructionsMetadata.entrySet().stream().toList();

        String jointIdentifier = elements.get(0).getValue().getAsString();
        String instructionIdentifier = elements.get(1).getKey();

        nameTester.accept(jointIdentifier);
        AnimationInstructionActor actor = null;
        float[] vector = new float[3];
        int index = identifiersIndexed.get(jointIdentifier);

        if (INSTRUCTION_PARSER.containsKey(instructionIdentifier)) {
            JsonArray vectorMetadata = elements.get(1).getValue().getAsJsonArray();
            actor = INSTRUCTION_PARSER.get(instructionIdentifier);

            int counter = 0;

            for (JsonElement element : vectorMetadata) {
                if (counter >= 3) break;

                vector[counter] = element.getAsFloat();

                counter++;
            }
        }

        return new AnimationInstruction(actor, index, vector[0], vector[1], vector[2]);
    }

    @Nullable
    private String resourceInput(ResourceLocation location) {
        try {
            StringWriter strBuf = new StringWriter();
            IOUtils.copy(Minecraft.getInstance()
                    .getResourceManager()
                    .getResource(location)
                    .getInputStream(), strBuf, "UTF-8");
            return strBuf.toString();
        } catch (Exception ex) {
            failedParsing(location, "the file does not exist or cannot be read as text.");
            ex.printStackTrace();
        }
        return null;
    }

    private static void failedParsing(ResourceLocation filename, String message) {
        new UnexpectedException(BCStringUtil.params("Couldn't deserialize animation file at {} because {}", filename, message)).printStackTrace();
    }
}
