package com.vloxlands.game.voxel;

import com.vloxlands.util.Direction;

public class VoxelGrass extends Voxel
{
	@Override
	public int getTextureIndex(int x, int y, int z, int d, int meta)
	{
		if (d == Direction.UP.ordinal()) return 2;
		if (d == Direction.DOWN.ordinal()) return 66;
		return 34;
	}
}
