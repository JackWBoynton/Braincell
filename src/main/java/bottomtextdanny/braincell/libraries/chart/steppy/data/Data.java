package bottomtextdanny.braincell.libraries.chart.steppy.data;

import java.util.function.Consumer;

public interface Data {

    <T> T cast(Class<T> clazz);

    Object getHolder();

    default <T> void getOrSetHolder(Consumer<T> consumer, T orPut) {
        Object holder = getHolder();
        if (holder != null) consumer.accept((T) holder);
        else setHolder(orPut);
    }

    void setHolder(Object object);
}
