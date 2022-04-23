package net.bottomtextdanny.braincell.mixin;

import net.bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import net.bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import net.bottomtextdanny.braincell.mod.capability.player.accessory.IQueuedJump;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow private double lastGoodY;
	
	@Shadow public ServerPlayer player;
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", shift = At.Shift.BEFORE), method = "handleMovePlayer", remap = true)
	public void de_updateJumpAccessories(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
		BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(this.player);
		double d1 = de_clampVertical(packet.getY(this.player.getY()));
		double d8 = d1 - this.lastGoodY;
		
		boolean flag = d8 > 0.0D;
		
		if (!flag && packet.isOnGround()) {
			if (!accessoryModule.goodOnGround) {
				accessoryModule.getJumpSet().forEach(IQueuedJump::reestablish);
				accessoryModule.goodOnGround = true;
			}
		} else {
			accessoryModule.goodOnGround = false;
		}
	}
	
	private static double de_clampVertical(double p_143654_) {
		return Mth.clamp(p_143654_, -2.0E7D, 2.0E7D);
	}
}
