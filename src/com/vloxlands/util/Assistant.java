package com.vloxlands.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.newdawn.slick.opengl.PNGDecoder;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

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
	
	public static ByteBuffer loadImage(InputStream is)
	{
		try
		{
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
		f.getParentFile().mkdirs();
		try
		{
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f));
			osw.write(s);
			osw.close();
		}
		catch (Exception e)
		{}
	}
	
	public static String getFolderChecksum(File folder)
	{
		if (!folder.exists()) return null;
		String[] files = folder.list();
		Arrays.sort(files);
		String f = Arrays.toString(files) + getFolderSize(folder);
		return MD5(f.getBytes());
	}
	
	public static String MD5(byte[] b)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return HexBin.encode(md.digest(b));
	}
	
	public static long getFolderSize(File directory)
	{
		long length = 0;
		for (File file : directory.listFiles())
		{
			if (file.isFile()) length += file.length();
			else length += getFolderSize(file);
		}
		return length;
	}
	
	public static void deleteFolder(File folder)
	{
		for (File f : folder.listFiles())
		{
			if (f.isDirectory()) deleteFolder(f);
			f.delete();
		}
	}
	
	public static String formatBinarySize(long size, int digits)
	{
		final String[] levels = { "", "K", "M", "G", "T" };
		for (int i = levels.length - 1; i > -1; i--)
			if (size > (long) Math.pow(1024, i))
			{
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(digits);
				df.setMinimumFractionDigits(digits);
				return df.format(size / Math.pow(1024, i)) + levels[i] + "B";
			}
		return null;
	}
}
