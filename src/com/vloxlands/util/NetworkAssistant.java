package com.vloxlands.util;

import java.net.MalformedURLException;
import java.net.URL;

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
	
	public static boolean login(String username, String password)
	{
		try
		{
			return Integer.parseInt(Assistant.getURLContent(new URL("http://dakror.de/vloxlands/api/login.php?username=" + username + "&password=" + Assistant.MD5(password.getBytes()) + "&ip=" + getClientIP()))) == 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
