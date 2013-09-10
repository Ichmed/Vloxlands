package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet4ServerInfo extends Packet
{
	String[] players;
	
	public Packet4ServerInfo(String[] players)
	{
		super(4);
		this.players = players;
	}
	
	public Packet4ServerInfo()
	{
		super(4);
	}
	
	public Packet4ServerInfo(byte[] data)
	{
		super(4);
		String s = readData(data);
		if (s.length() > 0) players = s.split(":");
	}
	
	public String[] getPlayers()
	{
		return players;
	}
	
	@Override
	public byte[] getPacketData()
	{
		String serialized = "";
		if (players != null)
		{
			for (String p : players)
				serialized += p + ":";
			
			serialized = serialized.substring(0, serialized.length() - 1);
		}
		return serialized.getBytes();
	}
}
