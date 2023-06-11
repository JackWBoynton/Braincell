package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.base.matrix.RotationMatrix;
import bottomtextdanny.braincell.mod._base.animation.AnimatableModelComponent;
import bottomtextdanny.braincell.mod._base.animation.JointMutator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public final class BCJoint implements ModelSectionReseter, AnimatableModelComponent<JointMutator> {
    private final ObjectList<BCBox> cubeList = new ObjectArrayList<>();
    private final ObjectList<BCJoint> childModels = new ObjectArrayList<>();
    private final BCModel model;
    @Nullable
    private BCJoint mirrorJoint;
    @Nullable
    private JointMirroring mirrorType;
    private BCJoint parent;
    private String name;
    public int textureOffsetX, textureOffsetY;
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
    private boolean mirror;

    public BCJoint(BCModel model, String name) {
        this(model);
        this.name = name;
    }

    public BCJoint(BCModel model) {
        this.setScaleCore(1.0F, 1.0F, 1.0F);
        model.addReseter(this);
        this.model = model;
        model.addJoint(this);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends BCJoint> T callback(BiConsumer<BCJoint, PoseStack> cons) {
    	this.renderCallback = cons;
    	return (T)this;
    }

    public BCJoint mirror(JointMirroring type) {
        this.mirrorType = type;
        return this;
    }

    public BCJoint mirrorJoint(BCJoint mirroring) {
        this.mirrorJoint = mirroring;
        return this;
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

        return entityPos.add(modelPosition);
    }

    @Override
    public void animationTransitionerPrevious(JointMutator previous, JointMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        float eased = (1 - easingMap.computeIfAbsent(current.getRotationEasing(), (p) -> p.progression(progression))) * multiplier;
        this.xRot += eased * previous.getRotationX();
        this.yRot += eased * previous.getRotationY();
        this.zRot += eased * previous.getRotationZ();
        eased = (1 - easingMap.computeIfAbsent(current.getOffsetEasing(), (p) -> p.progression(progression))) * multiplier;
        this.x += eased * previous.getOffsetX();
        this.y += eased * previous.getOffsetY();
        this.z += eased * (progression) * multiplier;
        this.scaleX += eased * previous.getScaleX();
        this.scaleY += eased * previous.getScaleY();
        this.scaleZ += eased * previous.getScaleZ();
    }

    @Override
    public void animationTransitionerCurrent(JointMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        float eased = (easingMap.computeIfAbsent(current.getRotationEasing(), (p) -> p.progression(progression))) * multiplier;
        this.xRot += eased * current.getRotationX();
        this.yRot += eased * current.getRotationY();
        this.zRot += eased * current.getRotationZ();
        eased = (easingMap.computeIfAbsent(current.getOffsetEasing(), (p) -> p.progression(progression))) * multiplier;
        this.x += eased * current.getOffsetX();
        this.y += eased * current.getOffsetY();
        this.z += eased * current.getOffsetZ();
        eased = (easingMap.computeIfAbsent(current.getScaleEasing(), (p) -> p.progression(progression))) * multiplier;
        this.scaleX += eased * current.getScaleX();
        this.scaleY += eased * current.getScaleY();
        this.scaleZ += eased * current.getScaleZ();
    }

    public BCModel getModel() {
        return model;
    }

    @Nullable
    public BCJoint getMirrorJoint() {
        return mirrorJoint;
    }

    @Nullable
    public JointMirroring getMirrorType() {
        return mirrorType;
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
