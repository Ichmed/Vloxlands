package com.vloxlands.gen;

import com.vloxlands.game.world.Island;

public class GenerationHelper
{
	public static void fillSphere(Island island, int x, int y, int z, int rad, int id)
	{
		for (int i = 0; i < rad * 2; i++)
		{
			for (int j = 0; j < rad * 2; j++)
			{
				for (int k = 0; k < rad * 2; k++)
				{
					if(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) +Math.pow(z, 2)) <= rad) island.setVoxel(x + i, y + j, z + k, (byte)id);
				}
			}
		}
	}
}
