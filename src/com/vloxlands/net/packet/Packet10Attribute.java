package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet10Attribute extends Packet
{
	String key;
	String value;
	
	/**
	 * Key format: class_field_type
	 */
	public Packet10Attribute(String key, Object value)
	{
		super(10);
		this.key = key;
		this.value = value.toString();
	}
	
	public Packet10Attribute(byte[] data)
	{
		super(10);
		String[] s = readData(data).split(":");
		key = s[0];
		value = s[1];
	}
	
	@Override
	protected byte[] getPacketData()
	{
		return (key + ":" + value).getBytes();
	}
	
	public String getKey()
	{
		return key;
	}
	
	public String getValue()
	{
		return value;
	}
}
