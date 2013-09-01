package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;

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
		
		HashMap<ArrayList<Integer>, VoxelFace>[] faceLists = generateFaces(cx, cy, cz, island);
		
		HashMap<ArrayList<Integer>, VoxelFace> greedy0 = generateGreedyMesh(cx, cy, cz, faceLists[0]);
		// HashMap<ArrayList<Integer>, VoxelFace> greedy1 = generateGreedyMesh(cx, cy, cz, faceLists[1]);
		if (greedy0.size() > 0) CFG.p(greedy0.size());
		glPushMatrix();
		glNewList(listIndex, GL_COMPILE);
		for (VoxelFace v : greedy0.values())
			v.render();
		// for (VoxelFace v : faceLists[0].values())
		// v.render();
		glEndList();
		glPopMatrix();
		
		glPushMatrix();
		glNewList(listIndex + 1, GL_COMPILE);
		// for (VoxelFace v : greedy1.values())
		// v.render();
		// for (VoxelFace v : faceLists[1].values())
		// v.render();
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
	private static HashMap<ArrayList<Integer>, VoxelFace>[] generateFaces(int cx, int cy, int cz, Island i)
	{
		HashMap<ArrayList<Integer>, VoxelFace> faces = new HashMap<>();
		HashMap<ArrayList<Integer>, VoxelFace> transparentFaces = new HashMap<>();
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
							VoxelFace f = new VoxelFace(d, new Vector3f(posX, posY, posZ), v.getTextureIndex());
							if (v.isOpaque()) faces.put(getVoxelFaceKey(x, y, z, d.ordinal()), f);
							else transparentFaces.put(getVoxelFaceKey(x, y, z, d.ordinal()), f);
						}
					}
				}
			}
		}
		return new HashMap[] { faces, transparentFaces };
	}
	
	private static HashMap<ArrayList<Integer>, VoxelFace> generateGreedyMesh(int cx, int cy, int cz, HashMap<ArrayList<Integer>, VoxelFace> originalMap)
	{
		HashMap<ArrayList<Integer>, VoxelFace> strips = new HashMap<>();
		
		if (originalMap.size() == 0) return originalMap;
		
		// greedy-mode along Z - axis
		for (int x = 0; x < Island.CHUNKSIZE; x++)
		{
			for (int y = 0; y < Island.CHUNKSIZE; y++)
			{
				VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
				for (int z = 0; z < Island.CHUNKSIZE; z++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						int posY = cy * Island.CHUNKSIZE + y;
						int posX = cx * Island.CHUNKSIZE + x;
						int posZ = cz * Island.CHUNKSIZE + z;
						
						if (activeStrips[i] != null)
						{
							if (!originalMap.containsKey(getVoxelFaceKey(x, y, z, i)))
							{
								CFG.p("reset: no field: " + getVoxelFaceKey(posX, posY, posZ, i));
								strips.put(getVoxelFaceKey(activeStrips[i]), activeStrips[i]);
								activeStrips[i] = null;
							}
							else if (originalMap.get(getVoxelFaceKey(x, y, z, i)).textureIndex == activeStrips[i].textureIndex)
							{
								CFG.p("increase: " + getVoxelFaceKey(posX, posY, posZ, i));
								activeStrips[i].increaseSize(0, 0, 1);
							}
							else
							{
								CFG.p("different: now: " + originalMap.get(getVoxelFaceKey(x, y, z, i)).textureIndex + " <- tex, " + getVoxelFaceKey(posX, posY, posZ, i) + " saving: " + activeStrips[i]);
								strips.put(getVoxelFaceKey(activeStrips[i]), activeStrips[i]);
								
								activeStrips[i] = new VoxelFace(Direction.values()[i], new Vector3f(posX, posY, posZ), originalMap.get(getVoxelFaceKey(x, y, z, i)).textureIndex);
							}
						}
						else if (originalMap.containsKey(getVoxelFaceKey(x, y, z, i)))
						{
							CFG.p("new: " + getVoxelFaceKey(posX, posY, posZ, i));
							activeStrips[i] = new VoxelFace(Direction.values()[i], new Vector3f(posX, posY, posZ), originalMap.get(getVoxelFaceKey(x, y, z, i)).textureIndex);
						}
					}
				}
				for (int i = 0; i < activeStrips.length; i++)
					if (activeStrips[i] != null) strips.put(getVoxelFaceKey(activeStrips[i]), activeStrips[i]);
			}
		}
		
		// greedy-mode along X - axis
		// for (int y = 0; y < Island.CHUNKSIZE; y++)
		// {
		// VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
		// for (int z = 0; z < Island.CHUNKSIZE; z++)
		// {
		// for (int x = 0; x < Island.CHUNKSIZE; x++)
		// {
		// for (int i = 0; i < activeStrips.length; i++)
		// {
		// int posY = cy * Island.CHUNKSIZE + y;
		// int posX = cx * Island.CHUNKSIZE + x;
		// int posZ = cz * Island.CHUNKSIZE + z;
		//
		// if (activeStrips[i] != null)
		// {
		// if (!strips.containsKey(getVoxelFaceKey(posX, posY, posZ, i)))
		// {
		// // CFG.p("no strip: " + getVoxelFaceKey(posX, posY, posZ, i));
		// continue;
		// }
		//
		// if (activeStrips[i].pos.z + activeStrips[i].sizeZ < posX)
		// {
		// CFG.p("cleared: " + getVoxelFaceKey(posX, posY, posZ, i) + " = no field");
		// strips.put(getVoxelFaceKey(activeStrips[i]), activeStrips[i]);
		// activeStrips[i] = null;
		// }
		// else if (strips.get(getVoxelFaceKey(posX, posY, posZ, i)).textureIndex == activeStrips[i].textureIndex && activeStrips[i].sizeZ == strips.get(getVoxelFaceKey(posX, posY, posZ, i)).sizeZ)
		// {
		// CFG.p("increased: " + getVoxelFaceKey(posX, posY, posZ, i));
		// activeStrips[i].increaseSize(1, 0, 0);
		// }
		// else
		// {
		// CFG.p("cleared: " + getVoxelFaceKey(posX, posY, posZ, i) + " = no field");
		// strips.put(getVoxelFaceKey(activeStrips[i]), activeStrips[i]);
		// activeStrips[i] = null;
		// }
		// }
		// else if (strips.containsKey(getVoxelFaceKey(posX, posY, posZ, i)))
		// {
		// CFG.p("new at: " + getVoxelFaceKey(posX, posY, posZ, i) + " = " + strips.get(getVoxelFaceKey(posX, posY, posZ, i)));
		// activeStrips[i] = strips.get(getVoxelFaceKey(posX, posY, posZ, i));
		// }
		//
		// }
		// }
		// }
		// for (int i = 0; i < activeStrips.length; i++)
		// {
		// if (activeStrips[i] != null)
		// {
		// CFG.p("added forgotten one: " + activeStrips[i]);
		// CFG.p("previous value: " + strips.put(getVoxelFaceKey(activeStrips[i]), activeStrips[i]));
		// }
		// }
		// }
		
		// CFG.p("[ChunkRenderer]: Greedy meshing took " + (System.currentTimeMillis() - time) + "ms");
		
		return strips;
	}
	
	private static ArrayList<Integer> getVoxelFaceKey(int x, int y, int z, int d)
	{
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(x);
		l.add(y);
		l.add(z);
		l.add(d);
		return l;
	}
	
	private static ArrayList<Integer> getVoxelFaceKey(VoxelFace face)
	{
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add((int) face.pos.x);
		l.add((int) face.pos.y);
		l.add((int) face.pos.x);
		l.add(face.dir.ordinal());
		return l;
	}
}
