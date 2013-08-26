package com.vloxlands.game.world;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.render.Face;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Direction;

public class Island
{
	public static final int MAXSIZE = 256;

	byte[][][] voxels = new byte[MAXSIZE][MAXSIZE][MAXSIZE];
	byte[][][] voxelMetadata = new byte[MAXSIZE][MAXSIZE][MAXSIZE];

	@SuppressWarnings("rawtypes")
	ArrayList[] faces = new ArrayList[32];

	short width = MAXSIZE;
	short height = MAXSIZE;
	short depth = MAXSIZE;

	public Island()
	{
		for (int i = 0; i < faces.length; i++)
		{
			faces[i] = new ArrayList<Face>();
		}
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				for (int k = 0; k < MAXSIZE; k++)
				{
					voxels[i][j][k] = -128;
					voxelMetadata[i][j][k] = -128;
				}
			}
		}
	}

	public void placeVoxel(short x, short y, short z, byte id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);
	}

	public void placeVoxel(short x, short y, short z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
		Voxel.getVoxelForId(id).onPlaced(x, y, z);
	}

	public short getVoxelId(short x, short y, short z)
	{
		if(x > 255 || y > 255 || z > 255 || x < 0 || y < 0 || z < 0) return 0;
		return (short) (voxels[x][y][z] + 128);
	}

	private int getVoxelId(int x, int y, int z)
	{
		return getVoxelId((short) x, (short) y, (short) z);
	}

	public byte getMetadata(short x, short y, short z)
	{
		return voxelMetadata[x][y][z];
	}

	public void setVoxel(short x, short y, short z, byte id)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = 0;
	}

	public void setVoxel(short x, short y, short z, byte id, byte metadata)
	{
		voxels[x][y][z] = id;
		voxelMetadata[x][y][z] = metadata;
	}

	public void setVoxelMetadata(short x, short y, short z, byte metadata)
	{
		voxelMetadata[x][y][z] = metadata;
	}

	public byte[] getVoxels()
	{
		byte[] bytes = new byte[(int) Math.pow(MAXSIZE, 3)];
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				for (int k = 0; k < MAXSIZE; k++)
				{
					bytes[i * MAXSIZE + j * MAXSIZE + k] = voxels[i][j][k];
				}
			}
		}
		return bytes;
	}

	public byte[] getVoxelMetadatas()
	{
		byte[] bytes = new byte[(int) Math.pow(MAXSIZE, 3)];
		for (int i = 0; i < MAXSIZE; i++)
		{
			for (int j = 0; j < MAXSIZE; j++)
			{
				for (int k = 0; k < MAXSIZE; k++)
				{
					bytes[i * MAXSIZE + j * MAXSIZE + k] = voxelMetadata[i][j][k];
				}
			}
		}
		return bytes;
	}

	public short getWidth()
	{
		return width;
	}

	public void setWidth(short width)
	{
		this.width = width;
	}

	public short getHeight()
	{
		return height;
	}

	public void setHeight(short height)
	{
		this.height = height;
	}

	public short getDepth()
	{
		return depth;
	}

	public void setDepth(short depth)
	{
		this.depth = depth;
	}

	@SuppressWarnings("unchecked")
	public void render()
	{
		for (ArrayList<Face> a : faces)
			for (Face f : a)
			{
				f.render();
			}
	}
	
	@SuppressWarnings("unchecked")
	public void generateFaces()
	{
		for(int t = 0; t < 64; t++)
		{
			int cx = t % 4;
			int cy = (t / 4) % 4;
		    int cz = t / 16;
		    
			for(int x = 0; x < 64; x++)
			{
				for(int y = 0; y < 64; y++)
				{
					for(int z = 0; z < 64; z++)
					{
						int posX = (cx * 64) + x;
						int posY = (cy * 64) + y;
						int posZ = (cz * 64) + z;
						
						int ti = Voxel.getVoxelForId(this.getVoxelId(posX, posY, posZ)).getTextureIndex();
						for(Direction d : Direction.values())
						{
							if(!Voxel.getVoxelForId(this.getVoxelId(posX + (int)d.dir.x, posY + (int)d.dir.y, posZ + (int)d.dir.z)).isOpaque())
								this.faces[t].add(new Face(d, new Vector3f(posX, posY, posZ), ti));
						}
					}
				}
			}
		}
	}
}
