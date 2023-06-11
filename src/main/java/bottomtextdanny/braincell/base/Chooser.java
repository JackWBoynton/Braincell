package bottomtextdanny.braincell.base;

import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.random.RandomGenerator;

public class Chooser {
   private final NavigableMap weightTable;
   private final float total;

   private Chooser(NavigableMap weightTable, float total) {
      this.weightTable = weightTable;
      this.total = total;
   }

   public Chooser(NavigableMap weightTable) {
      float total = 0.0F;

      Float weight;
      for(Iterator var3 = weightTable.keySet().iterator(); var3.hasNext(); total += weight) {
         weight = (Float)var3.next();
      }

      this.weightTable = weightTable;
      this.total = total;
   }

   public static Builder builder() {
      return new Builder();
   }

   public Object pick(RandomGenerator chooser) {
      return this.weightTable.higherEntry(this.total * chooser.nextFloat()).getValue();
   }

   public void fillArray(Object[] array, RandomGenerator chooser) {
      for(int i = 0; i < array.length; ++i) {
         array[i] = this.weightTable.higherEntry(this.total * chooser.nextFloat()).getValue();
      }

   }

   public static final class Builder {
      private final NavigableMap weightTable = new TreeMap();
      private float total;

      private Builder() {
      }

      public Builder put(float weight, Object item) {
         if (weight <= 0.0F) {
            return this;
         } else {
            this.total += weight;
            this.weightTable.put(this.total, item);
            return this;
         }
      }

      public Chooser build() {
         return new Chooser(this.weightTable, this.total);
      }
   }
}
