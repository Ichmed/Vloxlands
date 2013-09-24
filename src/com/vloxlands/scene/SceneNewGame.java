package com.vloxlands.scene;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.MultilineLabel;
import com.vloxlands.ui.TextButton;

/**
 * @author Dakror
 */
public class SceneNewGame extends Scene
{
	@Override
	public void init()
	{
		if (Game.client.isConnected()) Game.currentGame.addScene(new SceneLobby());
		
		setBackground();
		
		setTitle(Tr._("newGame"));
		
		MultilineLabel desc = new MultilineLabel(100, 50, Display.getWidth() - 200, 120, Tr._("spdesc"));
		content.add(desc);
		
		Container c = new Container(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 40, Display.getHeight() / 2 - (110 + TextButton.HEIGHT * 4) / 2, TextButton.WIDTH + 80, 110 + TextButton.HEIGHT * 4, true);
		
		TextButton b = new TextButton(TextButton.WIDTH / 2 + 40, 70, Tr._("lan"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.setIP(true);
				Game.currentGame.addScene(new SceneLobby());
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, b.getY() + 70, Tr._("global"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.setIP(false);
				Game.currentGame.addScene(new SceneLobby());
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 250, Tr._("back"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.removeActiveScene();
			}
		});
		c.add(b);
		
		content.add(c);
	}
}
