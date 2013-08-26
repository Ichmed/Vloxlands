package com.vloxlands.ui;

public abstract class Component
{
	int x;
	int y;
	int width;
	int height;

	String texture;

	boolean visible;

	public Component(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		visible = true;
		texture = null;
	}

	public void draw()
	{
	}

	public abstract void mouseEvent(int posX, int posY, byte b, boolean c);

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

	public String getTexture()
	{
		return texture;
	}

	public void setTexture(String texture)
	{
		this.texture = texture;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
}
