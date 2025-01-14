package bottomtextdanny.braincell.libraries._minor.entity_data_manager;

import bottomtextdanny.braincell.libraries.entity_variant.VariantProvider;
import bottomtextdanny.braincell.libraries._minor.entity.ModuleProvider;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface BCDataManagerProvider extends ModuleProvider {

    BCDataManager bcDataManager();

    @OnlyIn(Dist.CLIENT)
    default void afterClientDataUpdate() {
        if (this instanceof Mob mob && this instanceof VariantProvider provider && provider.operatingVariableModule() && provider.variableModule().getForm().boxSize() != null) {
            mob.refreshDimensions();
        }
    }
}
