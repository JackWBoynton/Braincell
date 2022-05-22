package bottomtextdanny.braincell.mod.world.builtin_entities;

import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.mod.entity.modules.animatable.*;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManager;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.entity.modules.looped_walk.LoopedWalkModule;
import bottomtextdanny.braincell.mod.entity.modules.looped_walk.LoopedWalkProvider;
import bottomtextdanny.braincell.mod.entity.modules.variable.IndexedVariableModule;
import bottomtextdanny.braincell.mod.entity.modules.variable.VariantProvider;
import bottomtextdanny.braincell.mod.world.entity_utilities.EntityClientMessenger;
import bottomtextdanny.braincell.mod.entity.modules.animatable.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class ModuledMob extends PathfinderMob implements EntityClientMessenger, BCDataManagerProvider, LoopedWalkProvider, LivingAnimatableProvider, VariantProvider {
    private BCDataManager deDataManager;
	public AnimationHandler<ModuledMob> mainHandler;
    @Deprecated
    public IntScheduler livingSoundTimer;
    @Nullable
    protected LoopedWalkModule loopedWalkModule;
    @Nullable
    protected IndexedVariableModule variableModule;
    protected LivingAnimatableModule animatableModule;
	
	public ModuledMob(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        this.livingSoundTimer = IntScheduler.ranged(800, 1200);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.animatableModule = new LivingAnimatableModule(this, getAnimations());
        if (operateAnimatableModule()) {
            this.mainHandler = addAnimationHandler(new AnimationHandler<>(this));
        }

        this.deDataManager = new BCDataManager(this);
        commonInit();
    }

    protected void commonInit() {}

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

    }

    //-----------looped walk module
    @Override
    public LoopedWalkModule loopedWalkModule() {
        return this.loopedWalkModule;
    }

    @Override
    public boolean operateWalkModule() {
        return this.loopedWalkModule != null;
    }

    @Override
    public void playLoopStepSound(BlockPos pos, BlockState blockIn) {
        playStepSound(pos, blockIn, Math.min(loopedWalkModule().renderLimbSwingAmount * 8.0F, 1.0F));
    }

    @Override
    public float getLoopWalkMultiplier() {
        return 0.25F;
    }

    @Override
    public float hurtLoopLimbSwingFactor() {
        return 0.25F;
    }

    //-----------looped walk module

    //-----------animatable module

    public AnimationGetter getAnimations() {
        return null;
    }

    @Override
    public LivingAnimatableModule animatableModule() {
        return this.animatableModule;
    }

    @Nullable
    public Animation<?> getDeathAnimation() {
        return null;
    }

    //-----------animatable module

    //-----------variable module

    @Override
    public IndexedVariableModule variableModule() {
        return this.variableModule;
    }

    //-----------variable module

    @Override
	protected final void registerGoals() {
		super.registerGoals();
        registerExtraGoals();
	}


	
	@Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }
	
	@Override
    public void tick() {
        super.tick();

        this.level.getProfiler().push("BC:dannyBaseTick");
        
        if (!this.level.isClientSide()) {
            if (getLivingSoundTimer().hasEnded()) {
                doLivingSound();
                getLivingSoundTimer().reset();
            } else {
                getLivingSoundTimer().advance();
            }
        }

        this.level.getProfiler().pop();
    }

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return super.getDimensions(poseIn);
	}

	protected void registerExtraGoals() {
	}

    /**
     * Reflection, verify this method each minecraft update
     */
    @Override
    public void knockback(double strength, double ratioX, double ratioZ) {
        net.minecraftforge.event.entity.living.LivingKnockBackEvent event = net.minecraftforge.common.ForgeHooks.onLivingKnockBack(this, (float)strength, ratioX, ratioZ);
        if(event.isCanceled()) return;
        float resistantFactor = (float) (1.0D - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        strength = event.getStrength();
        ratioX = event.getRatioX();
        ratioZ = event.getRatioZ();
        strength *= resistantFactor;
        if (!(strength <= 0.0F)) {
            this.hasImpulse = true;
            Vec3 motionFactor = this.getDeltaMovement();
            //change here //////////////////
            Vec3 knockBackFactor = modifyKnockBack(new Vec3(ratioX, 0, ratioZ).normalize().scale(strength)).add(0.0, 0.4 * resistantFactor, 0.0);
            this.setDeltaMovement(new Vec3(motionFactor.x / 2.0D - knockBackFactor.x, this.onGround ? Math.max(knockBackFactor.y, motionFactor.y) : motionFactor.y, motionFactor.z / 2.0D - knockBackFactor.z));
            ////////////////////////////////
            //was :
            //this.setMotion(vector3d.x / 2.0D - vector3d1.x, this.onGround ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            ////////////////////////////////
        }
    }

    public Vec3 modifyKnockBack(Vec3 knockback) {
        return knockback;
    }

    @Override
    public abstract boolean removeWhenFarAway(double distanceToClosestPlayer);

    @Nullable
    public SoundEvent getLivingSound() {
        return super.getAmbientSound();
    }

    public void playLivingSound() {
        SoundEvent soundevent = this.getLivingSound();
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
    }

    public IntScheduler getLivingSoundTimer() {
        return this.livingSoundTimer;
    }

    public void playStepSound(BlockPos pos, BlockState blockIn, float volume) {
        if (!blockIn.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(pos.above());
            SoundType soundtype = blockstate.is(Blocks.SNOW) ? blockstate.getSoundType(this.level, pos, this) : blockIn.getSoundType(this.level, pos, this);
            this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F * volume, soundtype.getPitch());
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return true;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    /**
     * fired on living sound perform
     */
    public void doLivingSound() {
        playLivingSound();
    }

    @Override
    public BCDataManager bcDataManager() {
        return this.deDataManager;
    }
}
