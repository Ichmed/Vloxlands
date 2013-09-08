package com.vloxlands.scene;

import java.awt.Font;

import org.lwjgl.opengl.Display;

import com.vloxlands.Vloxlands;
import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;

public class SceneMainMenu extends Scene
{
	@Override
	public void init()
	{
		Game.currentGame.initMultiplayer();
		
		setBackground();
		
		// setUserZone();
		
		Label l = new Label(0, 100, Display.getWidth(), 60, "Vloxlands");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		Container c = new Container(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 40, Display.getHeight() / 2 - (110 + TextButton.HEIGHT * 4) / 2, TextButton.WIDTH + 80, 110 + TextButton.HEIGHT * 4, true);
		
		TextButton b = new TextButton(TextButton.WIDTH / 2 + 40, 40, Tr._("play"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.addScene(new SceneNewGame());
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 110, Tr._("join"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				// TODO: trigger join button mainmenu
			}
		});
		b.setEnabled(false);
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 180, Tr._("settings"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.addScene(new SceneSettings());
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 250, Tr._("quitGame"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Vloxlands.exit();
			}
		});
		c.add(b);
		
		content.add(c);
	}
}
