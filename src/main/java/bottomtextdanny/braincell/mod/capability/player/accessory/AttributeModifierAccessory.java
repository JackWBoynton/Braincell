package bottomtextdanny.braincell.mod.capability.player.accessory;

import java.util.List;

public interface AttributeModifierAccessory extends IAccessory {
   void populateModifierData(List var1, List var2);

   default double getModifierBase(ModifierType modifierType) {
      return ((AccessoryAttributeModifierData)AccessoryKey.getAttributeData().get(this.getKey())).getBaseValue(modifierType);
   }

   default boolean isModifierActive(ModifierType modifierType) {
      return ((AccessoryAttributeModifierData)AccessoryKey.getAttributeData().get(this.getKey())).containsModifier(modifierType);
   }

   default float getLesserModifierBase(MiniAttribute modifierType) {
      return ((AccessoryAttributeModifierData)AccessoryKey.getAttributeData().get(this.getKey())).getBaseValue(modifierType);
   }

   default boolean isLesserModifierActive(MiniAttribute modifierType) {
      return ((AccessoryAttributeModifierData)AccessoryKey.getAttributeData().get(this.getKey())).containsLesserModifier(modifierType);
   }

   default double getFinalSpeedMultiplier() {
      return this.getModifierBase(ModifierType.MOVEMENT_SPEED_MLT) / 100.0 + 1.0;
   }

   default double getFinalAttackSpeedAddition() {
      return this.getModifierBase(ModifierType.ATTACK_SPEED_ADD);
   }

   default double getFinalAttackDamageAddition() {
      return this.getModifierBase(ModifierType.ATTACK_DAMAGE_ADD);
   }

   default double getFinalAttackKnockbackAddition() {
      return this.getModifierBase(ModifierType.ATTACK_KNOCKBACK_ADD);
   }

   default double getFinalKnockbackResistanceMultiplier() {
      return this.getModifierBase(ModifierType.KNOCKBACK_RESISTANCE_MLT);
   }

   default double getFinalKnockbackResistanceAddition() {
      return this.getModifierBase(ModifierType.KNOCKBACK_RESISTANCE_ADD);
   }

   default double getFinalArmorAddition() {
      return this.getModifierBase(ModifierType.ARMOR_ADD);
   }

   default double getFinalLuckAddition() {
      return this.getModifierBase(ModifierType.LUCK_ADD);
   }

   default float getFinalLesserAttributeValue(MiniAttribute attribute) {
      return this.getLesserModifierBase(attribute);
   }

   default List modifiedAttributes() {
      return ((AccessoryAttributeModifierData)AccessoryKey.getAttributeData().get(this.getKey())).modifierIterable;
   }
}
