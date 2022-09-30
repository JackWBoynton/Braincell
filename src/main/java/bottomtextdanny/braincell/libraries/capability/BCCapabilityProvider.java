package bottomtextdanny.braincell.libraries.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BCCapabilityProvider<T extends CapabilityWrap<?, ?>>(Capability<T> token, LazyOptional<T> capability) implements INBTSerializable<CompoundTag>, ICapabilityProvider  {

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == token) {
            return capability.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        T capabilityInst = capability
                .orElseThrow(() -> new IllegalStateException("Couldn't serialize capability because it cant be reached."));

        return capabilityInst.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        T capabilityInst = capability
                .orElseThrow(() -> new IllegalStateException("Couldn't deserialize capability because it cant be reached."));

        capabilityInst.deserializeNBT(nbt);
    }
}
