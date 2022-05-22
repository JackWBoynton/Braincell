package bottomtextdanny.braincell.base;

import java.util.HashSet;
import java.util.Set;
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

    private BCCharacterUtil() {}
}
