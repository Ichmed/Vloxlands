package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet3ChatMessage extends Packet
{
	String username, message;
	
	public Packet3ChatMessage(String username, String message)
	{
		super(3);
		this.username = username;
		this.message = message;
	}
	
	public Packet3ChatMessage(byte[] data)
	{
		super(3);
		String[] s = readData(data).split(":");
		username = s[0];
		message = s[1];
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	@Override
	public String getStringData()
	{
		return username + ":" + message;
	}
}
