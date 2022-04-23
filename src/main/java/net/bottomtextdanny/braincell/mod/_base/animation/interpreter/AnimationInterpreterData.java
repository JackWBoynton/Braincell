package net.bottomtextdanny.braincell.mod._base.animation.interpreter;

import java.util.List;
import java.util.Map;

public record AnimationInterpreterData(List<String> jointIdentifiers,
                                       List<Map<Float, List<AnimationInstruction>>> runners) {
    public static AnimationInterpreterData DUMMY = new AnimationInterpreterData(List.of(), List.of(Map.of()));
}
