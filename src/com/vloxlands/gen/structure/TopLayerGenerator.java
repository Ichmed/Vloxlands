package com.vloxlands.gen.island;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.gen.IslandGenerator;

/**
 * @author Dakror
 */
public class TopLayerGenerator extends Generator
{
	
	public static final float[] BEZIER = new float[] { 1, 0.6f, 0.3f, 1, 0.7f, 0.4f, 0, 0.5f };
	
	int x, y, z, radius, topLayers;
	
	public TopLayerGenerator(int x, int y, int z, int radius, int topLayers)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.topLayers = topLayers;
	}
	
	@Override
	public void generate(Island island, IslandGenerator gen)
	{
		generateBezier(island, BEZIER, x, z, radius, y, topLayers, Generator.createRatio(new byte[] { Voxel.get("DIRT").getId(), Voxel.get("STONE").getId() }, new int[] { 30, 1 }), true);
		gen.updateProgress();
	}
	
}
