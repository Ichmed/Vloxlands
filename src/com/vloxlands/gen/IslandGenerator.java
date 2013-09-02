package com.vloxlands.gen;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;
import com.vloxlands.util.MathHelper;

public class IslandGenerator extends Thread
{
	public static final int ISLAND_SMALL = 16;
	public static final int ISLAND_MEDIUM = 32;
	public static final int ISLAND_BIG = 64;
	
	/**
	 * Override L
	 */
	public static final boolean OVR_L = true;
	/**
	 * Override R
	 */
	public static final boolean OVR_R = false;
	
	
	public static final float[] ISLAND_BEZIER = new float[] { 1, 0.6f, 0.3f, 1, 0.7f, 0.4f, 0, 0.5f };
	public static final float[] SPIKE_BEZIER = new float[] { 1, 1, 0.5f, 0.5f, 0.5f, 0.5f, 0, 0 };
	
	public static final float MIN_VEIN_DISTANCE = 30;
	public static final Voxel[] CRYSTALS = { Voxel.get("STRONG_CRYSTAL"), Voxel.get("MEDIUM_CRYSTAL"), Voxel.get("WEAK_CRYSTAL") };
	
	public float progress;
	
	public Island finishedIsland;
	
	private int actions;
	private int quotient;
	
	public IslandGenerator()
	{
		progress = quotient = 0;
		start();
	}
	
	@Override
	public void run()
	{
		finishedIsland = generateIsland();
	}
	
	private Island generateIsland()
	{
		int radius = getRandomRadius(ISLAND_SMALL);
		
		quotient = (radius + 2) * 2 + 1;
		
		Island L = generatePerfectIsland(128, Island.SIZE / 2, 128, radius);
		Island R = generatePerfectIsland((int) (128 + radius / Math.PI), Island.SIZE / 2, 128, radius);
		
		Island m = mergeIslandData(L, R, OVR_L);
		generateCrystals(m, Island.SIZE / 2);
		m.grassify();
		return m;
	}
	
	private void updateProgress()
	{
		progress = actions / (float) quotient;
		actions++;
	}
	
	/**
	 * Does count <code>radius + 2</code> for Quotient
	 * 
	 * @param size Either a Constant or the real Radius
	 */
	private Island generatePerfectIsland(int x, int y, int z, int radius)
	{
		// int radius = size == ISLAND_SMALL || size == ISLAND_MEDIUM || size == ISLAND_BIG ? getRandomRadius(size) : size;
		int topLayers = (int) ((float) Math.random() * 3 + 3 + radius / 8.0f);
		
		Island island = new Island();
		
		// -- top Layers -- //
		generateBezier(island, ISLAND_BEZIER, x, z, radius, y, topLayers, createRatio(new byte[] { Voxel.get("DIRT").getId(), Voxel.get("STONE").getId() }, new int[] { 30, 1 }), true);
		updateProgress();
		
		// -- Spikes -- //
		for (int i = 0; i < radius; i++)
		{
			int MAXRAD = (int) (radius * 0.3f + 5);
			int rad = (int) Math.round(Math.random() * (radius * 0.3f)) + 3;
			
			Vector2f highest = getHighestBezierValue(ISLAND_BEZIER);
			
			int radiusAt0 = (int) (highest.y * radius);
			
			Vector2f pos = getRandomCircleInCircle(new Vector2f(x, z), radiusAt0, rad);// (radius);
			int h = (int) (((MAXRAD - rad) * (radiusAt0 - Vector2f.sub(pos, new Vector2f(x, z), null).length()) + topLayers) * 0.3f);
			island.setVoxel((int) pos.x, 127, (int) pos.y, Voxel.get("STONE").getId());
			generateBezier(island, SPIKE_BEZIER, (int) pos.x, (int) pos.y /* Z */, rad, (int) (y - highest.x * topLayers), h, createRatio(new byte[] { Voxel.get("STONE").getId(), Voxel.get("DIRT").getId() }, new int[] { 5, 1 }), false);
			updateProgress();
		}
		
		for (int i = 0; i < Island.SIZE; i++) // x axis
		{
			for (int j = 0; j < Island.SIZE; j++) // z axis
			{
				if (island.getVoxelId(i, y, j) == Voxel.get("STONE").getId())
				{
					island.setVoxel(i, y, j, Voxel.get("DIRT").getId());
				}
				
			}
		}
		updateProgress();
		
		return island;
	}
	
	private Vector2f getHighestBezierValue(float[] bezier)
	{
		float y = 0;
		float x = 0;
		for (float i = 0; i < 1; i += 0.01f)
		{
			Vector2f v = MathHelper.bezierCurve(bezier, i);
			if (v.y > y)
			{
				x = i;
				y = v.y;
			}
		}
		
		return new Vector2f(x, y);
	}
	
	private int getRandomRadius(int size)
	{
		return (int) (Math.random() * size) + size;
	}
	
