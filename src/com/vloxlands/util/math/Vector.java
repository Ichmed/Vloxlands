package com.vloxlands.util.math;

import org.json.JSONArray;
import org.json.JSONException;
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
	
	public Vector(JSONArray arr) throws JSONException
	{
		x = arr.getInt(0);
		y = arr.getInt(1);
		z = arr.getInt(2);
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
	
	public float get(int dimension)
	{
		switch (dimension)
		{
			case 0:
				return x;
			case 1:
				return y;
			case 2:
				return z;
			default:
				return -1;
		}
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
	
	public Vector3f toVector3f()
	{
		return new Vector3f(x, y, z);
	}
	
	public static Vector negate(Vector v)
	{
		return new Vector(-v.x, -v.y, -v.z);
	}
	
	public static JSONArray serialize(Vector v) throws JSONException
	{
		JSONArray arr = new JSONArray();
		arr.put(v.x);
		arr.put(v.y);
		arr.put(v.z);
		
		return arr;
	}
}
