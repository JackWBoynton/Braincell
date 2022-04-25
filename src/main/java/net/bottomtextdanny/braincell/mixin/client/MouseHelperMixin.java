package net.bottomtextdanny.braincell.mixin.client;

import com.mojang.blaze3d.Blaze3D;
import net.bottomtextdanny.braincell.mod.BraincellModules;
import net.bottomtextdanny.braincell.mod._mod.client_sided.events.MouseMovementEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.SmoothDouble;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHelperMixin {
	@Shadow private double accumulatedDX;
	@Shadow private double accumulatedDY;
	@Shadow private boolean mouseGrabbed;
	@Shadow private double lastMouseEventTime = Double.MIN_VALUE;
	private final SmoothDouble bcSmoothTurnX = new SmoothDouble();
	private final SmoothDouble bcSmoothTurnY = new SmoothDouble();

	@Inject(at = @At(value = "HEAD"), method = "turnPlayer", remap = true)
	public void updatePlayerLook(CallbackInfo ci) {
		if (BraincellModules.MOUSE_HOOKS.isActive()) {
			MouseMovementEvent event = new MouseMovementEvent();
			MinecraftForge.EVENT_BUS.post(event);
			bcUpdatePlayerLook(this.accumulatedDX, this.accumulatedDY, this.bcSmoothTurnX, this.bcSmoothTurnY, this.mouseGrabbed, this.lastMouseEventTime, event.computeResultingSensibility());
		}
	}

	private static void bcUpdatePlayerLook(double accumulatedDeltaX, double accumulatedDeltaY, SmoothDouble smoothTurnX, SmoothDouble smoothTurnY, boolean mouseGrabbed, double lastMouseEventTime, double reduction) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player != null && minecraft.player.isAlive()) {

			double time = Blaze3D.getTime();
			double timeDelta = time - lastMouseEventTime;

			if (mouseGrabbed && minecraft.isWindowActive()) {
				int finalMultiplierFactor = 1;
				double sensitivityFactor = minecraft.options.sensitivity * (1 - reduction) * (double) 0.6F + (double)0.2F;
				double easedSensitivityFactor = sensitivityFactor * sensitivityFactor * sensitivityFactor;
				double movementFactor = easedSensitivityFactor * 8.0D;
				double deltaXMovement;
				double deltaYMovement;
				if (minecraft.options.smoothCamera) {
					double smoothedDeltaXMovement = smoothTurnX.getNewDeltaValue(accumulatedDeltaX * movementFactor, timeDelta * movementFactor);
					double smoothedDeltaYMovement = smoothTurnY.getNewDeltaValue(accumulatedDeltaY * movementFactor, timeDelta * movementFactor);
					deltaXMovement = smoothedDeltaXMovement;
					deltaYMovement = smoothedDeltaYMovement;
				} else if (minecraft.options.getCameraType().isFirstPerson() && minecraft.player.isScoping()) {
					smoothTurnX.reset();
					smoothTurnY.reset();
					deltaXMovement = accumulatedDeltaX * easedSensitivityFactor;
					deltaYMovement = accumulatedDeltaY * easedSensitivityFactor;
				} else {
					smoothTurnX.reset();
					smoothTurnY.reset();
					deltaXMovement = accumulatedDeltaX * movementFactor;
					deltaYMovement = accumulatedDeltaY * movementFactor;
				}

				if (minecraft.options.invertYMouse) {
					finalMultiplierFactor = -1;
				}

				minecraft.player.turn(-deltaXMovement, -deltaYMovement * (double)finalMultiplierFactor);
			}
		}
	}
}
