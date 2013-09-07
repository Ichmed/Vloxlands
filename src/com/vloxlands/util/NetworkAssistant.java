package com.vloxlands.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class NetworkAssistant
{
	public static String getClientIP()
	{
		try
		{
			return Assistant.getURLContent(new URL("http://dakror.de/vloxlands/api/ip.php"));
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean login(String username, String md5password)
	{
		try
		{
			return Integer.parseInt(Assistant.getURLContent(new URL("http://dakror.de/vloxlands/api/login.php?username=" + username + "&password=" + md5password + "&ip=" + getClientIP()))) == 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static JSONObject getPlayerDataFromDatabase(String username)
	{
		try
		{
			return new JSONObject(Assistant.getURLContent(new URL("http://dakror.de/vloxlands/api/user.php?user=" + username)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONObject searchFriend(String name) throws Exception
	{
		return new JSONObject(Assistant.getURLContent(new URL("http://dakror.de/vloxlands/api/friend.php?user=" + CFG.USERNAME + "&search=" + name)));
	}
	
	/**
	 * @return -1 if friend already exists<br>
	 *         0 if sth went wrong<br>
	 *         1 if OK<br>
	 *         Exception String if exception got thrown
	 */
	public static String addFriend(int id)
	{
		try
		{
			return Assistant.getURLContent(new URL("http://dakror.de/vloxlands/api/friend.php?user=" + CFG.USERNAME + "&id=" + id));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return e.getClass().getSimpleName() + ": " + e.getMessage();
		}
	}
}
