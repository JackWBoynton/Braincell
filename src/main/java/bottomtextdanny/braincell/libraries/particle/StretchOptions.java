/*
 * Copyright Sunday October 02 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle;

import it.unimi.dsi.fastutil.floats.FloatList;

import javax.annotation.Nullable;
import java.util.Objects;

public final class StretchOptions implements ExtraOptions {
	private final int time;
	@Nullable
	public FloatList frameSpace;

	public StretchOptions(int time, FloatList frameSpace) {
		this.time = time;
		this.frameSpace = frameSpace;
	}

	public StretchOptions(int time) {
		this.time = time;
	}

	public int time() {
		return time;
	}

	public FloatList frameSpace() {
		return frameSpace;
	}
}
