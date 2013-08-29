package com.vloxlands.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.*;

import org.lwjgl.util.vector.Vector4f;

import com.vloxlands.game.world.Island;

public class IslandRenderer extends Thread
{
	public HashMap<Vector4f, VoxelFace> faces = new HashMap<>();
	public HashMap<Vector4f, VoxelFace> transparentFaces = new HashMap<>();
	
	private IntBuffer VBO;
	private Island parent;
	
	public IslandRenderer(Island parent)
	{	
		this.parent = parent;
	}
	
	public void run()
	{
		VBO = IntBuffer.allocate((int) (Math.pow(Island.MAXSIZE, 3) * 4));
		glGenBuffers(VBO);
		
		FloatBuffer vertices, normals, textures, colors;
		
		for (int i = 0; i < Island.MAXSIZE; i++)
		{
			for (int j = 0; j < Island.MAXSIZE; j++)
			{
				for (int k = 0; k < Island.MAXSIZE; k++)
				{
					int index = (i * Island.MAXSIZE + j) * Island.MAXSIZE + k;
					glBindBuffer(GL_ARRAY_BUFFER, VBO.get(index * 4));
					
					vertices = FloatBuffer.allocate(6 * 3 * 3 /* 64 */);
					
					for (int l = 0; l < 6; l++) {
						
					}
				}
			}
		}
	}
}
