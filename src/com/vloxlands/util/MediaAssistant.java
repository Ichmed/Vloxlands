package com.vloxlands.util;

import java.io.File;
import java.io.FileOutputStream;

import com.vloxlands.settings.CFG;

public class MediaAssistant
{
	public static void init()
	{
		new File(CFG.DIR, "natives").mkdirs();
		new File(CFG.DIR, "maps").mkdirs();
	}
	
	public static String[] getMaps()
	{
		File file = new File(CFG.DIR, "maps");
		if (!file.exists() || !file.isDirectory())
		{
			file.delete();
			file.mkdir();
		}
		String[] names = new String[file.list().length];
		
		for (int i = 0; i < names.length; i++)
		{
			names[i] = file.list()[i].substring(0, file.list()[i].lastIndexOf("."));
		}
		
		return names;
	}
	
	public static void initNatives()
	{
		File natives = new File(CFG.DIR, "natives");
		if (!natives.exists())
		{
			try
			{
				File tmpFile = new File(CFG.DIR, "tmp.zip");
				Assistant.copyInputStream(MediaAssistant.class.getResourceAsStream("/natives.zip"), new FileOutputStream(tmpFile));
				ZipAssistant.unzip(tmpFile, natives);
				tmpFile.delete();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static boolean needMediaUpdate(String folder)
	{
		try
		{
			new File(CFG.DIR, folder).mkdirs();
			boolean need = !Assistant.getFolderChecksum(new File(CFG.DIR, folder)).equals(CFG.class.getField(folder.toUpperCase() + "_CS").get(null));
			return need;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return true;
		}
	}
}
