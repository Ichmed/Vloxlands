package com.vloxlands.util.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dakror
 */
public class AABB
{
	Vector3f min, max;
	
	public AABB(Vector3f min, Vector3f max)
	{
		this.min = min;
		this.max = max;
	}
}
