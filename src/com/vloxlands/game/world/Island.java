package com.vloxlands.game.world;

public class Island
{
	int[][][] voxels = new int[65536][65536][65536];
	int[][][] metadata = new int[256][256][256];
	
	public void placeVoxel(int x, int y, int z, int id)
	{
		voxels[x][y][z] = id;
	}
}
