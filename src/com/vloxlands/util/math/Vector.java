package com.vloxlands.util.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dakror
 */
public class Vector
{
	public float x, y, z;
	
	public Vector(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vector3f v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vector add(Vector o)
	{
		x += o.x;
		y += o.y;
		z += o.z;
		
		return this;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Vector)
		{
			Vector o = (Vector) obj;
			
			return o.x == x && o.y == y && o.z == z;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