	private byte[] createRatio(byte[] keys, int[] vals)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < keys.length; i++)
		{
			for (int j = 0; j < vals[i]; j++)
			{
				baos.write(keys[i]);
			}
		}
		return baos.toByteArray();
	}
	
	private void generateCrystals(Island island, int y)
	{
		island.calculateWeight();
		
		float weightNeededToUplift = island.weight / Map.calculateUplift(0);
		while (weightNeededToUplift > 100)
		{
			int index = (int) (Math.random() * CRYSTALS.length);
			weightNeededToUplift -= createCrystalVein(island, index, y, weightNeededToUplift);
		}
		
		int[] amounts = new int[CRYSTALS.length];
		for (int i = 0; i < amounts.length; i++)
		{
			amounts[i] = (int) (weightNeededToUplift / CRYSTALS[i].getUplift());
			weightNeededToUplift %= CRYSTALS[i].getUplift();
		}
		
		placeCrystals(island, amounts, y);
		
		island.calculateWeight();
		island.calculateUplift();
		
		island.initBalance = (island.uplift * Map.calculateUplift(0) - island.weight) / 100000f;
		
		updateProgress();
	}
	
	private void placeCrystals(Island island, int[] amounts, int y)
	{
		for (int j = 0; j < amounts.length; j++)
		{
			for (int i = 0; i < amounts[j]; i++)
			{
				Vector3f v = pickRandomNaturalVoxel(island, y);
				island.setVoxel((int) v.x, (int) v.y, (int) v.z, CRYSTALS[j].getId());
			}
		}
	}
	
	/**
	 * @return uplifted
	 */
	private float createCrystalVein(Island island, int index, int y, float maximum)
	{
		int type = 0;// (int) (Math.random() * 3);
		int width = 0, height = 0, depth = 0;
		
		Vector3f c = pickRandomNaturalVoxel(island, y);
		
		float uplifted = 0;
		
		switch (type)
		{
			case 0: // qubic
			{
				depth = height = width = (int) (Math.random() * 3 + (index + 1));
				
				float maxDistance = (float) (width * Math.sqrt(3)) / 2;
				
				for (int i = (int) (c.x - width * .5f); i < c.x + width * .5f; i++)
				{
					for (int j = (int) (c.y - height * .5f); j < c.y + height * .5f; j++)
					{
						for (int k = (int) (c.z - depth * .5f); k < c.z + depth * .5f; k++)
						{
							if (Math.random() * maxDistance > Vector3f.sub(new Vector3f(i, j, k), c, null).length())
							{
								if (uplifted + CRYSTALS[index].getUplift() >= maximum) return uplifted;
								
								uplifted += CRYSTALS[index].getUplift();
								if (island.getVoxelId(i, j, k) != Voxel.get("AIR").getId()) uplifted += Voxel.getVoxelForId(island.getVoxelId(i, j, k)).getWeight();
								
								island.setVoxel(i, j, k, CRYSTALS[index].getId());
							}
						}
					}
				}
				break;
			}
		}
		
		return uplifted;
	}
	
	private Vector2f getRandomCircleInCircle(Vector2f center, int radius, int rad2)
	{
		Vector2f v = new Vector2f();
		do
		{
			v = new Vector2f((int) Math.round(Math.random() * radius * 2 - radius + center.x), (int) Math.round(Math.random() * radius * 2 - radius + center.y));
		}
		while (Vector2f.sub(v, center, null).length() > radius - rad2);
		
		return v;
	}
	
	/**
	 * For vein center selection only
	 */
	private Vector3f pickRandomNaturalVoxel(Island island, int y)
	{
		ArrayList<Byte> naturalVoxels = new ArrayList<>();
		naturalVoxels.add(Voxel.get("STONE").getId());
		naturalVoxels.add(Voxel.get("DIRT").getId());
		Vector3f v = new Vector3f();
		do
		{
			v = new Vector3f((int) Math.round(Math.random() * Island.SIZE), (int) Math.round(Math.random() * (Island.SIZE - y)), (int) Math.round(Math.random() * Island.SIZE));
		}
		while (!naturalVoxels.contains(island.getVoxelId((int) v.x, (int) v.y, (int) v.z))/* || getDistanceToClosesetVein(v) < MIN_VEIN_DISTANCE */);
		
		return v;
	}
	
	private void generateBezier(Island island, float[] c, int x, int z, int radius, int off, int h, byte[] b, boolean force)
	{
		for (int i = 0; i < h; i++)
		{
			float t = i / (float) h;
			
			float rad = (float) Math.floor(radius * MathHelper.bezierCurve(c, t).y);
			
			fillHorizontalCircle(island, x, off - i, z, rad, b, force);
		}
	}
	
	private void fillHorizontalCircle(Island island, int x, int y, int z, float radius, byte[] b, boolean force)
	{
		Vector2f center = new Vector2f(x, z);
		for (int i = 0; i < Island.SIZE; i++) // x axis
		{
			for (int j = 0; j < Island.SIZE; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(new Vector2f(i, j), center, null);
				if (distance.length() < radius)
				{
					if (force || !force && island.getVoxelId(i, y, j) == Voxel.get("AIR").getId())
					{
						island.setVoxel(i, y, j, b[(int) (Math.random() * b.length)]);
					}
				}
			}
		}
	}
	
	private Island mergeIslandData(Island L, Island R, boolean rule)
	{
		Island island = rule ? L.clone() : R.clone();
		
		Island ovr = rule ? R : L;
		for (int x = 0; x < Island.SIZE; x++)
		{
			for (int y = 0; y < Island.SIZE; y++)
			{
				for (int z = 0; z < Island.SIZE; z++)
				{
					if (ovr.getVoxelId(x, y, z) == Voxel.get("AIR").getId()) continue;
					
					island.setVoxel(x, y, z, ovr.getVoxelId(x, y, z));
				}
			}
		}
		updateProgress();
		return island;
	}
}
