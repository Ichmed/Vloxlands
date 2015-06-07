package com.vloxlands.net.packet;

/**
 * @author Dakror
 */
public class Packet1Disconnect extends Packet {
	private String reason;
	private String username;
	
	public Packet1Disconnect(byte[] data) {
		super(1);
		String[] s = readData(data).split(":");
		username = s[0];
		reason = s[1];
	}
	
	public Packet1Disconnect(String username, String reason) {
		super(1);
		this.username = username;
		this.reason = reason;
	}
	
	@Override
	public byte[] getPacketData() {
		return (username + ":" + reason).getBytes();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getReason() {
		return reason;
	}
}
