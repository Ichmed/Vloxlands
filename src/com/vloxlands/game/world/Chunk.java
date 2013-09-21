package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.render.ChunkRenderer;
import com.vloxlands.render.VoxelFace;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;

public class Chunk
{
	HashMap<VoxelFaceKey, VoxelFace>[] faces;
	@SuppressWarnings("unchecked")
	HashMap<VoxelFaceKey, VoxelFace>[] meshes = new HashMap[2];
	
	int x, y, z, opaqueID = -1, transparentID = -1;
	boolean opaqueUTD = false;
	boolean transparentUTD = false;
	
	/**
	 * Keeps track of which resources can be found on this Chunk
	 */
	int[] resources = new int[Voxel.VOXELS];
	
	
	public Chunk(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		for (int i = 0; i < resources.length; i++)
			resources[i] = 0;
		
		resources[Voxel.get("AIR").getId() + 128] = (int) Math.pow(Island.CHUNKSIZE, 3);
	}
	
	public int getResource(Voxel v)
	{
		return resources[v.getId() + 128];
	}
	
	public void addResource(Voxel v, int val)
	{
		if (resources[v.getId() + 128] + val > -1)
		{
			resources[v.getId() + 128] += val;
		}
	}
	
	public void updateMesh(Island i)
	{
		faces = ChunkRenderer.generateFaces(x, y, z, i);
		if (faces[0].size() + faces[1].size() == 0)
		{
			meshes[0] = new HashMap<>();
			meshes[1] = new HashMap<>();
			return;
		}
		meshes[0] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[0]);
		meshes[1] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[1]);
		
		opaqueUTD = false;
		transparentUTD = false;
	}
	
	public void render(Island i, boolean opaque)
	{
		if (meshes[0].size() + meshes[1].size() == 0) return;
		
		if (opaqueID == -1)
		{
			opaqueID = glGenLists(1);
			transparentID = glGenLists(1);
		}
		
		if (opaque && !opaqueUTD)
		{
			renderDisplayList(true);
			opaqueUTD = true;
		}
		
		if (!opaque && !transparentUTD)
		{
			renderDisplayList(false);
			transparentUTD = true;
		}
		if (Game.frustum.sphereInFrustum(x * Island.CHUNKSIZE + Island.CHUNKSIZE / 2 + i.pos.x, y * Island.CHUNKSIZE + Island.CHUNKSIZE / 2 + i.pos.y, z * Island.CHUNKSIZE + Island.CHUNKSIZE / 2 + i.pos.z, Island.CHUNKSIZE * (float) Math.sqrt(2)))
		{
			glPushMatrix();
			{
				glCallList(opaque ? opaqueID : transparentID);
			}
			glPopMatrix();
		}
	}
	
	public void renderDisplayList(boolean opaque)
	{
		glPushMatrix();
		glNewList(opaque ? opaqueID : transparentID, GL_COMPILE);
		for (VoxelFace v : meshes[opaque ? 0 : 1].values())
			v.render();
		glEndList();
		glPopMatrix();
	}
}
