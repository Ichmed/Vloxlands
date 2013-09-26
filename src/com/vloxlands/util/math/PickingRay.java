package com.vloxlands.util.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dakror
 */
public class PickingRay
{
	Vector3f start, end;
	float length;
	
	public PickingRay(float length)
	{
		this.length = length;
	}
	
	public void update()
	{
		// int x = Mouse.getX();
		// int y = Mouse.getY();
		//
		// Vector3f view = MathHelper.getNormalizedRotationVector(Game.camera.getRotation());
		// view.normalise();
		//
		// Vector3f h = Vector3f.cross(view, new Vector3f(0,1,0));
		//
		// Vector3f v = Vector3f.cross(view,)
		
	}
}
