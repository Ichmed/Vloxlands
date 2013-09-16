package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;

import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class ColorLabel extends Container
{
	Color color;
	
	public ColorLabel(int x, int y, int size, Color color)
	{
		super(x, y, size, size, false, false);
		this.color = color;
	}
	
	@Override
	public void render()
	{
		super.render();
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor3f(1, 0, 0);
		int fac = 8;
		RenderAssistant.drawRect(x + fac, y + fac, width - fac * 2, height - fac * 2);
		glColor3f(1, 1, 1);
	}
}
