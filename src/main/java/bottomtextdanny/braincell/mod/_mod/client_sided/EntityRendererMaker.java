package bottomtextdanny.braincell.mod._mod.client_sided;

import java.util.function.Function;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class EntityRendererMaker {
   @OnlyIn(Dist.CLIENT)
   public static EntityRendererProvider makeOf(Function provider) {
      return (context) -> {
         return (EntityRenderer)provider.apply(context);
      };
   }
}
