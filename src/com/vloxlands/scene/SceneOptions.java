package com.vloxlands.scene;

import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.Slider;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.RenderAssistant;

public class SceneOptions extends Scene
{
	@Override
	public void init()
	{
		setBackground();

		Label l = new Label(-Display.getWidth() / 2 + 140, 10, Display.getWidth(), 60, "Options");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);

		final Slider FOVSlider;
		FOVSlider = new Slider(30, 150);
		FOVSlider.setPos(new Vector2f(50, 150));
		FOVSlider.setIntegerMode(true);
		FOVSlider.setTitle("FOV");
		FOVSlider.setEvent(new IGuiEvent()
		{
			
			@Override
			public void activate()
			{
				Game.fov = (int) FOVSlider.getValue();
			}
		});

		content.add(FOVSlider);
		
		TextButton b = new TextButton(Display.getWidth() - 190, Display.getHeight() - 130, Tr._("title.back"));
		b.setClickEvent(new IGuiEvent()
		{
			
			@Override
			public void activate()
			{
				Game.currentGame.setScene(new SceneMainMenu());
			}
		});
		content.add(b);
	}
	
	@Override
	public void update()
	{
		super.update();
		RenderAssistant.renderOutline(20, 100, Display.getWidth() - 50, Display.getHeight() - 150, true);
	}
}
