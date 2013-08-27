package com.vloxlands.gen;

import org.lwjgl.util.vector.Vector2f;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.MathHelper;

public class IslandGenerator
{
	public static Island generatePerfectIsland()
	{
		long time = System.currentTimeMillis();
		
		short topLayers = (short) (Math.random() * 3 + 3);
		short tailLayers = (short) (Math.random() * 8 + 8);
		// short radius = (short) (Math.random() * 16);
		Island island = new Island();
		
		island.setVoxel((short) 0, (short) 0, (short) 0, Voxel.STONE.getId());
		
		generateBezier(new Vector2f(0, 0), new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), (short) 16, (short) 16, Voxel.GRASS.getId(), island);
		
		// System.exit(0);
		island.setVoxel((short) 128, (short) 0, (short) 128, Voxel.STONE.getId());
		CFG.p("[IslandGenerator]: Generation took " + (System.currentTimeMillis() - time) + "ms");
		return island;
		
	}
	
	private static void generateBezier(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3, short radius, short h, byte b, Island island)
	{
		Vector2f s0 = Vector2f.sub(p1, p0, null);
		Vector2f s1 = Vector2f.sub(p2, p1, null);
		Vector2f s2 = Vector2f.sub(p3, p2, null);
		
		for (int i = 0; i < h; i++)
		{
			float t = (i + 1) / (float) h;
			
			Vector2f g0 = Vector2f.add(MathHelper.setVector2fLength(s0, t), p0, null);
			Vector2f g1 = Vector2f.add(MathHelper.setVector2fLength(s1, t), p1, null);
			Vector2f g2 = Vector2f.add(MathHelper.setVector2fLength(s2, t), p2, null);
			
			Vector2f gs0 = Vector2f.sub(g1, g0, null);
			Vector2f gs1 = Vector2f.sub(g2, g1, null);
			
			Vector2f b0 = Vector2f.add(MathHelper.setVector2fLength(gs0, t), g0, null);
			Vector2f b1 = Vector2f.add(MathHelper.setVector2fLength(gs1, t), g1, null);
			
			Vector2f bs0 = Vector2f.sub(b1, b0, null);
			
			Vector2f pos = Vector2f.add((Vector2f) MathHelper.setVector2fLength(bs0, t), b0, null);
			
			fillHorizontalCircle((short) i, (float) Math.floor(radius * pos.y), b, island);
		}
	}
	
	private static void fillHorizontalCircle(short h, float radius, byte b, Island island)
	{
		int x = (int) (Island.MAXSIZE / 2f - radius);
		int z = (int) (Island.MAXSIZE / 2f - radius);
		// CFG.p("radius: " + radius);
		Vector2f center = new Vector2f(radius + x, radius + z);
		
		for (short i = (short) x; i < radius * 2 + x; i++) // x axis
		{
			for (short j = (short) z; j < radius * 2 + z; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(center, new Vector2f(i, j), null);
				if (distance.length() < radius) island.setVoxel(i, h, j, b);
			}
		}
	}
}
