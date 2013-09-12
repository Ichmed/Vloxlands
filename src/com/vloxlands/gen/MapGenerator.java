package com.vloxlands.gen;

import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;
import com.vloxlands.net.packet.Packet8Loading;
import com.vloxlands.net.packet.Packet9Island;

public class MapGenerator extends Thread
{
	public static enum MapSize
	{
		TINY(3),
		SMALL(5),
		MEDIUM(7),
		BIG(9),
		HUGE(12);
		
		private int size;
		
		private MapSize(int size)
		{
			this.size = size;
		}
		
		public int getSize()
		{
			return size;
		}
	}
	
	public Map map;
	public float progress, progressBefore, lastProgress;
	
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
			lastProgress = new Float(progress); // stupid reference system :(
			progress = gen.progress / (x * z) + progressBefore;
			
			if (progress != lastProgress)
			{
				try
				{
					Game.server.sendPacketToAllClients(new Packet8Loading("generatemap", progress));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			
			if (gen.finishedIsland != null)
			{
				Island i = gen.finishedIsland;
				i.setPos(new Vector3f((int) (((index - 1) / z) * size * 2 + (Math.random() * spread * 2) - spread), cacheY, (int) (((index - 1) % z) * size * 2 + (Math.random() * spread * 2) - spread)));
				try
				{
					Game.server.sendPacketToAllClients(new Packet9Island(i));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
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


// TODO: Will be finished soon
// package com.vloxlands.gen;
//
// import java.awt.Point;
// import java.util.ArrayList;
//
// import com.vloxlands.game.world.Map;
//
// public class MapGenerator extends Thread
// {
// public static enum MapSize
// {
// TINY(3),
// SMALL(5),
// MEDIUM(7),
// BIG(9),
// HUGE(12);
//
// private int size;
//
// private MapSize(int size)
// {
// this.size = size;
// }
//
// public int getSize()
// {
// return size;
// }
// }
//
// public Map map;
// public float progress, progressBefore, lastProgress;
//
// IslandGenerator gen;
// int index;
// int playerCount;
// int playableIslands;
// MapSize size;
// ArrayList<Point> takenSpots;
//
// public MapGenerator(int playerCount, MapSize size)
// {
// this.size = size;
// this.playerCount = playerCount;
// playableIslands = (int) (Math.random() * (playerCount / 5) + playerCount);
// progress = 0;
// progressBefore = 0;
// setDaemon(true);
// setName("MapGenerator-Thread");
// takenSpots = new ArrayList<>();
// }
//
// @Override
// public void run()
// {
// for (int i = 0; i < playableIslands; i++)
// {
// gen = new IslandGenerator(8,16,(float) (Math.random() * 256));
// while()
// }
// }
//
// private Point pickRandomSpot()
// {
// Point p = new Point();
//
// do
// {
// p.x = (int) (Math.random() * size.getSize());
// p.y = (int) (Math.random() * size.getSize());
// }
// while (!takenSpots.contains(p));
// takenSpots.add(p);
//
// return p;
// }
// }
