package com.vloxlands.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;

public class MapAssistant
{
	public static void saveMap(Map m, String name)
	{
		new File("src/test").mkdir();
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (Island island : m.getIslands())
			{
				saveIsland(island, baos);
			}
			
			Compressor.compressFile(new File("src/test/" + name + ".bin"), baos.toByteArray());
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Map loadMap(String name)
	{
		Map map = new Map();
		// -- bin file -- //
		File bin = new File("src/test/" + name + ".bin");
		byte[] data = Compressor.decompress(Compressor.getFileContentAsByteArray(bin));
		int pos = 0;
		while (pos < data.length)
		{
			Vector3f vec = new Vector3f();
			ByteBuffer bb = ByteBuffer.wrap(data, pos, 12);
			vec.x = bb.getFloat();
			vec.y = bb.getFloat();
			vec.z = bb.getFloat();
			pos += 12;
			
			bb = ByteBuffer.wrap(data, pos, 4);
			int length = bb.getInt();
			pos += 4;
			
			byte[] ids = new byte[length];
			System.arraycopy(data, pos, ids, 0, length);
			pos += length;
			
			bb = ByteBuffer.wrap(data, pos, 4);
			pos += 4;
			int length2 = bb.getInt();
			byte[] mds = new byte[length2];
			System.arraycopy(data, pos, mds, 0, length2);
			pos += length2;
			
			map.addIsland(loadIsland(vec, ids, mds));
		}
		return map;
	}
	
	private static Island loadIsland(Vector3f vec, byte[] ids, byte[] mds)
	{
		Island island = new Island();
		island.setPos(vec);
		
		ids = Compressor.decompressRow(ids);
		mds = Compressor.decompressRow(mds);
		
		for (int i = 0; i < ids.length; i++)
		{
			short[] pos = get3DIndex(i);
			island.setVoxel(pos[0], pos[1], pos[2], ids[i], mds[i]);
		}
		
		return island;
	}
	
	public static short[] get3DIndex(int i)
	{
		return new short[] { (short) (i / (float) Math.pow(Island.MAXSIZE, 2)), (short) ((i / (float) Island.MAXSIZE) % Island.MAXSIZE), (short) (i % Island.MAXSIZE) };
	}
	
	private static void saveIsland(Island island, OutputStream os) throws Exception
	{
		byte[] ids = Compressor.compressRow(island.getVoxels());
		byte[] mds = Compressor.compressRow(island.getVoxelMetadatas());
		
		ByteBuffer bb = ByteBuffer.allocate(12);
		bb.putFloat(island.getPos().x);
		bb.putFloat(island.getPos().y);
		bb.putFloat(island.getPos().z);
		os.write(bb.array());
		
		bb = ByteBuffer.allocate(4);
		bb.putInt(ids.length);
		os.write(bb.array());
		
		os.write(ids);
		
		bb = ByteBuffer.allocate(4);
		bb.putInt(mds.length);
		os.write(bb.array());
		
		os.write(mds);
	}
}
