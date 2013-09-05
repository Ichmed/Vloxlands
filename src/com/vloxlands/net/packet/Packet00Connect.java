package com.vloxlands.net.packet;

import com.vloxlands.net.Client;
import com.vloxlands.net.Server;

/**
 * @author Dakror
 */
public class Packet00Connect extends Packet
{
	
	private String username;
	
	public Packet00Connect(byte[] data)
	{
		super(00);
		username = readData(data);
	}
	
	public Packet00Connect(String username)
	{
		super(00);
		this.username = username;
	}
	
	
	@Override
	public void writeData(Client client)
	{
		client.sendData(getData());
	}
	
	@Override
	public void writeData(Server server)
	{
		server.sendDataToAllClients(getData());
	}
	
	@Override
	public byte[] getData()
	{
		return ("00" + username).getBytes();
	}
	
	public String getUsername()
	{
		return username;
	}
	
}
