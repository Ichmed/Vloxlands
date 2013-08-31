package com.vloxlands.scene;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.gen.IslandGenerator;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.ProgressBar;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.GUIAssistant;

public class Scene_Mainmenu implements Scene
{
	TextButton start;
	TextButton quit;
	ProgressBar progress;
	
	@Override
	public void init()
	{
		Label bg = new Label(0, 0, Display.getWidth(), Display.getHeight(), "");
		bg.setTexture("graphics/textures/ui/paper.png");
		GUIAssistant.addComponent(bg);
		
		start = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 - 80, "Spiel starten");
		GUIAssistant.addComponent(start);
		
		progress = new ProgressBar(Display.getWidth() / 2, 80, 280, 0, true);
		progress.setVisible(false);
		GUIAssistant.addComponent(progress);
		
		quit = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 + 20, "Spiel beenden");
		GUIAssistant.addComponent(quit);
	}
	
	@Override
	public void update()
	{
		if (Game.currentMap.islands.size() > 0) Game.currentGame.setScene(new Scene_Game());
		
		if (Game.currentMap.islandGenerator != null) progress.setValue(Game.currentMap.islandGenerator.progress);
		else progress.setVisible(false);
		
		if (start.getState() == 2)
		{
			Game.currentMap.islandGenerator = new IslandGenerator();
			progress.setValue(0);
			progress.setVisible(true);
			start.setState(0);
		}
		
		if (quit.getState() == 2) System.exit(0);
	}
}
