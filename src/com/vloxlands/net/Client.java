package com.vloxlands.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.vloxlands.Vloxlands;
import com.vloxlands.net.packet.Packet00Connect;
import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class Client extends Thread
{
	private DatagramSocket socket;
	private InetAddress serverIP;
	private Player player;
	
	public Client(Player player)
	{
		try
		{
			this.player = player;
			socket = new DatagramSocket();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while (Vloxlands.running)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try
			{
				socket.receive(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			CFG.p("[Server]: " + new String(data));
		}
	}
	
	public void connectToServer(InetAddress ip)
	{
		if (serverIP != null)
		{
			System.err.println("Client is already connected to a server. Disconnect first!");
			return;
		}
		
		serverIP = ip;
		Packet00Connect p = new Packet00Connect(player.getUsername());
		p.writeData(this);
	}
	
	public void sendData(byte[] data)
	{
		if (serverIP == null)
		{
			System.err.println("Connect to a server first!");
			return;
		}
		
		DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, CFG.SERVER_PORT);
		try
		{
			socket.send(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
