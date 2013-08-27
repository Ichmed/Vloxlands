package com.vloxlands.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

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
}
