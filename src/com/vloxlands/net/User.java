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
}
