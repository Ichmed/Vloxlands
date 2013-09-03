package com.vloxlands.settings;

import java.util.HashMap;

import com.vloxlands.util.CSVReader;

public class Tr
{
	private static HashMap<String, String> tr = new HashMap<>();
	
	public static void loadLanguage(String lang)
	{
		tr.clear();
		CSVReader csv = new CSVReader("/data/" + lang + ".csv");
		csv.skipRow();
		
		String cell;
		String key = "";
		while ((cell = csv.readNext()) != null)
		{
			if (csv.getIndex() == 0) key = cell;
			else tr.put(key, cell);
		}
	}
	
	public static String _(String key)
	{
		return tr.get(key);
	}
}
