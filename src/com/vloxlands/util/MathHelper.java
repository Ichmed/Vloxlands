package com.vloxlands.util;

public class MathHelper
{
	public static float clamp(float x, int i, int j)
	{
		return Math.max(i, Math.min(x, j));
	}
}
