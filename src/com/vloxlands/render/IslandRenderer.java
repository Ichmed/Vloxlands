package com.vloxlands.render;

import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector4f;

import com.vloxlands.game.world.Island;

public class IslandRenderer
{	
	public HashMap<Vector4f, VoxelFace> faces = new HashMap<>();
	public HashMap<Vector4f, VoxelFace> transparentFaces = new HashMap<>();
	
	
	
	public static void createVBOID()
	{
		IntBuffer buffer = BufferUtils.createIntBuffer((int) (Math.pow(Island.MAXSIZE, 3) * 4));
		GL15.glGenBuffers(buffer);		
	}

	
}
