package com.vloxlands.ui;



public abstract class IClickableGui extends IGuiElement
{
	public abstract void onTick();
	
	public abstract void handleMouse(int posX, int posY, int flag);
}
