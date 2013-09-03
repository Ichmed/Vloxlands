package com.vloxlands.render;

import org.lwjgl.util.vector.Vector2f;

public abstract class IRendering
{
	protected int x, y, width, height, zIndex;
	protected boolean visible;
	protected boolean enabled;
	protected String texture;
	
	public IRendering()
	{
		visible = true;
		enabled = true;
		texture = null;
		zIndex = 0;
	}
	
	public abstract void render();
	
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
	
	public Vector2f getPos()
	{
		return new Vector2f(x, y);
	}
	
	public Vector2f getSize()
	{
		return new Vector2f(width, height);
	}
	
	public int getZIndex()
	{
		return zIndex;
	}
	
	public void setZIndex(int zIndex)
	{
		this.zIndex = zIndex;
	}
}
