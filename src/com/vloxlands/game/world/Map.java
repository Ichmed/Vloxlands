package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import com.vloxlands.game.entity.Entity;
import com.vloxlands.gen.IslandGenerator;
import com.vloxlands.render.ChunkRenderer;

public class Map
{
	public ArrayList<Island> islands = new ArrayList<>();
	List<Entity> entities = new ArrayList<>();
	
	public IslandGenerator islandGenerator;
	
	public static final int MAXHEIGHT = 512;
	
	public void placeVoxel(Island i, int x, int y, int z, int id)
	{}
	
	public void render()
	{
		for (Island i : islands)
		{
			glPushMatrix();
			i.render();
			glPopMatrix();
		}
	}
	
	public void onTick()
	{
		if (islandGenerator != null)
		{
			if (islandGenerator.finishedIsland != null)
			{
				addIsland(islandGenerator.finishedIsland);
				islandGenerator = null;
			}
			
		}
		
		for (Island i : islands)
			i.onTick();
	}
	
	public Island[] getIslands()
	{
		return islands.toArray(new Island[] {});
	}
	
	public void addIsland(Island i)
	{
		islands.add(i);
		ChunkRenderer.renderChunks(i);
		i.calculateWeight();
		i.calculateUplift();
	}
	
	public static float calculateUplift(float height)
	{
		return (1 - height / MAXHEIGHT) * 4 + 0.1f;
	}
}
