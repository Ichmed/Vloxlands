import game.Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Main
{
	public static void main(String[] args)
	{
		Game.initGame();
		try
		{
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.create();
			Game.initGLSettings();
			while(!Display.isCloseRequested()) Game.currentGame.gameLoop();
				
		} 
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		Display.destroy();
	}
}