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
import com.vloxlands.net.packet.Packet02Rename;
import com.vloxlands.net.packet.Packet03ChatMessage;
import com.vloxlands.net.packet.Packet04ServerInfo;
import com.vloxlands.net.packet.Packet05Reject;
import com.vloxlands.net.packet.Packet05Reject.Cause;
import com.vloxlands.net.packet.Packet06Ready;
import com.vloxlands.settings.CFG;
import com.vloxlands.settings.Tr;

/**
 * @author Dakror
 */
public class Client extends Thread
{
	private DatagramSocket socket;
	private InetAddress serverIP;
	private Player player;
	
	boolean connected;
	Cause rejectionCause;
	
	public Client(Player player)
	{
		try
		{
			this.player = player;
			socket = new DatagramSocket();
			connected = false;
			rejectionCause = null;
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
	
	private void parsePacket(byte[] data)
	{
		PacketTypes type = Packet.lookupPacket(data[0]);
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
				else print(Tr._("mp.connect").replace("%player%", packet.getUsername()), "INFO");
				Game.currentGame.onClientReveivedPacket(packet);
				break;
			}
			case DISCONNECT:
			{
				Packet01Disconnect packet = new Packet01Disconnect(data);
				if (packet.getUsername().equals(player.getUsername())) connected = false;
				else print(Tr._("mp.disconnect").replace("%player%", packet.getUsername()) + " (" + Tr._(packet.getReason()) + ")", "INFO");
				Game.currentGame.onClientReveivedPacket(packet);
				break;
			}
			case RENAME:
			{
				Packet02Rename packet = new Packet02Rename(data);
				if (packet.getOldUsername().equals(player.getUsername())) player.setUsername(packet.getNewUsername());
				print(Tr._("mp.rename").replace("%oldname%", packet.getOldUsername()).replace("%newname%", packet.getNewUsername()), "INFO");
				Game.currentGame.onClientReveivedPacket(packet);
				break;
			}
			case CHATMESSAGE:
			{
				Packet03ChatMessage packet = new Packet03ChatMessage(data);
				print(packet.getUsername() + ": " + packet.getMessage(), "");
				Game.currentGame.onClientReveivedPacket(packet);
				break;
			}
			case SERVERINFO:
			{
				Game.currentGame.onClientReveivedPacket(new Packet04ServerInfo(data));
				break;
			}
			case REJECT:
			{
				Packet05Reject packet = new Packet05Reject(data);
				rejectionCause = packet.getCause();
				break;
			}
			case READY:
			{
				Packet06Ready packet = new Packet06Ready(data);
				Game.currentGame.onClientReveivedPacket(packet);
				break;
			}
		}
	}
	
	public void print(String message, String level)
	{
		Game.currentGame.onClientMessage(level + "$" + message);
	}
	
	public boolean connectToServer(InetAddress ip)
	{
		if (connected)
		{
			System.err.println("Client is already connected to a server. Disconnect first!");
			return false;
		}
		rejectionCause = null;
		
		serverIP = ip;
		try
		{
			sendPacket(new Packet00Connect(player.getUsername(), CFG.VERSION));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getUsername()
	{
		return player.getUsername();
	}
	
	public void disconnect()
	{
		if (!connected) return;
		
		rejectionCause = null;
		try
		{
			sendPacket(new Packet01Disconnect(player.getUsername(), "mp.reason.disconnect"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isRejected()
	{
		return rejectionCause != null;
	}
	
	public void resetRejection()
	{
		rejectionCause = null;
	}
	
	public Cause getRejectionCause()
	{
		return rejectionCause;
	}
	
	public boolean isConnected()
	{
		return connected;
	}
	
	public boolean isConnectedToLocalhost()
	{
		return connected && serverIP.equals(Game.IP) && Game.server != null;
	}
	
	public void renameClient(String newName)
	{
		if (connected)
		{
			try
			{
				sendPacket(new Packet02Rename(player.getUsername(), newName));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else player.setUsername(newName);
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
