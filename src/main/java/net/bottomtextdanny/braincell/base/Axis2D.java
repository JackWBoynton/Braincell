package net.bottomtextdanny.braincell.base;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public enum Axis2D implements StringRepresentable {
    X("x") {
        public int choose(int p_122496_, int p_122497_) {
            return p_122496_;
        }

        public double choose(double p_122492_, double p_122493_) {
            return p_122492_;
        }
    },
    Y("y") {
        public int choose(int p_122510_, int p_122511_) {
            return p_122511_;
        }

        public double choose(double p_122506_, double p_122507_) {
            return p_122507_;
        }
    };

    public static final Axis2D[] VALUES = values();
    public static final Codec<Direction.Axis> CODEC = StringRepresentable.fromEnum(Direction.Axis::values, Direction.Axis::byName);
    private static final Map<String, Axis2D> BY_NAME = Arrays.stream(VALUES).collect(Collectors.toMap(Axis2D::getName, (p_122470_) -> {
        return p_122470_;
    }));
    private final String name;

    Axis2D(String p_122456_) {
        this.name = p_122456_;
    }

    @Nullable
    public static Axis2D byName(String p_122474_) {
        return BY_NAME.get(p_122474_.toLowerCase(Locale.ROOT));
    }

    public String getName() {
        return this.name;
    }

    public boolean isVertical() {
        return this == Y;
    }

    public boolean isHorizontal() {
        return this == X;
    }

    public String toString() {
        return this.name;
    }

    public static Axis2D getRandom(Random p_122476_) {
        return Util.getRandom(VALUES, p_122476_);
    }

    public Direction.Plane getPlane() {
        Direction.Plane direction$plane;
        switch(this) {
            case X:
                direction$plane = Direction.Plane.HORIZONTAL;
                break;
            case Y:
                direction$plane = Direction.Plane.VERTICAL;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return direction$plane;
    }

    public String getSerializedName() {
        return this.name;
    }

    public abstract int choose(int p_122466_, int p_122467_);

    public abstract double choose(double p_122463_, double p_122464_);
}
