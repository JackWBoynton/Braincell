package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.mod.entity.modules.variable.VariableModule;
import bottomtextdanny.braincell.mod.entity.modules.variable.VariantProvider;
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

    @Inject(at = @At(value = "HEAD"),
            method = "finalizeSpawn",
            remap = true
    )
    public void finalizeSpawnHook(ServerLevelAccessor level,
                                  DifficultyInstance difficulty,
                                  MobSpawnType spawnType,
                                  SpawnGroupData spawnGroup,
                                  CompoundTag tag,
                                  CallbackInfoReturnable<SpawnGroupData> cir) {
        tryChooseVariant();
    }

    @Inject(at = @At(value = "TAIL"),
            method = "readAdditionalSaveData",
            remap = true
    )
    public void readAdditionalSaveDataHook(CompoundTag tag, CallbackInfo ci) {
        if (!this.level.isClientSide()) {
            tryChooseVariant();
        }
    }

    public void tryChooseVariant() {
        if (this instanceof VariantProvider provider && provider.operatingVariableModule()) {
            VariableModule module = provider.variableModule();
            Mob entity = ((Mob) (Object) this);

            if (!module.isUpdated()) {
                module.setForm(provider.chooseVariant());
                if (module.getForm() == null) {
                    VariantProvider.LOGGER
                            .error("invalid variant choosen for " + entity.getType().getRegistryName() + " at position " + entity.position());
                } else {
                    module.getForm().applyAttributeBonusesRaw(entity);
                    this.reapplyPosition();
                    this.refreshDimensions();
                }
            }
        }
    }
}
