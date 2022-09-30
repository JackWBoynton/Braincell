package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.chart.segment.ChartPiece;
import bottomtextdanny.braincell.libraries.chart.segment.VoidPiece;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

public final class BCStructureTypes {
    public static final BCRegistry<StructurePieceType> ENTRIES = new BCRegistry<>();
    public static final RegistryHelper<StructurePieceType> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

    public static final Wrap<StructurePieceType.ContextlessType> VOID = deferContextless("bcvoid", () -> VoidPiece::new);
    public static final Wrap<StructurePieceType.ContextlessType> CHILD_SEGMENT = deferContextless("bctrans", () -> VoidPiece::new);
    public static final Wrap<StructurePieceType.ContextlessType> CHART = deferContextless("chart", () -> ChartPiece::new);

    public static <T extends StructurePieceType.ContextlessType> Wrap<T> deferContextless(String name, Supplier<T> type) {
        return HELPER.defer(name, type);
    }
}
