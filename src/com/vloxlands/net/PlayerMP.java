package com.vloxlands.net;

import java.net.InetAddress;

import com.vloxlands.game.entity.Player;

/**
 * @author Dakror
 */
public class PlayerMP extends Player
{
	private InetAddress ipAddress;
	private int port;
	
	public PlayerMP(InetAddress ipAddress, int port, String username)
	{
		super(username);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public InetAddress getIPAddress()
	{
		return ipAddress;
	}
	
	public int getPort()
	{
		return port;
	}
}
