package com.vloxlands.game.world;

import java.util.ArrayList;
import java.util.List;

import com.vloxlands.game.entity.Entity;
import com.vloxlands.render.RenderThread;

public class Map
{
	public ArrayList<Island> islands = new ArrayList<>();
	List<Entity> entities = new ArrayList<>();
	
	public static final int MAXHEIGHT = 512;
	
	public void placeVoxel(Island i, int x, int y, int z, int id)
	{}
	
	public void render()
	{
		for (Island i : islands)
			i.render();
	}
	
	public void onTick()
	{
		for (Island i : islands)
			i.onTick();
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
		new RenderThread(RenderThread.GENERATE_ALL_FACES).start();
		for (Island i : islands)
		{
			i.calculateWeight();
			i.calculateUplift();
		}
	}
	
	public static float calculateUplift(float height)
	{
		return ((1 - (height / MAXHEIGHT)) * 2) + 0.1f;
	}
}
