package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.ui.Label;

public class SceneGame extends Scene
{
	public static Label testLabel;
	
	@Override
	public void init()
	{
		worldActive = true;
		glClearColor(0.5f, 0.8f, 0.85f, 1);
		
		testLabel = new Label(0, 0, 300, 25, "");
		content.add(testLabel);
		
		
		unlockScene();
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	@Override
	public void handleMouseWorld(int x, int y, int flag)
	{
		if (!Game.currentGame.isActiveScene(this)) return;
		
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
		if (key == Keyboard.KEY_ESCAPE && down) Game.currentGame.addScene(new ScenePause());
	}
}
