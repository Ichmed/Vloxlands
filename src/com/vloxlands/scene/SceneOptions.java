package com.vloxlands.scene;

import java.awt.Font;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.GuiRotation;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.Slider;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.RenderAssistant;

//TODO: Dakror claims to rework this class. Date: 04.09.2013

public class SceneOptions extends Scene
{
	@Override
	public void init()
	{
		setBackground();
		
		Label l = new Label(0, 0, Display.getWidth(), 60, "Options");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		final Slider fov = new Slider(Display.getWidth() / 4, 160, Display.getWidth() / 4 - 20, 30, 150, GuiRotation.HORIZONTAL);
		fov.setIntegerMode(true);
		// fov.setStepSize(5);
		fov.setEvent(new IGuiEvent()
		{
			@Override
			public void activate()
			{
				Game.fov = (int) fov.getValue();
			}
		});
		
		content.add(fov);
		
		TextButton b = new TextButton(Display.getWidth() / 2 - TextButton.WIDTH / 2, Display.getHeight() - TextButton.HEIGHT, Tr._("title.back"));
		b.setClickEvent(new IGuiEvent()
		{
			
			@Override
			public void activate()
			{
				Game.currentGame.setScene(new SceneMainMenu());
			}
		});
		content.add(b);
		
		TextButton s = new TextButton(Display.getWidth() / 2 + TextButton.WIDTH / 2, Display.getHeight() - TextButton.HEIGHT, Tr._("title.save"));
		s.setClickEvent(new IGuiEvent()
		{
			
			@Override
			public void activate()
			{
				// Game.currentGame.setScene(new SceneMainMenu());
			}
		});
		content.add(s);
		
		content.add(new Container(0, 115, Display.getWidth() / 2, Display.getHeight() - 220));
	}
	
	@Override
	public void update()
	{
		super.update();
		RenderAssistant.renderOutline(-15, 85, Display.getWidth() + 30, 1, false);
		// RenderAssistant.renderOutline(0, 115, Display.getWidth() / 2, Display.getHeight() - 220, true);
	}
}
