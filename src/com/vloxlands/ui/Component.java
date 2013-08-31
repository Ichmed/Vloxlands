package com.vloxlands.ui;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;

public abstract class Component
{
	int x;
	int y;
	int width;
	int height;
	
	String texture;
	
	boolean visible;
	boolean enabled;
	
	/**
	 * 0 = unselected 1 = hovered 2 = active
	 */
	int state;
	
	public Font font;
	
	public Component(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
		
		visible = true;
		enabled = true;
		state = 0;
		texture = "graphics/textures/ui/gui.png";
	}
	
	public void draw()
	{}
	
	public void mouseEvent(int posX, int posY, byte b, boolean c)
	{
		if (!c) state = 0;
		else if ((b & 1) == 1) state = 2;
		else state = 1;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public void setState(int s)
	{
		state = s;
	}
	
	public int getState()
	{
		return state;
	}
	
	public String getTexture()
	{
		return texture;
	}
	
	public void setTexture(String texture)
	{
		this.texture = texture;
	}
}
