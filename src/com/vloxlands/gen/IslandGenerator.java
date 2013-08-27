package com.vloxlands.gen;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.world.Island;

public class IslandGenerator
{
	public static Island generatePerfectIsland()
	{
		// TODO: do work
		int topLayers = (int) (Math.random() * 3 + 3);
		int tailLayers = (int) (Math.random() * 8 + 8);
		
		Island island = new Island();
		
		return island;
		
	}
	
	private static void fillHorizontalCircle(Vector3f middle, int radius, Island island)
	{	
		
	}
}
