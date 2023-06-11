package bottomtextdanny.braincell.mod;

import bottomtextdanny.braincell.Braincell;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.level.block.state.properties.Property;

public final class PropertyMap {
   private static final Map ENTRIES = new ConcurrentHashMap(1024);

   public static void processClass(String namespace, Class clazz) {
      ENTRIES.clear();
      int ch = isValidNamespace(namespace);
      if (ch != -1) {
         Braincell.common().logger.error("namespace \"{}\" has invalid character at index {}", namespace, ch);
      }

      Field[] var3 = clazz.getFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];

         try {
            Object var8 = field.get((Object)null);
            if (var8 instanceof Property property) {
               ENTRIES.put(namespace + ":" + field.getName().toLowerCase(), property);
            }
         } catch (IllegalAccessException var9) {
         }
      }

   }

   public static Property getProperty(String key) {
      return (Property)ENTRIES.get(key);
   }

   public static boolean has(String key) {
      return ENTRIES.containsKey(key);
   }

   public static int size() {
      return ENTRIES.size();
   }

   private static int isValidNamespace(String namespace) {
      for(int i = 0; i < namespace.length(); ++i) {
         if (!validNamespaceChar(namespace.charAt(i))) {
            return i;
         }
      }

      return -1;
   }

   private static boolean validNamespaceChar(char character) {
      return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
   }

   private PropertyMap() {
   }
}
