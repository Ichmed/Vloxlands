package com.vloxlands.game.world;

import com.vloxlands.game.voxel.Voxel;

public class Island
{
	public static final int MAXSIZE = 256;

	byte[][][] voxels = new byte[MAXSIZE][MAXSIZE][MAXSIZE];
	byte[][][] voxelMetadata = new byte[MAXSIZE][MAXSIZE][MAXSIZE];

	short width = MAXSIZE;
	short height = MAXSIZE;
	short depth = MAXSIZE;

	public Island()
	{
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

	public void placeVoxel(short x, short y, short z, byte id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);
	}

	public void placeVoxel(short x, short y, short z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);
	}

	public short getVoxelId(short x, short y, short z)
	{
		return voxels[x][y][z];
	}

	public byte getMetadata(short x, short y, short z)
	{
		return voxelMetadata[x][y][z];
	}

	public void setVoxel(short x, short y, short z, byte id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
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
					bytes[i * MAXSIZE + j * MAXSIZE + k] = voxels[i][j][k];
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
					bytes[i * MAXSIZE + j * MAXSIZE + k] = voxelMetadata[i][j][k];
				}
			}
		}
		return bytes;
	}

	public short getWidth()
	{
		return width;
	}

	public void setWidth(short width)
	{
		this.width = width;
	}

	public short getHeight()
	{
		return height;
	}

	public void setHeight(short height)
	{
		this.height = height;
	}

	public short getDepth()
	{
		return depth;
	}

	public void setDepth(short depth)
	{
		this.depth = depth;
	}
}
