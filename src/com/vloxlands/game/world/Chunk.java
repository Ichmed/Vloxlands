package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.render.ChunkRenderer;
import com.vloxlands.render.VoxelFace;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;
import com.vloxlands.settings.CFG;

public class Chunk
{
	HashMap<VoxelFaceKey, VoxelFace>[] faces;
	@SuppressWarnings("unchecked")
	HashMap<VoxelFaceKey, VoxelFace>[] meshes = new HashMap[2];
	
	int x, y, z, opaqueID = -1, transparentID = -1;
	boolean opaqueUTD = false;
	boolean transparentUTD = false;
	
	public Chunk(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void updateMesh(Island i)
	{
		faces = ChunkRenderer.generateFaces(x, y, z, i);
		
		meshes[0] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[0]);
		meshes[1] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[1]);
	}
	
	public void render(Island i, boolean opaque)
	{
		if (meshes[0].size() + meshes[1].size() == 0) return;
		
		if (opaqueID == -1)
		{
			CFG.p("new list");
			opaqueID = glGenLists(1);
			transparentID = glGenLists(1);
		}
		
		if (opaque && !opaqueUTD)
		{
			CFG.p("prerender opaque");
			renderDisplayList(true);
			opaqueUTD = true;
		}
		
		if (!opaque && !transparentUTD)
		{
			CFG.p("prerender transparent");
			renderDisplayList(false);
			transparentUTD = true;
		}
		if (Game.frustum.cubePartiallyInFrustum(new Vector3f(x * Island.CHUNKSIZE + Island.CHUNKSIZE / 2 + i.pos.x, y * Island.CHUNKSIZE + Island.CHUNKSIZE / 2 + i.pos.y, z * Island.CHUNKSIZE + Island.CHUNKSIZE / 2 + i.pos.z), Island.CHUNKSIZE / 2))
		{
			glCallList(opaque ? opaqueID : transparentID);
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
