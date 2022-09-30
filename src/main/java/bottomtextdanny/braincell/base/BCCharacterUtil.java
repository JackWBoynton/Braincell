package bottomtextdanny.braincell.base;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class BCCharacterUtil {
    private static final Set<Character> METADATA_SEPARATORS;

    static {
        METADATA_SEPARATORS = new HashSet<>();
        METADATA_SEPARATORS.add('/');
        METADATA_SEPARATORS.add('.');
        METADATA_SEPARATORS.add(':');
        METADATA_SEPARATORS.add(' ');
    }

    public static Predicate<Character> wordSeparator() {
        return ch -> ch == ' ';
    }

    public static Predicate<Character> metadataSeparator() {
        return METADATA_SEPARATORS::contains;
    }

    public static Function<String, String> metadataProcessor() {
        return (string) -> {
            StringBuilder builder = new StringBuilder();
            string.chars().forEach(i -> {
                char ch = (char) i;
                if (ch >= 'A' && ch <= 'Z') {
                    builder.append(Character.toLowerCase(ch));
                } else if (ch == ' ') {
                    builder.append('_');
                } else if (ch == '_' || ch == '-' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9') {
                    builder.append(ch);
                }
            });
            return builder.toString();
        };
    }

    private BCCharacterUtil() {}
}
