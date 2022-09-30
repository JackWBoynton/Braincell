package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries._minor.entity.ModuleProvider;
import bottomtextdanny.braincell.libraries.entity_animation.animations.Animation;
import net.minecraft.world.entity.Entity;

public interface BaseAnimatableProvider<B extends BaseAnimatableModule> {

    B animatableModule();

    default boolean operateAnimatableModule() {
        return animatableModule() != null && animatableModule().animationManager() != null;
    }

    default <A extends AnimationHandler> A addAnimationHandler(A module) {
        if (!operateAnimatableModule()) throw new UnsupportedOperationException("Tried to setup an animation handler on deactivated AnimatableModule, entity:" + ((Entity)this).getType().builtInRegistryHolder().key().toString());
        else {
            module.setIndex(animatableModule().animationHandlerList().size(), new BCAnimationToken());
            animatableModule().animationHandlerList().add(module);
        }
        return module;
    }

    default void animationEndCallout(AnimationHandler module, Animation animation) {}
}
