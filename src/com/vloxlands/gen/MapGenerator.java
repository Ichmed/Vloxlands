package com.vloxlands.gen;

import com.vloxlands.game.world.Map;

public class MapGenerator
{
	public static Map generateRandomMap()
	{
		Map map = new Map();
		int islands = 1;
		
		for (int i = 0; i < islands; i++)
		{
//			Island island = new Island();
//			island.setVoxel(128,128,128, Voxel.get("STONE").getId());
//			map.addIsland(island);
			map.addIsland(IslandGenerator.generateIsland());
		}
		return map;
	}
	
}
