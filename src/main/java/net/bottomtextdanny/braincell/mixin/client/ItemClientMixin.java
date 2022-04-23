package net.bottomtextdanny.braincell.mixin.client;

import net.bottomtextdanny.braincell.mixin_support.ItemStackClientExtensor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemClientMixin {

    @Inject(at = @At(value = "HEAD"), method = "onUseTick", remap = true)
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack stack, int p_41431_, CallbackInfo ci) {
        assertLivingEntityToItemstack(stack, p_41429_);
    }

    @Inject(at = @At(value = "HEAD"), method = "inventoryTick", remap = true)
    public void inventoryTick(ItemStack stack, Level p_41405_, Entity entity, int p_41407_, boolean p_41408_, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            assertLivingEntityToItemstack(stack, living);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "use", remap = true)
    public void use(Level p_41432_, Player entity, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        assertLivingEntityToItemstack(entity.getItemInHand(hand), entity);
    }

    @Inject(at = @At(value = "HEAD"), method = "hurtEnemy", remap = true)
    public void hurtEnemy(ItemStack stack, LivingEntity living, LivingEntity p_41397_, CallbackInfoReturnable<Boolean> cir) {
        assertLivingEntityToItemstack(stack, living);
    }

    @Inject(at = @At(value = "HEAD"), method = "mineBlock", remap = true)
    public void mineBlock(ItemStack stack, Level p_41417_, BlockState p_41418_, BlockPos p_41419_, LivingEntity living, CallbackInfoReturnable<Boolean> cir) {
        assertLivingEntityToItemstack(stack, living);
    }

    private void assertLivingEntityToItemstack(ItemStack stack, LivingEntity entity) {
        ((ItemStackClientExtensor)(Object)stack).setCachedHolder(entity);
    }
}
