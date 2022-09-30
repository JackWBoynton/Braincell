package bottomtextdanny.braincell.libraries._minor.entity.looped_walk;

import bottomtextdanny.braincell.BraincellHelper;
import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.libraries._minor.entity.EntityModule;
import bottomtextdanny.braincell.libraries.chart.help.Distance2s;
import bottomtextdanny.braincell.libraries.entity_animation.LivingAnimatableModule;
import bottomtextdanny.braincell.libraries.entity_animation.LivingAnimatableProvider;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LoopedWalkModule {
    public float x, xo, z, zo;
    public float limbSwingLoop;
    public float prevLimbSwingLoop;
    public float prevRenderLimbSwingAmount;
    public float renderLimbSwingAmount;

    public LoopedWalkModule() {}

    public <E extends LivingEntity & LoopedWalkProvider> void tick(E entity) {
        if (entity.level.isClientSide() && !entity.isInvisible()) {
//            double xd = entity.xOld - entity.getX();
//            double zd = entity.xOld - entity.getX();
//            float limbSwingDistance = Distance2s.d2Euclid().start(entity.getX(), entity.getZ()).get(entity.xo, entity.zo);
//
//            xo = x;
//            zo = z;
//            x = xd
//
//            this.prevLimbSwingLoopDir = limbSwingLoopDir;
//
//            float renderLimbSwingAmount = this.renderLimbSwingAmount;
//            if (renderLimbSwingAmount > 0.001) {
//                delta = BCMath.rotateY(entity.getYRot() * BCMath.FRAD - BCMath.FPI_HALF, delta);
//                float limbSwingDirection = (float) Mth.atan2(delta.z, delta.x);
//                this.limbSwingLoopDir = DEMath.approachDegrees(0.3F, limbSwingLoopDir, limbSwingDirection);
//            }
//
//            this.prevRenderLimbSwingAmount = renderLimbSwingAmount;
//            this.renderLimbSwingAmount += limbSwingDistance - renderLimbSwingAmount > 0 ?
//                    (limbSwingDistance - renderLimbSwingAmount) / 1.5
//                    : (limbSwingDistance - renderLimbSwingAmount) / 4;
//
//            this.prevLimbSwingLoop = this.limbSwingLoop;
//
//            this.limbSwingLoop = BCMath.loop(this.limbSwingLoop, 1.0F, this.renderLimbSwingAmount * this.provider.getLoopWalkMultiplier());
//
//            BlockPos blockpos = entity.getOnPos();
//            BlockState blockstate = entity.level.getBlockState(blockpos);
//
//            this.provider.lSSPlayer().getLogic(entity, blockstate, this.limbSwingLoop);
//
//            //
            float x = this.x;
            float z = this.z;
            double xd = (entity.xOld - entity.getX());
            double zd = (entity.zOld - entity.getZ());
            double limbSwingDistance = Math.sqrt(xd * xd + zd * zd);
            xo = x;
            zo = z;

            float renderLimbSwingAmount = this.renderLimbSwingAmount;

            if (renderLimbSwingAmount > 0.001 && limbSwingDistance > 0) {
                Vector3f vec = new Vector3f((float) (xd / limbSwingDistance), 0.0F, (float) (zd / limbSwingDistance));

                BCMath.rotateY(entity.getYRot() * BCMath.FRAD, vec);
                this.x = Mth.lerp(0.3F, x, vec.x());
                this.z = Mth.lerp(0.3F, z, -vec.z());
            }

            this.prevRenderLimbSwingAmount = renderLimbSwingAmount;
            this.renderLimbSwingAmount += limbSwingDistance - renderLimbSwingAmount > 0 ?
                (limbSwingDistance - renderLimbSwingAmount) / 1.5
                : (limbSwingDistance - renderLimbSwingAmount) / 4;

            this.prevLimbSwingLoop = this.limbSwingLoop;

            this.limbSwingLoop = BCMath.loop(this.limbSwingLoop, 1.0F, this.renderLimbSwingAmount * entity.getLoopWalkMultiplier());

            BlockPos blockpos = entity.getOnPos();
            BlockState blockstate = entity.level.getBlockState(blockpos);

            entity.lSSPlayer().getLogic(entity, blockstate, this.limbSwingLoop);
        }
    }
}
