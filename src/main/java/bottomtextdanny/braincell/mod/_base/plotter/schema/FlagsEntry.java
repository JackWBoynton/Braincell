package bottomtextdanny.braincell.mod._base.plotter.schema;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

public final class FlagsEntry {
   private final String name;
   private final IntList flagsIndexed;
   private final IntSet flagsHashed;

   public FlagsEntry(String name, IntList flagsIndexed, IntSet flagsHashed) {
      this.name = name;
      this.flagsIndexed = flagsIndexed;
      this.flagsHashed = flagsHashed;
   }

   public int flagAt(int index) {
      return index > 0 && index < this.flagsIndexed.size() ? this.flagsIndexed.getInt(index) : Integer.MIN_VALUE;
   }

   public Object flagAsEnum(Class clazz, int index) {
      return index > 0 && index < this.flagsIndexed.size() ? clazz.getEnumConstants()[this.flagsIndexed.getInt(index)] : null;
   }

   public int firstFlag() {
      return this.flagsIndexed.isEmpty() ? Integer.MIN_VALUE : this.flagsIndexed.getInt(0);
   }

   public boolean containsFlag(int flag) {
      return this.flagsHashed.contains(flag);
   }

   public int flags() {
      return this.flagsIndexed.size();
   }

   public String getName() {
      return this.name;
   }
}
