package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.libraries._minor.entity.looped_walk.LoopedWalkProvider;
import bottomtextdanny.braincell.libraries.entity_animation.BaseAnimatableProvider;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.BCDataManagerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    private boolean injectedMovementOnTravel;

    @Inject(at = @At(value = "TAIL"), method = "tick", remap = true)
    public void tickHook(CallbackInfo ci) {
        if (this instanceof BaseAnimatableProvider provider) {
            if (provider.operateAnimatableModule()) {
                provider.animatableModule().tick((Entity & BaseAnimatableProvider)provider);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "playStepSound", remap = true, cancellable = true)
    public void cancelStepSound(BlockPos positionBelow, BlockState blockStateBelow, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof LoopedWalkProvider provider) {
            if (provider.operateWalkModule()) {
                ci.cancel();
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "playAmethystStepSound", remap = true, cancellable = true)
    public void cancelAmethystStepSound(BlockState blockStateBelow, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof LoopedWalkProvider provider) {
            if (provider.operateWalkModule()) {
                ci.cancel();
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.BEFORE), method = "saveWithoutId", remap = true)
    public void preSaveExtraHook(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (this instanceof BCDataManagerProvider provider) {
            provider.bcDataManager().writeTag(tag);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setYBodyRot(F)V", shift = At.Shift.BEFORE), method = "load", remap = true)
    public void preLoadExtraHook(CompoundTag tag, CallbackInfo ci) {
        if (this instanceof BCDataManagerProvider provider) {
            provider.bcDataManager().readTag(tag);
        }
    }
}
