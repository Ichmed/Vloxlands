package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.TextureImpl;

import com.vloxlands.game.entity.Entity;
import com.vloxlands.render.ChunkRenderer;

public class Map
{
	public ArrayList<Island> islands = new ArrayList<>();
	public boolean initialized = false;
	public int initializedIslands = 0;
	
	public static final int MAXHEIGHT = 512;
	
	public List<Entity> entities = new ArrayList<>();
	
	public int renderedChunks = 0;
	public int chunks = 0;
	String name;
	
	public void placeVoxel(Island i, int x, int y, int z, int id)
	{}
	
	public void render()
	{
		renderedChunks = 0;
		for (Island i : islands)
		{
			glPushMatrix();
			i.render();
			glPopMatrix();
			
			renderedChunks += i.renderedChunks;
		}
		
		TextureImpl.bindNone();
	}
	
	public void onTick()
	{
		for (Island i : islands)
			i.onTick();
		for (Entity e : entities)
			e.onTick();
	}
	
	public Island[] getIslands()
	{
		return islands.toArray(new Island[] {});
	}
	
	public void addIsland(Island i)
	{
		islands.add(i);
		
		chunks = (int) (Math.pow(Island.SIZE / Chunk.SIZE, 3) * islands.size());
	}
	
	public void initMap()
	{
		initializedIslands = 0;
		for (Island i : islands)
		{
			ChunkRenderer.renderChunks(i);
			i.calculateInitBalance();
			initializedIslands++;
		}
		initialized = true;
	}
	
	public static float calculateUplift(float height)
	{
		return (1 - height / MAXHEIGHT) * 4 + 0.1f;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
}
