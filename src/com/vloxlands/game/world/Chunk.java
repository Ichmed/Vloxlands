package com.vloxlands.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.render.ChunkRenderer;
import com.vloxlands.render.VoxelFace;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;
import com.vloxlands.util.math.AABB;

public class Chunk extends AABB
{
	public static class ChunkKey
	{
		public int x, y, z;
		
		public ChunkKey(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public int hashCode()
		{
			int s = Island.SIZE / SIZE;
			return (x * s + y) * s + z;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof ChunkKey)
			{
				ChunkKey c = (ChunkKey) obj;
				return c.x == x && c.y == y && c.z == z;
			}
			return false;
		}
		
		@Override
		public String toString()
		{
			return String.format("[%d, %d, %d]", x, y, z);
		}
	}
	
	public static final int SIZE = 8;
	
	HashMap<VoxelFaceKey, VoxelFace>[] faces;
	@SuppressWarnings("unchecked")
	HashMap<VoxelFaceKey, VoxelFace>[] meshes = new HashMap[2];
	
	int x, y, z, opaqueID = -1, transparentID = -1;
	boolean opaqueUTD = false;
	boolean transparentUTD = false;
	
	Block[][][] blocks = new Block[SIZE][SIZE][SIZE];
	
	// byte[][][] voxels = new byte[SIZE][SIZE][SIZE];
	// byte[][][] voxelMetadata = new byte[SIZE][SIZE][SIZE];
	
	float weight, uplift;
	Island island;
	
	/**
	 * Keeps track of which resources can be found on this Chunk
	 */
	int[] resources = new int[Voxel.VOXELS];
	
	public Chunk(ChunkKey pos, Island island)
	{
		this(pos.x, pos.y, pos.z, island);
	}
	
