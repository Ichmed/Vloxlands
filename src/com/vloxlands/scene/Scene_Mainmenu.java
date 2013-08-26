package com.vloxlands.scene;

import java.awt.Font;

import org.lwjgl.opengl.*;

import com.vloxlands.ui.*;
import com.vloxlands.util.*;

import de.dakror.universion.UniVersion;

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
		Label version = new Label(0, Display.getHeight() - 20, -1, -1, UniVersion.prettyVersion());
		version.font = version.font.deriveFont(Font.PLAIN, 20f);
		GUIAssistant.addComponent(version);
	}

	@Override
	public void update()
	{
		if (quit.getState() == 2) System.exit(0);
	}

}
