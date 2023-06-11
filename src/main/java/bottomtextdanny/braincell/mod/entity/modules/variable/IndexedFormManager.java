package bottomtextdanny.braincell.mod.entity.modules.variable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class IndexedFormManager {
   private final List formList;
   private final Map indexMap;

   private IndexedFormManager(List keyFormBimap) {
      this.formList = keyFormBimap;
      this.indexMap = Maps.newIdentityHashMap();
      int size = this.formList.size();

      for(int i = 0; i < size; ++i) {
         this.indexMap.put((Form)this.formList.get(i), i);
      }

   }

   public static Builder builder() {
      return new Builder();
   }

   @Nullable
   public Form getForm(int key) {
      if (key >= 0 && key < this.formList.size()) {
         return (Form)this.formList.get(key);
      } else {
         return !this.formList.isEmpty() ? (Form)this.formList.get(0) : null;
      }
   }

   public int getKey(Form form) {
      return this.indexMap.containsKey(form) ? (Integer)this.indexMap.get(form) : 0;
   }

   public static class Builder {
      private final List keyFormBimap = Lists.newArrayList();

      private Builder() {
      }

      public Builder add(Form form) {
         this.keyFormBimap.add(form);
         return this;
      }

      public IndexedFormManager create() {
         return new IndexedFormManager(this.keyFormBimap);
      }
   }
}
