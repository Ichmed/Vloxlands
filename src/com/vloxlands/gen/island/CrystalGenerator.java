package com.vloxlands.gen.island;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;
import com.vloxlands.gen.IslandGenerator;

/**
 * @author Dakror
 */
public class CrystalGenerator extends Generator
{
	public static final Voxel[] CRYSTALS = { Voxel.get("STRONG_CRYSTAL"), Voxel.get("MEDIUM_CRYSTAL"), Voxel.get("WEAK_CRYSTAL") };
	int y;
	
	public CrystalGenerator(int y)
	{
		this.y = y;
	}
	
	@Override
	public void generate(Island island, IslandGenerator gen)
	{
		island.calculateWeight();
		
		float weightNeededToUplift = island.weight / Map.calculateUplift(y);
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
		
		gen.updateProgress();
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
	
}
