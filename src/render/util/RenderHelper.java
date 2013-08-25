package render.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

//import render.Model;
//import util.math.MathHelper;

public class RenderHelper
{
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();	
//	public static HashMap<String, Model> models = new HashMap<>();	

	public static boolean bindTexture(String path)
	{
		Texture t = textures.get(path);
		if (t != null)
		{
			t.bind();
			return true;
		}
		else
		{
			t = loadTexture(path);
			t.bind();
			textures.put(path, t);
			return true;
		}
	}
	
//	/**
//	 * Returns a model. If it doesn't exist the method will try to load it
//	 * @param path The path to the model file
//	 * @return The model / null if the model could not be loaded
//	 */
//	public static Model getModel(String path)
//	{
//		Model m = models.get(path);
//		if(m == null)
//		{
//			models.put(path, ModelLoader.loadModel(path));
//			m = models.get(path);
//		}
//		return m;
//	}
	
//	/**
//	 * Renders a model at the GL-coordinate-origin. If it doesn't exist the method will try to load the model
//	 * @param path The path to the model file
//	 */
//	public static void renderModel(String path)
//	{
//		getModel(path).renderModel();
//	}

	private static Texture loadTexture(String path)
	{
		try
		{
			return TextureLoader.getTexture(".png", new FileInputStream(new File(path)));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void renderRect(float posX, float posY, float sizeX, float sizeY)
	{
		renderRect(posX, posY, sizeX, sizeY, 1, 1);
	}

	public static void renderRect(float posX, float posY, float sizeX, float sizeY, float texSizeX, float texSizeY)
	{
		renderRect(posX, posY, sizeX, sizeY, 0, 0, texSizeX, texSizeY);
	}

	public static void renderRect(float posX, float posY, float sizeX, float sizeY, float texPosX, float texPosY, float texSizeX, float texSizeY)
	{
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(texPosX, texPosY + texSizeY);
		GL11.glVertex2f(posX, posY);

		GL11.glTexCoord2f(texPosX + texSizeX, texPosY + texSizeY);
		GL11.glVertex2f(posX + sizeX, posY);

		GL11.glTexCoord2f(texPosX + texSizeX, texPosY);
		GL11.glVertex2f(posX + sizeX, posY + sizeY);

		GL11.glTexCoord2f(texPosX, texPosY);
		GL11.glVertex2f(posX, posY + sizeY);

		GL11.glEnd();
	}

	public static void initGLSettings()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(100, (float) Display.getWidth() / Display.getHeight(), 0.01f, 1000);
		glMatrixMode(GL_MODELVIEW);

		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
	}
	
	public static void renderBox(float x, float y, float z)
	{
		glDisable(GL_CULL_FACE);
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, 0);
			glVertex3f(x, 0, 0);
			glVertex3f(x, 0, z);
			glVertex3f(0, 0, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, 0);
			glVertex3f(x, 0, 0);
			glVertex3f(x, y, 0);
			glVertex3f(0, y, 0);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, 0);
			glVertex3f(0, y, 0);
			glVertex3f(0, y, z);
			glVertex3f(0, 0, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(x, 0, 0);
			glVertex3f(x, y, 0);
			glVertex3f(x, y, z);
			glVertex3f(x, 0, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, y, 0);
			glVertex3f(x, y, 0);
			glVertex3f(x, y, z);
			glVertex3f(0, y, z);
		}
		glEnd();
		
		glBegin(GL_QUADS);
		{
			glVertex3f(0, 0, z);
			glVertex3f(x, 0, z);
			glVertex3f(x, y, z);
			glVertex3f(0, y, z);
		}
		glEnd();
	}

	public static void renderText(float x, float y, String text, Font font){
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		ttf.drawString(x, y, text);
		glDisable(GL_BLEND);
	}
}
