package com.vloxlands.gen;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.MathHelper;

public class IslandGenerator
{
	public static final float[] ISLAND_BEZIER = new float[] { 0.6f, 0.6f, 0.7f, 0.5f, 0.8f, 0.5f, 1, 0.3f };
	public static final float[] SPIKE_BEZIER = new float[] { 1, 1, 0.5f, 0.1f, 1, 1, 0, 0 };
	
	public static final int START_Y = 128;
	public static final Voxel[] CRYSTALS = { Voxel.STRONG_CRYSTAL, Voxel.MEDIUM_CRYSTAL, Voxel.WEAK_CRYSTAL };
	
	public static Island generatePerfectIsland()
	{
		CFG.p("------------------------------------------------------------------");
		long time = System.currentTimeMillis();
		
		int radius = (int) (Math.random() * 32) + 32;
		int topLayers = (int) (((float) Math.random() * 3 + 3) + radius / 8.0f);
		Island island = new Island();
		
		
		int x = Island.MAXSIZE / 2;
		int z = Island.MAXSIZE / 2;
		
		// int spikes = (int) (Math.random() * 10 + 10);
		//
		
		int dirt = 0;
		int stone = 0;
		int grass = 0;
		
		dirt += generateBezier(ISLAND_BEZIER, x, z, radius, START_Y, topLayers, Voxel.DIRT.getId(), true, island);
		
		for (int i = 0; i < 20; i++)
		{
			int MAXRAD = (int) ((radius * 0.3f) + 2);
			int rad = (int) Math.round(Math.random() * (radius * 0.3f)) + 3;
			
			int radiusAt0 = (int) (MathHelper.bezierCurve(ISLAND_BEZIER, 0).y * radius);
			
			Vector2f pos = MathHelper.getRandomCircleInCircle(new Vector2f(x, z), radiusAt0, rad);// (radius);
			int h = (int) (((MAXRAD - rad) * (radiusAt0 - Vector2f.sub(pos, new Vector2f(x, z), null).length()) + topLayers) * 0.3f);
			island.setVoxel((int) pos.x, 127, (int) pos.y, Voxel.STONE.getId());
			stone += generateBezier(SPIKE_BEZIER, (int) pos.x, (int) pos.y /* Z */, rad, START_Y - 1, h, Voxel.STONE.getId(), false, island);
		}
		
		grass = island.grassify();
		dirt -= grass;
		
		island.calculateWeight();
		
		float weightNeededToUplift = island.weight / Map.calculateUplift(0);
		
		int[] amounts = new int[CRYSTALS.length];
		
		for (int i = 0; i < amounts.length; i++)
		{
			amounts[i] = (int) (weightNeededToUplift / CRYSTALS[i].getUplift());
			weightNeededToUplift %= CRYSTALS[i].getUplift();
		}
		
		CFG.p("uplift left after generating: " + weightNeededToUplift);
		
		int crystals = placeCrystals(amounts, island);
		
		CFG.p("[IslandGenerator]: Generation took " + (System.currentTimeMillis() - time) + "ms, Voxels: " + (stone + dirt + grass + crystals));
		CFG.p("------------------------------------------------------------------");
		return island;
	}
	
	private static int placeCrystals(int[] amounts, Island island)
	{
		int voxels = 0;
		for (int j = 0; j < amounts.length; j++)
		{
			for (int i = 0; i < amounts[j]; i++)
			{
				Vector3f v = pickRandomNaturalVoxel(island);
				island.setVoxel((int) v.x, (int) v.y, (int) v.z, CRYSTALS[j].getId());
				voxels++;
			}
		}
		return voxels;
	}
	
	private static Vector3f pickRandomNaturalVoxel(Island island)
	{
		ArrayList<Byte> naturalVoxels = new ArrayList<>();
		naturalVoxels.add(Voxel.STONE.getId());
		naturalVoxels.add(Voxel.DIRT.getId());
		
		Vector3f v = new Vector3f();
		do
		{
			v = new Vector3f((int) Math.round(Math.random() * Island.MAXSIZE), (int) Math.round(Math.random() * (Island.MAXSIZE - START_Y)), (int) Math.round(Math.random() * Island.MAXSIZE));
		}
		while (!naturalVoxels.contains((Byte) island.getVoxelId((int) v.x, (int) v.y, (int) v.z)));
		
		return v;
	}
	
	private static int generateBezier(float[] c, int x, int z, int radius, int off, int h, byte b, boolean force, Island island)
	{
		int voxels = 0;
		for (int i = 0; i < h; i++)
		{
			float t = i / (float) h;
			
			float rad = (float) Math.floor(radius * MathHelper.bezierCurve(c, t).y);
			
			voxels += fillHorizontalCircle(x, off - i, z, rad, b, force, island);
		}
		
		return voxels;
	}
	
	private static int fillHorizontalCircle(int x, int y, int z, float radius, byte b, boolean force, Island island)
	{
		Vector2f center = new Vector2f(x, z);
		int voxels = 0;
		for (int i = 0; i < Island.MAXSIZE; i++) // x axis
		{
			for (int j = 0; j < Island.MAXSIZE; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(new Vector2f(i, j), center, null);
				if (distance.length() < radius)
				{
					if (force || (!force && island.getVoxelId(i, y, j) == Voxel.AIR.getId()))
					{
						voxels++;
						island.setVoxel(i, y, j, b);
					}
				}
			}
		}
		
		return voxels;
	}
}
