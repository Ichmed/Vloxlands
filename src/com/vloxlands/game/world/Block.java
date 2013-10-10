package com.vloxlands.game.world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.util.math.AABB;

/**
 * @author Dakror
 */
public class Block extends AABB
{
	public byte voxel;
	public byte metadata;
	
	Vector3f pos;
	Chunk chunk;
	
	public Block(byte voxel, byte metadata, Vector3f pos, Chunk chunk)
	{
		super(pos, 1, 1, 1);
		parent = chunk;
		cubic = true;
		this.pos = pos;
		this.chunk = chunk;
	}
	
	public void render()
	{
		if (!inViewFrustum()) return;
		
		GL11.glColor3f(1, 0.5f, 0);
		GL11.glColor3f(1, 1, 1);
	}
}
