package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.mod.world.item_utilities.CustomSweepAnimation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin {
	
	@Inject(at = @At(value = "HEAD"),
			method = "sweepAttack",
			remap = true,
			cancellable = true)
	public void spawnSweepParticles(CallbackInfo ci) {
		Item mainItem = ((Player)(Object)this).getItemInHand(InteractionHand.MAIN_HAND).getItem();
		if (mainItem instanceof CustomSweepAnimation) {
			((CustomSweepAnimation) mainItem).sweepParticleHook((Player) (Object) this);
			ci.cancel();
		}
	}
}
