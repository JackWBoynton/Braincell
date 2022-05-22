package bottomtextdanny.braincell.mod._base.rendering.ik;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public interface IKConstraint {

    void applyToSection(IKPartData parentData, IKPartData data, @Nullable IKPartData childData);

    @NotNull
    default IKConstraint andThen(@NotNull IKConstraint after) {
        Objects.requireNonNull(after);
        return (IKPartData v, IKPartData t, IKPartData u) -> {
            applyToSection(v, t, u);
            after.applyToSection(v, t, u);
        };
    }
}
