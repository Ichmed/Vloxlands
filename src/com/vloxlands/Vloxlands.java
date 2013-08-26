package com.vloxlands;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.vloxlands.game.Game;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Assistant;

import de.dakror.universion.UniVersion;

public class Vloxlands
{
	public static void main(String[] args)
	{
		CFG.INTERNET = Assistant.isInternetReachable();
	    
		//-- UniVersion & Reporter initialization -- //
	    UniVersion.offline = !CFG.INTERNET;
	    UniVersion.init(Vloxlands.class, CFG.VERSION, CFG.PHASE);
	    
	    if (!CFG.DEBUG)
	    {
	      // -- Deactivated while in development stage -- //
	      // Reporter.init(new File(FileManager.dir, "Logs"));
	    }
	    
		Game.initGame();
		try
		{
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.create();
			Game.initGLSettings();
			while (!Display.isCloseRequested())
				Game.currentGame.gameLoop();

		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		Display.destroy();
	}
}