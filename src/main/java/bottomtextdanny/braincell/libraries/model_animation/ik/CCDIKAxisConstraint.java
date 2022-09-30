package bottomtextdanny.braincell.libraries.model_animation.ik;

import net.minecraft.Util;

import java.util.LinkedList;

public interface CCDIKAxisConstraint {
    CCDIKAxisConstraint NO_CONSTRAINT = new CCDIKAxisConstraint() {
        @Override
        public float apply(float rot, float delta, float rotO) {
            return rot;
        }
    };
    CCDIKAxisConstraint LOCK = new CCDIKAxisConstraint() {
        @Override
        public float apply(float rot, float delta, float rotO) {
            return rot;
        }

        @Override
        public boolean skip() {
            return true;
        }
    };

    float apply(float rot, float delta, float rotO);

    default boolean skip() {
        return false;
    }

    default CCDIKAxisConstraint append(CCDIKAxisConstraint other) {
        CCDIKAxisConstraint t = this;
        boolean skipO = t.skip() || other.skip();
        return new CCDIKAxisConstraint() {
            boolean skip = skipO;
            final LinkedList<CCDIKAxisConstraint> list = Util.make(() -> {
                LinkedList<CCDIKAxisConstraint> list = new LinkedList<>();
                list.add(t);
                list.add(other);
                return list;
            });

            @Override
            public float apply(float rot, float delta, float rotO) {
                for (CCDIKAxisConstraint ccdikConstraint : list) {
                    rot = ccdikConstraint.apply(rot, delta, rotO);
                }
                return rot;
            }

            @Override
            public CCDIKAxisConstraint append(CCDIKAxisConstraint other) {
                list.add(other);
                skip = skip || other.skip();
                return this;
            }

            @Override
            public boolean skip() {
                return skip;
            }
        };
    }
}
