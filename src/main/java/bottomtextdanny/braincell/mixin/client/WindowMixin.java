package bottomtextdanny.braincell.mixin.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Window.class)
public class WindowMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V", ordinal = 2), method = "<init>", remap = true)
    private void hint2(int hint, int value) {
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, Minecraft.ON_OSX ? 3 : 4);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V", ordinal = 3), method = "<init>", remap = true)
    private void hint3(int hint, int value) {
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, Minecraft.ON_OSX ? 2 : 3);
    }
}
