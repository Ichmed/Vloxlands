package com.vloxlands.net.packet;



/**
 * @author Dakror
 */
public abstract class Packet
{
	public static enum PacketTypes
	{
		INVALID(-1),
		CONNECT(00),
		DISCONNECT(01),
		RENAME(02),
		CHATMESSAGE(03),
		SERVERINFO(04),
		USERNAMETAKEN(05),
		
		;
		
		private int packetID;
		
		private PacketTypes(int id)
		{
			packetID = id;
		}
		
		public int getID()
		{
			return packetID;
		}
	}
	
	public byte packetID;
	
	public Packet(int packetID)
	{
		this.packetID = (byte) packetID;
	}
	
	public abstract byte[] getData();
	
	public String readData(byte[] data)
	{
		String message = new String(data).trim();
		
		return message.substring(2);
	}
	
	public PacketTypes getType()
	{
		return Packet.lookupPacket(packetID);
	}
	
	public static PacketTypes lookupPacket(int id)
	{
		for (PacketTypes pt : PacketTypes.values())
		{
			if (pt.getID() == id) return pt;
		}
		
		return PacketTypes.INVALID;
	}
	
	public static PacketTypes lookupPacket(String id)
	{
		try
		{
			return lookupPacket(Integer.parseInt(id));
		}
		catch (NumberFormatException e)
		{
			return PacketTypes.INVALID;
		}
	}
}
