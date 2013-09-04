package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
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
	
	public void onTick(Island i, boolean opaque)
	{
		if (meshes[0].size() + meshes[1].size() == 0) return;
		
		for (Vector3f v : getCorners(i))
		{
			if (Game.currentGame.viewFrustum.isPointInsideFrustum(v))
			{
				glCallList(opaque ? opaqueID : transparentID);
				return;
			}
		}
		System.out.println("test");
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
	
	public Vector3f[] getCorners(Island island)
	{
		int i = Island.CHUNKSIZE;
		int x = (int) ((this.x * i) + island.getPos().x);
		int y = (int) ((this.y * i) + island.getPos().y);
		int z = (int) ((this.z * i) + island.getPos().z);
		return new Vector3f[] { new Vector3f(x, y, z), new Vector3f(x + i, y, z), new Vector3f(x + i, y + i, z), new Vector3f(x + i, y, z + i), new Vector3f(x + i, y + i, z + i), new Vector3f(x, y + i, z), new Vector3f(x, y, z + i), new Vector3f(x, y + i, z + i) };
	}
}
