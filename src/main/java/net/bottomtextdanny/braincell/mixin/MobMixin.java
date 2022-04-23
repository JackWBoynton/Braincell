package net.bottomtextdanny.braincell.mixin;

import net.bottomtextdanny.braincell.mod._base.entity.modules.variable.VariableModule;
import net.bottomtextdanny.braincell.mod._base.entity.modules.variable.VariantProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Mob.class, priority = 100)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(at = @At(value = "HEAD"), method = "finalizeSpawn", remap = true, cancellable = true)
    public void finalizeSpawnHook(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (!this.level.isClientSide()) {
            if (p_21436_ != MobSpawnType.COMMAND) {
                if (this instanceof VariantProvider provider && provider.operatingVariableModule()) {
                    VariableModule module = provider.variableModule();

                    if (!module.isUpdated()) {
                        module.setForm(provider.chooseVariant());
                        module.getForm().applyAttributeBonusesRaw((Mob) (Object) this);
                        this.reapplyPosition();
                        this.refreshDimensions();
                    }
                }
            }
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "readAdditionalSaveData", remap = true, cancellable = true)
    public void readAdditionalSaveDataHook(CompoundTag listtag, CallbackInfo ci) {
        if (!this.level.isClientSide()) {
            if (this instanceof VariantProvider provider && provider.operatingVariableModule()) {
                VariableModule module = provider.variableModule();

                if (!module.isUpdated()) {
                    module.setForm(provider.chooseVariant());
                    module.getForm().applyAttributeBonusesRaw((Mob) (Object) this);
                    this.reapplyPosition();
                    this.refreshDimensions();
                }
            }
        }
    }
}
