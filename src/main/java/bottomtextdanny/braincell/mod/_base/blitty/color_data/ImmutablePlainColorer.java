package bottomtextdanny.braincell.mod._base.blitty.color_data;

public record ImmutablePlainColorer(BlittyColorOperation operation, float r, float g, float b, float a) implements BlittyColorTransformer {
   public ImmutablePlainColorer(BlittyColorOperation operation, float r, float g, float b, float a) {
      this.operation = operation;
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = a;
   }

   public void transform(BlittyColor color) {
      color.r = (Float)this.operation.func.apply(color.r, this.r);
      color.g = (Float)this.operation.func.apply(color.g, this.g);
      color.b = (Float)this.operation.func.apply(color.b, this.b);
      color.a = (Float)this.operation.func.apply(color.a, this.a);
   }

   public BlittyColorOperation operation() {
      return this.operation;
   }

   public float r() {
      return this.r;
   }

   public float g() {
      return this.g;
   }

   public float b() {
      return this.b;
   }

   public float a() {
      return this.a;
   }
}
