package com.vloxlands.scene;

import java.awt.Font;

import org.lwjgl.opengl.Display;

import com.vloxlands.Vloxlands;
import com.vloxlands.game.Game;
import com.vloxlands.settings.CFG;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.NetworkAssistant;
import com.vloxlands.util.RenderAssistant;

public class SceneMainMenu extends Scene
{
	@Override
	public void init()
	{
		NetworkAssistant.pullUserLogo();
		
		setBackground();
		
		Label l = new Label(0, 100, Display.getWidth(), 60, "Vloxlands");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		TextButton b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 - 70, Tr._("title.play"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.addScene(new SceneNewGame());
			}
		});
		content.add(b);
		
		b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2, Tr._("title.settings"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.addScene(new SceneSettings());
			}
		});
		content.add(b);
		
		b = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 + 70, Tr._("title.quitGame"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Vloxlands.exit();
			}
		});
		content.add(b);
		
		content.add(new Container(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 40, Display.getHeight() / 2 - 120, TextButton.WIDTH + 80, 120 + TextButton.HEIGHT * 3, true));
		
		Label user = new Label(15, 15, 90, 90, "");
		user.setTexture("USER_LOGO");
		content.add(user);
		Label username = new Label(120, 10, 10, 30, CFG.USERNAME, false);
		int width = 140 + FontAssistant.getFont(username.font).getWidth(CFG.USERNAME);
		content.add(username);
		content.add(new Container(0, 0, (width > TextButton.WIDTH) ? width : TextButton.WIDTH, 120, true));
	}
	
	@Override
	public void render()
	{
		super.render();
		
		RenderAssistant.renderLine(115, 10, 100, false, false);
		
	}
}
