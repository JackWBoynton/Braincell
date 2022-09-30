package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart.help.IntBox;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorator;
import bottomtextdanny.braincell.libraries.registry.ContextualSerializer;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCContextualSerializers;
import bottomtextdanny.braincell.tables.BCSerializers;
import bottomtextdanny.braincell.tables.BCStepDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import static bottomtextdanny.braincell.libraries.chart.steppy.Steps.*;

public class BoxSegment extends Segment<BoxSegment.Data> {
    public static final String BOX_LABEL = "box";
    protected final IntBox box;

    public BoxSegment(IntBox box) {
        this.box = box;
    }

    public BoxSegment(CompoundTag root, @Nullable SegmentAdmin admin) {
        super(root, admin);

//        write(BCStepDataTypes.VEC3, levelPosVec())
//                .append(write(BCStepDataTypes.BLOCK_STATE, levelBlockState()))
//                .append(either(
//                        blockStateIsAir().negative()
//                                .and(virtual(
//                                        write(BCStepDataTypes.BLOCK_STATE, levelPosVec().)
//                                )),
//
//                        ));
//

        box = BCSerializers.INT_BOX.get().fromTag(root.get(BOX_LABEL));
    }

    @Override
    public CompoundTag childData() {
        CompoundTag root = super.childData();

        root.put(BOX_LABEL, Serializer.packDirectly(box, BCSerializers.INT_BOX.get()));

        return root;
    }

    public IntBox getBox() {
        return this.box;
    }

    @Override
    public void construct(WorldGenLevel level, SegmentDecorator iterator, BlockPos pos, Rotation rotation) {
        IntBox box = this.box;
        MutableData data = new MutableData(box, level, null, null, null, null, NT_RANDOM);
        BlockPos.MutableBlockPos worldPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos localPos = new BlockPos.MutableBlockPos();

        if (rotation != Rotation.NONE) {
            box.iterate((x, y, z) -> {
                localPos.set(x, y, z);

                ChartRotator.rotate(rotation, localPos);
                worldPos.set(pos.getX() + localPos.getX(),
                        pos.getY() + localPos.getY(),
                        pos.getZ() + localPos.getZ());
                BlockState state = level.getBlockState(worldPos);
                data.setProg(x - box.x1, y - box.y1, z - box.z1);;
                data.setOriginalState(state);
                data.setLocalPos(localPos);
                data.setBlockPos(worldPos);
                iterator.accept(data);
                if (data.state() != null) level.setBlock(worldPos, ChartRotator.rotateBlockState(rotation, data.state()), 2);
            });
        } else {
            box.iterate((x, y, z) -> {
                localPos.set(x, y, z);
                worldPos.set(pos.getX() + localPos.getX(),
                        pos.getY() + localPos.getY(),
                        pos.getZ() + localPos.getZ());
                BlockState state = level.getBlockState(worldPos);
                data.setProg(x - box.x1, y - box.y1, z - box.z1);
                data.setState(state);
                data.setOriginalState(state);
                data.setLocalPos(localPos);
                data.setBlockPos(worldPos);
                iterator.accept(data);
                if (data.state() != null) level.setBlock(worldPos, data.state(), 2);
            });
        }
    }

    @Override
    public BoundingBox getDefaultLocalBoundingBox() {
        return box.asBoundingBox();
    }

    @Override
    public Wrap<? extends ContextualSerializer<?, SegmentAdmin>> serializer() {
        return BCContextualSerializers.BOX_SEGMENT;
    }

    public interface Data extends SegmentData {
        IntBox box();
        int xProg();
        int yProg();
        int zProg();
    }

    private static class MutableData extends MutableSegmentData implements Data {
        private final IntBox box;
        private int xProg;
        private int yProg;
        private int zProg;

        public MutableData(IntBox box, WorldGenLevel level, BlockState state, BlockState originalState, BlockPos blockPos, BlockPos localPos, RandomSource random) {
            super(level, state, originalState, blockPos, localPos, random);
            this.box = box;
        }

        public void setProg(int x, int y, int z) {
            this.xProg = x;
            this.yProg = y;
            this.zProg = z;
        }

        @Override
        public IntBox box() {
            return this.box;
        }

        @Override
        public int xProg() {
            return this.xProg;
        }

        @Override
        public int yProg() {
            return this.yProg;
        }

        @Override
        public int zProg() {
            return this.zProg;
        }
    }
}
