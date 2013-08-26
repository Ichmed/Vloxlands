package com.vloxlands.game.util;

import org.lwjgl.util.vector.Vector3f;

public class Camera
{
	Vector3f position = new Vector3f();
	Vector3f rotation = new Vector3f();

	public Vector3f getPosition()
	{
		return new Vector3f(position.x, position.y, position.z);
	}

	public Vector3f getRotation()
	{
		return new Vector3f(rotation.x, rotation.y, rotation.z);
	}

	public void setPosition(Vector3f v)
	{
		this.setPosition(v.x, v.y, v.z);
	}

	public void setPosition(float x, float y, float z)
	{
		this.position = new Vector3f(x, y, z);
	}
	
	public void setRotation(Vector3f v)
	{
		this.setRotation(v.x, v.y, v.z);
	}

	public void setRotation(float x, float y, float z)
	{
		this.rotation = new Vector3f(x, y, z);
	}
	
	public void move(float x, float y, float z)
	{
		this.position.translate(x, y, z);
	}
}
