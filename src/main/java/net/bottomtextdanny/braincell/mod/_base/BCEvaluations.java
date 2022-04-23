package net.bottomtextdanny.braincell.mod._base;

import net.bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import net.bottomtextdanny.braincell.mod.capability.player.BCPlayerCapability;
import net.bottomtextdanny.braincell.mod.capability.player.accessory.MiniAttribute;
import net.bottomtextdanny.braincell.mod.capability.player.accessory.ModifierType;
import net.bottomtextdanny.braincell.mod._base.capability.CapabilityHelper;
import net.bottomtextdanny.braincell.base.evaluation.Evaluation;
import net.bottomtextdanny.braincell.base.evaluation.EvaluationTuple;
import net.minecraft.world.entity.player.Player;

public final class BCEvaluations {
    public static EvaluationTuple<BCAccessoryModule, ModifierType, Double> MODIFIER_VALUE = EvaluationTuple.create("Accessory Attribute Modifier", (accessoryModule, modifierType) -> 0.0, list -> {
        list.add(eval -> {
            double[] aDouble = {eval.get()};
            if (eval.getEvaluated1().baseValue == 1.0) {
                eval.getEvaluated0().getOrCreateAccessoryAttributeModifiers(eval.getEvaluated1()).forEach(accessory -> aDouble[0] += eval.getEvaluated1().valueGetter.apply(accessory) - 1.0);
            } else {
                eval.getEvaluated0().getOrCreateAccessoryAttributeModifiers(eval.getEvaluated1()).forEach(accessory -> aDouble[0] += eval.getEvaluated1().valueGetter.apply(accessory));
            }
            eval.set(aDouble[0]);
        });
        return list;
    });

    public static EvaluationTuple<BCAccessoryModule, MiniAttribute, Float> LESSER_MODIFIER_VALUE = EvaluationTuple.create("Accessory Lesser Attribute Modifier", (accessoryModule, miniAttribute) -> 0.0F, list -> {
        list.add(eval -> {
            float[] aFloat = {eval.get()};

            eval.getEvaluated0().getAttributeModifierAccessoryList().forEach(accessory -> {
                float localMod = accessory.getFinalLesserAttributeValue(eval.getEvaluated1());
                aFloat[0] += localMod;
            });

            eval.set(aFloat[0]);
        });
        return list;
    });

    public static Evaluation<Player, Float> EXTRA_JUMP_FORWARD = Evaluation.create("Extra Jump Forward", player -> 0.0F, list -> {
        list.add(eval -> {
            if (eval.getEvaluated().level.isClientSide) {
                eval.stopEvaluation();
            } else {
                float[] aFloat = {eval.get()};
                CapabilityHelper.get(eval.getEvaluated(), BCPlayerCapability.TOKEN).getAccessories()
                        .getOrCreateAccessoryAttributeModifiers(ModifierType.MOVEMENT_SPEED_MLT)
                        .forEach(accessory -> aFloat[0] += accessory.getFinalSpeedMultiplier());

                eval.set(aFloat[0]);
            }
        });
        return list;
    });

    public static void loadClass() {}

    private BCEvaluations() {}
}
