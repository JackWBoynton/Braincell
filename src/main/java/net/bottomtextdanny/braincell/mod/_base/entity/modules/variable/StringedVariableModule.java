package net.bottomtextdanny.braincell.mod._base.entity.modules.variable;

import net.bottomtextdanny.braincell.mod._base.entity.modules.data_manager.BCDataManager;
import net.bottomtextdanny.braincell.mod._base.entity.modules.data_manager.BCDataManagerProvider;
import net.bottomtextdanny.braincell.mod.entity.serialization.EntityData;
import net.bottomtextdanny.braincell.mod.entity.serialization.EntityDataReference;
import net.bottomtextdanny.braincell.mod.entity.serialization.RawEntityDataReference;
import net.bottomtextdanny.braincell.mod._base.serialization.builtin.BuiltinSerializers;
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
    private final EntityData<String> formKey;

    public StringedVariableModule(LivingEntity entity, StringedFormManager variantList) {
        super(entity);
        this.formManager = variantList;
        if (entity instanceof BCDataManagerProvider manager) {
            this.formKey = manager.bcDataManager().addSyncedData(EntityData.of(VARIANT_REF));
        } else {
            throw new UnsupportedOperationException("StringedVariableModule needs the holder entity to inherit BCDataManager");
        }
    }

    public Form<?> getForm() {
        return this.formManager.getForm(this.formKey.get());
    }

    public void setForm(Form<?> form) {
        this.formKey.set(this.formManager.getKey(form));
    }

    public boolean isUpdated() {
        return this.formKey.get() != NOT_UPDATED;
    }
}
