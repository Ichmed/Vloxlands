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
		
		Label l = new Label(0, 100, Display.getWidth(), 60, "Vloxlands");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		TextButton b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 - 80, Tr._("title.newGame"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.setScene(new SceneNewGame());
			}
		});
		content.add(b);
		
		b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 + 20, Tr._("title.settings"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.setScene(new SceneSettings());
			}
		});
		content.add(b);
		
		b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 + 120, Tr._("title.quitGame"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Vloxlands.exit();
			}
		});
		content.add(b);
		
		content.add(new Container(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 50, Display.getHeight() / 2 - 120, TextButton.WIDTH + 100, 100 + 60 + TextButton.HEIGHT * 3, false));
	}
}
