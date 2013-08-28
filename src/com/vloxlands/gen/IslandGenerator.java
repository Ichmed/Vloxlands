package com.vloxlands.gen;

import java.io.File;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Assistant;
import com.vloxlands.util.MathHelper;

public class IslandGenerator
{
	public static Island generatePerfectIsland()
	{
		long time = System.currentTimeMillis();
		
		// int topLayers = (Math.random() * 3 + 3);
		// int tailLayers = (Math.random() * 8 + 8);
		// int radius = (Math.random() * 16);
		Island island = new Island();
		
		island.setVoxel(0, 0, 0, Voxel.STONE.getId());
		
		generateBezier(new Vector2f(1, 0.3f), new Vector2f(0.8f, 0.5f), new Vector2f(0.7f, 0.5f), new Vector2f(0.6f, 0.6f), 16, 0, 6, Voxel.DIRT.getId(), island);
		
		
		// island.setVoxel( 128, 0, 128, Voxel.STONE.getId());
		
		island.grassify();
		
		CFG.p("[IslandGenerator]: Generation took " + (System.currentTimeMillis() - time) + "ms");
		return island;
		
	}
	
	private static void generateBezier(Vector2f p3, Vector2f p2, Vector2f p1, Vector2f p0, int radius, int off, int h, byte b, Island island)
	{
		
		Vector2f s0 = Vector2f.sub(p1, p0, null);
		Vector2f s1 = Vector2f.sub(p2, p1, null);
		Vector2f s2 = Vector2f.sub(p3, p2, null);
		
		Vector2f[][] v = new Vector2f[6][h];
		
		for (int i = 0; i <= h - 1; i++)
		{
			
			
			float t = i / (float) (h - 1);
			
			Vector2f g0 = Vector2f.add(MathHelper.scaleVector2f(s0, t), p0, null);
			v[0][i] = g0;
			
			Vector2f g1 = Vector2f.add(MathHelper.scaleVector2f(s1, t), p1, null);
			v[1][i] = g1;
			
			Vector2f g2 = Vector2f.add(MathHelper.scaleVector2f(s2, t), p2, null);
			v[2][i] = g2;
			
			Vector2f gs0 = Vector2f.sub(g1, g0, null);
			Vector2f gs1 = Vector2f.sub(g2, g1, null);
			
			Vector2f b0 = Vector2f.add(MathHelper.scaleVector2f(gs0, t), g0, null);
			v[3][i] = b0;
			
			Vector2f b1 = Vector2f.add(MathHelper.scaleVector2f(gs1, t), g1, null);
			v[4][i] = b1;
			
			Vector2f bs0 = Vector2f.sub(b1, b0, null);
			
			Vector2f pos = Vector2f.add((Vector2f) MathHelper.scaleVector2f(bs0, t), b0, null);
			v[5][i] = pos;
			
			fillHorizontalCircle((i + off), (float) Math.floor(radius * pos.y), b, island);
		}
		
		String s = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" >";
		
		for (int i = 0; i < v.length; i++)
		{
			Vector2f[] v2 = v[i];
			
			String color = "rgb(" + (int) Math.round(Math.random() * 256) + "," + (int) Math.round(Math.random() * 256) + "," + (int) Math.round(Math.random() * 256) + ")";
			s += "<polyline fill=\"none\" stroke=\"" + color + "\" stroke-width=\"10\" points=\"";
			for (Vector2f v3 : v2)
			{
				if (v3 == null) continue;
				s += (int) Math.round(v3.x * 300 + 200) + "," + (int) Math.round(v3.y * 300 + 200) + " ";
			}
			s += "\" />";
			s += "<rect x='20' y='" + (i * 25) + "' width='50' height='25' fill='" + color + "'></rect><text x='80' y='" + (i * 25 + 25) + "' fill='black'>" + i + "</text>";
		}
		
		s += "</svg>";
		
		Assistant.setFileContent(new File("src/test/graph.svg"), s);
	}
	
	private static void fillHorizontalCircle(int h, float radius, byte b, Island island)
	{
		int x = (int) (Island.MAXSIZE / 2f - radius);
		int z = (int) (Island.MAXSIZE / 2f - radius);
		// CFG.p("radius: " + radius);
		Vector2f center = new Vector2f(radius + x, radius + z);
		
		for (int i = x; i < radius * 2 + x; i++) // x axis
		{
			for (int j = z; j < radius * 2 + z; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(center, new Vector2f(i, j), null);
				if (distance.length() < radius) island.setVoxel(i, h, j, b);
			}
		}
	}
}
