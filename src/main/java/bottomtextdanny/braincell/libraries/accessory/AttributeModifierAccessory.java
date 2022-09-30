package bottomtextdanny.braincell.libraries.accessory;

import bottomtextdanny.braincell.base.pair.Pair;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public interface AttributeModifierAccessory extends IAccessory {

    List<AccessoryAttributeModifier> modifiers();

    double getModifier(AccessoryAttributeModifier modifierType, AttributeModifier.Operation operation);
}
