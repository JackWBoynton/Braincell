package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.mod.entity.modules.looped_walk.LoopedWalkProvider;
import bottomtextdanny.braincell.mod.entity.modules.additional_motion.ExtraMotionProvider;
import bottomtextdanny.braincell.mod.entity.modules.animatable.LivingAnimatableModule;
import bottomtextdanny.braincell.mod.entity.modules.animatable.LivingAnimatableProvider;
import bottomtextdanny.braincell.mod.entity.modules.motion_util.MotionUtilProvider;
import bottomtextdanny.braincell.mod.entity.modules.variable.Form;
import bottomtextdanny.braincell.mod.entity.modules.variable.VariantProvider;
import bottomtextdanny.braincell.mod.network.stc.MSGTrivialEntityActions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Shadow
	public abstract float getSpeed();

	@Shadow
	public float flyingSpeed;

	@Shadow
	public abstract boolean shouldDiscardFriction();

	@Shadow
	public abstract void calculateEntityAnimation(LivingEntity p_21044_, boolean p_21045_);

	@Shadow
	public abstract Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 p_21075_, float p_21076_);

	@Shadow
	public abstract boolean hasEffect(MobEffect p_21024_);

	@Shadow
	protected abstract SoundEvent getFallDamageSound(int p_21313_);

	@Shadow
	public abstract boolean isFallFlying();

	@Shadow
	public abstract boolean isEffectiveAi();

	@Shadow
	@Nullable
	public abstract AttributeInstance getAttribute(Attribute p_21052_);

	@Shadow
	@Final
	private static AttributeModifier SLOW_FALLING;

	@Shadow
	protected abstract boolean isAffectedByFluids();

	@Shadow
	protected abstract float getWaterSlowDown();

	@Shadow
	public abstract boolean canStandOnFluid(FluidState p_204042_);

	@Shadow
	public abstract boolean onClimbable();

	@Shadow
	public abstract Vec3 getFluidFallingAdjustedMovement(double p_20995_, boolean p_20996_, Vec3 p_20997_);

	@Shadow
	@Nullable
	public abstract MobEffectInstance getEffect(MobEffect p_21125_);

	@Shadow
	protected abstract float getFrictionInfluencedSpeed(float p_21331_);

	@Shadow
	protected abstract Vec3 handleOnClimbable(Vec3 p_21298_);

	@Shadow
	protected boolean jumping;

	public LivingEntityMixin(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	@Inject(at = @At(value = "HEAD"), method = "getDimensions", remap = true, cancellable = true)
	public void getDimensionsHook(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
		if (this instanceof VariantProvider provider && provider.operatingVariableModule() && provider.variableModule().isUpdated()) {
			Form<?> form = provider.variableModule().getForm();

			if (form != null) {
				EntityDimensions formDimensions = form.boxSize();
				if (formDimensions != null) {
					cir.setReturnValue(form.boxSize());
				}
			}
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "tick", remap = true)
	public void tickHook(CallbackInfo ci) {
		if (this instanceof LoopedWalkProvider provider) {
			if (provider.operateWalkModule()) {
				provider.loopedWalkModule().tick();
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "travel", remap = true, cancellable = true)
	public void travelHook(Vec3 vec, CallbackInfo ci) {

		Vec3 additional = Vec3.ZERO;
		float multiplier = 1.0F;

		if (this instanceof ExtraMotionProvider extra
				&& extra.operateExtraMotionModule()) {
			additional = extra.extraMotionModule().getAdditionalMotion();
			extra.extraMotionModule().travelHook();
		}

		if (this instanceof MotionUtilProvider util) {
			multiplier = util.inputMovementMultiplier();
			util.setInputMovementMultiplier(1.0F);
		}

		if (additional != Vec3.ZERO || multiplier != 1.0F) {

			LivingEntity asEntity = (LivingEntity) (Object) this;

			if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
				double d0;

				AttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());

				boolean flag = this.getDeltaMovement().y <= 0.0D;
				if (flag && this.hasEffect(MobEffects.SLOW_FALLING)) {
					if (!gravity.hasModifier(SLOW_FALLING)) gravity.addTransientModifier(SLOW_FALLING);
					this.resetFallDistance();
				} else if (gravity.hasModifier(SLOW_FALLING)) {
					gravity.removeModifier(SLOW_FALLING);
				}
				d0 = gravity.getValue();

				FluidState fluidstate = this.level.getFluidState(this.blockPosition());
				if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
					double d9 = this.getY();
					float f4 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
					float f5 = 0.02F;
					float f6 = (float) EnchantmentHelper.getDepthStrider(asEntity);
					if (f6 > 3.0F) {
						f6 = 3.0F;
					}

					if (!this.onGround) {
						f6 *= 0.5F;
					}

					if (f6 > 0.0F) {
						f4 += (0.54600006F - f4) * f6 / 3.0F;
						f5 += (this.getSpeed() - f5) * f6 / 3.0F;
					}
					if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
						f4 = 0.96F;
					}
					f5 *= (float) this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
					this.moveRelative(f5, vec.scale(multiplier));
					this.move(MoverType.SELF, this.getDeltaMovement().add(additional));
					Vec3 vec36 = this.getDeltaMovement();
					if (this.horizontalCollision && this.onClimbable()) {
						vec36 = new Vec3(vec36.x, 0.2D, vec36.z);
					}
					this.setDeltaMovement(vec36.multiply((double) f4, (double) 0.8F, (double) f4));
					Vec3 vec32 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
					this.setDeltaMovement(vec32);
					if (this.horizontalCollision && this.isFree(vec32.x, vec32.y + (double) 0.6F - this.getY() + d9, vec32.z)) {
						this.setDeltaMovement(vec32.x, (double) 0.3F, vec32.z);
					}
				} else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
					double d8 = this.getY();
					this.moveRelative(0.02F, vec.scale(multiplier));
					this.move(MoverType.SELF, this.getDeltaMovement().add(additional));
					if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
						this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, (double) 0.8F, 0.5D));
						Vec3 vec33 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
						this.setDeltaMovement(vec33);
					} else {
						this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
					}
					if (!this.isNoGravity()) {
						this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
					}
					Vec3 vec34 = this.getDeltaMovement();
					if (this.horizontalCollision && this.isFree(vec34.x, vec34.y + (double) 0.6F - this.getY() + d8, vec34.z)) {
						this.setDeltaMovement(vec34.x, (double) 0.3F, vec34.z);
					}
				} else if (this.isFallFlying()) {
					Vec3 vec3 = this.getDeltaMovement();
					if (vec3.y > -0.5D) {
						this.fallDistance = 1.0F;
					}
					Vec3 vec31 = this.getLookAngle();
					float f = this.getXRot() * ((float) Math.PI / 180F);
					double d1 = Math.sqrt(vec31.x * vec31.x + vec31.z * vec31.z);
					double d3 = vec3.horizontalDistance();
					double d4 = vec31.length();
					double d5 = Math.cos((double) f);
					d5 = d5 * d5 * Math.min(1.0D, d4 / 0.4D);
					vec3 = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + d5 * 0.75D), 0.0D);
					if (vec3.y < 0.0D && d1 > 0.0D) {
						double d6 = vec3.y * -0.1D * d5;
						vec3 = vec3.add(vec31.x * d6 / d1, d6, vec31.z * d6 / d1);
					}
					if (f < 0.0F && d1 > 0.0D) {
						double d10 = d3 * (double) (-Mth.sin(f)) * 0.04D;
						vec3 = vec3.add(-vec31.x * d10 / d1, d10 * 3.2D, -vec31.z * d10 / d1);
					}
					if (d1 > 0.0D) {
						vec3 = vec3.add((vec31.x / d1 * d3 - vec3.x) * 0.1D, 0.0D, (vec31.z / d1 * d3 - vec3.z) * 0.1D);
					}
					this.setDeltaMovement(vec3.multiply((double) 0.99F, (double) 0.98F, (double) 0.99F));
					this.move(MoverType.SELF, this.getDeltaMovement().add(additional));
					if (this.horizontalCollision && !this.level.isClientSide) {
						double d11 = this.getDeltaMovement().horizontalDistance();
						double d7 = d3 - d11;
						float f1 = (float) (d7 * 10.0D - 3.0D);
						if (f1 > 0.0F) {
							this.playSound(this.getFallDamageSound((int) f1), 1.0F, 1.0F);
							this.hurt(DamageSource.FLY_INTO_WALL, f1);
						}
					}
					if (this.onGround && !this.level.isClientSide) {
						this.setSharedFlag(7, false);
					}
				} else {
					BlockPos blockpos = this.getBlockPosBelowThatAffectsMyMovement();
					float f2 = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getFriction(level, this.getBlockPosBelowThatAffectsMyMovement(), this);
					float f3 = this.onGround ? f2 * 0.91F : 0.91F;
					Vec3 vec35 = this.handleRelativeFrictionAndCalculateMovement(vec, f2);
					double d2 = vec35.y;
					if (this.hasEffect(MobEffects.LEVITATION)) {
						d2 += (0.05D * (double) (this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2D;
						this.resetFallDistance();
					} else if (this.level.isClientSide && !this.level.hasChunkAt(blockpos)) {
						if (this.getY() > (double) this.level.getMinBuildHeight()) {
							d2 = -0.1D;
						} else {
							d2 = 0.0D;
						}
					} else if (!this.isNoGravity()) {
						d2 -= d0;
					}
					if (this.shouldDiscardFriction()) {
						this.setDeltaMovement(vec35.x, d2, vec35.z);
					} else {
						this.setDeltaMovement(vec35.x * (double) f3, d2 * (double) 0.98F, vec35.z * (double) f3);
					}
				}
			}
			this.calculateEntityAnimation(asEntity, this instanceof FlyingAnimal);
			ci.cancel();
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "handleRelativeFrictionAndCalculateMovement", remap = true, cancellable = true)
	public void handleNormalMotionHook(Vec3 vec, float friction, CallbackInfoReturnable<Vec3> cir) {

		Vec3 additional = Vec3.ZERO;
		float multiplier = 1.0F;

		if (this instanceof ExtraMotionProvider extra
				&& extra.operateExtraMotionModule()) {
			additional = extra.extraMotionModule().getAdditionalMotion();
			extra.extraMotionModule().travelHook();
		}

		if (this instanceof MotionUtilProvider util) {
			multiplier = util.inputMovementMultiplier();
			util.setInputMovementMultiplier(1.0F);
		}

		if (additional != Vec3.ZERO || multiplier != 1.0F) {
			moveRelative(getFrictionInfluencedSpeed(friction * multiplier), vec);
			setDeltaMovement(handleOnClimbable(getDeltaMovement()));
			move(MoverType.SELF, getDeltaMovement().add(additional));
			Vec3 vec3 = getDeltaMovement();
			if ((horizontalCollision || jumping) && (onClimbable() || getFeetBlockState().is(Blocks.POWDER_SNOW) && PowderSnowBlock.canEntityWalkOnPowderSnow(this))) {
				vec3 = new Vec3(vec3.x, 0.2D, vec3.z);
			}

			cir.setReturnValue(vec3);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "tickDeath", remap = true, cancellable = true)
	public void cancelTickDeath(CallbackInfo ci) {
		if (this instanceof LivingAnimatableProvider provider && provider.operateAnimatableModule()) {
			LivingAnimatableModule module = provider.animatableModule();
			if (module.deathHasBegun()) {
				module.tickDeathHook();
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"), method = "handleRelativeFrictionAndCalculateMovement", remap = true)
	public void modifyInputMovement(Vec3 travelVector, float p_21076_, CallbackInfoReturnable<Vec3> cir) {
		if (this instanceof MotionUtilProvider provider) {
			this.setDeltaMovement(getDeltaMovement().subtract(deGetInputVector(travelVector, this.getRelevantMoveFactor(p_21076_), this.getYRot()).scale(provider.inputMovementMultiplier())));
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "actuallyHurt", remap = true)
	public void hurtHook(CallbackInfo ci) {
		if (this instanceof LoopedWalkProvider provider) {
			if (provider.operateWalkModule()) {
				if (this.invulnerableTime == 20) {
					if (provider.hurtLoopLimbSwingFactor() > 0.0F) {
						new MSGTrivialEntityActions(this.getId(), MSGTrivialEntityActions.HIT_EVENT).sendTo(PacketDistributor.TRACKING_CHUNK.with(() -> this.level.getChunkAt(this.blockPosition())));
					}
				}
			}
		}
	}

	private static Vec3 deGetInputVector(Vec3 p_20016_, float p_20017_, float p_20018_) {



		double d0 = p_20016_.lengthSqr();
		if (d0 < 1.0E-7D) {
			return Vec3.ZERO;
		} else {
			Vec3 vec3 = (d0 > 1.0D ? p_20016_.normalize() : p_20016_).scale(p_20017_);
			float f = Mth.sin(p_20018_ * ((float)Math.PI / 180F));
			float f1 = Mth.cos(p_20018_ * ((float)Math.PI / 180F));
			return new Vec3(vec3.x * (double)f1 - vec3.z * (double)f, vec3.y, vec3.z * (double)f1 + vec3.x * (double)f);
		}
	}

	private float getRelevantMoveFactor(float p_213335_1_) {
		return this.onGround ? this.getSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : this.flyingSpeed;
	}
}
