package com.vloxlands.gen;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;

public class IslandGenerator
{
	public static Island generatePerfectIsland()
	{
		short topLayers = (short) (Math.random() * 3 + 3);
		short tailLayers = (short) (Math.random() * 8 + 8);
		short radius = (short) (Math.random() * 9 + 32);
		Island island = new Island();
		
		fillHorizontalCircle((short) 0, radius, Voxel.GRASS.getId(), island);
		
		return island;
		
	}
	
	private static void fillHorizontalCircle(short h, short radius, byte b, Island island)
	{
		Vector2f center = new Vector2f(radius, radius);
		
		int c = 0;
		
		for (short i = 0; i < radius * 2; i++) // x axis
		{
			for (short j = 0; j < radius * 2; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(center, new Vector2f(i, j), null);
				if (distance.length() <= radius) island.setVoxel(i, h, j, b);
				c++;
			}
		}
		
		CFG.p(c);
	}
}
