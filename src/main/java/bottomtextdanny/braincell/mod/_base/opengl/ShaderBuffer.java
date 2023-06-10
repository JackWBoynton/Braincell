package bottomtextdanny.braincell.mod._base.opengl;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static org.lwjgl.opengl.GL15.*;

@OnlyIn(Dist.CLIENT)
public class ShaderBuffer {
    private final int id;
    private final int size;

    public ShaderBuffer(int space) {
        super();
        this.id = glGenBuffers();
        this.size = space;

        glBindBuffer(GL_ARRAY_BUFFER, this.id);
        glBufferData(GL_ARRAY_BUFFER, this.size * Integer.BYTES, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void resetData() {
        glBindBuffer(GL_ARRAY_BUFFER, this.id);
        for (int i = 0; i < this.size; i++) {
            glBufferSubData(GL_ARRAY_BUFFER, i * Integer.BYTES, new int[]{0});
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int getId() {
        return this.id;
    }
}
