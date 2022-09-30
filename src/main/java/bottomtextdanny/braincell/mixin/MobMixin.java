package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.libraries.entity_variant.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
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
        if (tag == null) tryChooseVariant();
        else tryChooseVariant(tag);
    }

    @Inject(at = @At(value = "TAIL"),
            method = "readAdditionalSaveData",
            remap = true
    )
    public void readAdditionalSaveDataHook(CompoundTag tag, CallbackInfo ci) {
        if (!this.level.isClientSide()) {
            tryChooseVariant(tag);
        }
    }

    public void tryChooseVariant() {
        if (this instanceof VariantProvider provider && provider.operatingVariableModule() && !provider.variableModule().appliedChanges()) {
            VariableModule module = provider.variableModule();
            Mob entity = (Mob) (Object) this;

            if (!module.isUpdated()) {
                if (!module.hasFormTnput()) {
                    module.setForm(provider.chooseVariant());
                }

                if (module.getForm() == null) {
                    VariantProvider.LOGGER
                        .error("invalid variant choosen for " + entity.getType().builtInRegistryHolder().key().location() + " at position " + entity.position());
                } else {
                    boolean fullHealth = entity.getHealth() == entity.getMaxHealth();

                    module.getForm().applyAttributeBonusesRaw(entity);

                    if (fullHealth) {
                        entity.setHealth(entity.getMaxHealth());
                    }
                }
            }

            module.setAppliedChanges();
        }
    }

    public void tryChooseVariant(CompoundTag tag) {
        if (this instanceof VariantProvider provider && provider.operatingVariableModule() && !provider.variableModule().appliedChanges()) {
            VariableModule module = provider.variableModule();
            Mob entity = (Mob) (Object) this;

            if (!module.isUpdated()) {
                if (!module.hasFormTnput()) {
                    Tag variantTag = tag.get(VariableModule.VARIANT_TAG);
                    if (variantTag instanceof StringTag && module instanceof StringedVariableModule stringedModule) {
                        stringedModule.setForm(variantTag.getAsString());
                    } else if (variantTag instanceof IntTag && module instanceof IndexedVariableModule indexedModule) {
                        indexedModule.setForm(((IntTag) variantTag).getAsInt());
                    }

                }

                if (!module.hasFormTnput()) {
                    module.setForm(provider.chooseVariant());
                }

                if (module.getForm() == null) {
                    VariantProvider.LOGGER
                        .error("invalid variant choosen for " + entity.getType().builtInRegistryHolder().key().location() + " at position " + entity.position());
                } else {
                    float health = tag.getFloat("Health");

                    boolean fullHealth = health == entity.getMaxHealth() || !tag.contains("Health") || health <= 0.0F;

                    module.getForm().applyAttributeBonusesRaw(entity);

                    if (fullHealth) {
                        entity.setHealth(entity.getMaxHealth());
                    } else {
                        entity.setHealth(health);
                    }
                }
            }

            module.setAppliedChanges();
        }
    }
}
