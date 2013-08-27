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
			map.addIsland(IslandGenerator.generatePerfectIsland());
		}
		return map;
	}
	
}
