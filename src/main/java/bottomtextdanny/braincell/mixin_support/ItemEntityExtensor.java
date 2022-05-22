package bottomtextdanny.braincell.mixin_support;

public interface ItemEntityExtensor {

    int getShowingModel();

    void setShowingModel(int item);

    void setShowingModel(Enum<?> item);
}
