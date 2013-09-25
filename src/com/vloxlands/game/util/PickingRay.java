package com.vloxlands.game.util;

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
	{}
}
