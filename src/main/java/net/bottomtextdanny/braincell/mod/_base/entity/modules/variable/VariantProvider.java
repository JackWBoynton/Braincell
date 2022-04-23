package net.bottomtextdanny.braincell.mod._base.entity.modules.variable;

import net.bottomtextdanny.braincell.mod.entity.modules.ModuleProvider;

public interface VariantProvider extends ModuleProvider {

    VariableModule variableModule();

    default boolean operatingVariableModule() {
        return variableModule() != null;
    }

    default Form<?> chooseVariant() {
        if (operatingVariableModule()) {
            throw new UnsupportedOperationException("Variant not specified.");
        }
        return null;
    }
}
