package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.base.value_mapper.RandomIntegerMapper;
import bottomtextdanny.braincell.mod.entity.psyche.pos_finder.MobPosProcessor;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class WaterAvoidingRandomStrollAction extends RandomStrollAction {


    public WaterAvoidingRandomStrollAction(PathfinderMob mob, MobPosProcessor posProcessor, RandomIntegerMapper strollTime) {
        super(mob, posProcessor, strollTime);
    }

    @Nullable
    @Override
    protected Vec3 getRandomPosition() {

        if (this.mob.isInWaterOrBubble()) {
            Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7);
            return vec3 == null ? super.getRandomPosition() : vec3;
        } else {
            return LandRandomPos.getPos(this.mob, 10, 7);
        }
    }
}
