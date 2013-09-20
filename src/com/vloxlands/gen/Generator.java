package com.vloxlands.gen;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.gen.island.IslandGenerator;
import com.vloxlands.util.math.MathHelper;

/**
 * @author Dakror
 */
public abstract class Generator
{
	public abstract void generate(Island island, IslandGenerator gen);
	
	public static void generateBezier(Island island, float[] c, int x, int z, int radius, int off, int h, byte[] b, boolean force)
	{
		for (int i = 0; i < h; i++)
		{
			float t = i / (float) h;
			
			float rad = (float) Math.floor(radius * MathHelper.bezierCurve(c, t).y);
			
			fillHorizontalCircle(island, x, off - i, z, rad, b, force);
		}
	}
	
	public static void fillHorizontalCircle(Island island, int x, int y, int z, float radius, byte[] b, boolean force)
	{
		Vector2f center = new Vector2f(x, z);
		for (int i = 0; i < Island.SIZE; i++) // x axis
		{
			for (int j = 0; j < Island.SIZE; j++) // z axis
			{
				Vector2f distance = Vector2f.sub(new Vector2f(i, j), center, null);
				if (distance.length() < radius)
				{
					if (force || !force && island.getVoxelId(i, y, j) == Voxel.get("AIR").getId())
					{
						island.setVoxel(i, y, j, b[(int) (Math.random() * b.length)]);
					}
				}
			}
		}
	}
	
	public static Vector3f pickRandomNaturalVoxel(Island island, int y)
	{
		ArrayList<Byte> naturalVoxels = new ArrayList<>();
		naturalVoxels.add(Voxel.get("STONE").getId());
		naturalVoxels.add(Voxel.get("DIRT").getId());
		Vector3f v = new Vector3f();
		do
		{
			v = new Vector3f((int) Math.round(Math.random() * Island.SIZE), (int) Math.round(Math.random() * (Island.SIZE - y)), (int) Math.round(Math.random() * Island.SIZE));
		}
		while (!naturalVoxels.contains(island.getVoxelId((int) v.x, (int) v.y, (int) v.z)));
		
		return v;
	}
	
	public static Vector2f getRandomCircleInCircle(Vector2f center, int radius, int rad2)
	{
		Vector2f v = new Vector2f();
		do
		{
			v = new Vector2f((int) Math.round(Math.random() * radius * 2 - radius + center.x), (int) Math.round(Math.random() * radius * 2 - radius + center.y));
		}
		while (Vector2f.sub(v, center, null).length() > radius - rad2);
		
		return v;
	}
	
	public static Vector2f getHighestBezierValue(float[] bezier)
	{
		float y = 0;
		float x = 0;
		for (float i = 0; i < 1; i += 0.01f)
		{
			Vector2f v = MathHelper.bezierCurve(bezier, i);
			if (v.y > y)
			{
				x = i;
				y = v.y;
			}
		}
		
		return new Vector2f(x, y);
	}
	
	public static byte[] createRatio(byte[] keys, int[] vals)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < keys.length; i++)
		{
			for (int j = 0; j < vals[i]; j++)
			{
				baos.write(keys[i]);
			}
		}
		return baos.toByteArray();
	}
	
	public static void fillSphere(Island island, int x, int y, int z, int rad, int id)
	{
		for (int i = 0; i < rad * 2; i++)
		{
			for (int j = 0; j < rad * 2; j++)
			{
				for (int k = 0; k < rad * 2; k++)
				{
					if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) <= rad) island.setVoxel(x + i, y + j, z + k, (byte) id);
				}
			}
		}
	}
}
