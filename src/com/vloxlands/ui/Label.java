package com.vloxlands.ui;

import java.awt.Font;

import org.newdawn.slick.Color;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Label implements IGuiElement
{
	String title;
	String texture;
	int x, y, width, height;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	private boolean isVisible = true;
	
	public Label(int x, int y, int width, int height, String title)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = title;
		texture = null;
	}
	
	@Override
	public void render()
	{
		RenderAssistant.bindTexture(texture);
		RenderAssistant.renderRect(x, y, width, height);
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		RenderAssistant.renderText(x + ((width > -1) ? mx : 0), y + ((height > -1) ? height / 4f : 0), title, Color.white, font);
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
	public boolean shouldRender()
	{
		return isVisible;
	}
	
	public void setVisible(boolean b)
	{
		isVisible = b;
	}
	
	public void setTexture(String t)
	{
		texture = t;
	}
}
