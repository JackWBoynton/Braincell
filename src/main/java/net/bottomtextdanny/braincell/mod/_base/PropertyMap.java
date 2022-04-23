package net.bottomtextdanny.braincell.mod._base;

import net.bottomtextdanny.braincell.Braincell;
import net.minecraft.world.level.block.state.properties.Property;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PropertyMap {
    private static final Map<String, Property<?>> ENTRIES = new ConcurrentHashMap<>(1024);

    public static void processClass(String namespace, Class<?> clazz) {
        ENTRIES.clear();
        int ch = isValidNamespace(namespace);

        if (ch != -1) {
            Braincell.common().logger.error("namespace \"{}\" has invalid character at index {}", namespace, ch);
        }

        for (Field field : clazz.getFields()) {
            try {
                if (field.get(null) instanceof Property<?> property) {;
                    ENTRIES.put(namespace + ':' + field.getName().toLowerCase(), property);
                }
            } catch (IllegalAccessException ignored) {}
        }
    }

    public static <T extends Property<?>> T getProperty(String key) {
        return (T)ENTRIES.get(key);
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

    private PropertyMap() {}
}
