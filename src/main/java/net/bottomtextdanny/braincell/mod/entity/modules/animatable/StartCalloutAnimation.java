package net.bottomtextdanny.braincell.mod.entity.modules.animatable;

import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class StartCalloutAnimation<E extends Entity> extends SimpleAnimation {
    private final Consumer<E> startCallOut;

    public StartCalloutAnimation(int duration, Consumer<E> startCallOut) {
        super(duration);
        this.startCallOut = startCallOut;
    }

    @Override
    public void onStart(AnimationHandler<?> handler) {
        this.startCallOut.accept((E)handler.getEntity());
    }
}
