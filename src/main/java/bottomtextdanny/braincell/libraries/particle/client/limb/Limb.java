/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.limb;

import bottomtextdanny.braincell.libraries.model.BCBox;
import bottomtextdanny.braincell.libraries.model.BCBoxPosition;
import bottomtextdanny.braincell.libraries.particle.ExtraOptions;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record Limb(RenderType renderType, BCBox[] boxes, Vector3f boxOffset) implements ExtraOptions {

	public static Limb make(RenderType type, BCBox... boxes) {
		float xMax = 0;
		float yMax = 0;
		float zMax = 0;
		float xMin = 0;
		float yMin = 0;
		float zMin = 0;

		for (BCBox box : boxes) {
			xMax = Math.max(xMax, box.getPoint(BCBoxPosition.RIGHT_UP_FRONT).x);
			xMin = Math.min(xMin, box.getPoint(BCBoxPosition.LEFT_UP_FRONT).x);
			yMax = Math.max(yMax, box.getPoint(BCBoxPosition.RIGHT_UP_FRONT).y);
			yMin = Math.min(yMin, box.getPoint(BCBoxPosition.LEFT_DOWN_FRONT).y);
			zMax = Math.max(zMax, box.getPoint(BCBoxPosition.RIGHT_UP_BACK).z);
			zMin = Math.min(zMin, box.getPoint(BCBoxPosition.LEFT_DOWN_FRONT).z);
		}

		return new Limb(type, boxes, new Vector3f(
			Mth.lerp(0.5F, xMin, xMax) / 16.0F,
			Mth.lerp(0.5F, yMin, yMax) / 16.0F,
			Mth.lerp(0.5F, zMin, zMax) / 16.0F
		));
	}
}
