package com.vloxlands.util;

import java.io.File;
import java.io.FileOutputStream;

import com.vloxlands.settings.CFG;

public class MediaAssistant
{
	public static void initNatives()
	{
		File natives = new File(CFG.DIR, "natives");
		if (!natives.exists())
		{
			natives.mkdirs();
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
