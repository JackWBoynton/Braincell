package net.bottomtextdanny.braincell.mod._base.entity.psyche.input;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public final class UnbuiltActionInputs {
    private final Map<ActionInputKey<?>, Object> inputMap = Maps.newIdentityHashMap();

    public <T> void put(ActionInputKey<T> inputKey, T input) {
        this.inputMap.put(inputKey, input);
    }

    public Map<ActionInputKey<?>, Object> getInputMap() {
        return ImmutableMap.copyOf(this.inputMap);
    }
}
