package com.vloxlands.util;

import java.util.ArrayList;
import java.util.List;

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
}
