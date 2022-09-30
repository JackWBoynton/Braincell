package bottomtextdanny.braincell.libraries.entity_variant;

import bottomtextdanny.braincell.libraries._minor.entity.EntityParams;
import bottomtextdanny.braincell.tables.BCEntityKeys;
import bottomtextdanny.braincell.tables.BuiltinSerializers;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.BCDataManager;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.Persistent;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.EntityDataReference;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization.RawEntityDataReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StringedVariableModule extends VariableModule {
    public static final String NOT_UPDATED = "not_updated";
    public static final EntityDataReference<String> VARIANT_REF =
            BCDataManager.attribute(Entity.class,
                    RawEntityDataReference.of(
                            BuiltinSerializers.STRING,
                            () -> NOT_UPDATED,
                            VARIANT_TAG)
            );
    private final StringedFormManager formManager;
    private final Persistent<String> formKey;

    public StringedVariableModule(LivingEntity entity) {
        super(entity);
        this.formManager = EntityParams.get(entity.getType(), BCEntityKeys.STRINGED_FORMS.get());
        this.formKey = ((BCDataManagerProvider)entity).bcDataManager().addSyncedData(Persistent.of(VARIANT_REF));
    }

    public Form<?> getForm() {
        return this.formManager.getForm(this.formKey.get());
    }

    public String getFormKey() {
        return formKey.get();
    }

    public void setForm(Form<?> form) {
        this.formKey.set(this.formManager.getKey(form));
    }

    public void setForm(String stringId) {
        if (this.formManager.hasForm(stringId)) this.formKey.set(stringId);
    }

    @Override
    public boolean hasFormTnput() {
        return this.formKey.get() != NOT_UPDATED;
    }
}
