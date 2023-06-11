package bottomtextdanny.braincell.mod._mod.common_sided.events;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.Event;

public final class BlockEntityPlacementBuildEvent extends Event {
   private final Multimap lookup;

   public BlockEntityPlacementBuildEvent(Multimap lookup) {
      this.lookup = lookup;
   }

   public void put(Class classRef, Iterable placements) {
      this.lookup.putAll(classRef, placements);
   }

   public void put(Class classRef, Block... placements) {
      this.lookup.putAll(classRef, Arrays.stream(placements).toList());
   }

   public Iterable getAllBlocksFromClass(Class classRef) {
      return (Iterable)Registry.BLOCK.stream().filter((block) -> {
         return classRef.isAssignableFrom(block.getClass());
      }).collect(Collectors.toCollection(ArrayList::new));
   }
}
