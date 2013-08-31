package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Direction;

public class ChunkRenderer
{
	public static void renderChunk(int listIndex, int index, Island island)
	{
		int cs = Island.SIZE / Island.CHUNKSIZE;
		int cx = index / (cs * cs);
		int cy = index / cs % cs;
		int cz = index % cs;
		
		HashMap<ArrayList<Integer>, VoxelFace>[] faceLists = generateFaces(cx, cy, cz, island);
		
		genereateGreedyMesh(cx, cy, cz, faceLists[0]);
		
		glPushMatrix();
		glNewList(listIndex, GL_COMPILE);
		for (VoxelFace v : faceLists[0].values())
			v.render();
		glEndList();
		glPopMatrix();
		
		glPushMatrix();
		glNewList(listIndex + 1, GL_COMPILE);
		for (VoxelFace v : faceLists[1].values())
			v.render();
		glEndList();
		glPopMatrix();
	}
	
	public static void initChunks(Island island)
	{
		island.chunk0ID = glGenLists(island.chunks[0].capacity() * 2);
		for (int i = 0; i < island.chunks[0].capacity(); i++)
		{
			renderChunk(island.chunk0ID + i * 2, i, island);
			island.chunks[0].put(i);
			island.chunks[1].put(i);
		}
		
		island.chunks[0].flip();
		island.chunks[1].flip();
		CFG.p("[ChunkRenderer]: Initialized chunks on Island " + island);
	}
	
	@SuppressWarnings("unchecked")
	protected static HashMap<ArrayList<Integer>, VoxelFace>[] generateFaces(int cx, int cy, int cz, Island i)
	{
		HashMap<ArrayList<Integer>, VoxelFace> faces = new HashMap<>();
		HashMap<ArrayList<Integer>, VoxelFace> transparentFaces = new HashMap<>();
		for (int x = 0; x < Island.CHUNKSIZE; x++)
		{
			for (int y = 0; y < Island.CHUNKSIZE; y++)
			{
				for (int z = 0; z < Island.CHUNKSIZE; z++)
				{
					int posX = cx * Island.CHUNKSIZE + x;
					int posY = cy * Island.CHUNKSIZE + y;
					int posZ = cz * Island.CHUNKSIZE + z;
					
					if (i.getVoxelId(posX, posY, posZ) == Voxel.get("AIR").getId()) continue;
					Voxel v = Voxel.getVoxelForId(i.getVoxelId(posX, posY, posZ));
					for (Direction d : Direction.values())
					{
						Voxel w = Voxel.getVoxelForId(i.getVoxelId(posX + (int) d.dir.x, posY + (int) d.dir.y, posZ + (int) d.dir.z));
						if (!w.isOpaque() && !(w == v))
						{
							VoxelFace f = new VoxelFace(d, new Vector3f(posX, posY, posZ), v);
							if (v.isOpaque()) faces.put(getVoxelFaceKey(x, y, z, d.ordinal()), f);
							else transparentFaces.put(getVoxelFaceKey(x, y, z, d.ordinal()), f);
						}
					}
				}
			}
		}
		return new HashMap[] { faces, transparentFaces };
	}
	
	protected static HashMap<ArrayList<Integer>, VoxelFace> genereateGreedyMesh(int cx, int cy, int cz, HashMap<ArrayList<Integer>, VoxelFace> originalMap)
	{
		for (Direction d : Direction.values())
		{
			for (int x = 0; x < Island.CHUNKSIZE; x++)
			{
				for (int x1 = 0; x1 < Island.CHUNKSIZE; x1++)
				{
					for (int y1 = 0; y1 < Island.CHUNKSIZE; y1++)
					{
						for (int z1 = 0; z1 < Island.CHUNKSIZE; z1++)
						{
							
						}
					}
				}
			}
			
			for (int y = 0; y < Island.CHUNKSIZE; y++)
			{				
				for (int x1 = 0; x1 < Island.CHUNKSIZE; x1++)
				{
					for (int y1 = 0; y1 < Island.CHUNKSIZE; y1++)
					{
						for (int z1 = 0; z1 < Island.CHUNKSIZE; z1++)
						{	
							
						}
					}
				}
			}
			
			for (int z = 0; z < Island.CHUNKSIZE; z++)
			{				
				for (int x1 = 0; x1 < Island.CHUNKSIZE; x1++)
				{
					for (int y1 = 0; y1 < Island.CHUNKSIZE; y1++)
					{
						for (int z1 = 0; z1 < Island.CHUNKSIZE; z1++)
						{	
							
						}
					}
				}
			}
		}
	}
	
	protected static ArrayList<Integer> getVoxelFaceKey(int x, int y, int z, int d)
	{
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(x);
		l.add(y);
		l.add(z);
		l.add(d);
		return l;
	}
}
