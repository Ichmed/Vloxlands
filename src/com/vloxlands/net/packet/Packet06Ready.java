package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet06Ready extends Packet
{
	private String username;
	private boolean ready;
	
	public Packet06Ready()
	{
		super(06);
		username = "$$$";
	}
	
	public Packet06Ready(String username, boolean ready)
	{
		super(06);
		this.username = username;
		this.ready = ready;
	}
	
	public Packet06Ready(byte[] data)
	{
		super(06);
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
