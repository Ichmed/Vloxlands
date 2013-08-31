package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Direction;

public class ChunkRenderer
{
	public static void renderChunk(int listIndex, int index, Island island)
	{
		int cs = Island.SIZE / Island.CHUNKSIZE;
		int cx = index / (cs * cs);
		int cy = index / cs % cs;
		int cz = index % cs;
		
		ArrayList<VoxelFace>[] faceLists = generateFaces(cx, cy, cz, island);
		
		glPushMatrix();
		glNewList(listIndex, GL_COMPILE);
		for (int i = 0; i < faceLists[0].size(); i++)
		{
			faceLists[0].get(i).render();
		}
		glEndList();
		glPopMatrix();
		
		glPushMatrix();
		glNewList(listIndex + 1, GL_COMPILE);
		for (int i = 0; i < faceLists[1].size(); i++)
		{
			faceLists[1].get(i).render();
		}
		glEndList();
		glPopMatrix();
	}
	
	public static void initChunks(Island island)
	{
		island.chunk0ID = glGenLists(island.chunks[0].capacity() * 2);
		for (int i = 0; i < island.chunks[0].capacity(); i++)
		{
			renderChunk(island.chunk0ID + i * 2, i, island);
			island.chunks[0].put(i);
			island.chunks[1].put(i);
		}
		
		island.chunks[0].flip();
		island.chunks[1].flip();
		CFG.p("[ChunkRenderer]: Initialized chunks on Island " + island);
	}
	
	@SuppressWarnings("unchecked")
	protected static ArrayList<VoxelFace>[] generateFaces(int cx, int cy, int cz, Island i)
	{
		ArrayList<VoxelFace> faces = new ArrayList<>();
		ArrayList<VoxelFace> transparentFaces = new ArrayList<>();
		for (int x = 0; x < Island.CHUNKSIZE; x++)
		{
			for (int y = 0; y < Island.CHUNKSIZE; y++)
			{
				for (int z = 0; z < Island.CHUNKSIZE; z++)
				{
					int posX = cx * Island.CHUNKSIZE + x;
					int posY = cy * Island.CHUNKSIZE + y;
					int posZ = cz * Island.CHUNKSIZE + z;
					
					if (i.getVoxelId(posX, posY, posZ) == Voxel.get("AIR").getId()) continue;
					Voxel v = Voxel.getVoxelForId(i.getVoxelId(posX, posY, posZ));
					for (Direction d : Direction.values())
					{
						Voxel w = Voxel.getVoxelForId(i.getVoxelId(posX + (int) d.dir.x, posY + (int) d.dir.y, posZ + (int) d.dir.z));
						if (!w.isOpaque() && !(w == v))
						{
							VoxelFace f = new VoxelFace(d, new Vector3f(posX, posY, posZ), v);
							if (v.isOpaque()) faces.add(f);
							else transparentFaces.add(f);
						}
					}
				}
			}
		}
		return new ArrayList[] { faces, transparentFaces };
	}
}
