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
import com.vloxlands.net.packet.Packet01Disconnect;
import com.vloxlands.net.packet.Packet02Rename;
import com.vloxlands.net.packet.Packet03ChatMessage;
import com.vloxlands.net.packet.Packet04ServerInfo;
import com.vloxlands.net.packet.Packet05Reject;
import com.vloxlands.net.packet.Packet05Reject.Cause;
import com.vloxlands.net.packet.Packet06Ready;
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
			return;
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
		shutdown();
	}
	
	public void shutdown()
	{
		for (Player p : clients)
		{
			try
			{
				sendPacket(new Packet01Disconnect(p.getUsername(), "mp.reason.serverclosed"), p);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port)
	{
		PacketTypes type = Packet.lookupPacket(data[0]);
		switch (type)
		{
			case INVALID:
			{
				break;
			}
			case CONNECT:
			{
				Packet00Connect packet = new Packet00Connect(data);
				Player player = new Player(packet.getUsername(), address, port);
				if (packet.getVersion() < CFG.VERSION)
				{
					try
					{
						CFG.p("[SERVER]: Rejected " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): outdated client");
						sendPacket(new Packet05Reject(Cause.OUTDATEDCLIENT), player);
						return;
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else if (packet.getVersion() > CFG.VERSION)
				{
					try
					{
						CFG.p("[SERVER]: Rejected " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): outdated server");
						sendPacket(new Packet05Reject(Cause.OUTDATEDSERVER), player);
						return;
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				for (Player p : clients)
				{
					if (p.getUsername().equals(packet.getUsername()))
					{
						try
						{
							sendPacket(new Packet05Reject(Cause.USERNAMETAKEN), player);
							CFG.p("[SERVER]: Rejected " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): username taken");
							return;
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				CFG.p("[SERVER]: " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + ") has connected.");
				clients.add(player);
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			}
			case DISCONNECT:
			{
				Packet01Disconnect packet = new Packet01Disconnect(data);
				CFG.p("[SERVER]: " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + ") has disconnected. (" + packet.getReason() + ")");
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				for (int i = 0; i < clients.size(); i++)
				{
					if (clients.get(i).getUsername().equals(packet.getUsername()))
					{
						clients.remove(i);
						break;
					}
				}
				break;
			}
			case RENAME:
			{
				Packet02Rename packet = new Packet02Rename(data);
				for (int i = 0; i < clients.size(); i++)
				{
					if (clients.get(i).getUsername().equals(packet.getNewUsername()))
					{
						try
						{
							sendPacket(new Packet05Reject(Cause.USERNAMETAKEN), new Player(packet.getOldUsername(), address, port));
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						return;
					}
				}
				CFG.p("[SERVER]: " + packet.getOldUsername() + " changed their name to " + packet.getNewUsername() + ".");
				for (int i = 0; i < clients.size(); i++)
				{
					if (clients.get(i).getUsername().equals(packet.getOldUsername()))
					{
						clients.get(i).setUsername(packet.getNewUsername());
						break;
					}
				}
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			}
			case CHATMESSAGE:
			{
				Packet03ChatMessage packet = new Packet03ChatMessage(data);
				CFG.p("[SERVER]: " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): " + packet.getMessage());
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			}
			case SERVERINFO:
			{
				String[] players = new String[clients.size()];
				for (int i = 0; i < clients.size(); i++)
				{
					players[i] = clients.get(i).getUsername();
				}
				try
				{
					sendPacketToAllClients(new Packet04ServerInfo(players));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			}
			case READY:
			{
				Packet06Ready packet = new Packet06Ready(data);
				if (!packet.getUsername().equals("$$$"))
				{
					for (Player p : clients)
					{
						if (p.getUsername().equals(packet.getUsername()))
						{
							p.setReady(packet.getReady());
							break;
						}
					}
					try
					{
						sendPacketToAllClients(packet);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					Player player = new Player("", address, port);
					for (Player p : clients)
					{
						if (!p.isReady())
						{
							try
							{
								sendPacket(new Packet06Ready("$$$", false), player);
								return;
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
					}
					try
					{
						sendPacket(new Packet06Ready("$$$", true), player);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				break;
			}
			default:
				break;
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
			sendData(data, p);
	}
}
