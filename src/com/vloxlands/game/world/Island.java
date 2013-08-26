package com.vloxlands.game.world;

import com.vloxlands.game.voxel.Voxel;

public class Island
{
	byte[][][] voxels = new byte[65536][65536][65536];
	byte[][][] voxelMetadata = new byte[256][256][256];
	
	public void placeVoxel(int x, int y, int z, byte id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);		
	}

	public void placeVoxel(int x, int y, int z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);		
	}
	
	public int getVoxelId(int x, int y, int z)
	{
		return voxels[x][y][z];
	}

	public byte getMetadata(int x, int y, int z)
	{
		return voxelMetadata[x][y][z];
	}
	
	public void setVoxel(int x, int y, int z, byte id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
	}
	
	public void setVoxel(int x, int y, int z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
	}
}
