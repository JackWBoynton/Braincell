package bottomtextdanny.braincell.libraries.mod;

import bottomtextdanny.braincell.libraries.accessory.*;
import bottomtextdanny.braincell.base.evaluation.EvaluationTuple;
import bottomtextdanny.braincell.libraries.capability.CapabilityHelper;
import bottomtextdanny.braincell.base.evaluation.Evaluation;
import bottomtextdanny.braincell.tables.BCAccessoryAttributeModifiers;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public final class BCEvaluations {

    public static Evaluation<Player, Float> EXTRA_JUMP_FORWARD = Evaluation.create("Extra Jump Forward", player -> 0.0F, list -> {
        list.add(eval -> {
            if (eval.getEvaluated().level.isClientSide) {
                eval.stopEvaluation();
            } else {
                float[] aFloat = {eval.get()};
                CapabilityHelper.get(eval.getEvaluated(), BCAccessoryCapability.TOKEN).getAccessories()
                        .getOrCreateAccessoryAttributeModifiers(BCAccessoryAttributeModifiers.MOVEMENT_SPEED.get())
                        .forEach(accessory -> aFloat[0] += accessory.getModifier(BCAccessoryAttributeModifiers.MOVEMENT_SPEED.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));

                eval.set(aFloat[0]);
            }
        });
        return list;
    });

    public static void loadClass() {}

    private BCEvaluations() {}
}
