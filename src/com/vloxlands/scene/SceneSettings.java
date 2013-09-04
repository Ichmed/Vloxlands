package com.vloxlands.scene;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.CFG;
import com.vloxlands.settings.Settings;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.FlagButton;
import com.vloxlands.ui.GuiRotation;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.Slider;
import com.vloxlands.ui.TextButton;

//TODO: Dakror claims to rework this class. Date: 04.09.2013

public class SceneSettings extends Scene
{
	@Override
	public void init()
	{
		setBackground();
		setTitle(Tr._("title.settings"));
		
		
		content.add(new Container(0, 115, Display.getWidth() / 2, Display.getHeight() - 220));
		
		content.add(new Label(20, 125, 0, 25, Tr._("settings.language") + ":", false));
		content.add(new FlagButton(Display.getWidth() / 4, 130, "de"));
		content.add(new FlagButton(Display.getWidth() / 4 + FlagButton.SIZE, 130, "us"));
		
		content.add(new Label(20, 185, 0, 25, Tr._("settings.fov") + ":", false));
		
		final Slider fov = new Slider(Display.getWidth() / 4, 200, Display.getWidth() / 4 - 20, 30, 150, CFG.FOV, GuiRotation.HORIZONTAL);
		fov.setIntegerMode(true);
		
		content.add(fov);
		
		content.add(new Label(20, 245, 0, 25, Tr._("settings.fps") + ":", false));
		
		final Slider fps = new Slider(Display.getWidth() / 4, 260, Display.getWidth() / 4 - 20, 30, 121, CFG.FPS, GuiRotation.HORIZONTAL);
		fps.addCustomTitle(121, Tr._("lang.unlimited"));
		fps.setIntegerMode(true);
		
		content.add(fps);
		
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
				CFG.FOV = (int) fov.getValue();
				CFG.FPS = (int) fps.getValue();
				
				Settings.saveSettings();
			}
		});
		content.add(s);
	}
	
	@Override
	public void update()
	{
		super.update();
	}
}
