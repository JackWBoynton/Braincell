package net.bottomtextdanny.braincell.mod._base.plotter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;

public abstract class SpecialPlotter<DATA extends PlotterData> implements Plotter<DATA> {
    protected static final RandomGenerator NT_RANDOM = new SplittableRandom();
    protected final LevelAccessor level;
    private List<Plotter.ChildTicket> children;
    private final Map<Integer, PlotterIterator<DATA>> iteratorMap;
    private PlotterIterator<DATA> firstIterator;

    public SpecialPlotter(LevelAccessor level) {
        super();
        this.level = level;
        this.children = Lists.newArrayList();
        this.iteratorMap = Maps.newHashMap();
    }

    @Override
    public LevelAccessor level() {
        return this.level;
    }

    public void modify(int priority, PlotterIterator<DATA> plot) {
        if (priority == 0) {
            if (this.firstIterator == null) {
                this.firstIterator = plot;
            } else {
                this.firstIterator = this.firstIterator.andThen(plot);
            }
        } if (this.iteratorMap.containsKey(priority)) {
            this.iteratorMap.put(priority, this.iteratorMap.get(priority).andThen(plot));
        } else {
            this.iteratorMap.put(priority, plot);
        }
    }

    @Override
    public void addChild(Vec3i offset, Plotter<?> other) {
        this.children.add(new ChildTicket(new BlockPos(offset), other));
    }

    @Override
    public void makeFirst(Int2ObjectArrayMap<List<PlotterIteration<?>>> extraIterCollector, BlockPos pos, Rotation rotation) {
        List<ChildTicket> childrenRestoreCopy = new ArrayList<>(this.children);
        this.iteratorMap.forEach((prior, iter) -> {
            if (prior != null) {
                PlotterIteration<?> deferred = new PlotterIteration<>(this, iter, pos, rotation);
                if (!extraIterCollector.containsKey(prior)) {
                    List<PlotterIteration<?>> list = Lists.newLinkedList();
                    list.add(deferred);
                    extraIterCollector.put(prior, list);
                } else {
                    extraIterCollector.get(prior).add(deferred);
                }
            }
        });
        if (this.firstIterator != null) make(this.firstIterator, pos, rotation);
        this.children.forEach(c -> c.child().makeFirst(extraIterCollector, pos.offset(PlotterRotator.rotatedOf(rotation, c.offset())), rotation));
        this.children = childrenRestoreCopy;
    }

    @Override
    public void make(PlotterIterator<DATA> iterator, BlockPos pos, Rotation rotation) {
        makeOwnThing(pos, iterator, rotation);
    }

    public abstract void makeOwnThing(BlockPos pos, PlotterIterator<DATA> iterator, Rotation rotation);
}
