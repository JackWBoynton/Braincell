/*
 * Copyright Saturday September 03 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity;

import java.util.function.Supplier;

public record EntityKey<T>(Class<T> clazz, Supplier<T> defaultValue) {
}
