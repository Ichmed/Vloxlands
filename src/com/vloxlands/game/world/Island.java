package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.Arrays;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.render.IslandRenderer;
import com.vloxlands.render.VoxelFace;

public class Island
{
	public static final int MAXSIZE = 256;
	
	byte[][][] voxels = new byte[MAXSIZE][MAXSIZE][MAXSIZE];
	byte[][][] voxelMetadata = new byte[MAXSIZE][MAXSIZE][MAXSIZE];
	
	public IslandRenderer renderer = new IslandRenderer();
	
	Vector3f pos;
	
	public float weight, uplift, initBalance = 0;
	
	public Island()
	{
		pos = new Vector3f(0, 0, 0);
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				for (int k = 0; k < MAXSIZE; k++)
				{
					voxels[i][j][k] = Voxel.AIR.getId();
					voxelMetadata[i][j][k] = -128;
				}
			}
		}
	}
	
	@Override
	public Island clone()
	{
		Island island = new Island();
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				island.voxels[i][j] = Arrays.copyOf(voxels[i][j], MAXSIZE);
			}
		}
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				island.voxelMetadata[i][j] = Arrays.copyOf(voxelMetadata[i][j], MAXSIZE);
			}
		}
		return island;
	}
	
	public void onTick()
	{
		pos.translate(0, (uplift * Map.calculateUplift(pos.y) - weight) / 100000f - initBalance, 0);
	}
	
	public void calculateWeight()
	{
		weight = 0;
		for (int x = 0; x < 256; x++)
		{
			for (int y = 0; y < 256; y++)
			{
				for (int z = 0; z < 256; z++)
				{
					if (getVoxelId(x, y, z) == 0) continue;
					weight += Voxel.getVoxelForId(getVoxelId(x, y, z)).getWeight();
				}
			}
		}
	}
	
	public void calculateUplift()
	{
		uplift = 0;
		for (int x = 0; x < 256; x++)
		{
			for (int y = 0; y < 256; y++)
			{
				for (int z = 0; z < 256; z++)
				{
					if (getVoxelId(x, y, z) == 0) continue;
					uplift += Voxel.getVoxelForId(getVoxelId(x, y, z)).getUplift();
				}
			}
		}
	}
	
	public void placeVoxel(int x, int y, int z, byte id)
	{
		placeVoxel(x, y, z, id, (byte) 0);
	}
	
	public void placeVoxel(int x, int y, int z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);
		weight += Voxel.getVoxelForId(id).getWeight();
		uplift += Voxel.getVoxelForId(id).getUplift();
	}
	
	public void removeVoxel(int x, int y, int z)
	{
		Voxel v = Voxel.getVoxelForId(getVoxelId(x, y, z));
		setVoxel(x, y, z, Voxel.AIR.getId());
		weight -= v.getWeight();
		uplift -= v.getUplift();
	}
	
	public byte getVoxelId(int x, int y, int z)
	{
		if (x >= Island.MAXSIZE || y >= Island.MAXSIZE || z >= Island.MAXSIZE || x < 0 || y < 0 || z < 0) return 0;
		return voxels[x][y][z];
	}
	
	public byte getMetadata(int x, int y, int z)
	{
		return voxelMetadata[x][y][z];
	}
	
	public void setVoxel(int x, int y, int z, byte id)
	{
		if (x >= Island.MAXSIZE || y >= Island.MAXSIZE || z >= Island.MAXSIZE || x < 0 || y < 0 || z < 0) return;
		
		voxels[x][y][z] = id;
	}
	
	public void setVoxel(int x, int y, int z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
	}
	
	public void setVoxelMetadata(int x, int y, int z, byte metadata)
	{
		voxelMetadata[x][y][z] = metadata;
	}
	
	public byte[] getVoxels()
	{
		byte[] bytes = new byte[(int) Math.pow(MAXSIZE, 3)];
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				for (int k = 0; k < MAXSIZE; k++)
				{
					bytes[(i * MAXSIZE + j) * MAXSIZE + k] = voxels[i][j][k];
				}
			}
		}
		return bytes;
	}
	
	public byte[] getVoxelMetadatas()
	{
		byte[] bytes = new byte[(int) Math.pow(MAXSIZE, 3)];
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				for (int k = 0; k < MAXSIZE; k++)
				{
					bytes[(i * MAXSIZE + j) * MAXSIZE + k] = voxelMetadata[i][j][k];
				}
			}
		}
		return bytes;
	}
	
	public void render()
	{
		glTranslatef(pos.x, pos.y, pos.z);
		for (VoxelFace f : this.renderer.faces.values())
			f.render();
		for (VoxelFace f : this.renderer.transparentFaces.values())
			f.render();
	}
	
	public Vector3f getPos()
	{
		return pos;
	}
	
	public void setPos(Vector3f pos)
	{
		this.pos = pos;
	}
	
	public int grassify()
	{
		int grassed = 0;
		for (int i = 0; i < Island.MAXSIZE; i++)
		{
			for (int j = 0; j < Island.MAXSIZE; j++)
			{
				for (int k = 0; k < Island.MAXSIZE; k++)
				{
					if (getVoxelId(i, j, k) == Voxel.DIRT.getId())
					{
						if (getVoxelId(i, j + 1, k) == Voxel.AIR.getId())
						{
							grassed++;
							setVoxel(i, j, k, Voxel.GRASS.getId());
						}
					}
				}
			}
		}
		
		return grassed;
	}
}
