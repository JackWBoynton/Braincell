package bottomtextdanny.braincell.mod.entity.modules.variable;

import bottomtextdanny.braincell.mod._base.serialization.builtin.BuiltinSerializers;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManager;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.entity.serialization.EntityData;
import bottomtextdanny.braincell.mod.entity.serialization.EntityDataReference;
import bottomtextdanny.braincell.mod.entity.serialization.RawEntityDataReference;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class IndexedVariableModule extends VariableModule {
   public static int NOT_UPDATED = -1;
   public static final EntityDataReference VARIANT_REF;
   private final IndexedFormManager formManager;
   private final EntityData formKey;

   public IndexedVariableModule(LivingEntity entity, IndexedFormManager variantList) {
      super(entity);
      this.formManager = variantList;
      this.formKey = ((BCDataManagerProvider)entity).bcDataManager().addSyncedData(EntityData.of(VARIANT_REF));
   }

   @Nullable
   public Form getForm() {
      return this.formManager.getForm((Integer)this.formKey.get());
   }

   public void setForm(Form form) {
      this.formKey.set(this.formManager.getKey(form));
   }

   public boolean hasFormTnput() {
      return (Integer)this.formKey.get() != NOT_UPDATED;
   }

   static {
      VARIANT_REF = BCDataManager.attribute(Entity.class, RawEntityDataReference.of(BuiltinSerializers.INTEGER, () -> {
         return NOT_UPDATED;
      }, "variant"));
   }
}
