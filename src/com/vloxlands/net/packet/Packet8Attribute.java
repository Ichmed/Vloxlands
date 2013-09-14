package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet8Attribute extends Packet
{
	String key;
	String value;
	
	/**
	 * Key format: class_field_type
	 */
	public Packet8Attribute(String key, Object value)
	{
		super(8);
		this.key = key;
		this.value = value.toString();
	}
	
	public Packet8Attribute(byte[] data)
	{
		super(8);
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
