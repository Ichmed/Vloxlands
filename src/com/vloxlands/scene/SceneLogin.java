package com.vloxlands.scene;

import java.awt.Desktop;
import java.net.URL;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Action;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.Dialog;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.InputField;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.NetworkAssistant;

/**
 * @author Dakror
 */
public class SceneLogin extends Scene
{
	InputField username, password;
	TextButton login;
	
	@Override
	public void init()
	{
		setBackground();
		setTitle(Tr._("title.login"));
		content.add(new Label(Display.getWidth() / 2 - 200, Display.getHeight() / 2 - 200, 400, 60, Tr._("desc.login")));
		content.add(new Container(Display.getWidth() / 2 - TextButton.WIDTH - 10, Display.getHeight() / 2 - 100, TextButton.WIDTH * 2 + 20, 200, false, false));
		
		username = new InputField(Display.getWidth() / 2 - TextButton.WIDTH, Display.getHeight() / 2 - 90, TextButton.WIDTH * 2 - 20, "", Tr._("title.username"));
		content.add(username);
		
		password = new InputField(Display.getWidth() / 2 - TextButton.WIDTH, Display.getHeight() / 2 - 35, TextButton.WIDTH * 2 - 20, "", Tr._("title.password"));
		password.setHidden(true);
		content.add(password);
		
		TextButton register = new TextButton(Display.getWidth() / 2 - TextButton.WIDTH / 2, Display.getHeight() / 2 + 25, Tr._("title.register"));
		register.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				try
				{
					Desktop.getDesktop().browse(new URL("http://dakror.de/#register?lang=" + Tr.activeLanguage).toURI());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		content.add(register);
		
		login = new TextButton(Display.getWidth() / 2 + TextButton.WIDTH / 2, Display.getHeight() / 2 + 25, Tr._("title.login"));
		login.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				if (NetworkAssistant.login(username.getText(), password.getText())) Game.currentGame.setScene(new SceneMainMenu());
				else Game.currentGame.addScene(new Dialog(Tr._("error.login"), Tr._("desc.login.error"), true, new Action("OK", Dialog.CLOSE_EVENT)));
			}
		});
		login.setEnabled(false);
		content.add(login);
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		login.setEnabled(username.getText().length() > 0 && password.getText().length() > 0);
	}
}
