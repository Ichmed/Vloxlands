package com.vloxlands.settings;

import java.io.File;

import com.vloxlands.util.Assistant;

public class CFG
{
	// -- UniVersion -- //
	public static final int VERSION = 2013090102;
	public static final int PHASE = 0;
	// -- debug -- //
	public static boolean DEBUG = true;
	public static boolean INTERNET;
	// -- options -- //
	public static boolean LIGHTING = false;
	public static boolean FULLSCREEN = false;
	public static boolean SHOW_CHUNK_BOUNDRIES = false;
	public static boolean SHOW_DIRECTIONS = false;
	public static boolean SHOW_WIREFRAME = false;
	// -- checksums -- //
	public static final String NATIVES_CS = "D5F0AA01BE562FE478FBC52209F56167";
	
	public static final File DIR = new File(System.clearProperty("user.home") + "/.dakror/Vloxlands");
	
	static
	{
		DIR.mkdir();
		String[] fs = { "natives" };
		for (String f : fs)
		{
			String cs = Assistant.getFolderChecksum(new File(DIR, f));
			p(f + ": " + cs);
		}
	}
	
	public static void p(Object p)
	{
		System.out.println(p);
	}
	
	public static void b(Object... b)
	{
		String s = "";
		for (int i = 0; i < b.length; i += 2)
		{
			s += b[i] + ": " + b[i + 1] + ",";
		}
		p(s.substring(0, s.length() - 1));
	}
}
