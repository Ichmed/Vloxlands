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
			
			// data.put("username", (CFG.SAVE_USER) ? CFG.USERNAME : "");
			// data.put("password", (CFG.SAVE_USER) ? CFG.PASSWORD : "");
			data.put("lang", Tr.activeLanguage);
			data.put("fullscreen", CFG.FULLSCREEN);
			data.put("fov", CFG.FOV);
			data.put("fps", CFG.FPS);
			data.put("zfar", CFG.RENDER_DISTANCE);
			
			Assistant.setFileContent(file, data.toString());
		}
		catch (JSONException e)
		{}
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
			CFG.FOV = data.getInt("fov");
			CFG.FPS = data.getInt("fps");
			CFG.RENDER_DISTANCE = data.getInt("zfar");
		}
		catch (JSONException e)
		{}
	}
}
