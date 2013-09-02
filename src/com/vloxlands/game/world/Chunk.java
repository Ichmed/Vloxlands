package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import com.vloxlands.render.ChunkRenderer;
import com.vloxlands.render.VoxelFace;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;

public class Chunk
{
	HashMap<VoxelFaceKey, VoxelFace>[] faces;
	@SuppressWarnings("unchecked")
	HashMap<VoxelFaceKey, VoxelFace>[] meshes = new HashMap[2];
	int x, y, z, opaqueID = -1, transparentID = -1;
	
	public Chunk(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void update(Island i)
	{
		if (opaqueID == -1)
		{
			opaqueID = glGenLists(1);
			transparentID = glGenLists(1);
		}
		faces = ChunkRenderer.generateFaces(x, y, z, i);
		
		meshes[0] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[0]);
		meshes[1] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[1]);
	}
	
	public void onTick(boolean opaque)
	{
		glCallList(opaque ? opaqueID : transparentID);
	}
	
	public void render(boolean opaque)
	{
		glPushMatrix();
		glNewList(opaque ? opaqueID : transparentID, GL_COMPILE);
		for (VoxelFace v : meshes[opaque ? 0 : 1].values())
			v.render();
		glEndList();
		glPopMatrix();
	}
}
