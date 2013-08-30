package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import com.vloxlands.game.entity.Entity;
import com.vloxlands.render.ChunkRenderer;

public class Map
{
	public ArrayList<Island> islands = new ArrayList<>();
	List<Entity> entities = new ArrayList<>();
	
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
	}
	
	public void startMap()
	{
		for (Island i : islands)
		{
			ChunkRenderer.initChunks(i);
			i.calculateWeight();
			i.calculateUplift();
		}
	}
	
	public static float calculateUplift(float height)
	{
		return ((1 - (height / MAXHEIGHT)) * 4) + 0.1f;
	}
}
