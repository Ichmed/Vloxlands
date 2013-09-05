package com.vloxlands.net;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.vloxlands.Vloxlands;
import com.vloxlands.net.packet.Packet;
import com.vloxlands.net.packet.Packet.PacketTypes;
import com.vloxlands.net.packet.Packet00Connect;
import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class Server extends Thread
{
	private DatagramSocket socket;
	
	private ArrayList<PlayerMP> connectedPlayers = new ArrayList<>();
	
	public Server()
	{
		try
		{
			socket = new DatagramSocket(CFG.SERVER_PORT);
		}
		catch (BindException e)
		{
			CFG.p("There is a server running at that port already!");
			Vloxlands.exit();
		}
		catch (SocketException e)
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
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port)
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
				CFG.p("[" + address.getHostAddress() + ":" + port + "]: " + packet.getUsername() + " has connected.");
				PlayerMP playerMP = new PlayerMP(address, port, packet.getUsername());
				connectedPlayers.add(playerMP);
				break;
			}
			case DISCONNECT:
			{
				break;
			}
		}
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try
		{
			socket.send(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendDataToAllClients(byte[] data)
	{
		for (PlayerMP p : connectedPlayers)
		{
			sendData(data, p.getIPAddress(), p.getPort());
		}
	}
}
