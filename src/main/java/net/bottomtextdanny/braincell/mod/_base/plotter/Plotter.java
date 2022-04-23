package net.bottomtextdanny.braincell.mod._base.plotter;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;

import java.util.List;

public interface Plotter<DATA extends PlotterData> {

    LevelAccessor level();

    void modify(int priority, PlotterIterator<DATA> plot);

    void addChild(Vec3i offset, Plotter<?> other);

    void makeFirst(Int2ObjectArrayMap<List<PlotterIteration<?>>> extraIterCollector, BlockPos pos, Rotation rotation);

    void make(PlotterIterator<DATA> iterator, BlockPos pos, Rotation rotation);

    static void compute(Plotter<?> plotter, BlockPos pos, Rotation rotation) {
        Int2ObjectArrayMap<List<PlotterIteration<?>>> collector = new Int2ObjectArrayMap<>();
        plotter.makeFirst(collector, pos, rotation);
        collector.forEach((priority, iterationList) -> {
            iterationList.forEach(PlotterIteration::make);
        });
    }

    record ChildTicket(Vec3i offset, Plotter<?> child) {}

    record PlotterIteration<DATA extends PlotterData>(Plotter<DATA> plotter, PlotterIterator<DATA>iterator, BlockPos pos, Rotation rotation) {
        void make() {
            plotter.make(this.iterator, this.pos, this.rotation);
        }
    }
}
