package ui;

import java.util.ArrayList;
import java.util.List;

public class GUIHandler
{
	private static List<Component> components = new ArrayList<>();
	
	public static void addComponent(Component c)
	{
		components.add(c);
	}
	
	public static void renderComponents()
	{
		for(Component c : components) if(c.visible) c.draw();
	}
}
