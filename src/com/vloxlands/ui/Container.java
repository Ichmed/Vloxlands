package com.vloxlands.ui;

import com.vloxlands.util.RenderAssistant;

public class Container extends IGuiElement
{
	public boolean filled, doubled;
	
	public Container(int x, int y, int width, int height)
	{
		this(x, y, width, height, true, true);
	}
	
	public Container(int x, int y, int width, int height, boolean filled)
	{
		this(x, y, width, height, filled, true);
	}
	
	public Container(int x, int y, int width, int height, boolean filled, boolean doubled)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		zIndex = 0;
		this.doubled = doubled;
		this.filled = filled;
	}
	
	@Override
	public void render()
	{
		if (filled) RenderAssistant.renderContainer(x, y, width, height, doubled);
		else RenderAssistant.renderOutline(x, y, width, height, doubled);
	}
	
	@Override
	public void onTick()
	{}
}
