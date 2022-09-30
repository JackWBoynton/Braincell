package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.entity.EntityParams;
import bottomtextdanny.braincell.libraries._minor.entity.ModuleProvider;
import bottomtextdanny.braincell.libraries.entity_animation.animations.Animation;
import bottomtextdanny.braincell.tables.BCEntityKeys;
import com.google.common.collect.Lists;
import bottomtextdanny.braincell.libraries._minor.entity.EntityModule;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class BaseAnimatableModule {
    private final AnimationGetter animations;
    private final ArrayList<AnimationHandler> animationHandlersIndexed;

    public BaseAnimatableModule(EntityType<?> entityType) {
        if (EntityParams.has(entityType, BCEntityKeys.ANIMATIONS.get())) {
            this.animations = EntityParams.get(entityType, BCEntityKeys.ANIMATIONS.get());
        } else {
            Braincell.common().logger.error("Cannot initialize animation getter without {} parameter found in {}", BCEntityKeys.ANIMATIONS.getKey(), Registry.ENTITY_TYPE.getKey(entityType));
            this.animations = null;
        }
        this.animationHandlersIndexed = Lists.newArrayList();
    }

    public <E extends Entity & BaseAnimatableProvider<?>> void tick(E entity) {
        if (!entity.isRemoved()) {

            animationHandlerList().forEach(handler -> {
                Animation<?> animation = handler.getAnimation();

                if (animation != Animation.NULL) {
                    if (animation.goal(handler.getTick(), handler)) {
                        entity.animationEndCallout(handler, animation);
                        handler.deactivate();
                    } else {
                        handler.update(animation.progressTick(handler.getTick(), handler));
                    }
                }
            });
        }
    }

    public AnimationGetter animationManager() {
        return this.animations;
    }

    public List<AnimationHandler> animationHandlerList() {
        return this.animationHandlersIndexed;
    }
}
