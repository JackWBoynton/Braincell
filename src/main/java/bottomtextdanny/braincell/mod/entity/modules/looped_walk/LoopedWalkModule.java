package bottomtextdanny.braincell.mod.entity.modules.looped_walk;

import bottomtextdanny.braincell.BraincellHelper;
import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.mod.entity.modules.EntityModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LoopedWalkModule extends EntityModule<LivingEntity, LoopedWalkProvider> {
    public float limbSwingLoop;
    public float prevLimbSwingLoop;
    public float prevRenderLimbSwingAmount;
    public float renderLimbSwingAmount;

    public LoopedWalkModule(LivingEntity entity) {
        super(entity);
    }

    public void tick() {
        if (!this.entity.isInvisible()) {
            float limbSwingDistance = (float) BraincellHelper.RENDER_DIST2D_UTIL.start(this.entity.getX(), this.entity.getZ()).get(this.entity.xo, this.entity.zo);

            this.prevRenderLimbSwingAmount = this.renderLimbSwingAmount;
            this.renderLimbSwingAmount += limbSwingDistance - this.renderLimbSwingAmount > 0 ?
                    (limbSwingDistance - this.renderLimbSwingAmount) / 1.5
                    : (limbSwingDistance - this.renderLimbSwingAmount) / 4;

            this.prevLimbSwingLoop = this.limbSwingLoop;

            this.limbSwingLoop = BCMath.loop(this.limbSwingLoop, 1.0F, this.renderLimbSwingAmount * this.provider.getLoopWalkMultiplier());

            BlockPos blockpos = this.entity.getOnPos();
            BlockState blockstate = this.entity.level.getBlockState(blockpos);

            this.provider.lSSPlayer().getLogic(this.entity, blockstate, this.limbSwingLoop);
        }
    }
}
