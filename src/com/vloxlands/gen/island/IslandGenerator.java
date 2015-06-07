package com.vloxlands.gen.island;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.entity.EntityBuilding;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.gen.Generator;
import com.vloxlands.gen.structure.CrystalGenerator;
import com.vloxlands.gen.structure.SpikeGenerator;
import com.vloxlands.gen.structure.TopLayerGenerator;

public class IslandGenerator extends Thread {
	public static final float MIN_VEIN_DISTANCE = 30;
	
	public float progress;
	
	public Island finishedIsland;
	
	private int actions;
	private int quotient;
	
	int minSize, maxSize;
	float yPos;
	
	public IslandGenerator(int minSize, int maxSize, float yPos) {
		this.minSize = minSize;
		this.maxSize = maxSize;
		progress = quotient = 0;
		this.yPos = yPos;
		
		setName("IslandGenerator Thread");
	}
	
	@Override
	public void run() {
		finishedIsland = generateIsland();
	}
	
	private Island generateIsland() {
		int radius = rand(minSize, maxSize);
		int subislands = (int) Math.round((1f / radius * 16f) * Math.random() * 3);
		
		quotient = radius + 2;
		
		int[] radii = new int[subislands];
		for (int i = 0; i < radii.length; i++) {
			radii[i] = rand(minSize, maxSize / 2);
			quotient += radii[i] + 3;
		}
		
		
		Island island = generatePerfectIsland(Island.SIZE / 2, Island.SIZE / 4 * 3, Island.SIZE / 2, radius);
		
		
		for (int i = 0; i < subislands; i++) {
			Vector2f pos = Generator.getRandomCircleInCircle(new Vector2f(Island.SIZE / 2, Island.SIZE / 2), radius, 2);
			
			mergeIslandData(island, generatePerfectIsland((int) pos.x, Island.SIZE / 4 * 3, (int) pos.y, radii[i]));
		}
		
		// new TreeGenerator(Island.SIZE / 2, Island.SIZE / 2).generate(L, this);
		
		new CrystalGenerator(yPos).generate(island, this);
		
		EntityBuilding eb = new EntityBuilding(new Vector3f(Island.SIZE / 2, Island.SIZE / 4 * 3 + 1, Island.SIZE / 2), new Vector3f(4, 2, 4), "Kontor");
		island.addEntity(eb);
		
		island.grassify();
		return island;
	}
	
	public void updateProgress() {
		progress = actions / (float) quotient;
		actions++;
	}
	
	/**
	 * Does count <code>radius + 2</code> for Quotient
	 * 
	 * @param size Either a Constant or the real Radius
	 */
	private Island generatePerfectIsland(int x, int y, int z, int radius) {
		int topLayers = (int) ((float) Math.random() * 3 + 3 + radius / 8.0f);
		
		Island island = new Island();
		
		new TopLayerGenerator(x, y, z, radius, topLayers).generate(island, this);
		
		for (int i = 0; i < radius; i++) {
			new SpikeGenerator(x, y, z, radius, topLayers).generate(island, this);
		}
		
		// clearing upmost level
		for (int i = 0; i < Island.SIZE; i++) // x axis
		{
			for (int j = 0; j < Island.SIZE; j++) // z axis
			{
				if (island.getVoxelId(i, y, j) == Voxel.get("STONE").getId()) {
					island.setVoxel(i, y, j, Voxel.get("DIRT").getId());
				}
			}
		}
		updateProgress();
		
		return island;
	}
	
	private int rand(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}
	
	/**
	 * L gets overwritten by R
	 */
	public void mergeIslandData(Island L, Island R) {
		for (int x = 0; x < Island.SIZE; x++) {
			for (int y = 0; y < Island.SIZE; y++) {
				for (int z = 0; z < Island.SIZE; z++) {
					if (R.getVoxelId(x, y, z) == Voxel.get("AIR").getId()) continue;
					
					L.setVoxel(x, y, z, R.getVoxelId(x, y, z));
				}
			}
		}
		updateProgress();
	}
}
