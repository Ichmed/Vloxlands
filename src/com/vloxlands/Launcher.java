package com.vloxlands;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.json.JSONArray;
import org.json.JSONException;

import com.vloxlands.settings.CFG;
import com.vloxlands.util.Assistant;

import de.dakror.universion.UniVersion;

/**
 * @author Dakror
 */
public class Launcher {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CFG.INTERNET = Assistant.isInternetReachable();
		
		File jar = new File(CFG.DIR, "Vloxlands.jar");
		
		UniVersion.offline = !CFG.INTERNET;
		UniVersion.setAutoDownloadTarget(new File(CFG.DIR, "Vloxlands.jar"));
		
		int[] v = readVersion();
		
		UniVersion.init(Vloxlands.class, v[1], v[0]);
		
		if (!jar.exists() && !CFG.INTERNET) {
			JOptionPane.showMessageDialog(null,
																		"In order to launch the game the first time,you need to have an internet connection!\nEstablish a connection first, then start the game again.",
																		"Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "-XX:MinHeapFreeRatio=10", "-XX:MaxHeapFreeRatio=20", "\"" + jar.getPath() + "\"");
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int[] readVersion() {
		try {
			File version = new File(CFG.DIR, "version.json");
			
			if (!version.exists()) return new int[] { 0, 0 };
			
			JSONArray arr = new JSONArray(Assistant.getFileContent(version));
			return new int[] { arr.getInt(0), arr.getInt(1) };
		} catch (JSONException e) {
			e.printStackTrace();
			return new int[] { 0, 0 };
		}
		
	}
}
