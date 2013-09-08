package com.vloxlands.net;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
	InetAddress ip;
	private ArrayList<Player> clients = new ArrayList<>();
	
	public Server(InetAddress ipAddress)
	{
		try
		{
			ip = ipAddress;
			socket = new DatagramSocket(new InetSocketAddress(ipAddress, CFG.SERVER_PORT));
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
		start();
	}
	
	public InetAddress getIP()
	{
		return ip;
	}
	
	@Override
	public void run()
	{
		CFG.p("[SERVER]: Starting UDP");
		CFG.p("[SERVER]: -------------------------------------------------");
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
				CFG.p("[SERVER]: " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + ") has connected.");
				clients.add(new Player(packet.getUsername(), address, port));
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				// PlayerMP playerMP = new PlayerMP(address, port, packet.getUsername());
				// connectedPlayers.add(playerMP);
				break;
			}
			case DISCONNECT:
			{
				break;
			}
		}
	}
	
	public void sendPacket(Packet p, Player client) throws IOException
	{
		sendData(p.getData(), client);
	}
	
	public void sendData(byte[] data, Player client) throws IOException
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, client.getIP(), client.getPort());
		
		socket.send(packet);
		
	}
	
	
	public void sendPacketToAllClients(Packet packet) throws IOException
	{
		sendDataToAllClients(packet.getData());
	}
	
	public void sendDataToAllClients(byte[] data) throws IOException
	{
		for (Player p : clients)
		{
			sendData(data, p);
		}
	}
}
