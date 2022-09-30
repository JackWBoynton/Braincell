package bottomtextdanny.braincell.libraries.registry;

public interface Serializable {

    Wrap<? extends Serializer<?>> serializer();
}
