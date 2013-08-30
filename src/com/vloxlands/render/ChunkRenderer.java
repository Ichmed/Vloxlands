package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Direction;

public class ChunkRenderer
{
	public static void renderChunk(int listIndex, int index, Island island)
	{
		int cs = Island.SIZE / Island.CHUNKSIZE;
		int cx = (index / (cs * cs));
		int cy = (index / cs) % cs;
		int cz = (index % cs);
		
		// CFG.b("index", index, "cx", cx, "cy", cy, "cz", cz);
		glPushMatrix();
		glNewList(listIndex, GL_COMPILE);
		ArrayList<VoxelFace>[] faceLists = generateFaces(cx, cy, cz, island);
		// CFG.p("faces in this chunk: " + faces);
		for (int i = 0; i < faceLists[0].size(); i++)
		{
			faceLists[0].get(i).render();
		}
		for (int i = 0; i < faceLists[1].size(); i++)
		{
			faceLists[1].get(i).render();
		}
		
		glEndList();
		glPopMatrix();
	}
	
	public static void initChunks(Island island)
	{
		island.chunk0ID = glGenLists(island.chunks.capacity());
		for (int i = 0; i < island.chunks.capacity(); i++)
		{
			renderChunk(island.chunk0ID + i, i, island);
			island.chunks.put(i);
		}
		
		
		island.chunks.flip();
		CFG.p("[ChunkRenderer]: Initialized chunks on Island " + island);
	}
	
	// public static void updateChunk(int index, Island island)
	// {
	// int cs = Island.SIZE / Island.CHUNKSIZE;
	// int cx = (index / (cs * cs));
	// int cy = (index / cs) % cs;
	// int cz = (index % cs);
	//
	// ArrayList<VoxelFace>[] faceLists = generateFaces(cx, cy, cz, island);
	// int faces = faceLists[0].size() + faceLists[1].size();
	//
	// FloatBuffer vertices = BufferUtils.createFloatBuffer(faces * 4 * 3);
	// FloatBuffer normals = BufferUtils.createFloatBuffer(faces * 4 * 3);
	// FloatBuffer textures = BufferUtils.createFloatBuffer(faces * 4 * 2);
	//
	// for (VoxelFace face : faceLists[0])
	// {
	// for (int i = 0; i < 4; i++)
	// {
	// vertices.put(face.verts[i].x);
	// vertices.put(face.verts[i].y);
	// vertices.put(face.verts[i].z);
	//
	// normals.put(face.dir.dir.x);
	// normals.put(face.dir.dir.y);
	// normals.put(face.dir.dir.z);
	//
	// textures.put(face.texVerts[i].x);
	// textures.put(face.texVerts[i].y);
	// }
	// }
	//
	// for (VoxelFace face : faceLists[1])
	// {
	// for (int i = 0; i < 4; i++)
	// {
	// vertices.put(face.verts[i].x);
	// vertices.put(face.verts[i].y);
	// vertices.put(face.verts[i].z);
	//
	// normals.put(face.dir.dir.x);
	// normals.put(face.dir.dir.y);
	// normals.put(face.dir.dir.z);
	//
	// textures.put(face.texVerts[i].x);
	// textures.put(face.texVerts[i].y);
	// }
	// }
	//
	// vertices.flip();
	// normals.flip();
	// textures.flip();
	//
	// int vboV = glGenBuffers();
	// glBindBuffer(GL_ARRAY_BUFFER, vboV);
	// glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
	// glBindBuffer(GL_ARRAY_BUFFER, 0);
	//
	// int vboN = glGenBuffers();
	// glBindBuffer(GL_ARRAY_BUFFER, vboN);
	// glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
	// glBindBuffer(GL_ARRAY_BUFFER, 0);
	//
	// int vboT = glGenBuffers();
	// glBindBuffer(GL_ARRAY_BUFFER, vboT);
	// glBufferData(GL_ARRAY_BUFFER, textures, GL_STATIC_DRAW);
	// glBindBuffer(GL_ARRAY_BUFFER, 0);
	//
	// island.chunks[index] = new int[] { vboV, vboN, vboT };
	// }
	//
	// public static void renderChunk(int index, Island island)
	// {
	// RenderAssistant.bindTexture("graphics/textures/voxelTextures.png");
	//
	// glBindBuffer(GL_ARRAY_BUFFER, island.chunks[index][0]);
	// glVertexPointer(3, GL_FLOAT, 0, 0);
	//
	// glBindBuffer(GL_ARRAY_BUFFER, island.chunks[index][1]);
	// glNormalPointer(GL_FLOAT, 0, 0);
	//
	// glBindBuffer(GL_ARRAY_BUFFER, island.chunks[index][2]);
	// glTexCoordPointer(2, GL_FLOAT, 0, 0);
	//
	// glEnableClientState(GL_VERTEX_ARRAY);
	// glEnableClientState(GL_NORMAL_ARRAY);
	// glEnableClientState(GL_TEXTURE_COORD_ARRAY);
	// glDrawArrays(GL_QUADS, 0, 4);
	// glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	// glDisableClientState(GL_NORMAL_ARRAY);
	// glDisableClientState(GL_VERTEX_ARRAY);
	// }
	//
	// public static void initChunks(Island island)
	// {
	// long time = System.currentTimeMillis();
	//
	// for (int i = 0; i < island.chunks.length; i++)
	// {
	// updateChunk(i, island);
	// }
	//
	// CFG.p("[ChunkRenderer]: Initialized chunks on Island " + island + ". Took: " + (System.currentTimeMillis() - time) + "ms");
	// }
	
	@SuppressWarnings("unchecked")
	protected static ArrayList<VoxelFace>[] generateFaces(int cx, int cy, int cz, Island i)
	{
		ArrayList<VoxelFace> faces = new ArrayList<>();
		ArrayList<VoxelFace> transparentFaces = new ArrayList<>();
		
		for (int x = 0; x < Island.CHUNKSIZE; x++)
		{
			for (int y = 0; y < Island.CHUNKSIZE; y++)
			{
				for (int z = 0; z < Island.CHUNKSIZE; z++)
				{
					int posX = (cx * Island.CHUNKSIZE) + x;
					int posY = (cy * Island.CHUNKSIZE) + y;
					int posZ = (cz * Island.CHUNKSIZE) + z;
					
					if (i.getVoxelId(posX, posY, posZ) == 0) continue;
					
					Voxel v = Voxel.getVoxelForId(i.getVoxelId(posX, posY, posZ));
					for (Direction d : Direction.values())
					{
						Voxel w = Voxel.getVoxelForId(i.getVoxelId(posX + (int) d.dir.x, posY + (int) d.dir.y, posZ + (int) d.dir.z));
						if (!w.isOpaque() && !(w == v))
						{
							VoxelFace f = new VoxelFace(d, new Vector3f(posX, posY, posZ), v);
							if (v.isOpaque()) faces.add(f);
							else transparentFaces.add(f);
						}
					}
				}
			}
		}
		
		return new ArrayList[] { faces, transparentFaces };
	}
}
