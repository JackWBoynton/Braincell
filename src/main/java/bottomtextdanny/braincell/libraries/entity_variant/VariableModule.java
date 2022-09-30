package bottomtextdanny.braincell.libraries.entity_variant;

import bottomtextdanny.braincell.libraries._minor.entity.EntityParams;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.PersistentBoolean;
import bottomtextdanny.braincell.tables.BCEntityKeys;
import bottomtextdanny.braincell.tables.BuiltinSerializers;
import bottomtextdanny.braincell.libraries._minor.entity.EntityModule;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.BCDataManager;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.Persistent;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.EntityDataReference;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.RawEntityDataReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public abstract class VariableModule {
    public static final String VARIANT_TAG = "variant";
    protected static final String VARIANT_UPDATED_TAG = "varu";
    public static final EntityDataReference<Boolean> VARIANT_UPDATED_REF =
            BCDataManager.attribute(Entity.class,
                    RawEntityDataReference.of(
                            BuiltinSerializers.BOOLEAN,
                            () -> false,
                            VARIANT_UPDATED_TAG)
            );
    private final PersistentBoolean updated;
    private boolean appliedTransient;

    public VariableModule(LivingEntity entity) {
        if (entity instanceof BCDataManagerProvider manager) {
            this.updated = manager.bcDataManager().addSyncedData(PersistentBoolean.of(VARIANT_UPDATED_REF));
        } else {
            throw new IllegalStateException("Cannot initialize variable entity without given support to BCDataManagerProvider");
        }
    }

    @Nullable
    public static VariableModule prepareFor(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        if (EntityParams.has(type, BCEntityKeys.STRINGED_FORMS.get())) {
            return new StringedVariableModule(entity);
        } else if (EntityParams.has(type, BCEntityKeys.INDEXED_FORMS.get())) {
            return new IndexedVariableModule(entity);
        }
        return null;
    }

    public abstract Form<?> getForm();

    public abstract void setForm(Form<?> form);

    public boolean isUpdated() {
        return updated.getBoolean() && hasFormTnput();
    }

    public abstract boolean hasFormTnput();

    public boolean appliedChanges() {
        return appliedTransient;
    }

    public void setAppliedChanges() {
        updated.setBoolean(true);
        appliedTransient = true;
    }
}
