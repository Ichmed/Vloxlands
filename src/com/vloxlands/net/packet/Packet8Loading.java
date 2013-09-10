package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet8Loading extends Packet
{
	String action;
	int percentage;
	
	public Packet8Loading(String action, int percent)
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
		percentage = Integer.parseInt(s[1]);
	}
	
	@Override
	protected String getStringData()
	{
		return action + ":" + percentage;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public int getPercentage()
	{
		return percentage;
	}
}
