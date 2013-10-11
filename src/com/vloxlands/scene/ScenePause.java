package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.IGuiElement;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.MapAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class ScenePause extends Scene
{
	
	public ScenePause()
	{}
	
	@Override
	public void init()
	{
		Label l = new Label(0, 100, Display.getWidth(), 60, Tr._("pause"));
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		Container c = new Container(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 40, Display.getHeight() / 2 - (110 + TextButton.HEIGHT * 4) / 2, TextButton.WIDTH + 80, 110 + TextButton.HEIGHT * 4, true);
		
		TextButton b = new TextButton(TextButton.WIDTH / 2 + 40, 40, Tr._("back"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.removeActiveScene();
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 110, Tr._("save"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				MapAssistant.saveMap(Game.currentMap);
			}
		});
		b.setEnabled(Game.server.getConnectedClientCount() == 1);
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
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 250, Tr._("disconnect"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.client.disconnect();
			}
		});
		c.add(b);
		
		content.add(c);
	}
	
	@Override
	public void handleKeyboard(int key, char chr, boolean down)
	{
		if (key == Keyboard.KEY_ESCAPE && down) Game.currentGame.removeActiveScene();
	}
	
	@Override
	public void render()
	{
		glEnable(GL_BLEND);
		glColor4f(IGuiElement.gray.x, IGuiElement.gray.y, IGuiElement.gray.z, IGuiElement.gray.w);
		
		RenderAssistant.renderRect(0, 0, Display.getWidth(), Display.getHeight());
		super.render();
		
		glDisable(GL_BLEND);
	}
}
