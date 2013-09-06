package com.vloxlands.util;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

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
	
	public static void pullUserLogo()
	{
		if (RenderAssistant.textures.containsKey("USER_LOGO")) return;
		try
		{
			Texture t = BufferedImageUtil.getTexture("USER_LOGO", RenderAssistant.toBufferedImage(ImageIO.read(new URL("http://dakror.de/vloxlands/api/userlogo.php?user=" + CFG.USERNAME)).getScaledInstance(256, 256, BufferedImage.SCALE_SMOOTH)));
			RenderAssistant.textures.put("USER_LOGO", t);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
