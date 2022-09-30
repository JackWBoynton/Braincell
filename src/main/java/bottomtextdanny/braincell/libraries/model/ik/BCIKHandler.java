package bottomtextdanny.braincell.libraries.model.ik;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.base.matrix.RotationMatrix;
import bottomtextdanny.braincell.libraries.model_animation.ik.CCDIKLink;
import com.mojang.math.Vector3f;
import bottomtextdanny.braincell.libraries.model_animation.AnimatableModelComponent;
import bottomtextdanny.braincell.libraries.model_animation.PosMutator;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model.BCModel;
import bottomtextdanny.braincell.libraries.model.ModelSectionReseter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

import java.util.Map;

import static bottomtextdanny.braincell.base.BCMath.*;
import static net.minecraft.util.Mth.RAD_TO_DEG;
import static net.minecraft.util.Mth.atan2;

//this doesn't take account of rotations on the z axis, please avoid these in ik systems.
public class BCIKHandler<T> {
//    private static final RotationMatrix ROTATOR = new RotationMatrix();
//    protected final CCDIKLink<T>[] joints;
//    protected Vector3f goal = new Vector3f();
//    public final int top;
//
//    public BCIKHandler(CCDIKLink<T>... joints) {
//        super();
//        this.joints = joints;
//        this.top = this.joints.length - 1;
//    }
//
//    public void drag2(T object) {
//        IKPartData childData = null;
//        Vector3f lgoalVector = null;
//        for (int j = 0; j < 100; j++) {
//
//            for (int i = this.top; i >= 2; i--) {
//                CCDIKLink<T> current = this.joints[i];
//                IKPartData data = current.dataGetter().apply(object);
//                lgoalVector = this.goal;
//                Vector3f relativePos = getLocalPos(object, 0, i);
//                Vector3f relativeTopPos = getLocalPos(object, i, this.top);
//                // if (((relativeTopPos.z()) * (relativeTopPos.z()) + (relativeTopPos.x()) * (relativeTopPos.x())) < 0 && hDistToTop > 0) hDistToTop = -hDistToTop;
//
////                if (current.getConstraint() != null) {
////                    current.getConstraint().applyToSection(i > 1 ? this.joints[i - 1].dataGetter().apply(object) : null, data, childData);
////                }
//
////                data.yRot = (float) atan2(-x - data.x, -z - data.z);
////                data.xRot = (float) atan2(-y + data.y, -horizontalDistance + dataHorizontalDistance);
//
//
//                float hDistToTop = (float)Math.sqrt(dot2(relativeTopPos.x(), relativeTopPos.z()));
//                float hDistToGoal = (float)Math.sqrt(dot2(lgoalVector.x() - relativePos.x(), lgoalVector.z() - relativePos.z()));
//
//               // float hDistToPos = (float)Math.sqrt((relativePos.y()) * (relativePos.y()) + (relativePos.x()) * (relativePos.x()));
//
//                float xDif = radianAngleDiff(
//                        (float)atan2((lgoalVector.y() - relativePos.y()), (hDistToGoal)),
//                        (float)atan2((relativeTopPos.y()), (hDistToTop))
//                );
//
//                if (!Float.isNaN(xDif))  {
//                    data.xRot += xDif;
//                }
//                relativePos = getLocalPos(object, 0, i);
//                relativeTopPos = getLocalPos(object, i, this.top);
//
//                float yDif = radianAngleDiff(
//                        (float)atan2(lgoalVector.z() - relativePos.z(), lgoalVector.x() - relativePos.x()),
//                        (float)atan2(relativeTopPos.z(), relativeTopPos.x())
//                );
//
//                if (!Float.isNaN(yDif))  {
//                    data.yRot += yDif;
//                }


//                if (data.xRot != data.xRot) {
//                    data.xRot = 0.0F;
//                }
//                if (data.yRot != data.yRot) {
//                    data.yRot = 0.0F;
//                }
//                if (current.getConstraint() != null) {
//                    current.getConstraint().applyToSection(i > 1 ? this.joints[i - 1].dataGetter().apply(object) : null, data, childData);
//                }

