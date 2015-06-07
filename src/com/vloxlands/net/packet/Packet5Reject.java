package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet5Reject extends Packet {
	public static enum Cause {
		OUTDATEDCLIENT,
		OUTDATEDSERVER,
		USERNAMETAKEN,
		GAMERUNNING,
		FULL,
		
		;
	}
	
	private Cause cause;
	
	public Packet5Reject(Cause cause) {
		super(5);
		this.cause = cause;
	}
	
	public Packet5Reject(byte[] data) {
		super(5);
		cause = Cause.values()[Integer.parseInt(readData(data))];
	}
	
	public Cause getCause() {
		return cause;
	}
	
	@Override
	public byte[] getPacketData() {
		return (cause.ordinal() + "").getBytes();
	}
}
