package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import ui.Button;
import ui.GUIHandler;

public class Game
{
	public static Game currentGame;

	float pos = 0;
	int i = 0;

	public void gameLoop()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);

		gluPerspective((float) 30, 640f / 480f, 0.001f, 100);
		glPushMatrix();
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glTranslated(0, 0, pos);

			if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			{
				pos += 0.1f;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			{
				pos -= 0.1f;
			}
			glColor3f(0.5f, 0.5f, 1.0f);

			glPointSize(10);
			glColor4d(1, 1, 1, 1);

			glBegin(GL_POINTS);
			{
				glVertex3f(0, 0, 0);
			}
			glEnd();
		}
		glPopMatrix();

		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0.0, 640, 480, 0.0, -1.0, 10.0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glClear(GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glDisable(GL_DEPTH_TEST);

		GUIHandler.renderComponents();
		
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);

		Display.update();
		Display.sync(60);
	}

	public static void initGame()
	{
		currentGame = new Game();

		GUIHandler.addComponent(new Button(40, 40, 200, 200, "LOL, es geht"));
	}

	public static void initGLSettings()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
	}

}
