package com.vloxlands.scene;

import org.lwjgl.opengl.Display;

import com.vloxlands.ui.Spinner;

public class SceneGenerateMap extends Scene
{
	
	@Override
	public void init()
	{
		setBackground();
		
		Spinner s = new Spinner(Display.getWidth() / 2, Display.getHeight() / 2, 300, 0, 100, 0, 1);
		content.add(s);
	}
}
