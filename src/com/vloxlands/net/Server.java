package com.vloxlands.net;

import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vloxlands.game.world.Map;
import com.vloxlands.gen.MapGenerator;
import com.vloxlands.net.packet.Packet;
import com.vloxlands.net.packet.Packet.PacketTypes;
import com.vloxlands.net.packet.Packet0Connect;
import com.vloxlands.net.packet.Packet1Disconnect;
import com.vloxlands.net.packet.Packet2Rename;
import com.vloxlands.net.packet.Packet3ChatMessage;
import com.vloxlands.net.packet.Packet4ServerInfo;
import com.vloxlands.net.packet.Packet5Reject;
import com.vloxlands.net.packet.Packet5Reject.Cause;
import com.vloxlands.net.packet.Packet6Ready;
import com.vloxlands.net.packet.Packet7Settings;
import com.vloxlands.net.packet.Packet8Attribute;
import com.vloxlands.net.packet.Packet9Island;
import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class Server extends Thread
{
	public static final int PACKETSIZE = 65536;
	
	private DatagramSocket socket;
	InetAddress ip;
	ArrayList<Player> clients = new ArrayList<>();
	ArrayList<Player> connectableClients = new ArrayList<>();
	HashMap<String, String> settings = new HashMap<>();
	Map map;
	MapGenerator mapGenerator;
	boolean lobby;
	
	public Server(InetAddress ipAddress)
	{
		try
		{
			ip = ipAddress;
			socket = new DatagramSocket(new InetSocketAddress(ipAddress, CFG.SERVER_PORT));
			map = new Map();
			lobby = true;
		}
		catch (BindException e)
		{
			CFG.p("There is a server running at that port already!");
			return;
		}
		catch (SocketException e)
		{}
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
		while (!socket.isClosed())
		{
			byte[] data = new byte[PACKETSIZE];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try
			{
				socket.receive(packet);
				parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			}
			catch (Exception e)
			{}
		}
		shutdown();
	}
	
	public void shutdown()
	{
		try
		{
			for (Player p : clients)
			{
				sendPacket(new Packet1Disconnect(p.getUsername(), "mp.reason.serverclosed"), p);
			}
		}
		catch (Exception e)
		{}
		socket.close();
	}
	
	public void setMapGenerator(MapGenerator mg)
	{
		mapGenerator = mg;
		mapGenerator.start();
		lobby = false;
	}
	
	public boolean areAllClientsReady()
	{
		if (clients.size() == 1) return true;
		for (Player p : clients)
		{
			if (!p.isReady()) return false;
		}
		
		return true;
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) throws Exception
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
				Packet0Connect packet = new Packet0Connect(data);
				Player player = new Player(packet.getUsername(), address, port);
				if (packet.getVersion() < CFG.VERSION)
				{
					try
					{
						CFG.p("[SERVER]: Rejected " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): outdated client");
						sendPacket(new Packet5Reject(Cause.OUTDATEDCLIENT), player);
						return;
					}
					catch (Exception e)
					{}
				}
				else if (packet.getVersion() > CFG.VERSION)
				{
					try
					{
						CFG.p("[SERVER]: Rejected " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): outdated server");
						sendPacket(new Packet5Reject(Cause.OUTDATEDSERVER), player);
						return;
					}
					catch (Exception e)
					{}
				}
				else if (!lobby && !connectableClients.contains(player))
				{
					try
					{
						CFG.p("[SERVER]: Rejected " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): game already started");
						sendPacket(new Packet5Reject(Cause.GAMERUNNING), player);
						return;
					}
					catch (Exception e)
					{}
				}
				for (Player p : clients)
				{
					if (p.getUsername().equals(packet.getUsername()))
					{
						try
						{
							sendPacket(new Packet5Reject(Cause.USERNAMETAKEN), player);
							CFG.p("[SERVER]: Rejected " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): username taken");
							return;
						}
						catch (Exception e)
						{}
					}
				}
				CFG.p("[SERVER]: " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + ") has connected.");
				clients.add(player);
				if (!connectableClients.contains(player)) connectableClients.add(player);
				try
				{
					sendPacketToAllClients(packet);
					if (!lobby && connectableClients.contains(player)) sendPacket(new Packet8Attribute("net_rejoin", "_"), player);
				}
				catch (Exception e)
				{}
				break;
			}
			case DISCONNECT:
			{
				Packet1Disconnect packet = new Packet1Disconnect(data);
				CFG.p("[SERVER]: " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + ") has disconnected. (" + packet.getReason() + ")");
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (Exception e)
				{}
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
				Packet2Rename packet = new Packet2Rename(data);
				for (int i = 0; i < clients.size(); i++)
				{
					if (clients.get(i).getUsername().equals(packet.getNewUsername()))
					{
						try
						{
							sendPacket(new Packet5Reject(Cause.USERNAMETAKEN), new Player(packet.getOldUsername(), address, port));
						}
						catch (Exception e)
						{}
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
				catch (Exception e)
				{}
				break;
			}
			case CHATMESSAGE:
			{
				Packet3ChatMessage packet = new Packet3ChatMessage(data);
				CFG.p("[SERVER]: " + packet.getUsername() + " (" + address.getHostAddress() + ":" + port + "): " + packet.getMessage());
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (Exception e)
				{}
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
					sendPacketToAllClients(new Packet4ServerInfo(players));
					for (Player p : clients)
					{
						sendPacketToAllClients(new Packet6Ready(p.getUsername(), p.isReady()));
					}
					for (String key : settings.keySet())
					{
						sendPacketToAllClients(new Packet7Settings(key, settings.get(key)));
					}
				}
				catch (Exception e)
				{}
				break;
			}
			case READY:
			{
				Packet6Ready packet = new Packet6Ready(data);
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
				catch (Exception e)
				{}
				break;
			}
			case SETTINGS:
			{
				Packet7Settings packet = new Packet7Settings(data);
				settings.put(packet.getKey(), packet.getValue());
				try
				{
					sendPacketToAllClients(packet);
				}
				catch (Exception e)
				{}
				break;
			}
			case ATTRIBUTE:
			{
				Packet8Attribute packet = new Packet8Attribute(data);
				if (packet.getKey().equals("net_rejoin")) sendRejoinData(new Player("", address, port));
				
				break;
			}
			default:
				CFG.p("[SERVER]: reveived unhandled packet (" + address.getHostAddress() + ":" + port + ") " + type + " [" + Packet.readData(data) + "]");
		}
	}
	
	public void sendPacket(Packet p, Player client) throws Exception
	{
		sendData(p.getData(), client);
	}
	
	public void sendData(byte[] data, Player client) throws Exception
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, client.getIP(), client.getPort());
		
		socket.send(packet);
	}
	
	public void sendPacketToAllClients(Packet packet) throws Exception
	{
		sendDataToAllClients(packet.getData());
	}
	
	public void sendDataToAllClients(byte[] data) throws Exception
	{
		for (Player p : clients)
			sendData(data, p);
	}
	
	public int getConnectedClientCount()
	{
		return clients.size();
	}
	
	private void sendRejoinData(Player player)
	{
		try
		{
			CFG.p(map.islands.size());
			sendPacket(new Packet8Attribute("mapeditor_progress_float", 0), player);
			for (int i = 0; i < map.islands.size(); i++)
			{
				sendPacket(new Packet9Island(map.islands.get(i)), player);
				sendPacket(new Packet8Attribute("mapeditor_progress_float", i / (float) (map.islands.size() - 1)), player);
			}
		}
		catch (Exception e)
		{}
	}
	
	public void setMap(Map m)
	{
		map = m;
	}
}
