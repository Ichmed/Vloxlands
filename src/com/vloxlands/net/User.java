package com.vloxlands.net;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Dakror
 */
public class User
{
	String username;
	String ip;
	String logo;
	int id;
	User[] friends;
	
	public User(JSONObject data)
	{
		try
		{
			username = data.getString("USERNAME");
			ip = data.getString("IP");
			
		}
		catch (JSONException e)
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
	
	public String getIP()
	{
		return ip;
	}
	
	public void setIP(String ip)
	{
		this.ip = ip;
	}
	
	public String getLogo()
	{
		return logo;
	}
	
	public void setLogo(String logo)
	{
		this.logo = logo;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public User[] getFriends()
	{
		return friends;
	}
	
	public void setFriends(User[] friends)
	{
		this.friends = friends;
	}
}
