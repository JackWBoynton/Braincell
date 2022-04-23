package net.bottomtextdanny.braincell.mod._base.opengl;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class BCClippingHelper {
	private final Vector4f[] frustum = new Vector4f[6];
	private double cameraX;
	private double cameraY;
	private double cameraZ;
	
	public BCClippingHelper(Matrix4f matrix4f, Matrix4f projection) {
		this.calculateFrustum(matrix4f, projection);
	}
	
	public void setCameraPosition(double camX, double camY, double camZ) {
		this.cameraX = camX;
		this.cameraY = camY;
		this.cameraZ = camZ;
	}
	
	private void calculateFrustum(Matrix4f projection, Matrix4f frustrumMatrix) {
		Matrix4f matrix4f = frustrumMatrix.copy();
		matrix4f.multiply(projection);
		matrix4f.transpose();
		this.setFrustumPlane(matrix4f, -1, 0, 0, 0);
		this.setFrustumPlane(matrix4f, 1, 0, 0, 1);
		this.setFrustumPlane(matrix4f, 0, -1, 0, 2);
		this.setFrustumPlane(matrix4f, 0, 1, 0, 3);
		this.setFrustumPlane(matrix4f, 0, 0, -1, 4);
		this.setFrustumPlane(matrix4f, 0, 0, 1, 5);
	}
	
	private void setFrustumPlane(Matrix4f frustrumMatrix, int x, int y, int z, int id) {
		Vector4f vector4f = new Vector4f((float)x, (float)y, (float)z, 1.0F);
		vector4f.transform(frustrumMatrix);
		vector4f.normalize();
		this.frustum[id] = vector4f;
	}
	
	public boolean isBoundingBoxInFrustum(AABB aabbIn) {
		return this.isBoxInFrustum(aabbIn.minX, aabbIn.minY, aabbIn.minZ, aabbIn.maxX, aabbIn.maxY, aabbIn.maxZ);
	}
	
	private boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		float f = (float)(minX - this.cameraX);
		float f1 = (float)(minY - this.cameraY);
		float f2 = (float)(minZ - this.cameraZ);
		float f3 = (float)(maxX - this.cameraX);
		float f4 = (float)(maxY - this.cameraY);
		float f5 = (float)(maxZ - this.cameraZ);
		return this.isBoxInFrustumRaw(f, f1, f2, f3, f4, f5);
	}
	
	private boolean isBoxInFrustumRaw(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		for(int i = 0; i < 6; ++i) {
			Vector4f vector4f = this.frustum[i];
			if (!(vector4f.dot(new Vector4f(minX, minY, minZ, 1.0F)) > 0.0F) && !(vector4f.dot(new Vector4f(maxX, minY, minZ, 1.0F)) > 0.0F) && !(vector4f.dot(new Vector4f(minX, maxY, minZ, 1.0F)) > 0.0F) && !(vector4f.dot(new Vector4f(maxX, maxY, minZ, 1.0F)) > 0.0F) && !(vector4f.dot(new Vector4f(minX, minY, maxZ, 1.0F)) > 0.0F) && !(vector4f.dot(new Vector4f(maxX, minY, maxZ, 1.0F)) > 0.0F) && !(vector4f.dot(new Vector4f(minX, maxY, maxZ, 1.0F)) > 0.0F) && !(vector4f.dot(new Vector4f(maxX, maxY, maxZ, 1.0F)) > 0.0F)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isSphereInFrustum(float x, float y, float z, float radius) {
		for(int i = 0; i < 6; ++i) {
			Vector4f vector4f = this.frustum[i];

			if (vector4f.dot(new Vector4f(x - (float) this.cameraX, y - (float) this.cameraY, z - (float) this.cameraZ, 1.0F)) < -radius) {
				return false;
			}
		}
		return true;
	}
}
