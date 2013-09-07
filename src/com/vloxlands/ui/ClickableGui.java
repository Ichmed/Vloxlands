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
		return isUnderCursor(0, 0);
	}
	
	public boolean isUnderCursor(int tX, int tY)
	{
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		
		return this.x + tX < x && this.x + width + tX >= x && this.y + tY < y && this.y + height + tY >= y;
	}
	
	public void resetElement()
	{
	}
}
