package bottomtextdanny.braincell.mod._mod.common_sided;

import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public record EntityAttributeDeferror(Supplier wrappedType, Supplier rawAttributes) {
   public EntityAttributeDeferror(Supplier wrappedType, Supplier rawAttributes) {
      this.wrappedType = wrappedType;
      this.rawAttributes = rawAttributes;
   }

   public void register(EntityAttributeCreationEvent event) {
      event.put((EntityType)this.wrappedType.get(), ((AttributeSupplier.Builder)this.rawAttributes.get()).build());
   }

   public Supplier wrappedType() {
      return this.wrappedType;
   }

   public Supplier rawAttributes() {
      return this.rawAttributes;
   }
}
