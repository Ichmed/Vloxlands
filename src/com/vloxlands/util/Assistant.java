package com.vloxlands.util;

import java.net.InetAddress;

public class Assistant
{
	public static boolean isInternetReachable()
	{
		try
		{
			return InetAddress.getByName("dakror.de").isReachable(60000);
		}
		catch (Exception e)
		{
			return false;
		}
	}

}
