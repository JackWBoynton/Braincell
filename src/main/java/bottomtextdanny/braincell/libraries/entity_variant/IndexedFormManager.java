package bottomtextdanny.braincell.libraries.entity_variant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class IndexedFormManager {
    private final List<? extends Form<?>> formList;
    private final Map<Form<?>, Integer> indexMap;

    public IndexedFormManager(List<? extends Form<?>> list) {
        super();
        this.formList = list;
        this.indexMap = Maps.newIdentityHashMap();
        int size = this.formList.size();
        for (int i = 0; i < size; i++) {
            this.indexMap.put(this.formList.get(i), i);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Form<?> getForm(int key) {
        if (key >= 0 && key < this.formList.size()) return this.formList.get(key);
        if (!this.formList.isEmpty()) return this.formList.get(0);
        return null;
    }

    public int getKey(Form<?> form) {
        if (this.indexMap.containsKey(form)) return this.indexMap.get(form);
        return 0;
    }

    public int getSize() {
        return formList.size();
    }

    public static class Builder {
        private final List<Form<?>> formList = Lists.newArrayList();

        private Builder() {
            super();
        }

        public Builder add(Form<?> form) {
            this.formList.add(form);
            return this;
        }

        public IndexedFormManager create() {
            return new IndexedFormManager(this.formList);
        }
    }
}
