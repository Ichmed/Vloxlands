package com.vloxlands.game.voxel;

import com.vloxlands.util.Direction;

public class VoxelDouble extends Voxel
{
	@Override
	public int getTextureIndex(int x, int y, int z, int d, int meta)
	{
		if (d == Direction.UP.ordinal() || d == Direction.DOWN.ordinal()) return textureIndex;
		return textureIndex + 1;
	}
}
