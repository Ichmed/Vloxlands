package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet04ServerInfo extends Packet
{
	String[] players;
	
	public Packet04ServerInfo(String[] players)
	{
		super(04);
		this.players = players;
	}
	
	public Packet04ServerInfo()
	{
		super(04);
	}
	
	public Packet04ServerInfo(byte[] data)
	{
		super(04);
		String s = readData(data);
		if (s.length() > 0) players = s.split(":");
	}
	
	public String[] getPlayers()
	{
		return players;
	}
	
	@Override
	public String getStringData()
	{
		String serialized = "";
		if (players != null)
		{
			for (String p : players)
				serialized += p + ":";
			
			serialized = serialized.substring(0, serialized.length() - 1);
		}
		return serialized;
	}
}
