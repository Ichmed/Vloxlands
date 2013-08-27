package com.vloxlands.gen;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;

public class IslandGenerator
{
	public static Island generatePerfectIsland()
	{
		long time = System.currentTimeMillis();
		
		short topLayers = (short) (Math.random() * 3 + 3);
		short tailLayers = (short) (Math.random() * 8 + 8);
		// short radius = (short) (Math.random() * 16);
		Island island = new Island();
		
		for (int i = 0; i < 1; i++)
		{
			fillHorizontalCircle((short) i, (short) 4, Voxel.GRASS.getId(), island);
		}
		CFG.p("[IslandGenerator]: Generation took " + (System.currentTimeMillis() - time) + "ms");
		
		return island;
		
	}
	
	private static void fillHorizontalCircle(short h, short radius, byte b, Island island)
	{
		
		int x = (int) (Island.MAXSIZE / 2f - radius);
		int z = (int) (Island.MAXSIZE / 2f - radius);
		CFG.p("radius: " + radius);
		Vector2f center = new Vector2f(radius + x, radius + z);
		
		for (short i = (short) x; i < radius * 2 + x; i++) // x axis
		{
			for (short j = (short) z; j < radius * 2 + z; j++) // z axis
			{
				
				Vector2f distance = Vector2f.sub(center, new Vector2f(i, j), null);
				if (distance.length() < radius)
				{
					CFG.p(i + ":" + j);
					island.setVoxel(i, h, j, b);
				}
			}
		}
	}
}
