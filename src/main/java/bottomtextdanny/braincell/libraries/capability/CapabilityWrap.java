package bottomtextdanny.braincell.libraries.capability;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;

public abstract class CapabilityWrap<C extends CapabilityWrap<C,T>,T> {
    private final T holder;

    public CapabilityWrap(T holder) {
        super();
        this.holder = holder;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {}

    public T getHolder() {
        return this.holder;
    }

    protected abstract Capability<?> getToken();
}
