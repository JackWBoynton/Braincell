package bottomtextdanny.braincell.libraries.model_animation.interpreter;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public record AnimationInterpreterData(List<String> jointIdentifiers,
                                       List<Map<Float, List<AnimationInstruction>>> runners) {
    public static AnimationInterpreterData DUMMY = new AnimationInterpreterData(List.of(), List.of(Map.of()));
}
