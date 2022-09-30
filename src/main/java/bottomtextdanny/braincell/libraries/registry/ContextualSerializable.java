package bottomtextdanny.braincell.libraries.registry;

public interface ContextualSerializable<B> {

    Wrap<? extends ContextualSerializer<?, B>> serializer();
}
