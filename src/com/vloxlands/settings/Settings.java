package com.vloxlands.settings;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.vloxlands.util.Assistant;


public class Settings
{
	public static void saveSettings()
	{
		try
		{
			File file = new File(CFG.DIR, "settings.json");
			JSONObject data = new JSONObject();
			
			data.put("lang", Tr.activeLanguage);
			data.put("fullscreen", CFG.FULLSCREEN);
			
			Assistant.setFileContent(file, data.toString());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadSettings()
	{
		try
		{
			File file = new File(CFG.DIR, "settings.json");
			if (!file.exists())
			{
				Tr.loadLanguage("us");
				CFG.FULLSCREEN = false;
				return;
			}
			
			JSONObject data = new JSONObject(Assistant.getFileContent(file));
			Tr.loadLanguage(data.getString("lang"));
			CFG.FULLSCREEN = data.getBoolean("fullscreen");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
