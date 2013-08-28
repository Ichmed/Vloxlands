package com.vloxlands.gen;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.MathHelper;

public class IslandGenerator
{
	public static Island generatePerfectIsland()
	{
		long time = System.currentTimeMillis();
		
		int topLayers = (int) (Math.random() * 3 + 5);
		int radius = (int) (Math.random() * 16) + 16;
		Island island = new Island();
		
		island.setVoxel(0, 0, 0, Voxel.STONE.getId());
		
		generateBezier(new Vector2f(1, 0.3f), new Vector2f(0.8f, 0.5f), new Vector2f(0.7f, 0.5f), new Vector2f(0.6f, 0.6f), radius, 0, topLayers, Voxel.DIRT.getId(), island);
		
		int spikes = (int) (Math.random() * 10 + 10);
		
		for (int i = 0; i < spikes; i++)
		{
			Vector2f pos = getRandomPos(radius);
		}
		
		// island.setVoxel( 128, 0, 128, Voxel.STONE.getId());
		
		island.grassify();
		
		CFG.p("[IslandGenerator]: Generation took " + (System.currentTimeMillis() - time) + "ms");
		return island;
		
	}
	
	private static Vector2f getRandomPos(int radius)
	{
		Vector2f v = genRandomPos(radius);
		while (Vector2f.sub(v, new Vector2f(radius, radius), null).length() > radius)
		{
			v = genRandomPos(radius);
		}
		
		return v;
	}
	
	private static Vector2f genRandomPos(int radius)
	{
		return new Vector2f((int) (Math.random() * radius), (int) (Math.random() * radius));
	}
	
	private static void generateBezier(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3, int radius, int off, int h, byte b, Island island)
	{
		for (int i = 0; i <= h - 1; i++)
		{
			float t = i / (float) (h - 1);
			
			fillHorizontalCircle((i + off), (float) Math.floor(radius * MathHelper.bezierCurve(p0, p1, p2, p3, t).y), b, island);
		}
	}
	
	private static void fillHorizontalCircle(int h, float radius, byte b, Island island)
	{
		int x = (int) (Island.MAXSIZE / 2f - radius);
		int z = (int) (Island.MAXSIZE / 2f - radius);
		
		Vector2f center = new Vector2f(radius + x, radius + z);
		
		for (int i = x; i < radius * 2 + x; i++) // x axis
		{
			for (int j = z; j < radius * 2 + z; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(center, new Vector2f(i, j), null);
				if (distance.length() < radius) island.setVoxel(i, h, j, b);
			}
		}
	}
}
