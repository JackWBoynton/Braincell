package bottomtextdanny.braincell.mod.entity.modules.variable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Map;
import javax.annotation.Nullable;

public class StringedFormManager {
   private final BiMap keyFormBimap;
   private final String defaultKey;
   private final Form defaultForm;

   private StringedFormManager(String defaultKey, BiMap keyFormBimap) {
      this.keyFormBimap = keyFormBimap;
      if (defaultKey == null) {
         this.defaultKey = "";
         VariantProvider.LOGGER.warn("default key provided is null. an empty string will be used instead");
      } else {
         this.defaultKey = defaultKey;
      }

      if (this.keyFormBimap.containsKey(this.defaultKey)) {
         this.defaultForm = (Form)this.keyFormBimap.get(this.defaultKey);
      } else {
         this.defaultForm = null;
         VariantProvider.LOGGER.warn("supposed default key \"{}\" hasn't been found in variant map.", this.defaultKey);
      }

   }

   public static Builder builder(String defaultKey) {
      return new Builder(defaultKey);
   }

   @Nullable
   public Form getForm(String key) {
      return this.keyFormBimap.containsKey(key) ? (Form)this.keyFormBimap.get(key) : this.defaultForm;
   }

   @Nullable
   public String getKey(Form form) {
      Map map = this.keyFormBimap.inverse();
      if (map.containsKey(form)) {
         map.get(form);
      }

      return null;
   }

   public static class Builder {
      private final BiMap keyFormBimap = HashBiMap.create();
      private final String defaultKey;

      private Builder(String defaultKey) {
         this.defaultKey = defaultKey;
      }

      public Builder putForm(String key, Form form) {
         this.keyFormBimap.put(key, form);
         return this;
      }

      public StringedFormManager create() {
         return new StringedFormManager(this.defaultKey, this.keyFormBimap);
      }
   }
}
