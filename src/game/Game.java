package game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Game
{
	public static Game currentGame;

	float pos = 0;
	int i = 0;

	public void gameLoop()
	{
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

		System.out.println(pos);

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
		gluPerspective((float) 30, 640f / 480f, 0.001f, 100);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
	}

}
