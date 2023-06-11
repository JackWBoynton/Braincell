package bottomtextdanny.braincell.mod.entity.modules.variable;

import bottomtextdanny.braincell.mod._base.serialization.builtin.BuiltinSerializers;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManager;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.entity.serialization.EntityData;
import bottomtextdanny.braincell.mod.entity.serialization.EntityDataReference;
import bottomtextdanny.braincell.mod.entity.serialization.RawEntityDataReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StringedVariableModule extends VariableModule {
   public static final String NOT_UPDATED = "not_updated";
   public static final EntityDataReference VARIANT_REF;
   private final StringedFormManager formManager;
   private final EntityData formKey;

   public StringedVariableModule(LivingEntity entity, StringedFormManager variantList) {
      super(entity);
      this.formManager = variantList;
      if (entity instanceof BCDataManagerProvider manager) {
         this.formKey = manager.bcDataManager().addSyncedData(EntityData.of(VARIANT_REF));
      } else {
         throw new UnsupportedOperationException("StringedVariableModule needs the holder entity to inherit BCDataManager");
      }
   }

   public Form getForm() {
      return this.formManager.getForm((String)this.formKey.get());
   }

   public void setForm(Form form) {
      this.formKey.set(this.formManager.getKey(form));
   }

   public boolean hasFormTnput() {
      return this.formKey.get() != "not_updated";
   }

   static {
      VARIANT_REF = BCDataManager.attribute(Entity.class, RawEntityDataReference.of(BuiltinSerializers.STRING, () -> {
         return "not_updated";
      }, "variant"));
   }
}
