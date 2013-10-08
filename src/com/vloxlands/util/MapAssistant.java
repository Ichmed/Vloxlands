package com.vloxlands.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.world.Chunk;
import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;
import com.vloxlands.settings.CFG;

public class MapAssistant
{
	private static int pos;
	
	public static void saveMap(Map m)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (Island island : m.getIslands())
			{
				saveIsland(island, baos);
			}
			
			Compressor.compressFile(new File(CFG.DIR, "maps/" + m.getName() + ".map"), baos.toByteArray());
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Map loadMap(String name)
	{
		pos = 0;
		Map map = new Map();
		// -- bin file -- //
		File bin = new File(CFG.DIR, "maps/" + name + ".map");
		byte[] data = Compressor.decompress(Compressor.getFileContentAsByteArray(bin));
		while (pos < data.length)
		{
			map.addIsland(loadIsland(data, true));
		}
		return map;
	}
	
	public static Island loadIsland(byte[] data, boolean posing)
	{
		Island island = new Island();
		
		int pos = (posing) ? MapAssistant.pos : 0;
		Vector3f vec = new Vector3f();
		ByteBuffer bb = ByteBuffer.wrap(data, pos, 12);
		vec.x = bb.getFloat();
		vec.y = bb.getFloat();
		vec.z = bb.getFloat();
		pos += 12;
		
		island.setPos(vec);
		
		bb = ByteBuffer.wrap(data, pos, 4);
		int length = bb.getInt();
		pos += 4;
		
		byte[] chunkdata = new byte[length];
		System.arraycopy(data, pos, chunkdata, 0, length);
		pos += length;
		
		loadChunks(island, chunkdata);
		
		if (posing) MapAssistant.pos += pos;
		
		return island;
	}
	
	public static void loadChunks(Island island, byte[] data)
	{
		int pos = 0;
		
		while (pos < data.length - 1)
		{
			ByteBuffer bb = ByteBuffer.wrap(data, pos, 3);
			pos += 3;
			
			Chunk c = new Chunk(bb.get(), bb.get(), bb.get(), island);
			
			// ids
			bb = ByteBuffer.wrap(data, pos, 4);
			pos += 4;
			int idsLength = bb.getInt();
			
			byte[] ids = new byte[idsLength];
			System.arraycopy(data, pos, ids, 0, idsLength);
			
			ids = Compressor.decompressRow(ids);
			
			for (int i = 0; i < ids.length; i++)
			{
				int[] index = get3DIndex(i);
				c.setVoxel(index[0], index[1], index[2], ids[i]);
			}
			pos += idsLength;
			
			// mds
			bb = ByteBuffer.wrap(data, pos, 4);
			pos += 4;
			int mdsLength = bb.getInt();
			
			byte[] mds = new byte[mdsLength];
			System.arraycopy(data, pos, mds, 0, mdsLength);
			
			mds = Compressor.decompressRow(mds);
			
			for (int i = 0; i < mds.length; i++)
			{
				int[] index = get3DIndex(i);
				c.setMetadata(index[0], index[1], index[2], mds[i]);
			}
			pos += mdsLength;
			
			island.addChunk(c.getPos(), c);
		}
	}
	
	public static int[] get3DIndex(int i)
	{
		return new int[] { (int) (i / (float) Math.pow(Chunk.SIZE, 2)), (int) ((i / (float) Chunk.SIZE) % Chunk.SIZE), (i % Chunk.SIZE) };
	}
	
	public static void saveIsland(Island island, OutputStream os) throws Exception
	{
		ByteBuffer bb = ByteBuffer.allocate(12);
		bb.putFloat(island.getPos().x);
		bb.putFloat(island.getPos().y);
		bb.putFloat(island.getPos().z);
		os.write(bb.array());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		for (Chunk c : island.getChunks())
		{
			saveChunk(island, c, baos);
		}
		
		bb = ByteBuffer.allocate(4).putInt(baos.size());
		os.write(bb.array());
		
		os.write(baos.toByteArray());
	}
	
	public static void saveChunk(Island island, Chunk c, OutputStream os) throws Exception
	{
		// coordinates
		os.write((byte) c.getX());
		os.write((byte) c.getY());
		os.write((byte) c.getZ());
		
		byte[] ids = Compressor.compressRow(c.getVoxels());
		byte[] mds = Compressor.compressRow(c.getVoxelMetadatas());
		
		ByteBuffer bb = ByteBuffer.allocate(4);
		// length of voxel data
		bb.putInt(ids.length);
		os.write(bb.array());
		
		// voxel data
		os.write(ids);
		
		bb = ByteBuffer.allocate(4);
		// length of metadata data
		bb.putInt(mds.length);
		os.write(bb.array());
		
		// metadata data
		os.write(mds);
	}
}
