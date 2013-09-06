package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class Checkbox extends ClickableGui
{
	public static final int WIDTH = 44;
	public static final int HEIGHT = 44;
	boolean selected;
	int texX, texY;
	
	public Checkbox(int x, int y)
	{
		this(x, y, false);
	}
	
	public Checkbox(int x, int y, boolean selected)
	{
		this.x = x;
		this.y = y;
		width = WIDTH;
		height = HEIGHT;
		this.selected = selected;
		texture = "/graphics/textures/ui/gui.png";
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if (!enabled) return;
		if (flag == 2) selected = !selected;
		else texX = 183;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	@Override
	public void render()
	{
		glEnable(GL_BLEND);
		RenderAssistant.bindTexture(texture);
		
		RenderAssistant.renderRect(x, y, width, height, texX / 1024.0f, texY / 1024.0f, WIDTH / 1024.0f, HEIGHT / 1024.0f);
		glDisable(GL_BLEND);
	}
	
	@Override
	public void onTick()
	{
		if (!enabled) texX = 228;
		else texX = 138;
		
		if (selected) texY = 47;
		else texY = 3;
	}
	
}
