package com.vloxlands;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.vloxlands.settings.CFG;
import com.vloxlands.util.Assistant;

import de.dakror.universion.UniVersion;

/**
 * @author Dakror
 */
public class Launcher
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		CFG.INTERNET = Assistant.isInternetReachable();
		
		UniVersion.offline = !CFG.INTERNET;
		UniVersion.init(Vloxlands.class, CFG.VERSION, CFG.PHASE);
		File jar = new File(CFG.DIR, "Vloxlands.jar");
		
		if (!jar.exists() && !CFG.INTERNET)
		{
			JOptionPane.showMessageDialog(null, "In order to launch the game the first time,you need to have an internet connection!\nEstablish a connection first, then start the game again.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "-XX:MinHeapFreeRatio=10", "-XX:MaxHeapFreeRatio=20", "\"" + jar.getPath() + "\"");
		try
		{
			pb.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
