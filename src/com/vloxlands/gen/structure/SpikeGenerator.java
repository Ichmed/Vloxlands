package com.vloxlands.gen.structure;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.gen.Generator;
import com.vloxlands.gen.island.IslandGenerator;

/**
 * @author Dakror
 */
public class SpikeGenerator extends Generator
{
	
	public static final float[] BEZIER = new float[] { 1, 1, 0.0f, 0.5f, 0.5f, 0.5f, 0, 0 };
	
	int x, y, z, radius, topLayers;
	
	public SpikeGenerator(int x, int y, int z, int radius, int topLayers)
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
		int MAXRAD = (int) (radius * 0.3f + 5);
		int rad = (int) Math.round(Math.random() * (radius * 0.3f)) + 3;
		
		Vector2f highest = getHighestBezierValue(TopLayerGenerator.BEZIER);
		
		int radiusAt0 = (int) (highest.y * radius);
		
		Vector2f pos = getRandomCircleInCircle(new Vector2f(x, z), radiusAt0, rad);// (radius);
		int h = (int) (((MAXRAD - rad) * (radiusAt0 - Vector2f.sub(pos, new Vector2f(x, z), null).length()) + topLayers) * 0.3f);
		island.setVoxel((int) pos.x, Island.SIZE / 2 - 1, (int) pos.y, Voxel.get("STONE").getId());
		Generator.generateBezier(island, BEZIER, (int) pos.x, (int) pos.y /* Z */, rad, (int) (y - highest.x * topLayers), h, createRatio(new byte[] { Voxel.get("STONE").getId(), Voxel.get("DIRT").getId() }, new int[] { 5, 1 }), false);
		gen.updateProgress();
	}
	
}
