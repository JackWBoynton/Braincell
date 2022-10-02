/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.base;

public final class BitUtil {

	public static boolean get(int data, int shift) {
		return (data & 1 << shift) != 0;
	}

	public static int set(int data, int shift, boolean state) {
		if (state) {
			return data | 1 << shift;
		} else {
			return data & ~(1 << shift);
		}
	}
}
