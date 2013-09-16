package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

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
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		int fac = 8;
		TextureImpl.bindNone();
//		glColor3f(1, 0, 0);
//		RenderAssistant.drawRect(x + fac, y + fac, width - fac * 2, height - fac * 2);
//		glColor4f(1, 1, 1, 0.5f);
		RenderAssistant.bindTexture("/graphics/textures/ui/colorMod.png");
		RenderAssistant.drawRect(x + fac, y + fac, width - fac * 2, height - fac * 2);
		glColor3f(1, 1, 1);
		glDisable(GL_BLEND);
	}
}
