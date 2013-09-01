package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Label extends IGuiElement
{
	String title;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	public Label(int x, int y, int width, int height, String title)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	@Override
	public void render()
	{
		RenderAssistant.bindTexture(texture);
		RenderAssistant.renderRect(x, y, width, height);
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		glColor3f(1, 1, 1);
		RenderAssistant.renderText(x + ((width > -1) ? mx : 0), y + ((height > -1) ? height / 4f : 0), title, font);
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
}
