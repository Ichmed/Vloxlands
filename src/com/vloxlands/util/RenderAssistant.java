package com.vloxlands.util;

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
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

//import render.Model;
//import util.math.MathHelper;

public class RenderAssistant
{
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

	// public static HashMap<String, Model> models = new HashMap<>();

	public static boolean bindTexture(String path)
	{
		Texture t = textures.get(path);
		if (t != null)
		{
			t.bind();
			return true;
		} else
		{
			t = loadTexture(path);
			t.bind();
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			textures.put(path, t);
			return true;
		}
	}

	// /**
	// * Returns a model. If it doesn't exist the method will try to load it
	// * @param path The path to the model file
	// * @return The model / null if the model could not be loaded
	// */
	// public static Model getModel(String path)
	// {
	// Model m = models.get(path);
	// if(m == null)
	// {
	// models.put(path, ModelLoader.loadModel(path));
	// m = models.get(path);
	// }
	// return m;
	// }

	// /**
	// * Renders a model at the GL-coordinate-origin. If it doesn't exist the
	// method will try to load the model
	// * @param path The path to the model file
	// */
	// public static void renderModel(String path)
	// {
	// getModel(path).renderModel();
	// }

	private static Texture loadTexture(String path)
	{
		try
		{
			return TextureLoader.getTexture(".png", new FileInputStream(new File(path)));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
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
		glPushMatrix();
		{
			glDisable(GL_CULL_FACE);
			
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glTexCoord2d(texPosX, texPosY + texSizeY);
				GL11.glVertex2f(posX, posY);

				GL11.glTexCoord2d(texPosX + texSizeX, texPosY + texSizeY);
				GL11.glVertex2f(posX + sizeX, posY);

				GL11.glTexCoord2d(texPosX + texSizeX, texPosY);
				GL11.glVertex2f(posX + sizeX, posY + sizeY);

				GL11.glTexCoord2d(texPosX, texPosY);
				GL11.glVertex2f(posX, posY + sizeY);
			}
			GL11.glEnd();
		}
		glPopMatrix();

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

	public static void renderVoxel(byte occlusion, int textureIndex)
	{
		int texX = textureIndex % 32;
		int texY = textureIndex / 32;

		double squareSize = 0.03125d;

		bindTexture("textures/voxelTextures.png");

		 glDisable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glBegin(GL_QUADS);
		{
			glTexCoord2d(texX * squareSize, texY * squareSize);
			glVertex3f(0, 0, 0);
			glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
			glVertex3f(1, 0, 0);
			glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
			glVertex3f(1, 1, 0);
			glTexCoord2d(texX * squareSize, (texY + 1) * squareSize);
			glVertex3f(0, 1, 0);
		}
		glEnd();

		 glBegin(GL_QUADS);
		 {
		 glTexCoord2d(texX * squareSize, texY * squareSize);
		 glVertex3f(0, 0, 0);
		 glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
		 glVertex3f(1, 0, 0);
		 glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
		 glVertex3f(1, 1, 0);
		 glTexCoord2d(texX * squareSize, (texY + 1) * squareSize);
		 glVertex3f(0, 1, 0);
		 }
		 glEnd();
		
		 glBegin(GL_QUADS);
		 {
		 glTexCoord2d(texX * squareSize, texY * squareSize);
		 glVertex3f(0, 0, 0);
		 glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
		 glVertex3f(0, 1, 0);
		 glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
		 glVertex3f(0, 1, 1);
		 glTexCoord2d(texX * squareSize, (texY + 1) * squareSize);
		 glVertex3f(0, 0, 1);
		 }
		 glEnd();
		
		 glBegin(GL_QUADS);
		 {
		 glTexCoord2d(texX * squareSize, texY * squareSize);
		 glVertex3f(1, 0, 0);
		 glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
		 glVertex3f(1, 1, 0);
		 glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
		 glVertex3f(1, 1, 1);
		 glTexCoord2d(texX * squareSize, (texY + 1) * squareSize);
		 glVertex3f(1, 0, 1);
		 }
		 glEnd();
		
		 glBegin(GL_QUADS);
		 {
		 glTexCoord2d(texX * squareSize, texY * squareSize);
		 glVertex3f(0, 1, 0);
		 glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
		 glVertex3f(1, 1, 0);
		 glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
		 glVertex3f(1, 1, 1);
		 glTexCoord2d(texX * squareSize, (texY + 1) * squareSize);
		 glVertex3f(0, 1, 1);
		 }
		 glEnd();
		
		 glBegin(GL_QUADS);
		 {
		 glTexCoord2d(texX * squareSize, texY * squareSize);
		 glVertex3f(0, 0, 1);
		 glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
		 glVertex3f(1, 0, 1);
		 glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
		 glVertex3f(1, 1, 1);
		 glTexCoord2d(texX * squareSize, (texY + 1) * squareSize);
		 glVertex3f(0, 1, 1);
		 }
		 glEnd();
	}

	public static void renderText(float x, float y, String text, Color color, Font f)
	{
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		FontAssistant.getFont(f).drawString(x, y, text, color);
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
	}

	public static void set2DRenderMode(boolean t)
	{
		if (t)
		{
			glMatrixMode(GL_PROJECTION);
			glPushMatrix();
			glLoadIdentity();
			glOrtho(0.0, Display.getWidth(), Display.getHeight(), 0.0, -1.0, 10.0);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

			glClear(GL_DEPTH_BUFFER_BIT);
			glDisable(GL_TEXTURE_2D);
			glShadeModel(GL_SMOOTH);
			glDisable(GL_DEPTH_TEST);
		} else
		{
			glMatrixMode(GL_PROJECTION);
			glPopMatrix();
			glMatrixMode(GL_MODELVIEW);
		}
	}

	public static void glColorHex(String hex, float alpha)
	{
		glColor4f(Integer.parseInt(hex.substring(0, 2), 16) / 255f, Integer.parseInt(hex.substring(2, 4), 16) / 255f, Integer.parseInt(hex.substring(4, 6), 16) / 255f, alpha);
	}
}
