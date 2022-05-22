package bottomtextdanny.braincell.mod.capability;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;

public abstract class CapabilityWrap<C extends CapabilityWrap<C,T>,T> implements INBTSerializable<CompoundTag>, ICapabilityProvider {
    private final ImmutableList<CapabilityModule<T,C>> moduleImmutableList;
    private final T holder;

    public CapabilityWrap(T holder) {
        super();
        this.holder = holder;
        ImmutableList.Builder<CapabilityModule<T,C>> moduleListBuilder = ImmutableList.builder();
        populateModuleList(moduleListBuilder);
        ImmutableList<CapabilityModule<T,C>> modules = moduleListBuilder.build();
        this.moduleImmutableList = modules.isEmpty() ? ImmutableList.of() : modules;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (!this.moduleImmutableList.isEmpty()) {
            this.moduleImmutableList.forEach(module -> module.serializeNBT(tag));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (!this.moduleImmutableList.isEmpty()) {
            this.moduleImmutableList.forEach(module -> module.deserializeNBT(nbt));
        }
    }

    protected abstract void populateModuleList(ImmutableList.Builder<CapabilityModule<T,C>> moduleList);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == getToken() ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    public <CAP extends CapabilityModule<T, C>> CAP getModule(Class<CAP> clazz) {
        return (CAP)null;
    }

    public T getHolder() {
        return this.holder;
    }

    protected abstract Capability<?> getToken();
}
