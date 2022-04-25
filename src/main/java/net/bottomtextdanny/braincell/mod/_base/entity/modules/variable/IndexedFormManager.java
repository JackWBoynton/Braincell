package net.bottomtextdanny.braincell.mod._base.entity.modules.variable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class IndexedFormManager {
    private final List<? extends Form<?>> formList;
    private final Map<Form<?>, Integer> indexMap;

    private IndexedFormManager(List<? extends Form<?>> keyFormBimap) {
        super();
        this.formList = keyFormBimap;
        this.indexMap = Maps.newIdentityHashMap();
        int size = this.formList.size();
        for (int i = 0; i < size; i++) {
            this.indexMap.put(this.formList.get(i), i);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public Form<?> getForm(int key) {
        if (key >= 0 && key < this.formList.size()) return this.formList.get(key);
        if (!this.formList.isEmpty()) return this.formList.get(0);
        return null;
    }

    public int getKey(Form<?> form) {
        if (this.indexMap.containsKey(form)) return this.indexMap.get(form);
        return 0;
    }

    public static class Builder {
        private final List<Form<?>> keyFormBimap = Lists.newArrayList();

        private Builder() {
            super();
        }

        public Builder add(Form<?> form) {
            this.keyFormBimap.add(form);
            return this;
        }

        public IndexedFormManager create() {
            return new IndexedFormManager(this.keyFormBimap);
        }
    }
}
