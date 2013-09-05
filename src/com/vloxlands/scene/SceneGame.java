package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.Vloxlands;
import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.RenderAssistant;

public class SceneGame extends Scene
{
	boolean paused;
	
	@Override
	public void init()
	{
		Game.currentGame.resetCamera();
		glClearColor(0.5f, 0.8f, 0.85f, 1);
		paused = false;
		
		Label l = new Label(0, 100, Display.getWidth(), 60, Tr._("title.pause"));
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		TextButton mainmenu = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 - 65, Tr._("title.mainmenu"));
		mainmenu.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentMap = null;
				Game.currentGame.setScene(new SceneMainMenu());
			}
		});
		content.add(mainmenu);
		
		TextButton quit = new TextButton(Display.getWidth() / 2, Display.getHeight() / 2 + 10, Tr._("title.quitGame"));
		quit.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				Vloxlands.exit();
			}
		});
		content.add(quit);
		
		content.add(new Container(Display.getWidth() / 2 - 170, Display.getHeight() / 2 - 100, 340, 200));
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		content.get(0).setVisible(paused); // Title label
		content.get(1).setVisible(paused); // mainmenu button
		content.get(2).setVisible(paused); // quit button
		worldActive = !paused;
	}
	
	@Override
	public void handleMouseWorld(int x, int y, int flag)
	{
		if (Mouse.isButtonDown(1))
		{
			if (!Mouse.isGrabbed()) Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			
			Game.currentGame.mouseGrabbed = true;
			if (Mouse.isButtonDown(1)) Game.currentGame.rotateCamera();
			else Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}
		else Game.currentGame.mouseGrabbed = false;
	}
	
	@Override
	public void handleKeyboard(int key, char chr, boolean down)
	{
		if (key == Keyboard.KEY_ESCAPE && down)
		{
			paused = !paused;
		}
	}
	
	@Override
	public void render()
	{
		if (!paused) return;
		
		glEnable(GL_BLEND);
		glColor4f(0.4f, 0.4f, 0.4f, 0.6f);
		glBindTexture(GL_TEXTURE_2D, 0);
		RenderAssistant.renderRect(0, 0, Display.getWidth(), Display.getHeight());
		
		renderContent();
		
		glDisable(GL_BLEND);
	}
}
