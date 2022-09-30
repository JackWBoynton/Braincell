package bottomtextdanny.braincell.libraries.entity_variant;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StringedFormManager {
    private final BiMap<String, ? extends Form<?>> keyFormBimap;
    private final String defaultKey;
    private final Form<?> defaultForm;

    public StringedFormManager(String defaultKey, BiMap<String, ? extends Form<?>> keyFormBimap) {
        super();
        this.keyFormBimap = keyFormBimap;
        if (defaultKey == null) {
            this.defaultKey = "";
            VariantProvider.LOGGER.warn("default key provided is null. an empty string will be used instead");
        } else this.defaultKey = defaultKey;

        if (this.keyFormBimap.containsKey(this.defaultKey))
            this.defaultForm = this.keyFormBimap.get(this.defaultKey);
        else {
            this.defaultForm = null;
            VariantProvider.LOGGER
                    .warn("supposed default key \"{}\" hasn't been found in variant map.", this.defaultKey);
        }
    }

    public static Builder builder(String defaultKey) {
        return new Builder(defaultKey);
    }

    public Form<?> getForm(String key) {
        if (this.keyFormBimap.containsKey(key)) return this.keyFormBimap.get(key);
        return this.defaultForm;
    }

    @Nullable
    public Form<?> getFormOrNull(String key) {
        if (this.keyFormBimap.containsKey(key)) return this.keyFormBimap.get(key);
        return null;
    }

    public boolean hasForm(String key) {
        return this.keyFormBimap.containsKey(key);
    }

    @Nullable
    public String getKey(Form<?> form) {
        Map<? extends Form<?>, String> map = this.keyFormBimap.inverse();
        if (map.containsKey(form)) map.get(form);
        return null;
    }

    public Iterator<? extends Map.Entry<String, ? extends Form<?>>> iterator() {
        return keyFormBimap.entrySet().iterator();
    }

    public static class Builder {
        private final BiMap<String, Form<?>> keyFormBimap = HashBiMap.create();
        private final String defaultKey;

        private Builder(String defaultKey) {
            super();
            this.defaultKey = defaultKey;
        }

        public Builder putForm(String key, Form<?> form) {
            this.keyFormBimap.put(key, form);
            return this;
        }

        public StringedFormManager create() {
            return new StringedFormManager(this.defaultKey, this.keyFormBimap);
        }
    }
}
