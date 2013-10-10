package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
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
	
	@Override
	public void render()
	{
		if (!inViewFrustum() || voxel == Voxel.get("AIR").getId() || intersects() == -1) return;
		
		glColor3f(1, 0.5f, 0);
		super.render();
		glColor3f(1, 1, 1);
	}
}
