package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet8Loading extends Packet
{
	String action;
	float percentage;
	
	public Packet8Loading(String action, float percent)
	{
		super(8);
		this.action = action;
		percentage = percent;
	}
	
	public Packet8Loading(byte[] data)
	{
		super(8);
		String[] s = readData(data).split(":");
		action = s[0];
		percentage = Float.parseFloat(s[1]);
	}
	
	@Override
	protected byte[] getPacketData()
	{
		return (action + ":" + percentage).getBytes();
	}
	
	public String getAction()
	{
		return action;
	}
	
	public float getPercentage()
	{
		return percentage;
	}
}
