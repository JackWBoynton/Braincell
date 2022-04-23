package net.bottomtextdanny.braincell.mod.capability.player.accessory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.bottomtextdanny.braincell.base.CompressedBooleanGroup;
import net.bottomtextdanny.braincell.base.pair.Pair;

import java.util.LinkedList;
import java.util.List;

public final class AccessoryAttributeModifierData {
    public final List<ModifierType> modifierIterable;
    private final CompressedBooleanGroup usedModifiedAttributes;
    private final CompressedBooleanGroup usedModifiedLesserAttributes;
    private final double[] modifierValues;
    private final float[] lesserModifierValues;

    private AccessoryAttributeModifierData(List<ModifierType> modifierIterable, CompressedBooleanGroup modifiedAttributes, CompressedBooleanGroup modifiedLesserAttribute, double[] attributeModifierBaseValues, float[] lesserAttributeModifierBaseValues) {
        super();
        this.modifierIterable = modifierIterable;
        this.usedModifiedAttributes = modifiedAttributes;
        this.usedModifiedLesserAttributes = modifiedLesserAttribute;
        this.modifierValues = attributeModifierBaseValues;
        this.lesserModifierValues = lesserAttributeModifierBaseValues;
    }

    public static AccessoryAttributeModifierData create(List<Pair<ModifierType, Double>> modifiers, List<Pair<MiniAttribute, Float>> lesserModifiers) {
        double[] newModifiedValues = new double[ModifierType.values().length];
        float[] newLesserModifiedValues = new float[MiniAttribute.size()];
        CompressedBooleanGroup newModifiedAttributes = CompressedBooleanGroup.create(ModifierType.values().length);
        CompressedBooleanGroup newModifiedLesserAttributes = CompressedBooleanGroup.create(MiniAttribute.size());
        LinkedList<ModifierType> newModifierList = Lists.newLinkedList();

        for (Pair<ModifierType, Double> modifierData : modifiers) {
            ModifierType modifier = modifierData.left();
            double modifierValue = modifierData.right();
            int idx = modifier.ordinal();

            newModifierList.add(modifier);
            newModifiedValues[idx] = modifierValue;
            newModifiedAttributes.set(idx, true);
        }

        for (Pair<MiniAttribute, Float> miniAttributeData : lesserModifiers) {
            MiniAttribute attribute = miniAttributeData.left();
            float modifierValue = miniAttributeData.right();
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
