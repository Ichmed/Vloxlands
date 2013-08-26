package com.vloxlands.util;

import java.awt.Font;
import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;

public class FontAssistant
{
	private static HashMap<Font, TrueTypeFont> fonts = new HashMap<>();

	public static Font GAMEFONT;

	public static TrueTypeFont getFont(Font f)
	{
		if (fonts.containsKey(f)) return fonts.get(f);
		else
		{
			TrueTypeFont uf = new TrueTypeFont(f, false);
			fonts.put(f, uf);
			return uf;
		}
	}

	static
	{
		try
		{
			GAMEFONT = Font.createFont(Font.TRUETYPE_FONT, FontAssistant.class.getResourceAsStream("/alagard.ttf"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
