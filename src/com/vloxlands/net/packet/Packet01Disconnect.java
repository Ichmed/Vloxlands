package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet01Disconnect extends Packet
{
	
	private String username;
	
	public Packet01Disconnect(byte[] data)
	{
		super(01);
		username = readData(data);
	}
	
	public Packet01Disconnect(String username)
	{
		super(01);
		this.username = username;
	}
	
	@Override
	public byte[] getData()
	{
		return ("01" + username).getBytes();
	}
	
	public String getUsername()
	{
		return username;
	}
	
}
