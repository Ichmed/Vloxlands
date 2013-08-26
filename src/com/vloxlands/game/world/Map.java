package com.vloxlands.game.world;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.vloxlands.game.entity.Entity;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.render.Face;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Direction;

public class Map
{
	ArrayList<Island> islands = new ArrayList<>();
	List<Entity> entities = new ArrayList<>();

	public void placeVoxel(Island i, int x, int y, int z, int id)
	{
	}

	public void render()
	{
		for (Island i : islands)
			i.render();
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
		new Thread()
		{
			public void run()
			{
				for (Island i : islands)
				{
					for (int t = 0; t < 64; t++)
					{
						int cx = t % 4;
						int cy = (t / 4) % 4;
						int cz = t / 16;

						for (int x = 0; x < 64; x++)
						{
							for (int y = 0; y < 64; y++)
							{
								for (int z = 0; z < 64; z++)
								{
									int posX = (cx * 64) + x;
									int posY = (cy * 64) + y;
									int posZ = (cz * 64) + z;

									if (i.getVoxelId(posX, posY, posZ) == 0) continue;

									int ti = Voxel.getVoxelForId(i.getVoxelId(posX, posY, posZ)).getTextureIndex();
									for (Direction d : Direction.values())
									{
										if (!Voxel.getVoxelForId(i.getVoxelId(posX + (int) d.dir.x, posY + (int) d.dir.y, posZ + (int) d.dir.z)).isOpaque()) i.faces[t].add(new Face(d, new Vector3f(posX, posY, posZ), ti));
									}
								}
							}
						}
					}
				}
			}
		}.start();
	}

	public static Map generateRandomMap()
	{
		Map map = new Map();
		int islands = 2;

		for (int i = 0; i < islands; i++)
		{

			Island island = new Island();
			int voxels = 10;

			for (int j = 0; j < voxels; j++)
			{
				short x = (short) (Math.random() * Island.MAXSIZE);
				short y = (short) (Math.random() * Island.MAXSIZE);
				short z = (short) (Math.random() * Island.MAXSIZE);
				island.setVoxel(x, y, z, (byte) -127, (byte) 0);
			}

			map.addIsland(island);
		}
		return map;
	}
}
