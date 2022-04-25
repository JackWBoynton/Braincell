package net.bottomtextdanny.braincell.mod.entity.psyche.input;

import java.util.Map;

public final class ActionInputs {
    private final Map<ActionInputKey<?>, Object> inputMap;

    public ActionInputs(UnbuiltActionInputs rawInputs) {
        super();
        this.inputMap = rawInputs.getInputMap();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ActionInputKey<T> inputKey) {
        return (T)this.inputMap.getOrDefault(inputKey, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOfDefault(ActionInputKey<T> inputKey) {
        return (T)this.inputMap.getOrDefault(inputKey, inputKey.defaultValue.get());
    }
    
    public boolean containsInput(ActionInputKey<?> key) {
        return this.inputMap.containsKey(key);
    }
}
