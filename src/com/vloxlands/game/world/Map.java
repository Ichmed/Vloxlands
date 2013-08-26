package com.vloxlands.game.world;

import java.util.ArrayList;
import java.util.List;

import com.vloxlands.game.entity.Entity;

public class Map
{
	ArrayList<Island> islands = new ArrayList<>();
	List<Entity> entities = new ArrayList<>();

	public void placeVoxel(Island i, int x, int y, int z, int id)
	{
	}

	public Island[] getIslands()
	{
		return islands.toArray(new Island[] {});
	}

	public void addIsland(Island i)
	{
		islands.add(i);
	}

	public static Map generateRandomMap()
	{
		Map map = new Map();
		int islands = 10;

		for (int i = 0; i < islands; i++)
		{
			
			Island island = new Island();
			int voxels = 10000;

			for (int j = 0; j < voxels; j++)
			{
				short x = (short) (Math.random() * Island.MAXSIZE);
				short y = (short) (Math.random() * Island.MAXSIZE);
				short z = (short) (Math.random() * Island.MAXSIZE);
				island.setVoxel(x, y, z, (byte) (Math.random() * 255), (byte) (Math.random() * 255));
			}

			map.addIsland(island);
		}
		return map;
	}
}
