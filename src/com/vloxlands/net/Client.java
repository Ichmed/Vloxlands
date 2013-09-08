package com.vloxlands.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.vloxlands.Vloxlands;
import com.vloxlands.game.Game;
import com.vloxlands.net.packet.Packet;
import com.vloxlands.net.packet.Packet.PacketTypes;
import com.vloxlands.net.packet.Packet00Connect;
import com.vloxlands.net.packet.Packet01Disconnect;
import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class Client extends Thread
{
	private DatagramSocket socket;
	private InetAddress serverIP;
	private Player player;
	
	boolean connected;
	
	public Client(Player player)
	{
		try
		{
			this.player = player;
			socket = new DatagramSocket();
			connected = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		start();
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
			parsePacket(data);
		}
	}
	
	public boolean connectToServer(InetAddress ip)
	{
		if (connected)
		{
			System.err.println("Client is already connected to a server. Disconnect first!");
			return false;
		}
		
		serverIP = ip;
		try
		{
			sendPacket(new Packet00Connect(player.getUsername()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void disconnect()
	{
		if (!connected) return;
		
		try
		{
			sendPacket(new Packet01Disconnect(player.getUsername()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void parsePacket(byte[] data)
	{
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		switch (type)
		{
			default:
			case INVALID:
			{
				break;
			}
			case CONNECT:
			{
				Packet00Connect packet = new Packet00Connect(data);
				if (packet.getUsername().equals(player.getUsername())) connected = true;
				else CFG.p(packet.getUsername() + " joined the game");
				break;
			}
			case DISCONNECT:
			{
				Packet01Disconnect packet = new Packet01Disconnect(data);
				if (packet.getUsername().equals(player.getUsername())) connected = false;
				else CFG.p(packet.getUsername() + " left the game");
				break;
			}
		}
	}
	
	public boolean isConnected()
	{
		return connected;
	}
	
	public boolean isConnectedToLocalhost()
	{
		return connected && serverIP.equals(Game.IP);
	}
	
	public void sendPacket(Packet p) throws IOException
	{
		sendData(p.getData());
	}
	
	public void sendData(byte[] data) throws IOException
	{
		if (serverIP == null)
		{
			System.err.println("Connect to a server first!");
			return;
		}
		
		DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, CFG.SERVER_PORT);
		socket.send(packet);
	}
}
