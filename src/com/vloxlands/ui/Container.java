package com.vloxlands.ui;

import com.vloxlands.util.RenderAssistant;

public class Container extends IGuiElement
{
	public boolean filled, doubled;
	
	public Container(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		zIndex = 0;
		doubled = true;
		filled = true;
	}
	
	@Override
	public void render()
	{
		if (filled) RenderAssistant.renderContainer(x, y, width, height, doubled);
		else RenderAssistant.renderOutline(x, y, width, height, doubled);
	}
}
