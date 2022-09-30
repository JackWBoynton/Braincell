package bottomtextdanny.braincell.libraries.entity_variant;

import bottomtextdanny.braincell.libraries._minor.entity.EntityParams;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.PersistentInt;
import bottomtextdanny.braincell.tables.BCEntityKeys;
import bottomtextdanny.braincell.tables.BuiltinSerializers;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.BCDataManager;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.Persistent;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.EntityDataReference;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.RawEntityDataReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class IndexedVariableModule extends VariableModule {
    public static int NOT_UPDATED = -1;
    public static final EntityDataReference<Integer> VARIANT_REF =
            BCDataManager.attribute(Entity.class,
                    RawEntityDataReference.of(
                            BuiltinSerializers.INTEGER,
                            () -> NOT_UPDATED,
                            VARIANT_TAG)
            );
    private final IndexedFormManager formManager;
    private final PersistentInt formKey;

    public IndexedVariableModule(LivingEntity entity) {
        super(entity);
        this.formManager = EntityParams.get(entity.getType(), BCEntityKeys.INDEXED_FORMS.get());
        this.formKey = ((BCDataManagerProvider)entity).bcDataManager().addSyncedData(PersistentInt.of(VARIANT_REF));
    }

    @Nullable
    public Form<?> getForm() {
        return this.formManager.getForm(this.formKey.getInt());
    }

    public int getFormIndex() {
        return formKey.getInt();
    }

    public void setForm(Form<?> form) {
        this.formKey.setInt(this.formManager.getKey(form));
    }

    public void setForm(int form) {
        if (form >= 0 && form < formManager.getSize())
            this.formKey.setInt(form);
    }

    @Override
    public boolean hasFormTnput() {
        return this.formKey.getInt() != NOT_UPDATED;
    }
}
