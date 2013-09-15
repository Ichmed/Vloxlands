package com.vloxlands.util.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dakror
 */
public class Plane
{
	public Vector3f n;
	public float d;
	
	public Plane(float x, float y, float z, float d)
	{
		this(new Vector3f(x, y, z), d);
	}
	
	public Plane(Vector3f n, float d)
	{
		this.n = n;
		this.d = d;
		
		normalize();
	}
	
	public void normalize()
	{
		float length = 1 / n.length();
		n.normalise();
		d *= length;
	}
}
