package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class TextButton extends ClickableGui
{
	public static final int WIDTH = 288;
	public static final int HEIGHT = 59;
	
	String title;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	int texX = 12;
	int texY = 124;
	
	public TextButton(int x, int y, String title)
	{
		this.x = x - 144;
		this.y = y;
		width = WIDTH;
		height = HEIGHT;
		this.title = title;
		texture = "/graphics/textures/ui/gui.png";
	}
	
	@Override
	public void render()
	{
		RenderAssistant.disable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glColor3f(1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderAssistant.bindTexture(texture);
		
		RenderAssistant.renderRect(x, y, width, height, texX / 1024.0f, texY / 1024.0f, WIDTH / 1024.0f, HEIGHT / 1024.0f);
		glDisable(GL_BLEND);
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		if (enabled) glColor3f(1, 1, 1);
		else glColor3f(0.5f, 0.5f, 0.5f);
		RenderAssistant.renderText(x + mx, y + height / 4f, title, font);
		glColor3f(1, 1, 1);
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	@Override
	public void onTick()
	{
		if (!enabled) texY = 358;
		else texY = 124;
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if (!enabled) return;
		if ((flag & 1) != 0) texY = 202;
		else if ((flag & 2) != 0) clickEvent.trigger();
		else texY = 280;
	}
}
