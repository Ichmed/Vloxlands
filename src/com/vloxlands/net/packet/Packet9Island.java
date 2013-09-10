package com.vloxlands.net.packet;

import java.io.ByteArrayOutputStream;

import com.vloxlands.game.world.Island;
import com.vloxlands.util.Compressor;
import com.vloxlands.util.MapAssistant;

/**
 * @author Dakror
 */
public class Packet9Island extends Packet
{
	byte[] islandData;
	
	public Packet9Island(Island island)
	{
		super(9);
		
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			MapAssistant.saveIsland(island, baos);
			islandData = Compressor.compress(baos.toByteArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected String getStringData()
	{
		return new String(islandData);
	}
}
