package com.vloxlands.game.voxel;

import com.vloxlands.util.Direction;

public class VoxelGrass extends Voxel
{
	@Override
	public int getTextureIndex(int x, int y, int z, int d, int meta)
	{
		if (d == Direction.UP.ordinal()) return textureIndex;
		if (d == Direction.DOWN.ordinal()) return Voxel.get("DIRT").getTextureIndex(x, y, z, d, meta);
		return textureIndex + 1;
	}
}