                //childData = data;
               // Minecraft.getInstance().player.chat(String.valueOf(hDistToGoal));
//                Minecraft.getInstance().player.displayClientMessage(new TextComponent(String.valueOf(
//                        radianAngleDiff(
//                                (float)atan2(relativeTopPos.y(), hDistToTop),
//                                (float)atan2(this.goalY - relativePos.y(), hDistToGoal)
//                        )
//                ) + " " + data.xRot + " " +  (float)atan2(this.goalY - relativePos.y(), hDistToGoal) + " " + (float)atan2(relativeTopPos.y(), hDistToTop)).setStyle(Style.EMPTY.withColor(ChatFormatting.values()[i + 7 % ChatFormatting.values().length])), false);
//
//
//            }
//        }
//
//    }
//
//    public Vector3f rotateToModel(T object, Vector3f vector, int partIndex) {
//        float y = 0, x = 0;
//        for (int i = 0; i < partIndex; i++) {
//            IKPartData data = this.joints[i].dataGetter().apply(object);
//            x += data.xRot; y += data.yRot;
//        }
//
//        ROTATOR.rotX(x);
//        Vector3f rVec = ROTATOR.getTransform(vector);
//        ROTATOR.rotY(y);
//        ROTATOR.transform(rVec);
//        return rVec;
//    }
//
//    public Vector3f getLocalPos(T object,
//                                int startIndex,
//                                int index) {
//        float xDelta = 0, yDelta = 0, zDelta = 0, xRot = 0, yRot = 0;
//
//        for (int i = startIndex; i < index + 1; i++) {
//            BCJoint joint = this.joints[i].joint();
//            float dist = 0;
//            float yCos = Mth.cos(yRot);
//            float ySin = Mth.sin(yRot);
//            float xCos = Mth.cos(xRot);
//            float xSin = Mth.sin(xRot);
//            if (i - 1 >= 0) {
//                IKPartData data = this.joints[i - 1].dataGetter().apply(object);
//                xRot = Float.isNaN(data.xRot) ? 0.0F : data.xRot;
//                yRot = Float.isNaN(data.yRot) ? 0.0F : data.yRot;
//                yCos = Mth.cos(yRot);
//                ySin = Mth.sin(yRot);
//                xCos = Mth.cos(xRot);
//                xSin = Mth.sin(xRot);
//
//                dist = (float) Math.sqrt(joint.x * joint.x + joint.y * joint.y + joint.z * joint.z);
//            }
//
//            float xDeltaO = xDelta, yDeltaO = yDelta, zDeltaO = zDelta;
//            xDelta += ySin * xCos * dist;
//            yDelta += -xSin * dist;
//            zDelta += yCos * xCos * dist;
//
//            float calcDist = (float) Math.sqrt(xDelta * xDelta + zDelta * zDelta);
//            float calcDistO = (float) Math.sqrt(xDeltaO * xDeltaO + zDeltaO * zDeltaO);
//          //  Minecraft.getInstance().player.chat(String.valueOf((Math.atan2(yDelta - yDeltaO, calcDist - calcDistO)) - (xRot - xRotO)));
//        }
//
//        return new Vector3f(
//                xDelta,
//                yDelta,
//                zDelta
//        );
//
//    }
//
//    public Vector3f getLocalTopPos(T object, int index) {
//        float xDelta = 0, yDelta = 0, zDelta = 0, xRot = 0, yRot = 0;
//
//
//
//        return new Vector3f(
//                xDelta,
//                yDelta,
//                zDelta
//        );
//
//    }

//    public void drag2(T object) {
//        for (int j = 0; j < 1; j++) {
//            for (int i = this.top; i >= 0; i--) {
//                BCIKPart<T> current = this.joints[i];
//                IKPartData data = current.dataGetter().apply(object);
//                Vector3f relativePos = getLocalPos(object, i);
//                Vector3f relativeTopPos = getLocalTopPos(object, i);
//                relativeTopPos.add(relativePos);
//
////            float hDistToGoal = (float)Math.sqrt((this.goalZ + relativePos.z()) * (this.goalZ + relativePos.z()) + (this.goalX + relativePos.x()) * (this.goalX + relativePos.x()));
////
////            float hDistToTop = (float)Math.sqrt((relativeTopPos.z()) * (relativeTopPos.z()) + (relativeTopPos.x()) * (relativeTopPos.x()));
//
//
//                data.yRot +=
//                        radianAngleDiff(
//                                (float)atan2(this.goalX - relativePos.x(), this.goalY - relativePos.y()),
//                                (float)atan2(relativeTopPos.x() - relativePos.x(), relativeTopPos.y() - relativePos.y())
//                        )
//                ;
//
//
//
//
//
////            data.xRot += radianAngleDiff(
////                    (float)atan2(relativeTopPos.y(), relativeTopPos.x()),
////                    (float)atan2(this.goalZ - relativePos.z(), this.goalX - relativePos.x())
////            );
//
//                Minecraft.getInstance().player.displayClientMessage(new TextComponent(String.valueOf(
//                        radianAngleDiff(
//                                (float)atan2(this.goalX, this.goalY),
//                                (float)atan2(relativeTopPos.x() - relativePos.x(), relativeTopPos.y() - relativePos.y())
//                        )
//                ) + " " + data.yRot + " " + (float)atan2(this.goalX, this.goalY) + " " + (float)atan2(relativeTopPos.x() - relativePos.x(), relativeTopPos.y() - relativePos.y())).setStyle(Style.EMPTY.withColor(ChatFormatting.values()[i + 7 % ChatFormatting.values().length])), false);
//
//
//            }
//        }
//
//    }
//
//    public Vector3f getLocalPos(T object, int layer) {
//
//        float xDelta = 0, yDelta = 0, zDelta = 0, xRot = 0, yRot = 0;
//        for (int i = 0; i < layer; i++) {
//            IKPartData data = this.joints[i].dataGetter().apply(object);
//            if (i + 1 < this.joints.length) {
//                BCJoint joint = this.joints[i + 1].joint();
//                xRot = data.xRot;
//                yRot = data.yRot;
//                float yCos = Mth.cos(yRot);
//                float ySin = Mth.sin(yRot);
//                float xCos = Mth.cos(xRot);
//                float xSin = Mth.sin(xRot);
//                float dist = (float) Math.sqrt(joint.x * joint.x + joint.y * joint.y);
//
//                xDelta += ySin * dist;
//                yDelta += yCos * dist;
//                //   zDelta += yCos * xSin * joint.z;
//            }
//        }
//
////        Minecraft.getInstance().player.chat(String.valueOf(new Vector3f(
////                xDelta,
////                yDelta,
////                zDelta
////        )));
//
//        return new Vector3f(
//                xDelta,
//                yDelta,
//                zDelta
//        );
//
//    }
//
//    public Vector3f getLocalTopPos(T object, int layer) {
//        float xDelta = 0, yDelta = 0, zDelta = 0, xRot = 0, yRot = 0;
//        for (int i = layer; i < this.joints.length; i++) {
//            IKPartData data = this.joints[i].dataGetter().apply(object);
//            if (i + 1 < this.joints.length) {
//                BCJoint joint = this.joints[i + 1].joint();
//                xRot = data.xRot;
//                yRot = data.yRot;
//                float yCos = Mth.cos(yRot);
//                float ySin = Mth.sin(yRot);
//                float xCos = Mth.cos(xRot);
//                float xSin = Mth.sin(xRot);
//                float dist = (float) Math.sqrt(joint.x * joint.x + joint.y * joint.y);
//
//                xDelta += ySin * dist;
//                yDelta += yCos * dist;
//                //   zDelta += yCos * xSin * joint.z;
//            }
//        }
//
////        Minecraft.getInstance().player.chat(String.valueOf(new Vector3f(
////                xDelta,
////                yDelta,
////                zDelta
////        )));
//
//        return new Vector3f(
//                xDelta,
//                yDelta,
//                zDelta
//        );
//
//    }

//    public void drag(T object) {
//        float xGoal = this.goal.x();
//        float yGoal = this.goal.y();
//        float zGoal = this.goal.z();
//        for (int j = 0; j < 1; j++) {
//
//            for (int i = this.joints.length - 1; i >= 0; i--) {
//                CCDIKLink<T> current = this.joints[i];
//                IKPartData data = current.dataGetter().apply(object);
//
//
//                if (i + 1 < this.joints.length) {
//                    CCDIKLink<T> child = this.joints[i + 1];
//                    IKPartData childData = child.dataGetter().apply(object);
//                    dragJoint(i, object, data, current.getConstraints(), childData.x, childData.y, childData.z, child.joint().x, child.joint().y, child.joint().z);
//                } else {
//                    dragJoint(i, object, data, current.getConstraints(), xGoal, yGoal, zGoal, 0, 0, 0);
//                }
//
//            }
//            IKPartData firstData = this.joints[0].dataGetter().apply(object);
//
//            float dx = firstData.x, dy = firstData.y, dz = firstData.z;
//
//            for (int i = 0; i < this.joints.length; i++) {
//                CCDIKLink<T> part = this.joints[i];
//                IKPartData data = part.dataGetter().apply(object);
//                data.x -= dx;
//                data.y -= dy;
//                data.z -= dz;
//                if (i != this.joints.length - 1) {
//                    CCDIKLink<T> child = this.joints[i + 1];
//                    IKPartData childData = child.dataGetter().apply(object);
//                    dragJoint(i, object, data, part.getConstraints(), childData.x, childData.y, childData.z, child.joint().x, child.joint().y, child.joint().z);
//                }
//
//            }
//
//            xGoal = this.goal.x() + dx;
//            yGoal = this.goal.y() + dy;
//            zGoal = this.goal.z() + dz;
//        }
//
//    }
//
//    public void dragJoint(int index, T object, IKPartData data, IKConstraint constraint, float lookX, float lookY, float lookZ, float repoX, float repoY, float repoZ) {
//        float dist = (float)Math.sqrt(repoX * repoX + repoY * repoY + repoZ * repoZ);
//        aimJoint(index, object, data, constraint, lookX, lookY, lookZ);
//        float yCos = Mth.cos(data.yRot);
//        float ySin = Mth.sin(data.yRot);
//        float xCos = Mth.cos(data.xRot);
//        float xSin = Mth.sin(data.xRot);
////        currentData.x += -(ySin * xCos * (childData.x + child.joint().x));
////        currentData.y += -(-xSin * (childData.y + child.joint().y));
////        currentData.z += -(yCos * xCos * (childData.z + child.joint().z));
//        data.x = lookX - ySin * xCos * dist;
//        data.y = lookY - -xSin * dist;
//        data.z = lookZ - yCos * xCos * dist;
//
//    }
//
//    public void aimJoint(int index, T object, IKPartData data, IKConstraint constraint, float x, float y, float z) {
//        boolean negDist = (z * z + x * x) < 0;
//        float horizontalDistance = (float)Math.sqrt(z * z + x * x);
//       // if (negDist && horizontalDistance >= 0) horizontalDistance = -horizontalDistance;
//        negDist = (data.z * data.z + data.x * data.x) < 0;
//        float dataHorizontalDistance = (float)Math.sqrt(data.z * data.z + data.x * data.x);
//       // if (negDist && dataHorizontalDistance >= 0) dataHorizontalDistance = -dataHorizontalDistance;
//
//        data.yRot = (float) atan2(-x - data.x, -z - data.z);
//        data.xRot = (float) atan2(-y + data.y, -horizontalDistance + dataHorizontalDistance);
//
//        if (true) {
//            data.yRot = (float) atan2(-x - data.x, -z - data.z);
//            data.xRot = (float) atan2(-y + data.y, -horizontalDistance + dataHorizontalDistance);
//
//        }
//
//
//
//    }
//
//    public void setToModel(T object) {
//
//        CCDIKLink<T>[] bcikParts = this.joints;
//        float posX = 0, posY = 0, posZ = 0, stackDeltaX = 0, stackDeltaY = 0;
//
//
//
//        for (int i = 0; i < bcikParts.length; i++) {
//
//            CCDIKLink<T> joint = bcikParts[i];
//            IKPartData data = bcikParts[i].dataGetter().apply(object);
//
//            Vector3f np = null;
//            Vector3f p = getLocalPos(object, 0, i);
//
//            if (i + 1 < this.joints.length) {
//                np = getLocalPos(object, 0, i + 1);
//            }
//
//            if (i == 1) {
//                joint.joint().xRot += data.xRot - stackDeltaX;
//                joint.joint().yRot += data.yRot - stackDeltaY;
//                stackDeltaX += joint.joint().xRot;
//                stackDeltaY += joint.joint().yRot;
//
//            } else {
//                joint.joint().xRot += data.xRot;
//                joint.joint().yRot += data.yRot;
//                stackDeltaX += joint.joint().xRot;
//                stackDeltaY += joint.joint().yRot;
//                if (i == 2)
//                Minecraft.getInstance().player.chat("x: " + (Mth.wrapDegrees(RAD_TO_DEG * data.xRot)) + "  y: " + (Mth.wrapDegrees(RAD_TO_DEG * data.yRot)));
//            }
//
//
////            if (i == 1) {
////                //joint.joint().xRot -= BCMath.FPI;
////                IKPartData prevData = bcikParts[i - 1].dataGetter().apply(object);
////
////
////            } else if (i == 0) {
////            //    joint.joint().xRot += BCMath.FPI_HALF;
////
////                joint.joint().xRot += (data.xRot) - stackOffsetX;
////                joint.joint().yRot += (data.yRot) - stackOffsetY;
////            } else {
////                joint.joint().xRot += (data.xRot) - stackOffsetX;
////                joint.joint().yRot += (data.yRot - FPI_HALF) - stackOffsetY;
////            }
//
//
//        }
//
//        this.joints[0].joint().xRot += FPI;
//       // this.joints[0].joint().yRot += FPI;
//        Vector3f finalPos = getLocalPos(object, 0, this.top);
//
//
//
////        IKPartData data = this.joints[3].dataGetter().apply(object);
////        this.joints[0].joint().x = finalPos.x();
////        this.joints[0].joint().y = finalPos.y();
////        this.joints[0].joint().z = finalPos.z();
//
//    }
//
//    public void set(float x, float y, float z) {
//        this.goal.set(x, y, z);
//    }
//
//    public void add(float x, float y, float z) {
//        this.goal.add(x, y, z);
//    }
//
//    public float getGoalX() {
//        return this.goal.x();
//    }
//
//    public float getGoalY() {
//        return this.goal.y();
//    }
//
//    public float getGoalZ() {
//        return this.goal.z();
//    }
//
//    @Override
//    public String name() {
//        return "unknown";
//    }
//
//    @Override
//    public PosMutator newAnimationData() {
//        return new PosMutator();
//    }
//
//    @Override
//    public void animationTransitionerPrevious(PosMutator previous, PosMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
//        progression = 1 - progression;
//        this.goal.add(
//                progression * previous.getPosX(),
//                progression * previous.getPosY(),
//                progression * previous.getPosZ());
//    }
//
//    @Override
//    public void animationTransitionerCurrent(PosMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
//        this.goal.add(
//                progression * current.getPosX(),
//                progression * current.getPosY(),
//                progression * current.getPosZ());
//    }
//
//    @Override
//    public void reset(BCModel model) {
//        this.goal.set(0.0F, 0.0F, 0.0F);
//    }
}
