package bottomtextdanny.braincell.libraries.model_animation.ik;

import net.minecraft.Util;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.LinkedList;

public interface CCDIKConstraint {
    CCDIKConstraint NO_CONSTRAINT = (data, xDelta, yDelta, zDelta, xRotO, yRotO, zRotO) -> {};

    void apply(CCDIKPartData data, float xDelta, float yDelta, float zDelta, float xRotO, float yRotO, float zRotO);

    default CCDIKConstraint append(CCDIKConstraint other) {
        CCDIKConstraint t = this;
        return new CCDIKConstraint() {
            final LinkedList<CCDIKConstraint> list = Util.make(() -> {
                LinkedList<CCDIKConstraint> list = new LinkedList<>();
                list.add(t);
                list.add(other);
                return list;
            });

            @Override
            public void apply(CCDIKPartData data, float xDelta, float yDelta, float zDelta, float xRotO, float yRotO, float zRotO) {
                for (CCDIKConstraint ccdikConstraint : list) {
                    ccdikConstraint.apply(data, xDelta, yDelta, zDelta, xRotO, yRotO, zRotO);
                }
            }

            @Override
            public CCDIKConstraint append(CCDIKConstraint other) {
                list.add(other);
                return this;
            }
        };
    }
}
