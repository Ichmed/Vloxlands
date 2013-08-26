package com.vloxlands.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GUIHandler
{
	private static List<Component> components = new ArrayList<>();

	public static void addComponent(Component c)
	{
		components.add(c);
	}

	public static void renderComponents()
	{
		for (Component c : components)
			if (c.visible) c.draw();
	}

	public static void handleMouse()
	{
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();

		for (Component c : components)
		{
			byte b = 0;
			if (Mouse.isButtonDown(0)) b += 1;
			if (Mouse.isButtonDown(1)) b += 2;
			if (Mouse.isButtonDown(2)) b += 4;

			c.mouseEvent(x - c.x, y - c.y, b, x > c.x && x <= c.x + c.width && y > c.y && y <= c.y + c.height);
		}
	}
}
