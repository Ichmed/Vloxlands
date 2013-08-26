package com.vloxlands.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;

public class MapAssistant
{
	public static void saveMap(Map m)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (Island island : m.getIslands())
			{
				saveIsland(island, baos);
			}
			Compressor.compressFile(new File("src/test/testMap.bin"), baos.toByteArray());

			baos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void saveIsland(Island island, OutputStream os) throws Exception
	{
		byte[] ids = Compressor.rowCompression(island.getVoxels());
		byte[] mds = Compressor.rowCompression(island.getVoxelMetadatas());

		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(ids.length);
		os.write(bb.array());

		os.write(ids);

		bb = ByteBuffer.allocate(4);
		bb.putInt(mds.length);
		os.write(bb.array());

		os.write(mds);
	}
}
