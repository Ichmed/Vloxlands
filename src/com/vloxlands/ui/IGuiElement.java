package com.vloxlands.ui;

import com.vloxlands.render.IRendering;

public abstract class IGuiElement extends IRendering
{
	// not abstract so that implementing won't be forced
	public void handleKeyboard(int key, boolean down)
	{}
}
