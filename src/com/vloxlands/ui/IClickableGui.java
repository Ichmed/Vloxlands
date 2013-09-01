package com.vloxlands.ui;

import org.lwjgl.util.vector.Vector2f;

public abstract class IClickableGui extends IGuiElement
{
	public abstract void onTick();
	
	public abstract void handleMouse(int posX, int posY, int flag);
	
	public abstract void setActive(boolean b);
	
	public abstract Vector2f getPos();
	
	public abstract Vector2f getSize();
}
