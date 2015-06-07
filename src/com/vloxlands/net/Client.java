package com.vloxlands.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.vloxlands.Vloxlands;
import com.vloxlands.game.Game;
import com.vloxlands.game.world.Island;
import com.vloxlands.net.packet.Packet;
import com.vloxlands.net.packet.Packet.PacketTypes;
import com.vloxlands.net.packet.Packet0Connect;
import com.vloxlands.net.packet.Packet1Disconnect;
import com.vloxlands.net.packet.Packet2Rename;
import com.vloxlands.net.packet.Packet3ChatMessage;
import com.vloxlands.net.packet.Packet5Reject;
import com.vloxlands.net.packet.Packet5Reject.Cause;
import com.vloxlands.net.packet.Packet8Attribute;
import com.vloxlands.net.packet.Packet9Island;
import com.vloxlands.settings.CFG;
import com.vloxlands.settings.Tr;

/**
 * @author Dakror
 */
public class Client extends Thread {
	private DatagramSocket socket;
	private InetAddress serverIP;
	private Player player;
	
	boolean connected;
	boolean rejoin;
	Cause rejectionCause;
	
	public Client(Player player) {
		try {
			this.player = player;
			socket = new DatagramSocket();
			connected = false;
			rejectionCause = null;
			setName("Client-Thread");
		} catch (Exception e) {
			e.printStackTrace();
		}
		start();
	}
	
	@Override
	public void run() {
		while (Vloxlands.running) {
			byte[] data = new byte[Server.PACKETSIZE];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(data);
		}
	}
	
	private void parsePacket(byte[] data) {
		PacketTypes type = Packet.lookupPacket(data[0]);
		switch (type) {
			case INVALID: {
				CFG.p("received invalid packet: " + new String(data));
				return;
			}
			
			case ATTRIBUTE: {
				Packet8Attribute p = new Packet8Attribute(data);
				if (p.getKey().equals("net_rejoin")) rejoin = true;
				
				break;
			}
			case SERVERINFO:
			case PLAYER:
			case SETTINGS:
			case ENTITYBUILDING:
				break;
			
			case CONNECT: {
				Packet0Connect packet = new Packet0Connect(data);
				if (packet.getUsername().equals(player.getUsername())) connected = true;
				else print(Tr._("mp.connect").replace("%player%", packet.getUsername()), "INFO");
				break;
			}
			case DISCONNECT: {
				Packet1Disconnect packet = new Packet1Disconnect(data);
				if (packet.getUsername().equals(player.getUsername())) {
					if (isConnectedToLocalhost()) {
						Game.server.shutdown();
						Game.server = null;
					}
					connected = false;
				} else print(Tr._("mp.disconnect").replace("%player%", packet.getUsername()) + " (" + Tr._(packet.getReason()) + ")", "INFO");
				break;
			}
			case RENAME: {
				Packet2Rename packet = new Packet2Rename(data);
				if (packet.getOldUsername().equals(player.getUsername())) player.setUsername(packet.getNewUsername());
				print(Tr._("mp.rename").replace("%oldname%", packet.getOldUsername()).replace("%newname%", packet.getNewUsername()), "INFO");
				break;
			}
			case CHATMESSAGE: {
				Packet3ChatMessage packet = new Packet3ChatMessage(data);
				print(packet.getUsername() + ": " + packet.getMessage(), "");
				break;
			}
			case REJECT: {
				Packet5Reject packet = new Packet5Reject(data);
				rejectionCause = packet.getCause();
				break;
			}
			case ISLAND: {
				final Packet9Island packet = new Packet9Island(data);
				new Thread() {
					@Override
					public void run() {
						Island island = packet.getIsland();
						Game.currentMap.addIsland(island);
					}
				}.start();
				break;
			}
			// case ENTITYBUILDING:
			// {
			// Packet10EntityBuilding packet = new Packet10EntityBuilding(data);
			//
			// if (packet.getIslandIndex() >= Game.currentMap.islands.size() - 1)
			// {
			// cache.add(packet);
			// break;
			// }
			//
			// Game.currentMap.islands.get(packet.getIslandIndex()).addEntity(packet.getEntityBuilding());
			//
			// break;
			// }
			default:
				CFG.p("reveived unhandled packet: " + type + " [" + Packet.readData(data) + "]");
		}
		
		Game.currentGame.onClientReveivedData(data);
	}
	
	public void print(String message, String level) {
		Game.currentGame.onClientMessage(level + "$" + message);
	}
	
	public boolean connectToServer(InetAddress ip) {
		if (connected) {
			System.err.println("Client is already connected to a server. Disconnect first!");
			return false;
		}
		rejectionCause = null;
		
		serverIP = ip;
		try {
			sendPacket(new Packet0Connect(player.getUsername(), CFG.VERSION));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getUsername() {
		return player.getUsername();
	}
	
	public void disconnect() {
		if (!connected) return;
		
		rejectionCause = null;
		try {
			sendPacket(new Packet1Disconnect(player.getUsername(), "mp.reason.disconnect"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isRejoined() {
		return rejoin;
	}
	
	public void resetRejoined() {
		rejoin = false;
	}
	
	public boolean isRejected() {
		return rejectionCause != null;
	}
	
	public void resetRejection() {
		rejectionCause = null;
	}
	
	public Cause getRejectionCause() {
		return rejectionCause;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isConnectedToLocalhost() {
		return connected && serverIP.equals(Game.IP) && Game.server != null;
	}
	
	public void renameClient(String newName) {
		if (connected) {
			try {
				sendPacket(new Packet2Rename(player.getUsername(), newName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else player.setUsername(newName);
	}
	
	public void sendPacket(Packet p) throws IOException {
		sendData(p.getData());
	}
	
	public void sendData(byte[] data) throws IOException {
		if (serverIP == null) {
			System.err.println("Connect to a server first!");
			return;
		}
		
		DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, CFG.SERVER_PORT);
		socket.send(packet);
	}
}
