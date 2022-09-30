package bottomtextdanny.braincell.libraries.chart.steppy.iteration;

import bottomtextdanny.braincell.base.pair.Pair;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class TransientData {
    private final HashMap<StepDataType<?>, Object> map;

    private final HashMap<StepDataType<?>, Object> historyAccessor;
    private final LinkedList<Pair<StepDataType<?>, Object>> history;
    private int historyPointer;

    private final BlockPos.MutableBlockPos vec3ToPos = new BlockPos.MutableBlockPos();

    public TransientData(int initialSize, float loadFactor) {
        super();
        map = new HashMap<>(initialSize, loadFactor);
        history = new LinkedList<>();
        historyAccessor = new HashMap<>();
    }

    public <T> void put(StepDataType<? super T> type, T data) {
        Object prev = map.put(type, data);
        if (prev != null && historyAccessor.put(type, prev) == null) {
            historyPointer++;
        }
    }

    public <T> Optional<T> get(StepDataType<T> type) {
        Object data = map.get(type);

        if (data == null) return Optional.empty();

        return Optional.of((T)data);
    }

    public <T> T getOrDefault(StepDataType<T> type) {
        Object data = map.get(type);

        if (data == null) return type.defaultValue();

        return (T)data;
    }

    public <T> T getOrNull(StepDataType<T> type) {
        Object data = map.get(type);

        if (data == null) return null;

        return (T)data;
    }

    public <T> boolean containsOfType(StepDataType<T> type) {
        return map.containsKey(type);
    }

    public int getHistoryPointer() {
        return historyPointer;
    }

    public void uploadCurrentLayerToHistory() {
        historyPointer += historyAccessor.size();

        historyAccessor.forEach((type, obj) -> {
            history.add(Pair.of(type, obj));
        });

        historyAccessor.clear();
    }

    public void resetToHistory(int opsBack) {
        if (!SharedConstants.IS_RUNNING_IN_IDE)
            opsBack = Math.min(opsBack, history.size());

        for (int i = 0; i < opsBack; i++) {
            history.removeLast();
        }
    }

    public BlockPos block(Vec3 vec) {
        vec3ToPos.set(vec.x, vec.y, vec.z);
        return vec3ToPos;
    }

    public void clear() {
        map.clear();
    }
}
