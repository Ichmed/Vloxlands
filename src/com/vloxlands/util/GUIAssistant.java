package com.vloxlands.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.ui.Component;

public class GUIAssistant
{
	private static List<Component> components = new ArrayList<>();
	
	public static void addComponent(Component c)
	{
		components.add(c);
	}
	
	public static void renderComponents()
	{
		for (Component c : components)
			if (c.isVisible()) c.draw();
	}
	
	public static void clearComponents()
	{
		components.clear();
	}
	
	public static void handleMouse()
	{
		while (Mouse.next())
		{
			if (Mouse.isButtonDown(1))
			{
				Mouse.setGrabbed(true);
				if (Mouse.isButtonDown(1)) Game.currentGame.rotateCamera();
				else Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			}
			else Mouse.setGrabbed(false);
			
			int x = Mouse.getX();
			int y = Display.getHeight() - Mouse.getY();
			
			for (Component c : components)
			{
				byte b = 0;
				if (Mouse.isButtonDown(0)) b += 1;
				if (Mouse.isButtonDown(1)) b += 2;
				if (Mouse.isButtonDown(2)) b += 4;
				if (b == 0 || (b != 0 && Mouse.getEventButtonState())) c.mouseEvent(x - c.getX(), y - c.getY(), b, x > c.getX() && x <= c.getX() + c.getWidth() && y > c.getY() && y <= c.getY() + c.getHeight());
			}
		}
	}
}
