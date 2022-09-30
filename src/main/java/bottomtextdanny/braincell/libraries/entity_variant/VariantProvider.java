package bottomtextdanny.braincell.libraries.entity_variant;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.entity.ModuleProvider;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface VariantProvider extends ModuleProvider {
    Logger LOGGER = LogManager.getLogger(Braincell.ID + "_variant_module");

    VariableModule variableModule();

    default boolean operatingVariableModule() {
        return variableModule() != null;
    }

    default Form<?> chooseVariant() {
        if (operatingVariableModule()) {
            throw new UnsupportedOperationException("Variant not specified | " + Registry.ENTITY_TYPE.getKey(((Entity) this).getType()));
        }
        return null;
    }
}
