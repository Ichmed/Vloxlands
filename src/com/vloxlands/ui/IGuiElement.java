package com.vloxlands.ui;

import com.vloxlands.render.IRendering;

public abstract class IGuiElement implements IRendering
{
	protected int x, y, width, height, zIndex;
	protected boolean visible;
	protected boolean wantsRender;
	protected boolean enabled;
	protected String texture;
	
	public IGuiElement()
	{
		visible = true;
		enabled = true;
		wantsRender = true;
		texture = null;
		zIndex = 1;
	}
	
	public abstract void onTick();
	
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
	
	public String getTexture()
	{
		return texture;
	}
	
	public void setTexture(String texture)
	{
		this.texture = texture;
	}
	
	public int getZIndex()
	{
		return zIndex;
	}
	
	@Override
	public String toString()
	{
		return getClass() + ", z=" + zIndex + ", " + enabled;
	}
	
	public void setZIndex(int zIndex)
	{
		this.zIndex = zIndex;
	}
	
	// not abstract so that implementing won't be forced
	public void handleKeyboard(int key, char chr, boolean down)
	{}
	
	public boolean wantsRender()
	{
		return wantsRender;
	}
	
	public void setWantsRender(boolean wantsRender)
	{
		this.wantsRender = wantsRender;
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
		x = x + this.width / 2 - width / 2;
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
}
