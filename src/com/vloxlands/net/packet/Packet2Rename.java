package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet2Rename extends Packet
{
	String oldUsername, newUsername;
	
	public Packet2Rename(String oldUsername, String newUsername)
	{
		super(2);
		this.oldUsername = oldUsername;
		this.newUsername = newUsername;
	}
	
	public Packet2Rename(byte[] data)
	{
		super(2);
		String[] s = readData(data).split(":");
		oldUsername = s[0];
		newUsername = s[1];
	}
	
	@Override
	public byte[] getPacketData()
	{
		return (oldUsername + ":" + newUsername).getBytes();
	}
	
	public String getOldUsername()
	{
		return oldUsername;
	}
	
	public String getNewUsername()
	{
		return newUsername;
	}
}
