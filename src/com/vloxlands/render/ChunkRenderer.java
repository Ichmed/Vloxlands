package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;
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
		
		HashMap<VoxelFaceKey, VoxelFace>[] faceLists = generateFaces(cx, cy, cz, island);
		
		HashMap<VoxelFaceKey, VoxelFace> greedy0 = generateGreedyMesh(cx, cy, cz, faceLists[0]);
		HashMap<VoxelFaceKey, VoxelFace> greedy1 = generateGreedyMesh(cx, cy, cz, faceLists[1]);
		
		glPushMatrix();
		glNewList(listIndex, GL_COMPILE);
		for (VoxelFace v : greedy0.values())
			v.render();
		glEndList();
		glPopMatrix();
		
		glPushMatrix();
		glNewList(listIndex + 1, GL_COMPILE);
		for (VoxelFace v : greedy1.values())
			v.render();
		glEndList();
		glPopMatrix();
	}
	
	public static void renderChunks(Island island)
	{
		long time = System.currentTimeMillis();
		
		island.chunk0ID = glGenLists(island.chunks[0].capacity() * 2);
		for (int i = 0; i < island.chunks[0].capacity(); i++)
		{
			renderChunk(island.chunk0ID + i * 2, i, island);
			island.chunks[0].put(i);
			island.chunks[1].put(i);
		}
		
		island.chunks[0].flip();
		island.chunks[1].flip();
		
		CFG.p("[ChunkRenderer]: Rendered chunks on Island " + island + ". Took " + (System.currentTimeMillis() - time) + "ms");
	}
	
	@SuppressWarnings("unchecked")
	private static HashMap<VoxelFaceKey, VoxelFace>[] generateFaces(int cx, int cy, int cz, Island i)
	{
		HashMap<VoxelFaceKey, VoxelFace> faces = new HashMap<>();
		HashMap<VoxelFaceKey, VoxelFace> transparentFaces = new HashMap<>();
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
							VoxelFace f = new VoxelFace(d, new Vector3f(posX, posY, posZ), v.getTextureIndex());
							if (v.isOpaque()) faces.put(new VoxelFaceKey(posX, posY, posZ, d.ordinal()), f);
							else transparentFaces.put(new VoxelFaceKey(posX, posY, posZ, d.ordinal()), f);
						}
					}
				}
			}
		}
		
		
		
		return new HashMap[] { faces, transparentFaces };
	}
	
	private static HashMap<VoxelFaceKey, VoxelFace> generateGreedyMesh(int cx, int cy, int cz, HashMap<VoxelFaceKey, VoxelFace> originalMap)
	{
		HashMap<VoxelFaceKey, VoxelFace> strips0 = new HashMap<>();
		
		if (originalMap.size() == 0) return originalMap;
		
		// greedy-mode along Z - axis
		for (int x = 0; x < Island.CHUNKSIZE; x++)
		{
			for (int y = 0; y < Island.CHUNKSIZE; y++)
			{
				VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
				for (int z = 0; z < Island.CHUNKSIZE; z++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						
						int posX = cx * Island.CHUNKSIZE + x;
						int posY = cy * Island.CHUNKSIZE + y;
						int posZ = cz * Island.CHUNKSIZE + z;
						
						VoxelFaceKey key = new VoxelFaceKey(posX, posY, posZ, i);
						VoxelFace val = originalMap.get(key);
						
						if (activeStrips[i] != null)
						{
							if (val == null)
							{
								strips0.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
								activeStrips[i] = null;
							}
							else if (val.textureIndex == activeStrips[i].textureIndex)
							{
								activeStrips[i].increaseSize(0, 0, 1);
							}
							else
							{
								strips0.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
								activeStrips[i] = new VoxelFace(Direction.values()[i], new Vector3f(posX, posY, posZ), val.textureIndex);
							}
						}
						else if (val != null)
						{
							activeStrips[i] = new VoxelFace(Direction.values()[i], new Vector3f(posX, posY, posZ), val.textureIndex);
						}
					}
				}
				for (int i = 0; i < activeStrips.length; i++)
					if (activeStrips[i] != null) strips0.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
			}
		}
		
		HashMap<VoxelFaceKey, VoxelFace> strips1 = new HashMap<>();
		
		// greedy-mode along X - axis
		for (int y = 0; y < Island.CHUNKSIZE; y++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int z = 0; z < Island.CHUNKSIZE; z++)
			{
				for (int x = 0; x < Island.CHUNKSIZE; x++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						int posX = cx * Island.CHUNKSIZE + x;
						int posY = cy * Island.CHUNKSIZE + y;
						int posZ = cz * Island.CHUNKSIZE + z;
						
						VoxelFaceKey key = new VoxelFaceKey(posX, posY, posZ, i);
						VoxelFace val = strips0.get(key);
						
						
						if (val != null)
						{
							if (activeStrips[i] == null)
							{
								activeStrips[i] = new VoxelFace(val);
							}
							else
							{
								if (val.textureIndex == activeStrips[i].textureIndex && val.sizeZ == activeStrips[i].sizeZ)
								{
									activeStrips[i].increaseSize(1, 0, 0);
								}
								else
								{
									strips1.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
									
									activeStrips[i] = new VoxelFace(val);
								}
							}
						}
						else if (activeStrips[i] != null)
						{
							strips1.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
							activeStrips[i] = null;
						}
					}
				}
			}
			for (int i = 0; i < activeStrips.length; i++)
				if (activeStrips[i] != null) strips1.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
		}
		
		HashMap<VoxelFaceKey, VoxelFace> strips2 = new HashMap<>();
		
		// greedy-mode along Y - axis
		for (int x = 0; x < Island.CHUNKSIZE; x++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int z = 0; z < Island.CHUNKSIZE; z++)
			{
				for (int y = 0; y < Island.CHUNKSIZE; y++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						int posX = cx * Island.CHUNKSIZE + x;
						int posY = cy * Island.CHUNKSIZE + y;
						int posZ = cz * Island.CHUNKSIZE + z;
						
						VoxelFaceKey key = new VoxelFaceKey(posX, posY, posZ, i);
						VoxelFace val = strips1.get(key);
						
						if (val != null)
						{
							if (activeStrips[i] == null)
							{
								activeStrips[i] = new VoxelFace(val);
							}
							else
							{
								if (val.textureIndex == activeStrips[i].textureIndex && val.sizeZ == activeStrips[i].sizeZ && val.sizeX == activeStrips[i].sizeX)
								{
									activeStrips[i].increaseSize(0, 1, 0);
								}
								else
								{
									strips2.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
									
									activeStrips[i] = new VoxelFace(val);
								}
							}
						}
						else if (activeStrips[i] != null)
						{
							strips2.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
							activeStrips[i] = null;
						}
					}
				}
			}
			for (int i = 0; i < activeStrips.length; i++)
				if (activeStrips[i] != null) strips2.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
		}
		
		return strips2;
	}
}
