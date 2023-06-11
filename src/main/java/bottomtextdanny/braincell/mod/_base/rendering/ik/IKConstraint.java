package bottomtextdanny.braincell.mod._base.rendering.ik;

import java.util.Objects;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public interface IKConstraint {
   void applyToSection(IKPartData var1, IKPartData var2, @Nullable IKPartData var3);

   default @NotNull IKConstraint andThen(@NotNull IKConstraint after) {
      Objects.requireNonNull(after);
      return (v, t, u) -> {
         this.applyToSection(v, t, u);
         after.applyToSection(v, t, u);
      };
   }
}
