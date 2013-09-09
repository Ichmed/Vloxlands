package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet01Disconnect extends Packet
{
	private String reason;
	private String username;
	
	public Packet01Disconnect(byte[] data)
	{
		super(01);
		String s = readData(data);
		username = s.substring(0, s.indexOf(":"));
		reason = s.substring(s.indexOf(":") + 1);
	}
	
	public Packet01Disconnect(String username, String reason)
	{
		super(01);
		this.username = username;
		this.reason = reason;
	}
	
	@Override
	public byte[] getData()
	{
		return ("01" + username + ":" + reason).getBytes();
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getReason()
	{
		return reason;
	}
}
