package net.bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.bottomtextdanny.braincell.base.matrix.RotationMatrix;
import net.bottomtextdanny.braincell.mod._base.animation.AnimatableModelComponent;
import net.bottomtextdanny.braincell.mod._base.animation.JointMutator;
import net.bottomtextdanny.braincell.mod.rendering.modeling.BCEntityModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

public final class BCJoint implements ModelSectionReseter, AnimatableModelComponent<JointMutator> {
    private boolean mirror;
    private BCJoint parent;
    private String name;
    public int textureOffsetX, textureOffsetY;
    public float texWidth, texHeight;
    private BiConsumer<BCJoint, PoseStack> renderCallback;
    public float defaultAngleX, defaultAngleY, defaultAngleZ,
            defaultOffsetX, defaultOffsetY, defaultOffsetZ,
            defaultSizeX, defaultSizeY, defaultSizeZ;
    public float scaleX, scaleY, scaleZ;
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public boolean visible = true;
    private final BCModel model;
    private final ObjectList<BCBox> cubeList = new ObjectArrayList<>();
    private final ObjectList<BCJoint> childModels = new ObjectArrayList<>();

    public BCJoint(BCModel model, String name) {
        this(model);
        this.name = name;
    }

    public BCJoint(BCModel model) {
        this.setScaleCore(1.0F, 1.0F, 1.0F);
        this.setTexSize(model.getTexWidth(), model.getTexHeight());
        model.addReseter(this);
        this.model = model;

        if (model instanceof BCEntityModel<?> entityModel)
            entityModel.addJoint(this);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends BCJoint> T callback(BiConsumer<BCJoint, PoseStack> cons) {
    	this.renderCallback = cons;
    	return (T)this;
    }

    @Override
    public void reset(BCModel model) {
        this.xRot = this.defaultAngleX;
        this.yRot = this.defaultAngleY;
        this.zRot = this.defaultAngleZ;

        this.x = this.defaultOffsetX;
        this.y = this.defaultOffsetY;
        this.z = this.defaultOffsetZ;

        this.scaleX = this.defaultSizeX;
        this.scaleY = this.defaultSizeY;
        this.scaleZ = this.defaultSizeZ;
    }

    public void addChild(BCJoint renderer) {
        this.childModels.add(renderer);
        renderer.parent = this;
    }

    public BCBox getCube(int index) {
        return cubeList.get(index);
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public void setPosCore(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.x = this.defaultOffsetX = rotationPointXIn;
        this.y = this.defaultOffsetY = rotationPointYIn;
        this.z = this.defaultOffsetZ = rotationPointZIn;
    }

    public void setRotationAngleCore(float rotationAngleXIn, float rotationAngleYIn, float rotationAngleZIn) {
        this.xRot = this.defaultAngleX = rotationAngleXIn;
        this.yRot = this.defaultAngleY = rotationAngleYIn;
        this.zRot = this.defaultAngleZ = rotationAngleZIn;
    }

    public void setScaleCore(float x, float y, float z) {
        this.scaleX = this.defaultSizeX = x;
        this.scaleY = this.defaultSizeY = y;
        this.scaleZ = this.defaultSizeZ = z;
    }

    public void setPos(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.x = rotationPointXIn;
        this.y = rotationPointYIn;
        this.z = rotationPointZIn;
    }

    public void setRotationAngle(float rotationAngleXIn, float rotationAngleYIn, float rotationAngleZIn) {
        this.xRot = rotationAngleXIn;
        this.yRot = rotationAngleYIn;
        this.zRot = rotationAngleZIn;
    }

    public void setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public void addToPos(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.x += rotationPointXIn;
        this.y += rotationPointYIn;
        this.z += rotationPointZIn;
    }

    public void addToRotationAngle(float rotationAngleXIn, float rotationAngleYIn, float rotationAngleZIn) {
        this.xRot += rotationAngleXIn;
        this.yRot += rotationAngleYIn;
        this.zRot += rotationAngleZIn;
    }

    public void addToScale(float x, float y, float z) {
        this.scaleX += x;
        this.scaleY += y;
        this.scaleZ += z;
    }

    public BCJoint uvOffset(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

  
    public BCJoint setTexSize(int textureWidthIn, int textureHeightIn) {
        this.texWidth = (float)textureWidthIn;
        this.texHeight = (float)textureHeightIn;
        return this;
    }

    public Boxer makeBox() {
        return new Boxer(this);
    }

    public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.visible) {
	        matrixStackIn.pushPose();
	        this.translateRotate(matrixStackIn);
	        matrixStackIn.scale(this.scaleX, this.scaleY, this.scaleZ);
	        if (this.renderCallback != null) {
                this.renderCallback.accept(this, matrixStackIn);
	        }
            if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
                this.renderCubes(matrixStackIn.last(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

                for (BCJoint modelrenderer : this.childModels) {
                    modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }
            }
	        matrixStackIn.popPose();
        }
    }

    private void renderCubes(PoseStack.Pose matrixEntryIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Matrix4f matrixPosition = matrixEntryIn.pose();
        Matrix3f normal = matrixEntryIn.normal();

        for (BCBox box : this.cubeList) {
            for (BCTexturedQuad texturedQuad : box.quads) {
                if (texturedQuad != null) {
                    Vector3f vector3f = texturedQuad.normal.copy();
                    vector3f.transform(normal);
                    float f = vector3f.x();
                    float f1 = vector3f.y();
                    float f2 = vector3f.z();

                    for (int i = 0; i < 4; ++i) {
                        BCVertex vertex = texturedQuad.getVertex(i);
                        float[] uv = texturedQuad.getUV(i);
                        float x = vertex.getX() / 16.0F;
                        float y = vertex.getY() / 16.0F;
                        float z = vertex.getZ() / 16.0F;
                        Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
                        vector4f.transform(matrixPosition);
                        bufferIn.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, uv[0], uv[1], packedOverlayIn, packedLightIn, f, f1, f2);
                    }
                }
            }
        }
    }

    public boolean isMirrored() {
        return this.mirror;
    }

    //*\\*//*\\*//*\\?//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//

    public Vec3 modelPositionWithParentsInverted(Vec3 holder) {
        Vec3 rendererPos = new Vec3(holder.x(), holder.y(), holder.z());
        Vec3 translation = new Vec3(this.x / 16, -this.y / 16, -this.z / 16);
        RotationMatrix matrixRotX = new RotationMatrix();
        RotationMatrix matrixRotY = new RotationMatrix();
        RotationMatrix matrixRotZ = new RotationMatrix();

        matrixRotX.rotX(this.xRot);
        matrixRotY.rotY(-this.yRot);
        matrixRotZ.rotZ(-this.zRot);

        rendererPos = matrixRotZ.getTransform(rendererPos);
        rendererPos = matrixRotX.getTransform(rendererPos);
        rendererPos = matrixRotY.getTransform(rendererPos);
        rendererPos = rendererPos.add(translation);
        if (this.parent != null) {
            return this.parent.modelPositionWithParentsInverted(rendererPos);
        }

        return new Vec3(rendererPos.x(), rendererPos.y(), rendererPos.z());
    }

    public void translateRotate(PoseStack matrixStackIn) {
        matrixStackIn.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.zRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(this.yRot));
        }

