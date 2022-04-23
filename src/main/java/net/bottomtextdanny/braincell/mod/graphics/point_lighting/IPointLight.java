package net.bottomtextdanny.braincell.mod.graphics.point_lighting;

import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IPointLight {

    Vec3 color();

    Vec3 position();

    float radius();

    float brightness();

    float lightupFactor();
}
