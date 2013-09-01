package com.vloxlands.render;

public abstract class IRendering
{
	protected boolean visible;
	
	public IRendering()
	{
		visible = true;
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
}