        if (this.xRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(this.xRot));
        }

    }

    //*\\*//*\\*//*\\?//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//

    public BCJoint addBox(String partName, float x, float y, float z, int width, int height, int depth, float delta, int texX, int texY) {
        this.uvOffset(texX, texY);
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, (float)width, (float)height, (float)depth, delta, delta, delta, this.mirror);
        return this;
    }

    public BCJoint addBox(float x, float y, float z, float width, float height, float depth) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, this.mirror);
        return this;
    }

    public BCJoint addBox(float x, float y, float z, float width, float height, float depth, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, mirrorIn);
        return this;
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, mirrorIn);
    }

    private void addBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn) {
        this.cubeList.add(new BCBox(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, this));
    }

    public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        this.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    //*\\*//*\\*//*\\????????????????????????????????????????????*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//

    public void translateRotateWithParents(PoseStack matrixStackIn) {
        if (this.parent != null) {
            this.parent.translateRotateWithParents(matrixStackIn);
        }

        matrixStackIn.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.zRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(this.yRot));
        }

        if (this.xRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(this.xRot));
        }
    }

    public void translateRotateWithParentsInverted(PoseStack matrixStackIn) {
        if (this.parent != null) {
            this.parent.translateRotateWithParentsInverted(matrixStackIn);
        }

        matrixStackIn.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.zRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(-this.yRot));
        }

        if (this.xRot != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(-this.xRot));
        }
    }

    public Vec3 getAbsolutePosition(Vec3 holder, float partialTicks, LivingEntity entity) {
        Vec3 modelPosition = modelPositionWithParentsInverted(holder);
        double entityPositionX = Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double entityPositionY = Mth.lerp(partialTicks, entity.yOld, entity.getY());
        double entityPositionZ = Mth.lerp(partialTicks, entity.zOld, entity.getZ());

        Vec3 entityPos = new Vec3(entityPositionX, entityPositionY, entityPositionZ);

        RotationMatrix matrixRotY = new RotationMatrix();

        matrixRotY.rotY((float)Math.toRadians(-entity.yBodyRot));

        modelPosition = matrixRotY.getTransform(modelPosition);

        modelPosition = modelPosition.add(0.0, 1.5, 0.0);

        return  entityPos.add(modelPosition);
    }

    @Override
    public void animationTransitionerPrevious(JointMutator previous, float multiplier, float progression, float invertedProgression) {

        this.xRot += invertedProgression * previous.getRotationX();
        this.yRot += invertedProgression * previous.getRotationY();
        this.zRot += invertedProgression * previous.getRotationZ();
        this.x += invertedProgression * previous.getOffsetX();
        this.y += invertedProgression * previous.getOffsetY();
        this.z += invertedProgression * previous.getOffsetZ();
        this.scaleX += invertedProgression * previous.getScaleX();
        this.scaleY += invertedProgression * previous.getScaleY();
        this.scaleZ += invertedProgression * previous.getScaleZ();
    }

    @Override
    public void animationTransitionerCurrent(JointMutator current, float multiplier, float progression, float invertedProgression) {

        this.xRot += progression * current.getRotationX();
        this.yRot += progression * current.getRotationY();
        this.zRot += progression * current.getRotationZ();
        this.x += progression * current.getOffsetX();
        this.y += progression * current.getOffsetY();
        this.z += progression * current.getOffsetZ();
        this.scaleX += progression * current.getScaleX();
        this.scaleY += progression * current.getScaleY();
        this.scaleZ += progression * current.getScaleZ();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public JointMutator newAnimationData() {
        return new JointMutator();
    }

    public static class Boxer {
        private final BCJoint joint;
        private float x;
        private float y;
        private float z;
        private float width;
        private float height;
        private float depth;
        private float deltaX;
        private float deltaY;
        private float deltaZ;
        private int u;
        private int v;
        private boolean mirror;

        Boxer(BCJoint joint) {
            super();
            this.joint = joint;
        }

        public Boxer pos(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public Boxer dimension(float width, float height, float depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
            return this;
        }

        public Boxer delta(float x, float y, float z) {
            this.deltaX = x;
            this.deltaY = y;
            this.deltaZ = z;
            return this;
        }

        public Boxer delta(float amount) {
            this.x = amount;
            this.y = amount;
            this.z = amount;
            return this;
        }

        public Boxer uvPosition(int u, int v) {
            this.u = u;
            this.v = v;
            return this;
        }

        public Boxer mirror() {
            this.mirror = true;
            return this;
        }

        public BCBox implement() {
            BCBox box = new BCBox(this.u, this.v, this.x, this.y, this.z, this.width, this.height, this.depth, this.deltaX, this.deltaY, this.deltaZ, this.mirror, this.joint);
            this.joint.cubeList.add(box);
            return box;
        }
    }
}
