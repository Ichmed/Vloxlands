package game;


import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Game
{
	public static Game currentGame;
	
	int i = 0;
	
	public void gameLoop()
	{
		i++;
		glPushMatrix();
		{
			glColor4d(1, 1, 1, 1);
			glPointSize(5);
			glTranslated(i % 3 - 2, i % 7 - 4, i % 5 - 3);
//			glTranslated(0, 0, -3);
		}
		glPopMatrix();
		
		System.out.println("blub");

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Display.update();
		Display.sync(60);		
	}

	public static void initGame()
	{
		currentGame = new Game();
	}
	
	public static void initGLSettings()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(100, (float) Display.getWidth() / Display.getHeight(), 0.01f, 1000);
		glMatrixMode(GL_MODELVIEW);

		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
//		glEnable(GL_CULL_FACE);
	}	
}
