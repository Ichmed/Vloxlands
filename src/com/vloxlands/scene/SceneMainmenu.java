package com.vloxlands.scene;

import java.awt.Font;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.ui.IClickEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.RenderAssistant;

public class SceneMainmenu extends Scene
{
	@Override
	public void init()
	{
		setBackground();
		
		Label l = new Label(0, 100, Display.getWidth(), 60, "Vloxlands");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		TextButton b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 - 80, "Neues Spiel");
		b.setClickEvent(new IClickEvent()
		{
			@Override
			public void onClick()
			{
				Game.currentGame.setScene(new SceneNewGame());
			}
		});
		content.add(b);
		
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
		
		RenderAssistant.renderOutline(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 50, Display.getHeight() / 2 - 120, TextButton.WIDTH + 100, 100 + 20 + TextButton.HEIGHT * 2, true);
	}
}