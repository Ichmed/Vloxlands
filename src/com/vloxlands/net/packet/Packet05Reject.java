package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet05Reject extends Packet
{
	public static enum Cause
	{
		OUTDATEDCLIENT,
		OUTDATEDSERVER,
		USERNAMETAKEN,
		
		;
	}
	
	private Cause cause;
	
	public Packet05Reject(Cause cause)
	{
		super(05);
		this.cause = cause;
	}
	
	public Packet05Reject(byte[] data)
	{
		super(05);
		cause = Cause.values()[Integer.parseInt(readData(data))];
	}
	
	public Cause getCause()
	{
		return cause;
	}
	
	@Override
	public String getStringData()
	{
		return cause.ordinal() + "";
	}
}
