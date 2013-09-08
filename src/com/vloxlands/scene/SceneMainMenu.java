package com.vloxlands.scene;

import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lwjgl.opengl.Display;

import com.vloxlands.Vloxlands;
import com.vloxlands.game.Game;
import com.vloxlands.settings.CFG;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Action;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.Dialog;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.InputField;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;

public class SceneMainMenu extends Scene
{
	@Override
	public void init()
	{
		Game.currentGame.initMultiplayer();
		
		setBackground();
		
		// setUserZone();
		
		Label l = new Label(0, 100, Display.getWidth(), 60, "Vloxlands");
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		Container c = new Container(Display.getWidth() / 2 - TextButton.WIDTH / 2 - 40, Display.getHeight() / 2 - (110 + TextButton.HEIGHT * 4) / 2, TextButton.WIDTH + 80, 110 + TextButton.HEIGHT * 4, true);
		
		TextButton b = new TextButton(TextButton.WIDTH / 2 + 40, 40, Tr._("play"));
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.addScene(new SceneNewGame());
			}
		});
		c.add(b);
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 110, Tr._("join"));
		b.setEnabled(CFG.INTERNET);
		b.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				final InputField ip = new InputField(0, 0, 0, "", "");
				Dialog dialog = new Dialog(Tr._("join"), Tr._("joindesc"), new Action(Tr._("abort"), Dialog.CLOSE_EVENT), new Action(Tr._("connect"), new IGuiEvent()
				{
					@Override
					public void trigger()
					{
						new Thread()
						{
							Dialog d;
							boolean abort = false;
							
							@Override
							public void run()
							{
								d = ((Dialog) Game.currentGame.getActiveScene());
								d.buttons[1].setEnabled(false);
								d.buttons[0].setClickEvent(new IGuiEvent()
								{
									@Override
									public void trigger()
									{
										abort = true;
										d.buttons[1].setEnabled(true);
									}
								});
								try
								{
									boolean response = Game.client.connectToServer(InetAddress.getByName(ip.getText()));
									if (!response) CFG.p("catch that error you fool");
									
									while (!abort)
									{
										if (!Game.client.isConnected())
										{
											try
											{
												Thread.sleep(100);
											}
											catch (InterruptedException e)
											{
												e.printStackTrace();
											}
										}
										else
										{
											CFG.p("valid server. YAY");
											// TODO: connect
											break;
										}
									}
								}
								catch (UnknownHostException e)
								{
									showErrorDialog();
								}
								
								resetButtons();
							}
							
							
							void resetButtons()
							{
								d.buttons[0].setClickEvent(Dialog.CLOSE_EVENT);
								d.buttons[1].setEnabled(true);
							}
							
							
							void showErrorDialog()
							{
								resetButtons();
								Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("iperror").replace("%ip%", ip.getText()), new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
							}
						}.start();
					}
				}));
				ip.setWidth(dialog.getWidth() - 50);
				dialog.addComponent(ip);
				Game.currentGame.addScene(dialog);
			}
		});
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
		
		b = new TextButton(TextButton.WIDTH / 2 + 40, 250, Tr._("quitGame"));
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
