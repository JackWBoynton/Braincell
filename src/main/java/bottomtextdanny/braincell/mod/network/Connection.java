package bottomtextdanny.braincell.mod.network;

import java.util.function.Supplier;
import net.minecraftforge.fml.loading.FMLLoader;

public final class Connection {
   public static Object makeServerSide(Supplier objectSupplier) {
      return FMLLoader.getDist().isDedicatedServer() ? objectSupplier.get() : null;
   }

   public static Object makeServerSideUnknown(Supplier objectSupplier) {
      return FMLLoader.getDist().isDedicatedServer() ? objectSupplier.get() : null;
   }

   public static Object makeServerSideOrElse(Supplier objectSupplier, Supplier fallbackSupplier) {
      return FMLLoader.getDist().isDedicatedServer() ? objectSupplier.get() : fallbackSupplier.get();
   }

   public static void doServerSide(Runnable action) {
      if (FMLLoader.getDist().isDedicatedServer()) {
         action.run();
      }

   }

   public static Object makeClientSide(Supplier objectSupplier) {
      return FMLLoader.getDist().isClient() ? objectSupplier.get() : null;
   }

   public static Object makeClientSideUnknown(Supplier objectSupplier) {
      return FMLLoader.getDist().isClient() ? objectSupplier.get() : null;
   }

   public static Object makeClientSideOrElse(Supplier objectSupplier, Supplier fallbackSupplier) {
      return FMLLoader.getDist().isClient() ? objectSupplier.get() : fallbackSupplier.get();
   }

   public static void doClientSide(Runnable action) {
      if (FMLLoader.getDist().isClient()) {
         action.run();
      }

   }

   private Connection() {
   }
}
