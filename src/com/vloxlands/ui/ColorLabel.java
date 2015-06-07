package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class ColorLabel extends Container {
	Color color;
	public boolean grayOnDisable = true;
	
	public ColorLabel(int x, int y, int size, Color color) {
		super(x, y, size, size, false, false);
		this.color = color;
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag) {
		super.handleMouse(posX, posY, flag);
		
		if (clickEvent != null && flag == 2) clickEvent.trigger();
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	@Override
	public void render() {
		if (!enabled && grayOnDisable) Color.gray.bind();
		super.render();
		glEnable(GL_BLEND);
		
		int fac = 8;
		TextureImpl.bindNone();
		color.bind();
		
		RenderAssistant.drawRect(x + fac, y + fac, width - fac * 2, height - fac * 2);
		glColor4f(1, 1, 1, 0.5f);
		RenderAssistant.bindTexture("/graphics/textures/ui/colorMod.png");
		RenderAssistant.renderRect(x + fac, y + fac, width - fac * 2, height - fac * 2);
		if (!enabled && grayOnDisable) {
			glColor4f(0.1f, 0.1f, 0.1f, 0.5f);
			RenderAssistant.drawRect(x + fac, y + fac, width - fac * 2, height - fac * 2);
		}
		glColor3f(1, 1, 1);
		glDisable(GL_BLEND);
	}
}
