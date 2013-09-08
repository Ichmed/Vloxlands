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
		String s = readData(data);
		oldUsername = s.substring(0, s.indexOf(":"));
		newUsername = s.substring(s.indexOf(":") + 1);
	}
	
	@Override
	public byte[] getData()
	{
		return ("02" + oldUsername + ":" + newUsername).getBytes();
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
