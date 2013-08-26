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

					for (int x = 0; x < 256; x++)
					{
						for (int y = 0; y < 256; y++)
						{
							for (int z = 0; z < 256; z++)
							{
								if (i.getVoxelId(x, y, z) == 0) continue;

								int ti = Voxel.getVoxelForId(i.getVoxelId(x, y, z)).getTextureIndex();
								for (Direction d : Direction.values())
								{
									if (!Voxel.getVoxelForId(i.getVoxelId(x + (int) d.dir.x, y + (int) d.dir.y, z + (int) d.dir.z)).isOpaque()) i.faces[0].add(new Face(d, new Vector3f(x, y, z), ti));
								}
							}
						}
					}

				}
				System.out.println("Finished generating Faces");
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
			int voxels = 300;

			for (int j = 0; j < voxels; j++)
			{
				short x = (short) ((Math.random() * 20));
				short y = (short) ((Math.random() * 20));
				short z = (short) ((Math.random() * 20));
				island.setVoxel(x, y, z, (byte) -127, (byte) 0);
			}

			map.addIsland(island);
		}
		return map;
	}
}
