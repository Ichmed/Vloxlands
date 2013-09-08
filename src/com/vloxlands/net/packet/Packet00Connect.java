package com.vloxlands.net.packet;




/**
 * @author Dakror
 */
public class Packet00Connect extends Packet
{
	
	private String username;
	
	public Packet00Connect(byte[] data)
	{
		super(00);
		username = readData(data);
	}
	
	public Packet00Connect(String username)
	{
		super(00);
		this.username = username;
	}
	
	@Override
	public byte[] getData()
	{
		return ("00" + username).getBytes();
	}
	
	public String getUsername()
	{
		return username;
	}
	
}
