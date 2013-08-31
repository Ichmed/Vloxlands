package com.vloxlands.ui;

import org.lwjgl.util.vector.Vector2f;

public interface IClickableGui extends IGuiElement
{
	void onTick();
	
	void handleMouse(int posX, int posY, int flag);
	
	Vector2f getPos();
	
	Vector2f getSize();
}
