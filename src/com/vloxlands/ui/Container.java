package com.vloxlands.ui;

import com.vloxlands.util.RenderAssistant;

public class Container extends IGuiElement
{
	
	public Container(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		zIndex = 0;
	}
	
	@Override
	public void render()
	{
		RenderAssistant.renderContainer(x, y, width, height, true);
	}
}
