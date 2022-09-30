/*
 * Copyright Saturday August 27 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.accessory;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public record AccessoryAttributeModifier(Attribute attribute, UUID... uuids) {
}
