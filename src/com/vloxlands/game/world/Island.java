package com.vloxlands.game.world;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.render.VoxelFace;
import com.vloxlands.util.Direction;

public class Island
{
	public static final int MAXSIZE = 256;
	
	byte[][][] voxels = new byte[MAXSIZE][MAXSIZE][MAXSIZE];
	byte[][][] voxelMetadata = new byte[MAXSIZE][MAXSIZE][MAXSIZE];
	
	public ArrayList<VoxelFace> faces = new ArrayList<>();
	public ArrayList<VoxelFace> transparentFaces = new ArrayList<>();
	
	Vector3f pos;
	
	public float weight, uplift;
	
	public Island()
	{
		pos = new Vector3f(0, 0, 0);
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				for (int k = 0; k < MAXSIZE; k++)
				{
					voxels[i][j][k] = -128;
					voxelMetadata[i][j][k] = -128;
				}
			}
		}
	}
	
	public void onTick()
	{
		this.pos.translate(0, (uplift * Game.currentMap.calculateUplift(this.pos.y) - this.weight) / 100000f, 0);
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
					if (this.getVoxelId(x, y, z) == 0) continue;
					weight += Voxel.getVoxelForId(this.getVoxelId(x, y, z)).getWeight();
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
					if (this.getVoxelId(x, y, z) == 0) continue;
					uplift += Voxel.getVoxelForId(this.getVoxelId(x, y, z)).getUplift();
				}
			}
		}
	}
	
	public void placeVoxel(short x, short y, short z, byte id)
	{
		this.placeVoxel(x, y, z, id, (byte)0);
	}
	
	public void placeVoxel(short x, short y, short z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);
		this.weight += Voxel.getVoxelForId(id).getWeight();
		this.uplift += Voxel.getVoxelForId(id).getUplift();
	}
	
	public void removeVoxel(short x, short y, short z)
	{
		Voxel v = Voxel.getVoxelForId(this.getVoxelId(x, y, z));
		this.setVoxel(x, y, z, Voxel.AIR.getId());
		this.weight -= v.getWeight();
		this.uplift -= v.getUplift();
	}
	
	public short getVoxelId(short x, short y, short z)
	{
		if (x >= Island.MAXSIZE || y >= Island.MAXSIZE || z >= Island.MAXSIZE || x < 0 || y < 0 || z < 0) return 0;
		return (short) (voxels[x][y][z] + 128);
	}
	
	public int getVoxelId(int x, int y, int z)
	{
		return getVoxelId((short) x, (short) y, (short) z);
	}
	
	public byte getMetadata(short x, short y, short z)
	{
		return voxelMetadata[x][y][z];
	}
	
	public void setVoxel(short x, short y, short z, byte id)
	{
		this.setVoxel(x, y, z, id, (byte)0);
	}
	
	public void setVoxel(short x, short y, short z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
	}
	
	public void setVoxelMetadata(short x, short y, short z, byte metadata)
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
		glTranslatef(this.pos.x, this.pos.y, this.pos.z);
		for (VoxelFace f : faces)
			f.render();
		for (VoxelFace f : transparentFaces)
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
}
