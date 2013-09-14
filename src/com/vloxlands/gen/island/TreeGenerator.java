package com.vloxlands.gen.island;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.gen.IslandGenerator;
import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class TreeGenerator extends Generator
{
	public static final float[] BEZIER = new float[] { 1, 0, 1, 1, 0.5f, 0.7f, 0, 0 };
	int x, z;
	
	public TreeGenerator(int x, int z)
	{
		this.x = x;
		this.z = z;
	}
	
	@Override
	public void generate(Island island, IslandGenerator gen)
	{
		int y = island.getHighestVoxel(x, z) + 1;
		
		if (y < 0) CFG.p("There is no free space in this column to generate a tree!");
		
		int height = (int) (Math.random() * 5 + 5);
		for (int i = 0; i < height; i++)
		{
			island.setVoxel(x, y + i, z, Voxel.get("WOOD").getId());
		}
		
		generateBezier(island, BEZIER, x, z, 5, (int) (y + height * 1.5f), (int) (height * 1.4f), new byte[] { Voxel.get("LEAVES").getId() }, false);
	}
}
