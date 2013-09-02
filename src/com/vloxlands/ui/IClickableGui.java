package com.vloxlands.ui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;



public abstract class IClickableGui extends IGuiElement
{
	protected IClickEvent clickEvent;
	
	public abstract void onTick();
	
	public abstract void handleMouse(int posX, int posY, int flag);
	
	public IClickEvent getClickEvent()
	{
		return clickEvent;
	}
	
	public void setClickEvent(IClickEvent clickEvent)
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
