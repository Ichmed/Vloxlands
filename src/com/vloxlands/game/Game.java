package com.vloxlands.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import com.vloxlands.game.util.Camera;
import com.vloxlands.game.world.Map;
import com.vloxlands.gen.MapGenerator;
import com.vloxlands.scene.Scene;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.GUIAssistant;
import com.vloxlands.util.MathHelper;
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
	private Vector3f lightPos = new Vector3f();
	
	public void gameLoop()
	{
		
		if (start == 0) start = System.currentTimeMillis();
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		//ShaderLoader.useProgram("graphics/shaders/", "default");
		RenderAssistant.enable(GL_LIGHTING);
		
		moveCamera();
		
		Mouse.next();
		int p = Mouse.getEventButton();
		
		if (Mouse.isButtonDown(1))
		{
			Mouse.setGrabbed(true);
			if (p != 1) rotateCamera();
			else Mouse.setCursorPosition((Display.getWidth() / 2), (Display.getHeight() / 2));
		}
		else Mouse.setGrabbed(false);
		
		
		gluPerspective((float) 50, Display.getWidth() / (float) Display.getHeight(), 0.001f, 100);
		
		glRotated(camera.getRotation().x, 1f, 0f, 0f);
		glRotated(camera.getRotation().y, 0f, 1f, 0f);
		glRotated(camera.getRotation().z, 0f, 0f, 1f);
		
		glTranslated(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
		
		glPushMatrix();
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			currentMap.render();
			
			glPointSize(10);
			glBegin(GL_POINTS);
			{
				glVertex3f(lightPos.x, lightPos.y, lightPos.z);
			}
			glEnd();
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			lightPos.x = camera.position.x;
			lightPos.y = camera.position.y;
			lightPos.z = camera.position.z;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			lightPos.z++;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			lightPos .z--;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			lightPos.x++;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			lightPos.x--;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) cameraSpeed = 0.5f;
		else cameraSpeed = 0.1f;
		glLight(GL_LIGHT0, GL_POSITION, MathHelper.asFloatBuffer(new float[] { lightPos.x, lightPos.y, lightPos.z, 1 }));

		
		currentMap.onTick();
		
		Display.update();
		Display.sync(60);
		
		frames++;
		
	}
	
	public static void initGame()
	{
		currentGame = new Game();
		currentMap = MapGenerator.generateRandomMap();
		currentMap.startMap();
		currentGame.camera.setPosition(128.5f, 160, 128.5f);
		currentGame.camera.setRotation(90, 0, 0);
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

//		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, MathHelper.asFloatBuffer(new float[] { 0.1f, 0.1f, 0.1f, 1f }));
		glLight(GL_LIGHT0, GL_DIFFUSE, MathHelper.asFloatBuffer(new float[] { 1.5f, 1.5f, 1.5f, 1 }));
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glMaterialf(GL_FRONT, GL_SHININESS, 100f);

		glMaterial(GL_FRONT, GL_DIFFUSE, MathHelper.asFloatBuffer(new float[]{1, 0, 0, 1}));
		glMaterial(GL_FRONT, GL_SPECULAR, MathHelper.asFloatBuffer(new float[]{1, 1, 1, 1}));
		glMaterial(GL_FRONT, GL_AMBIENT, MathHelper.asFloatBuffer(new float[]{0.1f, 0.1f, 0.1f, 1}));
	}
	
	public void moveCamera()
	{
		double x = Math.sin(Math.toRadians(camera.rotation.y)) * cameraSpeed;
		double y = -Math.sin(Math.toRadians(camera.rotation.x)) * cameraSpeed;
		double z = -Math.cos(Math.toRadians(camera.rotation.y)) * cameraSpeed;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			camera.position.x += x * Math.cos(Math.toRadians(camera.rotation.x));
			camera.position.y += y;
			camera.position.z += z * Math.cos(Math.toRadians(camera.rotation.x));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			camera.position.x -= x * Math.cos(Math.toRadians(camera.rotation.x));
			camera.position.y -= y;
			camera.position.z -= z * Math.cos(Math.toRadians(camera.rotation.x));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			camera.position.x += Math.sin(Math.toRadians(camera.rotation.y - 90)) * cameraSpeed;
			camera.position.z -= Math.cos(Math.toRadians(camera.rotation.y - 90)) * cameraSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			camera.position.x += Math.sin(Math.toRadians(camera.rotation.y + 90)) * cameraSpeed;
			camera.position.z -= Math.cos(Math.toRadians(camera.rotation.y + 90)) * cameraSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			camera.move(0, 0.5f, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			camera.move(0, -0.5f, 0);
		}
	}
	
	public void rotateCamera()
	{
		float x = ((Mouse.getY() - (Display.getHeight() / 2)) / (float) Display.getHeight()) * cameraRotationSpeed;
		float y = ((Mouse.getX() - (Display.getWidth() / 2)) / (float) Display.getWidth()) * cameraRotationSpeed;
		
		camera.rotate(-x, y, 0);
		
		Mouse.setCursorPosition((Display.getWidth() / 2), (Display.getHeight() / 2));
	}
}
