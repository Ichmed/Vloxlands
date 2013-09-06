package com.vloxlands;

import java.io.File;
import java.nio.ByteBuffer;

import javax.swing.UIManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.vloxlands.game.Game;
import com.vloxlands.game.UpdateThread;
import com.vloxlands.scene.SceneLogo;
import com.vloxlands.settings.CFG;
import com.vloxlands.settings.Settings;
import com.vloxlands.util.Assistant;
import com.vloxlands.util.MediaAssistant;

import de.dakror.universion.UniVersion;

public class Vloxlands
{
	public static boolean running;
	
	public static void main(String[] args)
	{
		CFG.INTERNET = Assistant.isInternetReachable();
		
		Settings.loadSettings();
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		// -- UniVersion & Reporter initialization -- //
		UniVersion.offline = !CFG.INTERNET;
		UniVersion.init(Vloxlands.class, CFG.VERSION, CFG.PHASE);
		
		if (!CFG.DEBUG)
		{
			// -- Deactivated while in development stage -- //
			// Reporter.init(new File(FileManager.dir, "Logs"));
		}
		
		MediaAssistant.initNatives();
		Thread.currentThread().setName("Main Thread");
		System.setProperty("org.lwjgl.librarypath", new File(CFG.DIR, "natives").getAbsolutePath());
		try
		{
			running = true;
			setFullscreen();
			Display.setIcon(new ByteBuffer[] { Assistant.loadImage(Vloxlands.class.getResourceAsStream("/graphics/logo/logo16.png")), Assistant.loadImage(Vloxlands.class.getResourceAsStream("/graphics/logo/logo32.png")) });
			Display.setTitle("Vloxlands");
			Display.setResizable(true);
			Display.create();
			Game.initGLSettings();
			
			Game.initGame();
			Game.currentGame.addScene(new SceneLogo());
			while (running)
			{
				if (Display.isCloseRequested()) break;
				
				Game.currentGame.gameLoop();
			}
			running = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Display.destroy();
		exit();
	}
	
	public static void setFullscreen()
	{
		if (CFG.FULLSCREEN) enterFullscreen();
		else leaveFullscreen();
	}
	
	public static void enterFullscreen()
	{
		try
		{
			Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			Display.setVSyncEnabled(true);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void leaveFullscreen()
	{
		// Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		
		try
		{
			Display.setDisplayMode(new DisplayMode(1280/* d.width - 300 */, /* (int) (d.height - 300 * (d.height / (float) d.width)) */720));
			Display.setFullscreen(false);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void exit()
	{
		UpdateThread.requestStop = true;
		Settings.saveSettings();
		
		System.exit(0);
	}
}
