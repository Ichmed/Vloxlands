package com.vloxlands.scene;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.gen.IslandGenerator;
import com.vloxlands.ui.IClickEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.ProgressBar;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.GUIAssistant;

public class Scene_Mainmenu extends Scene
{
	ProgressBar progress;
	
	@Override
	public void init()
	{
		Label bg = new Label(0, 0, Display.getWidth(), Display.getHeight(), "");
		bg.setTexture("graphics/textures/ui/paper.png");
		content.add(bg);
		
		TextButton b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 - 80, "Spiel starten");
		b.setClickEvent(new IClickEvent()
		{			
			@Override
			public void onClick()
			{
				Game.currentMap.islandGenerator = new IslandGenerator();
				progress.setValue(0);
				progress.setVisible(true);
			}
		});
		content.add(b);
		
		progress = new ProgressBar(Display.getWidth() / 2, 80, 280, 0, true);
		progress.setVisible(false);
		content.add(progress);
		
		b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 + 20, "Spiel beenden");
		b.setClickEvent(new IClickEvent()
		{			
			@Override
			public void onClick()
			{
				System.exit(0);
			}
		});
		content.add(b);
	}
	
	@Override
	public void update()
	{
		super.update();
		if (Game.currentMap.islands.size() > 0) Game.currentGame.setScene(new Scene_Game());
		
		if (Game.currentMap.islandGenerator != null) progress.setValue(Game.currentMap.islandGenerator.progress);
		else progress.setVisible(false);
	}
}
