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
		setBackground();
		
		setUserZone();
		
		Label l = new Label(0, 100, Display.getWidth(), 60, "Vloxlands");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		Container c = new Container(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 40, Display.getHeight() / 2 - 120, TextButton.WIDTH + 80, 120 + TextButton.HEIGHT * 3, true);
		
		TextButton b = new TextButton(TextButton.WIDTH / 2 + 40, 50, Tr._("title.play"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.addScene(new SceneNewGame());
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 120, Tr._("title.settings"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.addScene(new SceneSettings());
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 190, Tr._("title.quitGame"));
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
