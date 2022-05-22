package bottomtextdanny.braincell.mod._base.blitty.color_data;

public class MutablePlainColorer implements BlittyColorTransformer {
    private final BlittyColorOperation operation;
    public float r, g, b, a;

    public MutablePlainColorer(BlittyColorOperation operation) {
        super();
        this.operation = operation;
    }

    @Override
    public void transform(BlittyColor color) {
        color.r = this.operation.func.apply(color.r, this.r);
        color.g = this.operation.func.apply(color.g, this.g);
        color.b = this.operation.func.apply(color.b, this.b);
        color.a = this.operation.func.apply(color.a, this.a);
    }
}
