package bottomtextdanny.braincell.mod.entity.modules.variable;

import bottomtextdanny.braincell.mod._base.serialization.builtin.BuiltinSerializers;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManager;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.entity.serialization.EntityData;
import bottomtextdanny.braincell.mod.entity.serialization.EntityDataReference;
import bottomtextdanny.braincell.mod.entity.serialization.RawEntityDataReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class IndexedVariableModule extends VariableModule {
    public static int NOT_UPDATED = -2;
    public static final EntityDataReference<Integer> VARIANT_REF =
            BCDataManager.attribute(Entity.class,
                    RawEntityDataReference.of(
                            BuiltinSerializers.INTEGER,
                            () -> NOT_UPDATED,
                            VARIANT_TAG)
            );
    private final IndexedFormManager formManager;
    private final EntityData<Integer> formKey;

    public IndexedVariableModule(LivingEntity entity, IndexedFormManager variantList) {
        super(entity);
        this.formManager = variantList;
        if (entity instanceof BCDataManagerProvider manager) {
            this.formKey = manager.bcDataManager().addSyncedData(EntityData.of(VARIANT_REF));
        } else {
            throw new UnsupportedOperationException("IndexedVariableModule needs the holder entity to inherit BCDataManager");
        }
    }

    @Nullable
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
