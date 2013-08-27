package com.vloxlands.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.nio.ByteBuffer;

import org.newdawn.slick.opengl.PNGDecoder;

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
	
	public static ByteBuffer loadImage(URL url)
	{
		try
		{
			InputStream is = url.openStream();
			PNGDecoder decoder = new PNGDecoder(is);
			ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
			decoder.decode(bb, decoder.getWidth() * 4, PNGDecoder.RGBA);
			bb.flip();
			is.close();
			return bb;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setFileContent(File f, String s)
	{
		try
		{
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f));
			osw.write(s);
			osw.close();
		}
		catch (Exception e)
		{}
	}
}
