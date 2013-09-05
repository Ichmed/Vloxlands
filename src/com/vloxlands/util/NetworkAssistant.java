package com.vloxlands.util;

import java.net.MalformedURLException;
import java.net.URL;

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
	
	public static boolean login(String username, String password)
	{
		try
		{
			CFG.p(Assistant.getURLContent(new URL("http://dakror.de/vloxlands/api/login.php?username=" + username + "&password=" + password)));
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
