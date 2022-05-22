package bottomtextdanny.braincell.mod.capability.player.accessory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import bottomtextdanny.braincell.Braincell;

import java.util.List;

public class MiniAttribute {
	private static final List<MiniAttribute> VALUES = Lists.newArrayList();
	public static final MiniAttribute CRITICAL_DAMAGE_MLT = new MiniAttribute(100.0F, 0.0F, 1000.0F);//
	public static final MiniAttribute ARCHERY_SPEED = new MiniAttribute(0.0F, -10.0F, 10.0F);//
	public static final MiniAttribute ARCHERY_DAMAGE_MLT = new MiniAttribute(100.0F, 0.0F, 1000.0F);//
	public static final MiniAttribute ARCHERY_DAMAGE_ADD = new MiniAttribute(0.0F, -1024.0F, 1024.0F);//
	public static final MiniAttribute ARROW_SPEED_MLT = new MiniAttribute(100.0F, 0.0F, 500.0F);//
	
	public final float baseValue;
	public final float clampMin;
	public final float clampMax;
	private final int index;
	
	public MiniAttribute(float baseValue, float clampMin, float clampMax) {
		this.baseValue = baseValue;
		this.clampMin = clampMin;
		this.clampMax = clampMax;
		if (Braincell.common().hasPassedInitialization()) {
			throw new UnsupportedOperationException("Cannot add more attribute modifiers after mod initialization.");
		} else {
			this.index = VALUES.size();
			VALUES.add(this);
		}
	}

	public int getIndex() {
		return index;
	}

	public static MiniAttribute getModifierByIndex(int index) {
		return VALUES.get(index);
	}

	public static List<MiniAttribute> values() {
		return ImmutableList.copyOf(VALUES);
	}

	public static int size() {
		return VALUES.size();
	}

	public static void loadClass() {}
}
