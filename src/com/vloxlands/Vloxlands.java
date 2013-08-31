package com.vloxlands;

import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.vloxlands.game.Game;
import com.vloxlands.scene.Scene_Mainmenu;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Assistant;

import de.dakror.universion.UniVersion;

public class Vloxlands
{
	public static void main(String[] args)
	{
		CFG.INTERNET = Assistant.isInternetReachable();
		
		// -- UniVersion & Reporter initialization -- //
		UniVersion.offline = !CFG.INTERNET;
		UniVersion.init(Vloxlands.class, CFG.VERSION, CFG.PHASE);
		
		if (!CFG.DEBUG)
		{
			// -- Deactivated while in development stage -- //
			// Reporter.init(new File(FileManager.dir, "Logs"));
		}
		
		System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
		
		// Direction.get90DegreeDiretions(Direction.UP);
		
		
		try
		{
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setDisplayMode(new DisplayMode(1080, 720));
			Display.setIcon(new ByteBuffer[] { Assistant.loadImage(Vloxlands.class.getResourceAsStream("/graphics/logo/logo16.png")), Assistant.loadImage(Vloxlands.class.getResourceAsStream("/graphics/logo/logo32.png")) });
			Display.setTitle("Vloxlands");
			// Display.setInitialBackground(0.5f, 0.8f, 0.85f);
			Display.create();
			Game.initGLSettings();
			
			Game.initGame();
			Game.currentGame.setScene(new Scene_Mainmenu());
			
			while (!Display.isCloseRequested())
				Game.currentGame.gameLoop();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Display.destroy();
	}
}
