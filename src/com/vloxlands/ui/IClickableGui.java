package com.vloxlands.ui;

import java.awt.Dimension;

public interface IClickableGui extends IGuiElement
{
	void onTick();
	
	void handleMouse(int posX, int posY, int flag);
	
	Dimension getPos();

	Dimension getSize();
}
