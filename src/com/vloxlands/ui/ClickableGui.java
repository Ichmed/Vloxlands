package com.vloxlands.ui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;



public abstract class ClickableGui extends IGuiElement
{
	protected IGuiEvent clickEvent;
	
	public abstract void handleMouse(int posX, int posY, int flag);
	
	public IGuiEvent getClickEvent()
	{
		return clickEvent;
	}
	
	public void setClickEvent(IGuiEvent clickEvent)
	{
		this.clickEvent = clickEvent;
	}
	
	public boolean isUnderCursor()
	{
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		
		return this.x < x && this.x + width >= x && this.y < y && this.y + height >= y;
	}
}
