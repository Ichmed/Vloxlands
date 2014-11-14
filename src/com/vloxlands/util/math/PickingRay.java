package com.vloxlands.util.math;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dakror
 */
public class PickingRay {
	public Vector3f start, end;
	float length;
	
	public PickingRay(Vector3f n, Vector3f f) {
		start = n;
		end = f;
		length = Vector3f.sub(n, f, null).length();
	}
	
	public static PickingRay getPickingRay(float mouseX, float mouseY) {
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		
		FloatBuffer near = BufferUtils.createFloatBuffer(3);
		FloatBuffer far = BufferUtils.createFloatBuffer(3);
		
		GLU.gluUnProject(mouseX, mouseY, 0, modelview, projection, viewport, near);
		GLU.gluUnProject(mouseX, mouseY, 1/* CFG.RENDER_DISTANCES[CFG.RENDER_DISTANCE] */, modelview, projection, viewport, far);
		
		return new PickingRay((Vector3f) new Vector3f().load(near), (Vector3f) new Vector3f().load(far));
	}
}
