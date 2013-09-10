package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet6Ready extends Packet
{
	private String username;
	private boolean ready;
	
	public Packet6Ready()
	{
		super(6);
		username = "$$$";
	}
	
	public Packet6Ready(String username, boolean ready)
	{
		super(6);
		this.username = username;
		this.ready = ready;
	}
	
	public Packet6Ready(byte[] data)
	{
		super(6);
		String[] s = readData(data).split(":");
		username = s[0];
		ready = Boolean.valueOf(s[1]);
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public boolean getReady()
	{
		return ready;
	}
	
	@Override
	public String getStringData()
	{
		return username + ":" + Boolean.toString(ready);
	}
}
