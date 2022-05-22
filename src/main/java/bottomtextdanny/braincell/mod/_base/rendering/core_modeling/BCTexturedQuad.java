package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BCTexturedQuad implements ModelSectionReseter {
    public final BCVertex[] vertices;
    public float[][] defaultPositions;
    public float[][] positions;
    public final Vector3f normal;

    public BCTexturedQuad(BCVertex[] vertices,
                          float left,
                          float top,
                          float right,
                          float bottom,
                          float texWidth,
                          float texHeight,
                          boolean mirrorIn,
                          Direction directionIn) {
        this.vertices = vertices;
        this.defaultPositions = new float[][]{
                {right / texWidth, top / texHeight},
                {left / texWidth, top / texHeight},
                {left / texWidth, bottom / texHeight},
                {right / texWidth, bottom / texHeight}
        };
        if (mirrorIn) {
            int last = vertices.length - 1;
            int lengthByTwo = vertices.length / 2;

            for(int j = 0; j < lengthByTwo; ++j) {
                BCVertex mirroredVertex = vertices[j];
                float[] mirroredUV = this.defaultPositions[j];
                vertices[j] = vertices[last - j];
                this.defaultPositions[j] = this.defaultPositions[last - j];
                vertices[last - j] = mirroredVertex;
                this.defaultPositions[last - j] = mirroredUV;
            }
        }
        this.positions = this.defaultPositions.clone();
        this.normal = directionIn.step();
        if (mirrorIn) {
            this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }

    public void setDefault(BCModel model, float posX, float posY, float width, float height) {
        posX /= model.getTexWidth();
        posY /= model.getTexHeight();
        width /= model.getTexWidth();
        height /= model.getTexHeight();

        this.defaultPositions = new float[][]{
                {posX + width, posY},
                {posX, posY},
                {posX, posY + height},
                {posX + width, posY + height}
        };
    }

    public void move(BCModel model, float xPosAddition, float yPosAddition) {
        xPosAddition /= model.getTexWidth();
        yPosAddition /= model.getTexHeight();

        for (float[] position : this.positions) {
            position[0] += xPosAddition;
            position[1] += yPosAddition;
        }
    }

    public void set(BCModel model, float posX, float posY, float width, float height) {
        posX /= model.getTexWidth();
        posY /= model.getTexHeight();
        width /= model.getTexWidth();
        height /= model.getTexHeight();

        this.positions = new float[][]{
                {posX + width, posY},
                {posX, posY},
                {posX, posY + height},
                {posX + width, posY + height}
        };
    }

    public BCVertex getVertex(int index) {
        return this.vertices[index];
    }

    public float[] getUV(int index) {
        return this.positions[index];
    }

    public float[] getUV(BCUV type) {
        return this.positions[type.ordinal()];
    }

    public void setUVCorner(BCModel model, BCUV type, float x, float y) {
        this.positions[type.ordinal()][0] = x / model.getTexWidth();
        this.positions[type.ordinal()][1] = y / model.getTexHeight();
    }

    @Override
    public void reset(BCModel model) {
        for (int i = 0; i < this.defaultPositions.length; i++) {
            this.positions[i][0] = this.defaultPositions[i][0];
            this.positions[i][1] = this.defaultPositions[i][1];
        }
    }
}
