package bottomtextdanny.braincell.mod.serialization;

import bottomtextdanny.braincell.mod.serialization.entity.VariableIntSchedulerSerializer;

public final class BCSerializers {
    //*\\*//*\\*//*\\SIMPLE SERIALIZERS//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    public static final IntSchedulerSerializer INT_SCHEDULER = new IntSchedulerSerializer();
    public static final RangedIntSchedulerSerializer RANGED_INT_SCHEDULER = new RangedIntSchedulerSerializer();
    //*\\*//*\\*//*\\SIMPLE SERIALIZERS//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//

    //*\\*//*\\*//*\\WORLD DATA SERIALIZERS\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    //none :p
    //*\\*//*\\*//*\\WORLD DATA SERIALIZERS\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//

    //*\\*//*\\*//*\\ENTITY DATA SERIALIZERS*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    public static final VariableIntSchedulerSerializer VARIABLE_INT_SCHEDULER = new VariableIntSchedulerSerializer();
    //*\\*//*\\*//*\\ENTITY DATA SERIALIZERS*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
}
