package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.chart.segment.SchemaSegment;
import bottomtextdanny.braincell.libraries.chart.segment.SegmentData;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SegmentPredicates {
    private static final SegmentPredicate NO_SEGMENT_PREDICATE = new SegmentPredicate() {

        @Override
        public boolean test(SegmentData data) {
            return false;
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_CANCEL;
        }
    };

    public static SegmentPredicate spCancel() {
        return NO_SEGMENT_PREDICATE;
    }

    public static Concat spConcat() {
        return new Concat();
    }

    public static ConcatOred spConcatOred() {
        return new ConcatOred();
    }

    public static Either spEither(SegmentPredicate statement,
                                  SegmentPredicate positive,
                                  SegmentPredicate negative) {
        return new Either(statement, positive, negative);
    }

    public static OrElse spEither(SegmentPredicate statement,
                                  SegmentPredicate negative) {
        return new OrElse(statement, negative);
    }

    public static Negate spNegate(SegmentPredicate statement) {
        return new Negate(statement);
    }

    public static FirstFlagEquals spFirstFlagEquals(int flag) {
        return new FirstFlagEquals(flag);
    }

    public static FlagEquals spFlagEquals(int index, int flag) {
        return new FlagEquals(index, flag);
    }

    public static class Concat implements SegmentPredicate {
        private final List<SegmentPredicate> list = new LinkedList<>();

        @Override
        public SegmentPredicates.Concat spAppend(SegmentPredicate predicate) {
            list.add(predicate);
            return this;
        }

        @Override
        public boolean test(SegmentData data) {
            return list.stream().allMatch(predicate -> predicate.test(data));
        }

        public void iterate(Consumer<SegmentPredicate> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_CONCAT;
        }
    }

    public static class ConcatOred implements SegmentPredicate {
        private final List<SegmentPredicate> list = new LinkedList<>();

        @Override
        public SegmentPredicates.ConcatOred spOr(SegmentPredicate predicate) {
            list.add(predicate);
            return this;
        }

        @Override
        public boolean test(SegmentData data) {
            return list.stream().anyMatch(predicate -> predicate.test(data));
        }

        public void iterate(Consumer<SegmentPredicate> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_CONCAT_ORED;
        }
    }

    public record Either(SegmentPredicate statement,
                         SegmentPredicate positive,
                         SegmentPredicate negative) implements SegmentPredicate {

        @Override
        public boolean test(SegmentData data) {
            return statement.test(data)
                    ? positive.test(data)
                    : negative.test(data);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_EITHER;
        }
    }

    public record OrElse(SegmentPredicate statement,
                         SegmentPredicate negative) implements SegmentPredicate {

        @Override
        public boolean test(SegmentData data) {
            return statement.test(data) || negative.test(data);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_OR_ELSE;
        }
    }

    public record Negate(SegmentPredicate predicate) implements SegmentPredicate {

        @Override
        public boolean test(SegmentData data) {
            return !predicate.test(data);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_NEGATE;
        }
    }

    public record FirstFlagEquals(int flag) implements SegmentPredicate {

        @Override
        public boolean test(SegmentData data) {
            if (data instanceof SchemaSegment.Data schemaData) {
                return schemaData.flags().firstFlag() == flag;
            }
            return false;
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_FIRST_FLAG_EQUALS;
        }
    }

    public record FlagEquals(int index, int flag) implements SegmentPredicate {

        @Override
        public boolean test(SegmentData data) {
            if (data instanceof SchemaSegment.Data schemaData) {
                return schemaData.flags().flagAt(index) == flag;
            }
            return false;
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
            return BCSerializers.SEGMENT_PREDICATE_FLAG_EQUALS;
        }
    }
}
