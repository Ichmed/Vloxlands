package com.vloxlands.settings;

import java.util.HashMap;

import com.vloxlands.util.Assistant;
import com.vloxlands.util.CSVReader;

public class Tr {
	private static HashMap<String, String> tr = new HashMap<>();
	
	public static String activeLanguage;
	
	public static void loadLanguage(String langName) {
		langName = langName.toLowerCase();
		
		activeLanguage = langName;
		
		tr.clear();
		CSVReader csv = new CSVReader("/data/lang.csv");
		String[] langs = csv.readRow();
		int lang = Assistant.asList(langs).indexOf(langName);
		
		String cell;
		String key = "";
		while ((cell = csv.readNext()) != null) {
			if (csv.getIndex() == 0) key = cell;
			else if (csv.getIndex() == lang) tr.put(key, cell);
		}
	}
	
	public static String _(String key) {
		String s = tr.get(key);
		return s == null ? key : s;
	}
}
