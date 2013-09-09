package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet03ChatMessage extends Packet
{
	String username, message;
	
	public Packet03ChatMessage(String username, String message)
	{
		super(03);
		this.username = username;
		this.message = message;
	}
	
	public Packet03ChatMessage(byte[] data)
	{
		super(03);
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
	public byte[] getData()
	{
		return ("03" + username + ":" + message).getBytes();
	}
}
