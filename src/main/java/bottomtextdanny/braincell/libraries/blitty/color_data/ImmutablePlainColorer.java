package bottomtextdanny.braincell.libraries.blitty.color_data;

public record ImmutablePlainColorer(BlittyColorOperation operation, float r, float g, float b, float a) implements BlittyColorTransformer {

    @Override
    public void transform(BlittyColor color) {
        color.r = this.operation.func.apply(color.r, this.r);
        color.g = this.operation.func.apply(color.g, this.g);
        color.b = this.operation.func.apply(color.b, this.b);
        color.a = this.operation.func.apply(color.a, this.a);
    }
}
