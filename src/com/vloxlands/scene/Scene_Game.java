package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;

public class Scene_Game extends Scene
{
	@Override
	public void init()
	{
		glClearColor(0.5f, 0.8f, 0.85f, 1);
	}
	
	@Override
	public void update()
	{
		super.update();
	}
	
	@Override
	public void handleMouseWorld(int x, int y, int flag)
	{
		super.handleMouseWorld(x, y, flag);
		if (Mouse.isButtonDown(1))
		{
			Mouse.setGrabbed(true);
			if (Mouse.isButtonDown(1)) Game.currentGame.rotateCamera();
			else Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}
		else Mouse.setGrabbed(false);
	}
}
