package com.vloxlands.net;

import java.net.InetAddress;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Color;

/**
 * @author Dakror
 */
public class Player
{
	public static final Color[] COLORS = { Color.black, Color.blue, Color.cyan, Color.gray, Color.green, Color.magenta, Color.orange, Color.red, Color.white, Color.yellow };
	String username;
	InetAddress ip;
	int port;
	boolean ready;
	Color color;
	
	public Player(String username)
	{
		this(username, null, 0);
	}
	
	public Player(String username, InetAddress ip, int port)
	{
		this.username = username;
		this.ip = ip;
		this.port = port;
		color = COLORS[0];
	}
	
	public Player(JSONObject o)
	{
		try
		{
			username = o.getString("username");
			ip = InetAddress.getByName(o.getString("ip"));
			port = o.getInt("port");
			ready = o.getBoolean("ready");
			color = COLORS[o.getInt("color")];
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
	
	public boolean isReady()
	{
		return ready;
	}
	
	public void setReady(boolean ready)
	{
		this.ready = ready;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Player)) return false;
		
		Player p = (Player) obj;
		
		return username.equals(p.username) && ip.equals(p.ip);
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public String serialize()
	{
		JSONObject o = new JSONObject();
		
		try
		{
			o.put("username", username);
			o.put("ip", ip.getHostAddress());
			o.put("port", port);
			o.put("ready", ready);
			o.put("color", Arrays.asList(COLORS).indexOf(color));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		
		return o.toString();
	}
}
