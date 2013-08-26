package com.vloxlands.scene;

import org.lwjgl.opengl.Display;

import com.vloxlands.ui.Button;
import com.vloxlands.util.GUIAssistant;

public class Scene_Mainmenu implements Scene
{
	Button start;
	Button quit;

	@Override
	public void init()
	{
		start = new Button(Display.getWidth() / 2 - 110, Display.getHeight() / 2 - 80, 220, 60, "Spiel starten");
		GUIAssistant.addComponent(start);

		quit = new Button(Display.getWidth() / 2 - 110, Display.getHeight() / 2 + 20, 220, 60, "Spiel beenden");
		GUIAssistant.addComponent(quit);

	}

	@Override
	public void update()
	{
		if (quit.getState() == 2) System.exit(0);
	}

}
