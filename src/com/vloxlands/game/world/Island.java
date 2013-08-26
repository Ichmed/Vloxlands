package com.vloxlands.game.world;

import com.vloxlands.game.voxel.Voxel;

public class Island
{
	int[][][] voxels = new int[65536][65536][65536];
	int[][][] voxelMetadata = new int[256][256][256];
	
	public void placeVoxel(int x, int y, int z, int id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);		
	}

	public void placeVoxel(int x, int y, int z, int id, int metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);		
	}
	
	public void setVoxel(int x, int y, int z, int id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
	}
	
	public void setVoxel(int x, int y, int z, int id, int metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
	}
}
