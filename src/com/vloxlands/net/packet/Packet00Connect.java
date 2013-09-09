package com.vloxlands.net.packet;





/**
 * @author Dakror
 */
public class Packet00Connect extends Packet
{
	
	private String username;
	private int version;
	
	public Packet00Connect(byte[] data)
	{
		super(00);
		String[] s = readData(data).split(":");
		username = s[0];
		version = Integer.parseInt(s[1]);
	}
	
	public Packet00Connect(String username, int version)
	{
		super(00);
		this.version = version;
		this.username = username;
	}
	
	@Override
	public byte[] getData()
	{
		return ("00" + username + ":" + version).getBytes();
	}
	
	public int getVersion()
	{
		return version;
	}
	
	public String getUsername()
	{
		return username;
	}
}
