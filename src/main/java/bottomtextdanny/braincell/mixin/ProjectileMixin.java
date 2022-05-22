package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import bottomtextdanny.braincell.mod.capability.player.accessory.MiniAttribute;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {
	@Shadow @Nullable public abstract Entity getOwner();
	private boolean appliedModifiers;
	
	@Inject(at = @At(value = "HEAD"), method = "tick", remap = true)
	public void tryPutModifiers(CallbackInfo ci) {
		if (!this.appliedModifiers && (Object)this instanceof AbstractArrow arrow && getOwner() instanceof Player player) {
			BCAccessoryModule cap = BCCapabilityHelper.accessoryModule(player);
			arrow.setDeltaMovement(arrow.getDeltaMovement().scale(cap.getLesserModifier(MiniAttribute.ARROW_SPEED_MLT) / 100.0F));
			arrow.setBaseDamage((arrow.getBaseDamage() + cap.getLesserModifier(MiniAttribute.ARCHERY_DAMAGE_ADD)) * (cap.getLesserModifier(MiniAttribute.ARCHERY_DAMAGE_MLT) / 100.0F));
            this.appliedModifiers = true;
		}
	}
}
