package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.data.DataRequires;
import bottomtextdanny.braincell.libraries.chart.steppy.data.ReadableLevelArgument;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.BooleanStep;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCStepDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class LevelBooleanSteps {

    @DataRequires({ReadableLevelArgument.class})
    public record InBiome() implements BooleanStep {

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            if (preceding.containsOfType(BCStepDataTypes.TAG_KEY.get())
                    && preceding.containsOfType(BCStepDataTypes.VEC3.get())) {
                data.cast(ReadableLevelArgument.class).readableLevel().getBiome(new BlockPos(preceding.getOrDefault(BCStepDataTypes.VEC3.get())))
                        .is((TagKey<Biome>) preceding.getOrDefault(BCStepDataTypes.TAG_KEY.get()));
            }
            return false;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
