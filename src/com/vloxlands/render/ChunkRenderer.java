package com.vloxlands.render;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Chunk;
import com.vloxlands.game.world.Island;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;
import com.vloxlands.util.Direction;

public class ChunkRenderer
{
	public static void renderChunks(Island island)
	{
		ArrayList<Chunk> chunks = new ArrayList<>(island.getChunks());
		for (int i = 0; i < chunks.size(); i++)
		{
			chunks.get(i).updateMesh();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<VoxelFaceKey, VoxelFace>[] generateFaces(int cx, int cy, int cz, Island i)
	{
		HashMap<VoxelFaceKey, VoxelFace> faces = new HashMap<>();
		HashMap<VoxelFaceKey, VoxelFace> transparentFaces = new HashMap<>();
		for (int x = 0; x < Chunk.SIZE; x++)
		{
			for (int y = 0; y < Chunk.SIZE; y++)
			{
				for (int z = 0; z < Chunk.SIZE; z++)
				{
					int posX = cx * Chunk.SIZE + x;
					int posY = cy * Chunk.SIZE + y;
					int posZ = cz * Chunk.SIZE + z;
					
					if (i.getVoxelId(posX, posY, posZ) == Voxel.get("AIR").getId()) continue;
					Voxel v = Voxel.getVoxelForId(i.getVoxelId(posX, posY, posZ));
					for (Direction d : Direction.values())
					{
						int x1 = posX + (int) d.dir.x;
						int y1 = posY + (int) d.dir.y;
						int z1 = posZ + (int) d.dir.z;
						Voxel w = null;
						if (x1 >= 0 && y1 >= 0 && z1 >= 0 && x1 < Island.SIZE && y1 < Island.SIZE && z1 < Island.SIZE) w = Voxel.getVoxelForId(i.getVoxelId(x1, y1, z1));
						
						if (w == null || !w.isOpaque() && !(w == v))
						{
							VoxelFace f = new VoxelFace(d, new Vector3f(posX, posY, posZ), v.getTextureIndex(posX, posY, posZ, d.ordinal(), i.getMetadata(posX, posY, posZ)));
							if (v.isOpaque() || w == null) faces.put(new VoxelFaceKey(posX, posY, posZ, d.ordinal()), f);
							else transparentFaces.put(new VoxelFaceKey(posX, posY, posZ, d.ordinal()), f);
						}
					}
				}
			}
		}
		
		return new HashMap[] { faces, transparentFaces };
	}
	
	public static HashMap<VoxelFaceKey, VoxelFace> generateGreedyMesh(int cx, int cy, int cz, HashMap<VoxelFaceKey, VoxelFace> originalMap)
	{
		HashMap<VoxelFaceKey, VoxelFace> strips0 = new HashMap<>();
		
		if (originalMap.size() == 0) return originalMap;
		
		// greedy-mode along Z - axis
		for (int x = 0; x < Chunk.SIZE; x++)
		{
			for (int y = 0; y < Chunk.SIZE; y++)
			{
				VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
				for (int z = 0; z < Chunk.SIZE; z++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						
						int posX = cx * Chunk.SIZE + x;
						int posY = cy * Chunk.SIZE + y;
						int posZ = cz * Chunk.SIZE + z;
						
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
		for (int y = 0; y < Chunk.SIZE; y++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int z = 0; z < Chunk.SIZE; z++)
			{
				for (int x = 0; x < Chunk.SIZE; x++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						int posX = cx * Chunk.SIZE + x;
						int posY = cy * Chunk.SIZE + y;
						int posZ = cz * Chunk.SIZE + z;
						
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
								if (val.textureIndex == activeStrips[i].textureIndex && val.sizeZ == activeStrips[i].sizeZ && val.pos.z == activeStrips[i].pos.z)
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
		for (int x = 0; x < Chunk.SIZE; x++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int z = 0; z < Chunk.SIZE; z++)
			{
				for (int y = 0; y < Chunk.SIZE; y++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						int posX = cx * Chunk.SIZE + x;
						int posY = cy * Chunk.SIZE + y;
						int posZ = cz * Chunk.SIZE + z;
						
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
								if (val.textureIndex == activeStrips[i].textureIndex && val.sizeZ == activeStrips[i].sizeZ && val.sizeX == activeStrips[i].sizeX && val.pos.x == activeStrips[i].pos.x && val.pos.z == activeStrips[i].pos.z)
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
