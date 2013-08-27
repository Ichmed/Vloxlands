package com.vloxlands.game.world;

import java.util.ArrayList;
import java.util.List;

import com.vloxlands.game.entity.Entity;
import com.vloxlands.render.RenderThread;

public class Map
{
	public ArrayList<Island> islands = new ArrayList<>();
	List<Entity> entities = new ArrayList<>();

	public void placeVoxel(Island i, int x, int y, int z, int id)
	{
	}

	public void render()
	{
		for (Island i : islands)
			i.render();
	}

	public Island[] getIslands()
	{
		return islands.toArray(new Island[] {});
	}

	public void addIsland(Island i)
	{
		islands.add(i);
	}

	public void startMap()
	{
		new RenderThread().run(RenderThread.GENERATE_ALL_FACES);
	}

	public static Map generateRandomMap()
	{
		Map map = new Map();
		int islands = 1;

		for (int i = 0; i < islands; i++)
		{

			Island island = new Island();
			int voxels = 300;

			for (int j = 0; j < voxels; j++)
			{
				short x = (short) ((Math.random() * 20));
				short y = (short) ((Math.random() * 20));
				short z = (short) ((Math.random() * 20));
				island.setVoxel(x, y, z, (byte) ((Math.random() * 4) -128), (byte) 0);
			}

			map.addIsland(island);
		}
		return map;
	}
}
