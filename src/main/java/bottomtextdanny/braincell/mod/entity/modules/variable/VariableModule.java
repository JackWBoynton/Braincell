package bottomtextdanny.braincell.mod.entity.modules.variable;

import bottomtextdanny.braincell.mod._base.serialization.builtin.BuiltinSerializers;
import bottomtextdanny.braincell.mod.entity.modules.EntityModule;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManager;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.entity.serialization.EntityData;
import bottomtextdanny.braincell.mod.entity.serialization.EntityDataReference;
import bottomtextdanny.braincell.mod.entity.serialization.RawEntityDataReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class VariableModule extends EntityModule {
   protected static final String VARIANT_TAG = "variant";
   protected static final String VARIANT_UPDATED_TAG = "varu";
   public static final EntityDataReference VARIANT_UPDATED_REF;
   private final EntityData updated;
   private boolean appliedTransient;

   public VariableModule(LivingEntity entity) {
      super(entity);
      if (entity instanceof BCDataManagerProvider manager) {
         this.updated = manager.bcDataManager().addSyncedData(EntityData.of(VARIANT_UPDATED_REF));
      } else {
         throw new UnsupportedOperationException("VariableModule needs the holder entity to inherit BCDataManager");
      }
   }

   public abstract Form getForm();

   public abstract void setForm(Form var1);

   public boolean isUpdated() {
      return (Boolean)this.updated.get() && this.hasFormTnput();
   }

   public abstract boolean hasFormTnput();

   public boolean appliedChanges() {
      return this.appliedTransient;
   }

   public void setAppliedChanges() {
      this.updated.set(true);
      this.appliedTransient = true;
   }

   static {
      VARIANT_UPDATED_REF = BCDataManager.attribute(Entity.class, RawEntityDataReference.of(BuiltinSerializers.BOOLEAN, () -> {
         return false;
      }, "varu"));
   }
}
