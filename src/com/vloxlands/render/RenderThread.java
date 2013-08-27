package com.vloxlands.render;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.util.Direction;

public class RenderThread extends Thread
{
	public static final int GENERATE_FACES = 0;
	public static final int GENERATE_ALL_FACES = 1;
	
	public void run(int arg, Object... args)
	{
		if (arg == GENERATE_FACES) generateFaces((Integer) args[0], (Island) args[1]);
		else if (arg == GENERATE_ALL_FACES) generateAllFaces();
	}
	
	public void generateFaces(int chunk, Island i)
	{
		int cx = chunk % 4;
		int cy = (chunk / 4) % 4;
		int cz = chunk / 16;
		
		int chunkSize = Game.CHUNK_SIZE;
		
		for (int x = 0; x < 64; x++)
		{
			for (int y = 0; y < 64; y++)
			{
				for (int z = 0; z < 64; z++)
				{
					int posX = (cx * chunkSize) + x;
					int posY = (cy * chunkSize) + y;
					int posZ = (cz * chunkSize) + z;
					
					if (i.getVoxelId(posX, posY, posZ) == 0) continue;
					Voxel v = Voxel.getVoxelForId(i.getVoxelId(posX, posY, posZ));
					for (Direction d : Direction.values())
					{
						if (!Voxel.getVoxelForId(i.getVoxelId(posX + (int) d.dir.x, posY + (int) d.dir.y, posZ + (int) d.dir.z)).isOpaque())
						{
							Face f = new Face(d, new Vector3f(x, y, z), v.getTextureIndex());
							if (v.isOpaque()) i.faces.add(f);
							else i.transparentFaces.add(f);
						}
					}
				}
			}
		}
		System.out.println("Finished generating Faces for chunk " + chunk + " on island " + i);
	}
	
	public void generateAllFaces()
	{
		for (Island i : Game.currentMap.islands)
		{
			for (int x = 0; x < 256; x++)
			{
				for (int y = 0; y < 256; y++)
				{
					for (int z = 0; z < 256; z++)
					{
						if (i.getVoxelId(x, y, z) == 0) continue;
						Voxel v = Voxel.getVoxelForId(i.getVoxelId(x, y, z));
						for (Direction d : Direction.values())
						{
							if (!Voxel.getVoxelForId(i.getVoxelId(x + (int) d.dir.x, y + (int) d.dir.y, z + (int) d.dir.z)).isOpaque())
							{
								Face f = new Face(d, new Vector3f(x, y, z), v.getTextureIndex());
								if (v.isOpaque()) i.faces.add(f);
								else i.transparentFaces.add(f);
							}
						}
					}
				}
			}
		}
		System.out.println("Finished generating all Faces");
	}
}