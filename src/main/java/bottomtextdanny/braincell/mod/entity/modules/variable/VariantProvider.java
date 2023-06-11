package bottomtextdanny.braincell.mod.entity.modules.variable;

import bottomtextdanny.braincell.mod.entity.modules.ModuleProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface VariantProvider extends ModuleProvider {
   Logger LOGGER = LogManager.getLogger("braincell_variant_module");

   VariableModule variableModule();

   default boolean operatingVariableModule() {
      return this.variableModule() != null;
   }

   default Form chooseVariant() {
      if (this.operatingVariableModule()) {
         throw new UnsupportedOperationException("Variant not specified.");
      } else {
         return null;
      }
   }
}
