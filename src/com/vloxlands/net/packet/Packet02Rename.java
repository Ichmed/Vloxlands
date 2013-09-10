package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet02Rename extends Packet
{
	String oldUsername, newUsername;
	
	public Packet02Rename(String oldUsername, String newUsername)
	{
		super(02);
		this.oldUsername = oldUsername;
		this.newUsername = newUsername;
	}
	
	public Packet02Rename(byte[] data)
	{
		super(02);
		String[] s = readData(data).split(":");
		oldUsername = s[0];
		newUsername = s[1];
	}
	
	@Override
	public String getStringData()
	{
		return oldUsername + ":" + newUsername;
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
