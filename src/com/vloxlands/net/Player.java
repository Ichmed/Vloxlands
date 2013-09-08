package com.vloxlands.net;

import java.net.InetAddress;

/**
 * @author Dakror
 */
public class Player
{
	String username;
	InetAddress ip;
	int port;
	
	public Player(String username)
	{
		this.username = username;
	}
	
	public Player(String username, InetAddress ip, int port)
	{
		this.username = username;
		this.ip = ip;
		this.port = port;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public InetAddress getIP()
	{
		return ip;
	}
	
	public void setIP(InetAddress ip)
	{
		this.ip = ip;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
}
