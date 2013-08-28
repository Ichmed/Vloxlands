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
	public static final float MIN_VEIN_DISTANCE = 30;
	public static final Voxel[] CRYSTALS = { Voxel.STRONG_CRYSTAL, Voxel.MEDIUM_CRYSTAL, Voxel.WEAK_CRYSTAL };
	
	private static Island currentIsland;
	private static ArrayList<Vector3f> veinCenters;
	
	public static Island generatePerfectIsland()
	{
		long time = System.currentTimeMillis();
		
		int radius = (int) (Math.random() * 32) + 32;
		int topLayers = (int) (((float) Math.random() * 3 + 3) + radius / 8.0f);
		
		currentIsland = new Island();
		veinCenters = new ArrayList<>();
		
		int x = Island.MAXSIZE / 2;
		int z = Island.MAXSIZE / 2;
		
		// int spikes = (int) (Math.random() * 10 + 10);
		//
		
		generateBezier(ISLAND_BEZIER, x, z, radius, START_Y, topLayers, Voxel.DIRT.getId(), true);
		
		for (int i = 0; i < 20; i++)
		{
			int MAXRAD = (int) ((radius * 0.3f) + 2);
			int rad = (int) Math.round(Math.random() * (radius * 0.3f)) + 3;
			
			int radiusAt0 = (int) (MathHelper.bezierCurve(ISLAND_BEZIER, 0).y * radius);
			
			Vector2f pos = MathHelper.getRandomCircleInCircle(new Vector2f(x, z), radiusAt0, rad);// (radius);
			int h = (int) (((MAXRAD - rad) * (radiusAt0 - Vector2f.sub(pos, new Vector2f(x, z), null).length()) + topLayers) * 0.3f);
			currentIsland.setVoxel((int) pos.x, 127, (int) pos.y, Voxel.STONE.getId());
			generateBezier(SPIKE_BEZIER, (int) pos.x, (int) pos.y /* Z */, rad, START_Y - 1, h, Voxel.STONE.getId(), false);
		}
		
		currentIsland.calculateWeight();
		
		float weightNeededToUplift = currentIsland.weight / Map.calculateUplift(0);
		while (weightNeededToUplift > 100)
		{
			int index = (int) (Math.random() * CRYSTALS.length);
			weightNeededToUplift -= createCrystalVein(index);
		}
		int[] amounts = new int[CRYSTALS.length];
		
		for (int i = 0; i < amounts.length; i++)
		{
			amounts[i] = (int) (weightNeededToUplift / CRYSTALS[i].getUplift());
			weightNeededToUplift %= CRYSTALS[i].getUplift();
			
		}
		placeCrystals(amounts);
		
		currentIsland.calculateWeight();
		currentIsland.calculateUplift();
		
		currentIsland.initBalance = (currentIsland.uplift * Map.calculateUplift(0) - currentIsland.weight) / 100000f;
		
		currentIsland.grassify();
		
		CFG.p("[IslandGenerator]: Generation took " + (System.currentTimeMillis() - time) + "ms");
		return currentIsland;
	}
	
	private static void placeCrystals(int[] amounts)
	{
		for (int j = 0; j < amounts.length; j++)
		{
			for (int i = 0; i < amounts[j]; i++)
			{
				Vector3f v = pickRandomNaturalVoxel();
				currentIsland.setVoxel((int) v.x, (int) v.y, (int) v.z, CRYSTALS[j].getId());
			}
		}
	}
	
	/**
	 * @return uplifted
	 */
	private static float createCrystalVein(int index)
	{
		int type = 0;// (int) (Math.random() * 3);
		int width = 0, height = 0, depth = 0;
		
		Vector3f c = pickRandomNaturalVoxel();
		
		float uplifted = 0;
		
		switch (type)
		{
			case 0: // qubic
			{
				depth = height = width = (int) ((Math.random() * 3) + (index + 1)* 2);
				
				float maxDistance = (float) (width * Math.sqrt(3)) / 2;
				
				for (int i = (int) (c.x - width * .5f); i < c.x + width * .5f; i++)
				{
					for (int j = (int) (c.y - height * .5f); j < c.y + height * .5f; j++)
					{
						for (int k = (int) (c.z - depth * .5f); k < c.z + depth * .5f; k++)
						{
							if (Math.random() * maxDistance > Vector3f.sub(new Vector3f(i, j, k), c, null).length())
							{
								uplifted += CRYSTALS[index].getUplift();
								if (currentIsland.getVoxelId(i, j, k) != Voxel.AIR.getId()) uplifted += Voxel.getVoxelForId(currentIsland.getVoxelId(i, j, k)).getWeight();
								
								currentIsland.setVoxel(i, j, k, CRYSTALS[index].getId());
							}
						}
					}
				}
				break;
			}
		}
		
		veinCenters.add(c);
		return uplifted;
	}
	
	@SuppressWarnings("unused")
	private static float getDistanceToClosesetVein(Vector3f v)
	{
		float dist = Float.MAX_VALUE;
		for (Vector3f vector : veinCenters)
		{
			if (Vector3f.sub(v, vector, null).length() < dist) dist = Vector3f.sub(v, vector, null).length();
		}
		
		return dist;
	}
	
	/**
	 * For vein center selection only
	 */
	private static Vector3f pickRandomNaturalVoxel()
	{
		ArrayList<Byte> naturalVoxels = new ArrayList<>();
		naturalVoxels.add(Voxel.STONE.getId());
		naturalVoxels.add(Voxel.DIRT.getId());
		
		Vector3f v = new Vector3f();
		do
		{
			v = new Vector3f((int) Math.round(Math.random() * Island.MAXSIZE), (int) Math.round(Math.random() * (Island.MAXSIZE - START_Y)), (int) Math.round(Math.random() * Island.MAXSIZE));
		}
		while (!naturalVoxels.contains((Byte) currentIsland.getVoxelId((int) v.x, (int) v.y, (int) v.z))/* || getDistanceToClosesetVein(v) < MIN_VEIN_DISTANCE */);
		
		return v;
	}
	
	private static void generateBezier(float[] c, int x, int z, int radius, int off, int h, byte b, boolean force)
	{
		for (int i = 0; i < h; i++)
		{
			float t = i / (float) h;
			
			float rad = (float) Math.floor(radius * MathHelper.bezierCurve(c, t).y);
			
			fillHorizontalCircle(x, off - i, z, rad, b, force);
		}
	}
	
	private static void fillHorizontalCircle(int x, int y, int z, float radius, byte b, boolean force)
	{
		Vector2f center = new Vector2f(x, z);
		for (int i = 0; i < Island.MAXSIZE; i++) // x axis
		{
			for (int j = 0; j < Island.MAXSIZE; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(new Vector2f(i, j), center, null);
				if (distance.length() < radius)
				{
					if (force || (!force && currentIsland.getVoxelId(i, y, j) == Voxel.AIR.getId()))
					{
						currentIsland.setVoxel(i, y, j, b);
					}
				}
			}
		}
	}
}
