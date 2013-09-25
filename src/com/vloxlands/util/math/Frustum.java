package com.vloxlands.util.math;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dakror
 */
public class Frustum
{
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final int BOTTOM = 2;
	public static final int TOP = 3;
	public static final int BACK = 4;
	public static final int FRONT = 5;
	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;
	public static final int D = 3;
	Plane[] frustum = new Plane[6];
	
	FloatBuffer modelViewBuffer;
	FloatBuffer projectionBuffer;
	
	public Frustum()
	{
		modelViewBuffer = BufferUtils.createFloatBuffer(16);
		projectionBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	public void calculateFrustum()
	{
		float[] proj = new float[16];
		float[] modl = new float[16];
		float[] clip = new float[16];
		
		
		projectionBuffer.rewind();
		glGetFloat(GL_PROJECTION_MATRIX, projectionBuffer);
		projectionBuffer.rewind();
		projectionBuffer.get(proj);
		
		modelViewBuffer.rewind();
		glGetFloat(GL_MODELVIEW_MATRIX, modelViewBuffer);
		modelViewBuffer.rewind();
		modelViewBuffer.get(modl);
		
		clip[0] = modl[0] * proj[0] + modl[1] * proj[4] + modl[2] * proj[8] + modl[3] * proj[12];
		clip[1] = modl[0] * proj[1] + modl[1] * proj[5] + modl[2] * proj[9] + modl[3] * proj[13];
		clip[2] = modl[0] * proj[2] + modl[1] * proj[6] + modl[2] * proj[10] + modl[3] * proj[14];
		clip[3] = modl[0] * proj[3] + modl[1] * proj[7] + modl[2] * proj[11] + modl[3] * proj[15];
		clip[4] = modl[4] * proj[0] + modl[5] * proj[4] + modl[6] * proj[8] + modl[7] * proj[12];
		clip[5] = modl[4] * proj[1] + modl[5] * proj[5] + modl[6] * proj[9] + modl[7] * proj[13];
		clip[6] = modl[4] * proj[2] + modl[5] * proj[6] + modl[6] * proj[10] + modl[7] * proj[14];
		clip[7] = modl[4] * proj[3] + modl[5] * proj[7] + modl[6] * proj[11] + modl[7] * proj[15];
		clip[8] = modl[8] * proj[0] + modl[9] * proj[4] + modl[10] * proj[8] + modl[11] * proj[12];
		clip[9] = modl[8] * proj[1] + modl[9] * proj[5] + modl[10] * proj[9] + modl[11] * proj[13];
		clip[10] = modl[8] * proj[2] + modl[9] * proj[6] + modl[10] * proj[10] + modl[11] * proj[14];
		clip[11] = modl[8] * proj[3] + modl[9] * proj[7] + modl[10] * proj[11] + modl[11] * proj[15];
		clip[12] = modl[12] * proj[0] + modl[13] * proj[4] + modl[14] * proj[8] + modl[15] * proj[12];
		clip[13] = modl[12] * proj[1] + modl[13] * proj[5] + modl[14] * proj[9] + modl[15] * proj[13];
		clip[14] = modl[12] * proj[2] + modl[13] * proj[6] + modl[14] * proj[10] + modl[15] * proj[14];
		clip[15] = modl[12] * proj[3] + modl[13] * proj[7] + modl[14] * proj[11] + modl[15] * proj[15];
		
		frustum[RIGHT] = new Plane(clip[3] - clip[0], clip[7] - clip[4], clip[11] - clip[8], clip[15] - clip[12]);
		
		frustum[LEFT] = new Plane(clip[3] + clip[0], clip[7] + clip[4], clip[11] + clip[8], clip[15] + clip[12]);
		
		frustum[BOTTOM] = new Plane(clip[3] + clip[1], clip[7] + clip[5], clip[11] + clip[9], clip[15] + clip[13]);
		
		frustum[TOP] = new Plane(clip[3] - clip[1], clip[7] - clip[5], clip[11] - clip[9], clip[15] - clip[13]);
		
		frustum[BACK] = new Plane(clip[3] - clip[2], clip[7] - clip[6], clip[11] - clip[10], clip[15] - clip[14]);
		
		frustum[FRONT] = new Plane(clip[3] + clip[2], clip[7] + clip[6], clip[11] + clip[10], clip[15] + clip[14]);
	}
	
	public boolean pointInFrustum(float x, float y, float z)
	{
		for (int i = 0; i < 6; i++)
		{
			if (Vector3f.dot(frustum[i].n, new Vector3f(x, y, z)) + frustum[i].d <= 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean sphereInFrustum(float x, float y, float z, float radius)
	{
		for (int i = 0; i < 6; i++)
		{
			if (Vector3f.dot(frustum[i].n, new Vector3f(x, y, z)) + frustum[i].d <= -radius)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static Vector2f calculate2DPlaneDimension(float fov, float dist, int width, int height)
	{
		float rad = (float) (Math.toRadians(fov) / 2f);
		
		return new Vector2f((float) Math.tan(rad) * width / 2f, (float) Math.tan(rad) * height / 2f);
	}
}
