package com.vloxlands.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.vloxlands.game.util.Camera;
import com.vloxlands.game.world.Map;
import com.vloxlands.gen.MapGenerator;
import com.vloxlands.scene.Scene;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.GUIAssistant;
import com.vloxlands.util.RenderAssistant;

public class Game
{
	public static final int CHUNK_SIZE = 64;
	public static Game currentGame;
	public static Map currentMap;
	
	public Camera camera = new Camera();
	
	long start = 0;
	public int frames = 21;
	boolean showFPS = false;
	
	Scene scene;
	
	public float cameraSpeed = 0.1f;
	public int cameraRotationSpeed = 180;
	
	public void gameLoop()
	{
		
		if (start == 0) start = System.currentTimeMillis();
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		
		moveCamera();
		if (Mouse.isButtonDown(1))
		{
			Mouse.setGrabbed(true);
			rotateCamera();
		}
		else Mouse.setGrabbed(false);
		
		
		gluPerspective((float) 50, Display.getWidth() / (float) Display.getHeight(), 0.001f, 100);
		
		glRotated(camera.getRotation().x, 1f, 0f, 0f);
		glRotated(camera.getRotation().y, 0f, 1f, 0f);
		glRotated(camera.getRotation().z, 0f, 0f, 1f);
		
		glTranslated(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
		
		glPushMatrix();
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			
			
			currentMap.render();
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
		currentMap = MapGenerator.generateRandomMap();
		currentMap.startMap();
		currentGame.camera.setPosition(0, 0, 0);
		currentGame.camera.setRotation(0, 180, 0);
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
		// glEnable(GL_ALPHA_TEST);
		
		glShadeModel(GL_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}
	
	public void moveCamera()
	{
		float speed = 0.3f;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) camera.move(0, 0, speed);
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) camera.move(0, speed, 0);
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) camera.move(speed, 0, 0);
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) camera.move(0, 0, -speed);
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) camera.move(0, -speed, 0);
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) camera.move(-speed, 0, 0);
	}
	
	public void rotateCamera()
	{
		float x = ((Mouse.getY() - (Display.getHeight() / 2)) / (float) Display.getHeight()) * cameraRotationSpeed;
		float y = ((Mouse.getX() - (Display.getWidth() / 2)) / (float) Display.getWidth()) * cameraRotationSpeed;
		
		camera.rotate(-x, y, 0);
		
		Mouse.setCursorPosition((Display.getWidth() / 2), (Display.getHeight() / 2));
	}
}
