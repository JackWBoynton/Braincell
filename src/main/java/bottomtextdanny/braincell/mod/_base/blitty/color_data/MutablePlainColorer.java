package bottomtextdanny.braincell.mod._base.blitty.color_data;

public class MutablePlainColorer implements BlittyColorTransformer {
   private final BlittyColorOperation operation;
   public float r;
   public float g;
   public float b;
   public float a;

   public MutablePlainColorer(BlittyColorOperation operation) {
      this.operation = operation;
   }

   public void transform(BlittyColor color) {
      color.r = (Float)this.operation.func.apply(color.r, this.r);
      color.g = (Float)this.operation.func.apply(color.g, this.g);
      color.b = (Float)this.operation.func.apply(color.b, this.b);
      color.a = (Float)this.operation.func.apply(color.a, this.a);
   }
}
