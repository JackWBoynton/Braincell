package bottomtextdanny.braincell.mod._base.animation;

import bottomtextdanny.braincell.base.Easing;
import com.google.common.collect.Maps;
import com.google.gson.*;
import bottomtextdanny.braincell.base.BCStringUtil;
import bottomtextdanny.braincell.base.function.Lazy;
import bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInstruction;
import bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInstructionActor;
import bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInterpreter;
import bottomtextdanny.braincell.mod._base.animation.interpreter.AnimationInterpreterData;
import bottomtextdanny.braincell.mod.rendering.modeling.BCEntityModel;
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
    private static final Map<String, Easing> EASING_PARSER = Util.make(() -> {
        Map<String, Easing> map = Maps.newHashMap();

        map.put("ease_in_sine", Easing.EASE_IN_SINE);
        map.put("ease_out_sine", Easing.EASE_OUT_SINE);
        map.put("ease_in_out_sine", Easing.EASE_IN_OUT_SINE);

        map.put("ease_in_square", Easing.EASE_IN_SQUARE);
        map.put("ease_out_square", Easing.EASE_OUT_SQUARE);
        map.put("ease_in_out_square", Easing.EASE_IN_OUT_SQUARE);

        map.put("ease_in_cubic", Easing.EASE_IN_CUBIC);
        map.put("ease_out_cubic", Easing.EASE_OUT_CUBIC);
        map.put("ease_in_out_cubic", Easing.EASE_IN_OUT_CUBIC);

        map.put("ease_in_quart", Easing.EASE_IN_QUART);
        map.put("ease_out_quart", Easing.EASE_OUT_QUART);
        map.put("ease_in_out_quart", Easing.EASE_IN_OUT_QUART);

        map.put("ease_in_quint", Easing.EASE_IN_QUINT);
        map.put("ease_out_quint", Easing.EASE_OUT_QUINT);
        map.put("ease_in_out_quint", Easing.EASE_IN_OUT_QUINT);

        map.put("bounce_in", Easing.BOUNCE_IN);
        map.put("bounce_out", Easing.BOUNCE_OUT);
        map.put("bounce_in_out", Easing.BOUNCE_IN_OUT);

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
        Easing easing = null;

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

        if (instructionsMetadata.has("easing")) {
            String easingIdentifier = instructionsMetadata.get("easing").getAsString();

            if (EASING_PARSER.containsKey(easingIdentifier)) {
                easing = EASING_PARSER.get(easingIdentifier);
            }
        }

        return new AnimationInstruction(actor, index, vector[0], vector[1], vector[2], easing);
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
