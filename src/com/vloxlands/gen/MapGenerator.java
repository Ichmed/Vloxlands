package com.vloxlands.gen;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;

public class MapGenerator extends Thread
{
	public Map map;
	public float progress, progressBefore;
	
	IslandGenerator gen;
	int z, x, spread, size, index;
	float cacheY;
	
	public MapGenerator(int x, int z, int spread, int size)
	{
		this.x = x;
		this.z = z;
		this.spread = spread;
		this.size = size;
		progress = 0;
		progressBefore = 0;
		setDaemon(true);
		setName("MapGenerator-Thread");
	}
	
	@Override
	public void run()
	{
		map = new Map();
		while (!isDone())
		{
			if (gen == null)
			{
				progressBefore = progress;
				cacheY = (float) ((Math.random() * spread * 2) - spread);
				gen = new IslandGenerator((int) (size * 0.75), (int) (size * 1.25), cacheY);
				gen.start();
				index++;
			}
			
			System.out.print(""); // don't why this is needed
			
			progress = gen.progress / (x * z) + progressBefore;
			if (gen.finishedIsland != null)
			{
				Island i = gen.finishedIsland;
				i.setPos(new Vector3f((int) (((index - 1) / z) * size * 2 + (Math.random() * spread * 2) - spread), cacheY, (int) (((index - 1) % z) * size * 2 + (Math.random() * spread * 2) - spread)));
				
				map.addIsland(i);
				gen = null;
			}
		}
	}
	
	public boolean isDone()
	{
		if (map == null) return false;
		return map.islands.size() == (x * z);
	}
}