	public Chunk(int x, int y, int z, Island island)
	{
		super(new Vector3f(x * SIZE, y * SIZE, z * SIZE), SIZE, SIZE, SIZE);
		parent = island;
		cubic = true;
		
		this.island = island;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		for (int i = 0; i < resources.length; i++)
			resources[i] = 0;
		
		resources[Voxel.get("AIR").getId() + 128] = (int) Math.pow(Chunk.SIZE, 3);
		
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				for (int k = 0; k < SIZE; k++)
				{
					blocks[i][j][k] = new Block(Voxel.get("AIR").getId(), (byte) 0, new Vector3f(i, j, k), this);
					// voxels[i][j][k] = Voxel.get("AIR").getId();
					// voxelMetadata[i][j][k] = 0;
				}
			}
		}
	}
	
	public int getResource(Voxel v)
	{
		return resources[v.getId() + 128];
	}
	
	public void addResource(Voxel v, int val)
	{
		if (resources[v.getId() + 128] + val > -1)
		{
			resources[v.getId() + 128] += val;
		}
	}
	
	public void updateMesh()
	{
		faces = ChunkRenderer.generateFaces(x, y, z, island);
		if (faces[0].size() + faces[1].size() == 0)
		{
			meshes[0] = new HashMap<>();
			meshes[1] = new HashMap<>();
			return;
		}
		meshes[0] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[0]);
		meshes[1] = ChunkRenderer.generateGreedyMesh(x, y, z, faces[1]);
		
		opaqueUTD = false;
		transparentUTD = false;
	}
	
	public boolean render(boolean opaque)
	{
		if (meshes[0].size() + meshes[1].size() == 0) return false;
		
		if (opaqueID == -1)
		{
			opaqueID = glGenLists(1);
			transparentID = glGenLists(1);
		}
		
		if (opaque && !opaqueUTD)
		{
			renderDisplayList(true);
			opaqueUTD = true;
		}
		
		if (!opaque && !transparentUTD)
		{
			renderDisplayList(false);
			transparentUTD = true;
		}
		if (/* inViewFrustum() */Game.frustum.sphereInFrustum(x * Chunk.SIZE + Chunk.SIZE / 2 + island.pos.x, y * Chunk.SIZE + Chunk.SIZE / 2 + island.pos.y, z * Chunk.SIZE + Chunk.SIZE / 2 + island.pos.z, Chunk.SIZE * (float) Math.sqrt(2) / 2))
		{
			glPushMatrix();
			{
				glCallList(opaque ? opaqueID : transparentID);
			}
			glPopMatrix();
			return true;
		}
		return false;
	}
	
	public void renderDisplayList(boolean opaque)
	{
		glPushMatrix();
		glNewList(opaque ? opaqueID : transparentID, GL_COMPILE);
		for (VoxelFace v : meshes[opaque ? 0 : 1].values())
			v.render();
		glEndList();
		glPopMatrix();
	}
	
	public byte getVoxelId(int x, int y, int z)
	{
		if (x >= SIZE || y >= SIZE || z >= SIZE || x < 0 || y < 0 || z < 0) return 0;
		return blocks[x][y][z].voxel;
	}
	
	public void setMetadata(int x, int y, int z, byte metadata)
	{
		blocks[x][y][z].metadata = metadata;
	}
	
	public byte[] getVoxels()
	{
		byte[] bytes = new byte[(int) Math.pow(SIZE, 3)];
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				for (int k = 0; k < SIZE; k++)
				{
					bytes[(i * SIZE + j) * SIZE + k] = blocks[i][j][k].voxel;
				}
			}
		}
		return bytes;
	}
	
	public byte getMetadata(int x, int y, int z)
	{
		return blocks[x][y][z].metadata;
	}
	
	public void setVoxel(int x, int y, int z, byte id)
	{
		if (x >= SIZE || y >= SIZE || z >= SIZE || x < 0 || y < 0 || z < 0) return;
		addResource(Voxel.getVoxelForId(blocks[x][y][z].voxel), -1);
		addResource(Voxel.getVoxelForId(id), 1);
		
		blocks[x][y][z].voxel = id;
	}
	
	public byte[] getVoxelMetadatas()
	{
		byte[] bytes = new byte[(int) Math.pow(SIZE, 3)];
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				for (int k = 0; k < SIZE; k++)
				{
					bytes[(i * SIZE + j) * SIZE + k] = blocks[i][j][k].metadata;
				}
			}
		}
		return bytes;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public void calculateWeight()
	{
		weight = 0;
		for (int x = 0; x < SIZE; x++)
		{
			for (int y = 0; y < SIZE; y++)
			{
				for (int z = 0; z < SIZE; z++)
				{
					if (getVoxelId(x, y, z) == 0) continue;
					weight += Voxel.getVoxelForId(getVoxelId(x, y, z)).getWeight();
				}
			}
		}
	}
	
	public void calculateUplift()
	{
		uplift = 0;
		for (int x = 0; x < SIZE; x++)
		{
			for (int y = 0; y < SIZE; y++)
			{
				for (int z = 0; z < SIZE; z++)
				{
					if (getVoxelId(x, y, z) == 0) continue;
					uplift += Voxel.getVoxelForId(getVoxelId(x, y, z)).getUplift();
				}
			}
		}
	}
	
	public int getHighestVoxel(int x, int z)
	{
		for (int i = SIZE - 1; i > -1; i--)
		{
			if (blocks[x][i][z].voxel != Voxel.get("AIR").getId()) return i;
		}
		
		return -1;
	}
	
	public int grassify(Island island)
	{
		int grassed = 0;
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				for (int k = 0; k < SIZE; k++)
				{
					if (island.getVoxelId(i + x * SIZE, j + y * SIZE, k + z * SIZE) == Voxel.get("DIRT").getId())
					{
						if (island.getVoxelId(i + x * SIZE, j + 1 + y * SIZE, k + z * SIZE) == Voxel.get("AIR").getId())
						{
							grassed++;
							island.setVoxel(i + x * SIZE, j + y * SIZE, k + z * SIZE, Voxel.get("GRASS").getId());
						}
					}
				}
			}
		}
		
		return grassed;
	}
	
	public ChunkKey getPos()
	{
		return new ChunkKey(x, y, z);
	}
}
