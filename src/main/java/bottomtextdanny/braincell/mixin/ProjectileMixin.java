package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.libraries.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.libraries.accessory.BCAccessoryModule;
import bottomtextdanny.braincell.tables.BCAttributes;
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
			arrow.setDeltaMovement(arrow.getDeltaMovement().scale(player.getAttributeValue(BCAttributes.ARROW_SPEED_MULTIPLIER.get())));
			arrow.setBaseDamage((arrow.getBaseDamage() * player.getAttributeValue(BCAttributes.ARCHERY_DAMAGE_MULTIPLIER.get())));
            this.appliedModifiers = true;
		}
	}
}
