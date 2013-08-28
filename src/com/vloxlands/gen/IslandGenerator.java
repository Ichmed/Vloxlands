package com.vloxlands.gen;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.MathHelper;

public class IslandGenerator
{
	public static final float[] ISLAND_BEZIER = new float[] { 0.6f, 0.6f, 0.7f, 0.5f, 0.8f, 0.5f, 1, 0.3f };
	public static final float[] SPIKE_BEZIER = new float[] { 1, 1, 0.5f, 0.1f, 1, 1, 0, 0 };
	
	
	public static Island generatePerfectIsland()
	{
		long time = System.currentTimeMillis();
		
		int radius = (int) (Math.random() * 32) + 32;
		int topLayers = (int) (((float) Math.random() * 3 + 3) + radius / 8.0f);
		Island island = new Island();
		
		island.setVoxel(0, 0, 0, Voxel.STONE.getId());
		
		int x = Island.MAXSIZE / 2;
		int z = Island.MAXSIZE / 2;
		
		generateBezier(ISLAND_BEZIER, x, z, radius, 128, topLayers, Voxel.DIRT.getId(), true, island);
		//
		// int spikes = (int) (Math.random() * 10 + 10);
		//
		for (int i = 0; i < 40; i++)
		{
			int MAXRAD = (int) ((radius * 0.3f) + 2);
			int rad = (int) Math.round(Math.random() * (radius * 0.3f)) + 2;
			
			int radiusAt0 = (int) (MathHelper.bezierCurve(ISLAND_BEZIER, 0).y * radius);
			
			Vector2f pos = MathHelper.getRandomCircleInCircle(new Vector2f(x, z), radiusAt0, rad);// (radius);
			int h = (int) (((MAXRAD - rad) * (radiusAt0 - Vector2f.sub(pos, new Vector2f(x, z), null).length()) + topLayers) * 0.3f);
			island.setVoxel((int) pos.x, 127, (int) pos.y, Voxel.STONE.getId());
			generateBezier(SPIKE_BEZIER, (int) pos.x, (int) pos.y /* Z */, rad, 127, h, Voxel.STONE.getId(), false, island);
		}
		
		island.grassify();
		
		CFG.p("[IslandGenerator]: Generation took " + (System.currentTimeMillis() - time) + "ms");
		return island;
	}
	
	private static void generateBezier(float[] c, int x, int z, int radius, int off, int h, byte b, boolean force, Island island)
	{
		for (int i = 0; i < h; i++)
		{
			float t = i / (float) h;
			
			float rad = (float) Math.floor(radius * MathHelper.bezierCurve(c, t).y);
			
			fillHorizontalCircle(x, off - i, z, rad, b, force, island);
		}
	}
	
	private static void fillHorizontalCircle(int x, int y, int z, float radius, byte b, boolean force, Island island)
	{
		Vector2f center = new Vector2f(x, z);
		
		for (int i = 0; i < Island.MAXSIZE; i++) // x axis
		{
			for (int j = 0; j < Island.MAXSIZE; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(new Vector2f(i, j), center, null);
				if (distance.length() < radius)
				{
					// CFG.p(i + " " + y + " " + j);
					if (force || (island.getVoxelId(i, y, j) == Voxel.AIR.getId() && !force)) island.setVoxel(i, y, j, b);
				}
				
			}
		}
	}
}
