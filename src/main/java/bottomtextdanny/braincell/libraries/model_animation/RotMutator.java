package bottomtextdanny.braincell.libraries.model_animation;

public class RotMutator {
    private float rotX, rotY, rotZ;

    public void setRot(float posX, float posY, float posZ) {
        this.rotX = posX;
        this.rotY = posY;
        this.rotZ = posZ;
    }

    public void addRot(float rotX, float rotY, float rotZ) {
        this.rotX += rotX;
        this.rotY += rotY;
        this.rotZ += rotZ;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }
}
