package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.libraries.chart._base.BCCustomTerrainProvider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(Beardifier.class)
public class BeardifierMixin {
    private static final AtomicReference<StructurePiece> ref = new AtomicReference<>();

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructurePiece;isCloseToChunk(Lnet/minecraft/world/level/ChunkPos;I)Z"), method = "lambda$forStructuresInChunk$2", remap = true)
    private static boolean getRef(StructurePiece instance, ChunkPos chunkPos, int idk) {
        ref.set(instance);
        return instance.isCloseToChunk(chunkPos, idk);
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectList;add(Ljava/lang/Object;)Z", ordinal = 3), method = "lambda$forStructuresInChunk$2", remap = true)
    private static Object postProcessing(Object par1) {
        StructurePiece piece = ref.get();
        if (piece instanceof BCCustomTerrainProvider prov) {
            return new Beardifier.Rigid(prov.terrainBoundingBox(), prov.terrainAdjustment(), 0);
        }
        return par1;
    }
}
