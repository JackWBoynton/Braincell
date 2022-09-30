package bottomtextdanny.braincell.libraries.chart.steppy.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface DataRequires {
    Class<?>[] value();
}
