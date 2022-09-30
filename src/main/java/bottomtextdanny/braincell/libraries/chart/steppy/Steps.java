package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.libraries.chart.help.GridSampler;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.StepDataType;
import bottomtextdanny.braincell.libraries.chart.steppy.step.BooleanStep;
import bottomtextdanny.braincell.libraries.chart.steppy.step.NumberStep;
import bottomtextdanny.braincell.libraries.chart.steppy.step.Step;
import bottomtextdanny.braincell.libraries.chart.steppy.step.Vec3Step;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class Steps {

    public static <T> UtilitySteps.Write<T> write(StepDataType<T> type, Step valueGetter) {
        return new UtilitySteps.Write<>(type, valueGetter);
    }

    public static <T> UtilitySteps.Write<T> write(Wrap<StepDataType<T>> type, Step valueGetter) {
        return new UtilitySteps.Write<>(type.get(), valueGetter);
    }

    public static <T> UtilitySteps.GetTransient<T> getTransient(Wrap<StepDataType<T>> type) {
        return new UtilitySteps.GetTransient<>(type.get());
    }

    public static <T> UtilitySteps.GetTransient<T> getTransient(StepDataType<T> type) {
        return new UtilitySteps.GetTransient<>(type);
    }

    public static UtilitySteps.Virtual virtual(Step step) {
        return new UtilitySteps.Virtual(step);
    }

    public static UtilitySteps.Metadata metaData(int index) {
        return new UtilitySteps.Metadata(index);
    }

    public static UtilitySteps.MetadataStep metaDataStep(int index) {
        return new UtilitySteps.MetadataStep(index);
    }

    public static UtilitySteps.Indices indices(List<Step> steps) {
        return new UtilitySteps.Indices(steps);
    }

    public static UtilitySteps.Null nullify() {
        return UtilitySteps.Null.INSTANCE;
    }

    public static UtilitySteps.Either either(Step conditional,
                                             Step positiveOutcome,
                                             Step negativeOutcome) {
        return new UtilitySteps.Either(conditional, positiveOutcome, negativeOutcome);
    }

    public static UtilitySteps.If onlyOf(Step conditional,
                                         Step positiveOutcome) {
        return new UtilitySteps.If(conditional, positiveOutcome);
    }

    public static UtilitySteps.Picker picker(Step indices, Step number) {
        return new UtilitySteps.Picker(indices, number);
    }

    public static UtilitySteps.ChooseByWeight chooseByWeight() {
        return new UtilitySteps.ChooseByWeight();
    }

    public static BooleanSteps.False boolFalse() {
        return BooleanSteps.False.INSTANCE;
    }

    public static BooleanSteps.True boolTrue() {
        return BooleanSteps.True.INSTANCE;
    }

    public static BooleanSteps.IsNegative prevIsNegative() {
        return BooleanSteps.IsNegative.INSTANCE;
    }

    public static BooleanSteps.NegativeOf negativeOf(Step conditional) {
        return new BooleanSteps.NegativeOf(conditional);
    }

    public static ChartBooleanSteps.ReachesThreshold reachesThreshold(Step numberThreshold) {
        return new ChartBooleanSteps.ReachesThreshold(numberThreshold);
    }

    public static NumberSteps.FixedFloat constFloat(float value) {
        return new NumberSteps.FixedFloat(value);
    }

    public static NumberSteps.FixedInt constInt(int value) {
        return new NumberSteps.FixedInt(value);
    }

    public static NumberSteps.RandomNumber randomNumber(Step minNumber, Step maxNumber) {
        return new NumberSteps.RandomNumber(minNumber, maxNumber);
    }

    public static NumberSteps.Sample sampleNumber(GridSampler sampler) {
        return new NumberSteps.Sample(sampler);
    }

    public static NumberSteps.Addition addNumber(Step addition) {
        return new NumberSteps.Addition(addition);
    }

    public static NumberSteps.Scale scaleNumber(Step scalar) {
        return new NumberSteps.Scale(scalar);
    }

    public static NumberStep modNumber(Step modulo) {
        return new NumberSteps.Mod(modulo);
    }

    public static Vec3Step constVec(Vec3 vector) {
        return new Vec3Steps.Fixed(vector);
    }

    public static Vec3Step constVec(Vec3i vector) {
        return new Vec3Steps.Fixed(new Vec3(vector.getX(), vector.getY(), vector.getZ()));
    }

    public static Vec3Step levelPosVec() {
        return Vec3Steps.LevelArgument.INSTANCE;
    }

    public static Vec3Steps.Offset offsetVec(Step vector) {
        return new Vec3Steps.Offset(vector);
    }

    public static Vec3Steps.Multiply multiplyVec(Step scalarVector) {
        return new Vec3Steps.Multiply(scalarVector);
    }

    public static Vec3Steps.ToHeight vecToHeight(Heightmap.Types type) {
        return new Vec3Steps.ToHeight(type);
    }

    public static BlockStateSteps.Fixed constBlockState(BlockState blockState) {
        return new BlockStateSteps.Fixed(blockState);
    }

    public static BlockStateSteps.FromBlock defaultBlockState(Block blockState) {
        return new BlockStateSteps.FromBlock(blockState);
    }

    public static BlockStateSteps.LevelArgument levelBlockState() {
        return BlockStateSteps.LevelArgument.INSTANCE;
    }

    public static BlockStateSteps.InjectSchemaProperties injectSchemaProperties() {
        return BlockStateSteps.InjectSchemaProperties.INSTANCE;
    }

    public static BooleanStep blockStateIsAir() {
        return ChartBooleanSteps.IS_AIR;
    }

    public static BooleanStep blockStateCanOcclude() {
        return ChartBooleanSteps.CAN_OCCLUDE;
    }

    public static BooleanStep blockStateHasBlockEntity() {
        return ChartBooleanSteps.HAS_BLOCK_ENTITY;
    }

    public static BooleanStep blockStateIsBurning() {
        return ChartBooleanSteps.IS_BURNING;
    }

    public static BooleanStep blockStateIsSuffocating() {
        return ChartBooleanSteps.IS_SUFFOCATING;
    }

    public static BooleanStep blockStateIsViewBlocking() {
        return ChartBooleanSteps.IS_VIEW_BLOCKING;
    }

    public static BooleanStep blockStateCanSurvive() {
        return ChartBooleanSteps.CAN_SURVIVE;
    }

    public static BooleanStep blockStateHasPostProcess() {
        return ChartBooleanSteps.HAS_POST_PROCESS;
    }

    public static ChartBooleanSteps.IsBlock blockStateIsBlock(Block block) {
        return new ChartBooleanSteps.IsBlock(block);
    }

    public static ChartBooleanSteps.IsTag blockStateIsTag(Step tag) {
        return new ChartBooleanSteps.IsTag(tag);
    }

    public static ChartBooleanSteps.IsTag blockStateIsTag(TagKey<Block> tag) {
        return new ChartBooleanSteps.IsTag(new DataSteps.TagKey(tag));
    }
}
