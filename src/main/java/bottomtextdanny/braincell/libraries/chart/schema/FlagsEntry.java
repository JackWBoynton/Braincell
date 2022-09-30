package bottomtextdanny.braincell.libraries.chart.schema;

import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

public final class FlagsEntry {
    private final String name;
    private final ByteList flagsIndexed;

    public FlagsEntry(String name, ByteList flagsIndexed) {
        this.name = name;
        this.flagsIndexed = flagsIndexed;
    }

    public byte flagAt(int index) {
        return index >= 0 && index < flagsIndexed.size() ? flagsIndexed.getByte(index) : Byte.MIN_VALUE;
    }

    public <T> T flagAsEnum(Class<T> clazz, int index) {
        return index >= 0 && index < flagsIndexed.size() ? clazz.getEnumConstants()[flagsIndexed.getByte(index)] : null;
    }

    public int firstFlag() {
        return flagsIndexed.isEmpty() ? Integer.MIN_VALUE : flagsIndexed.getByte(0);
    }

    public int flags() {
        return this.flagsIndexed.size();
    }

    public String getName() {
        return name;
    }
}
