package bottomtextdanny.braincell.mod.serialization;

import bottomtextdanny.braincell.mod.serialization.entity.VariableIntSchedulerSerializer;

public final class BCSerializers {
   public static final IntSchedulerSerializer INT_SCHEDULER = new IntSchedulerSerializer();
   public static final RangedIntSchedulerSerializer RANGED_INT_SCHEDULER = new RangedIntSchedulerSerializer();
   public static final VariableIntSchedulerSerializer VARIABLE_INT_SCHEDULER = new VariableIntSchedulerSerializer();
}
