package com.vloxlands.net.packet;


/**
 * @author Dakror
 */
public class Packet7Settings extends Packet
{
	String key;
	String value;
	
	public Packet7Settings(String key, String value)
	{
		super(7);
		this.key = key;
		this.value = value;
	}
	
	public Packet7Settings(byte[] data)
	{
		super(7);
		String[] s = readData(data).split(":");
		key = s[0];
		value = s[1];
	}
	
	public String getKey()
	{
		return key;
	}
	
	public String getValue()
	{
		return value;
	}
	
	@Override
	protected String getStringData()
	{
		return key + ":" + value.toString();
	}
}
