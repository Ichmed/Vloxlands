package com.vloxlands.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.render.util.RenderHelper;
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
		RenderHelper.set2DRenderMode(true);

		for (Component c : components)
			if (c.isVisible()) c.draw();

		RenderHelper.set2DRenderMode(false);
	}

	public static void clearComponents()
	{
		components.clear();
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

			c.mouseEvent(x - c.getX(), y - c.getY(), b, x > c.getX() && x <= c.getX() + c.getWidth() && y > c.getY() && y <= c.getY() + c.getHeight());
		}
	}
}
