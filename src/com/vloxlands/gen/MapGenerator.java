package com.vloxlands.gen;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;

public class MapGenerator
{
	public static Map generateRandomMap()
	{
		Map map = new Map();
		int islands = 1;
		
		for (int i = 0; i < islands; i++)
		{
			
			Island island = new Island();
			int voxels = 20;
			
			for (int j = 0; j < voxels; j++)
			{
				island.setVoxel((short) j, (short) 0, (short) 0, Voxel.STONE.getId(), (byte) 0);
			}
			
			map.addIsland(island);
		}
		return map;
	}
	
}
