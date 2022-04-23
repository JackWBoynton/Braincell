package net.bottomtextdanny.braincell.mod.capability.player.accessory;

import net.bottomtextdanny.braincell.base.pair.Pair;

import java.util.List;

public interface AttributeModifierAccessory extends IAccessory {

    void populateModifierData(List<Pair<ModifierType, Double>> modifierList, List<Pair<MiniAttribute, Float>> lesserModifierList);

    default double getModifierBase(ModifierType modifierType) {
        return AccessoryKey.getAttributeData().get(getKey()).getBaseValue(modifierType);
    }

    default boolean isModifierActive(ModifierType modifierType) {
        return AccessoryKey.getAttributeData().get(getKey()).containsModifier(modifierType);
    }

    default float getLesserModifierBase(MiniAttribute modifierType) {
        return AccessoryKey.getAttributeData().get(getKey()).getBaseValue(modifierType);
    }

    default boolean isLesserModifierActive(MiniAttribute modifierType) {
        return AccessoryKey.getAttributeData().get(getKey()).containsLesserModifier(modifierType);
    }

    default double getFinalSpeedMultiplier() {
        return getModifierBase(ModifierType.MOVEMENT_SPEED_MLT) / 100.0 + 1.0;
    }

    default double getFinalAttackSpeedAddition() {
        return getModifierBase(ModifierType.ATTACK_SPEED_ADD);
    }

    default double getFinalAttackDamageAddition() {
        return getModifierBase(ModifierType.ATTACK_DAMAGE_ADD);
    }

    default double getFinalAttackKnockbackAddition() {
        return getModifierBase(ModifierType.ATTACK_KNOCKBACK_ADD);
    }

    default double getFinalKnockbackResistanceMultiplier() {
        return getModifierBase(ModifierType.KNOCKBACK_RESISTANCE_MLT);
    }

    default double getFinalKnockbackResistanceAddition() {
        return getModifierBase(ModifierType.KNOCKBACK_RESISTANCE_ADD);
    }

    default double getFinalArmorAddition() {
        return getModifierBase(ModifierType.ARMOR_ADD);
    }

    default double getFinalLuckAddition() {
        return getModifierBase(ModifierType.LUCK_ADD);
    }

    default float getFinalLesserAttributeValue(MiniAttribute attribute) {
        return getLesserModifierBase(attribute);
    }

    default List<ModifierType> modifiedAttributes() {
        return AccessoryKey.getAttributeData().get(getKey()).modifierIterable;
    }
}
