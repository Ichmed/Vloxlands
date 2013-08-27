package com.vloxlands.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;

public class MathHelper
{
	public static float clamp(float x, int i, int j)
	{
		return Math.max(i, Math.min(x, j));
	}
	
	public static FloatBuffer asFloatBuffer(float[] fs)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(fs.length);
		buffer.put(fs);
		buffer.flip();
		return buffer;
	}
	
	public static Vector2f setVector2fLength(Vector2f v, float length)
	{
		return new Vector2f(v.x * (length / v.length()), v.y * (length / v.length()));
	}
}
