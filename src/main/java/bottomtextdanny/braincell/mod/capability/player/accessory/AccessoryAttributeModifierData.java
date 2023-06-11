package bottomtextdanny.braincell.mod.capability.player.accessory;

import bottomtextdanny.braincell.base.CompressedBooleanGroup;
import bottomtextdanny.braincell.base.pair.Pair;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class AccessoryAttributeModifierData {
   public final List modifierIterable;
   private final CompressedBooleanGroup usedModifiedAttributes;
   private final CompressedBooleanGroup usedModifiedLesserAttributes;
   private final double[] modifierValues;
   private final float[] lesserModifierValues;

   private AccessoryAttributeModifierData(List modifierIterable, CompressedBooleanGroup modifiedAttributes, CompressedBooleanGroup modifiedLesserAttribute, double[] attributeModifierBaseValues, float[] lesserAttributeModifierBaseValues) {
      this.modifierIterable = modifierIterable;
      this.usedModifiedAttributes = modifiedAttributes;
      this.usedModifiedLesserAttributes = modifiedLesserAttribute;
      this.modifierValues = attributeModifierBaseValues;
      this.lesserModifierValues = lesserAttributeModifierBaseValues;
   }

   public static AccessoryAttributeModifierData create(List modifiers, List lesserModifiers) {
      double[] newModifiedValues = new double[ModifierType.values().length];
      float[] newLesserModifiedValues = new float[MiniAttribute.size()];
      CompressedBooleanGroup newModifiedAttributes = CompressedBooleanGroup.create(ModifierType.values().length);
      CompressedBooleanGroup newModifiedLesserAttributes = CompressedBooleanGroup.create(MiniAttribute.size());
      LinkedList newModifierList = Lists.newLinkedList();
      Iterator var7 = modifiers.iterator();

      Pair miniAttributeData;
      while(var7.hasNext()) {
         miniAttributeData = (Pair)var7.next();
         ModifierType modifier = (ModifierType)miniAttributeData.left();
         double modifierValue = (Double)miniAttributeData.right();
         int idx = modifier.ordinal();
         newModifierList.add(modifier);
         newModifiedValues[idx] = modifierValue;
         newModifiedAttributes.set(idx, true);
      }

      var7 = lesserModifiers.iterator();

      while(var7.hasNext()) {
         miniAttributeData = (Pair)var7.next();
         MiniAttribute attribute = (MiniAttribute)miniAttributeData.left();
         float modifierValue = (Float)miniAttributeData.right();
         int idx = attribute.getIndex();
         newLesserModifiedValues[idx] = modifierValue;
         newModifiedLesserAttributes.set(idx, true);
      }

      return new AccessoryAttributeModifierData(ImmutableList.copyOf(newModifierList), newModifiedAttributes, newModifiedLesserAttributes, newModifiedValues, newLesserModifiedValues);
   }

   public boolean containsModifier(ModifierType modifierType) {
      return this.usedModifiedAttributes.get(modifierType.ordinal());
   }

   public boolean containsLesserModifier(MiniAttribute attribute) {
      return this.usedModifiedLesserAttributes.get(attribute.getIndex());
   }

   public double getBaseValue(ModifierType modifierType) {
      return this.modifierValues[modifierType.ordinal()];
   }

   public float getBaseValue(MiniAttribute attribute) {
      return this.lesserModifierValues[attribute.getIndex()];
   }
}
