package com.vloxlands.settings;

public class CFG
{
	// -- UniVersion -- //
	public static final int VERSION = 2013082609;
	public static final int PHASE = 0;
	// -- debug -- //
	public static boolean DEBUG = true;
	public static boolean INTERNET;	
	// -- options -- //
	public static boolean LIGHTING = false;
	
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
