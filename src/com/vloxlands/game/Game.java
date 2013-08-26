package com.vloxlands.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.vloxlands.game.util.Camera;
import com.vloxlands.scene.Scene;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.GUIAssistant;
import com.vloxlands.util.RenderAssistant;

public class Game
{
	public static Game currentGame;

	public Camera camera = new Camera();

	long start = 0;
	int frames = 21;
	boolean showFPS = false;

	Scene scene;

	public void gameLoop()
	{
		if (start == 0) start = System.currentTimeMillis();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);

		gluPerspective((float) 30, Display.getWidth() / (float) Display.getHeight(), 0.001f, 100);
		glPushMatrix();
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glTranslated(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);

			if (Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				camera.move(0, 0, 0.1f);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S))
			{
				camera.move(0, 0, -0.1f);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				camera.move(0.1f, 0, 0);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				camera.move(-0.1f, 0, 0);
			}
			
			glColor3f(0.5f, 0.5f, 1.0f);

			glColor4d(1, 1, 1, 1);
			
			RenderAssistant.renderVoxel((byte)0, 1);

		}
		glPopMatrix();

		GUIAssistant.handleMouse();

		if (scene != null) scene.update();

		RenderAssistant.set2DRenderMode(true);

		GUIAssistant.renderComponents();

		while (Keyboard.next())
			if (Keyboard.getEventKey() == Keyboard.KEY_F4 && !Keyboard.getEventKeyState()) showFPS = !showFPS;
		
		if (showFPS) RenderAssistant.renderText(0, 0, getFPS() + "", Color.white, FontAssistant.GAMEFONT.deriveFont(30f));

		RenderAssistant.set2DRenderMode(false);

		Display.update();
		Display.sync(60);

		frames++;

	}

	public static void initGame()
	{
		currentGame = new Game();
	}

	public void setScene(Scene s)
	{
		GUIAssistant.clearComponents();

		scene = s;
		scene.init();
	}

	public int getFPS()
	{
		return Math.round(frames / ((System.currentTimeMillis() - start) / 1000f));
	}

	public static void initGLSettings()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_ALPHA_TEST);
	}
}
