package com.vloxlands.gen;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Chunk;
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
	
	public static Vector3f pickRandomNaturalChunk(Island island)
	{
		Vector3f v = new Vector3f();
		int s = Island.SIZE / Island.CHUNKSIZE;
		do
		{
			v = new Vector3f((int) (Math.random() * s), (int) (Math.random() * s), (int) (Math.random() * s));
		}
		while (!hasNaturalVoxel(island.getChunk((int) v.x, (int) v.y, (int) v.z)));
		
		return v;
	}
	
	public static boolean hasNaturalVoxel(Chunk c)
	{
		ArrayList<Voxel> naturalVoxels = new ArrayList<>();
		naturalVoxels.add(Voxel.get("STONE"));
		naturalVoxels.add(Voxel.get("DIRT"));
		
		int res = 0;
		
		for (Voxel b : naturalVoxels)
		{
			res += c.getResource(b);
		}
		
		return res > 0;
	}
	
	public static Vector3f pickRandomNaturalVoxel(Island island)
	{
		ArrayList<Byte> naturalVoxels = new ArrayList<>();
		naturalVoxels.add(Voxel.get("STONE").getId());
		naturalVoxels.add(Voxel.get("DIRT").getId());
		
		Vector3f chunk = pickRandomNaturalChunk(island);
		
		ArrayList<Vector3f> chunkVoxels = new ArrayList<>();
		
		for (int i = 0; i < Island.CHUNKSIZE; i++)
		{
			for (int j = 0; j < Island.CHUNKSIZE; j++)
			{
				for (int k = 0; k < Island.CHUNKSIZE; k++)
				{
					byte id = island.getVoxelId((int) chunk.x * Island.CHUNKSIZE + i, (int) chunk.y * Island.CHUNKSIZE + j, (int) chunk.z * Island.CHUNKSIZE + k);
					if (naturalVoxels.contains(id)) chunkVoxels.add(new Vector3f(i + chunk.x * Island.CHUNKSIZE, j + chunk.y * Island.CHUNKSIZE, k + chunk.z * Island.CHUNKSIZE));
				}
			}
		}
		
		return chunkVoxels.get((int) (Math.random() * chunkVoxels.size()));
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
