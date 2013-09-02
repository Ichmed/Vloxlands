package com.vloxlands.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.ui.IClickableGui;
import com.vloxlands.ui.IGuiElement;
import com.vloxlands.ui.Label;

public abstract class Scene
{
	public List<IGuiElement> content = new ArrayList<>();
	private boolean wasButton0Down;
	private boolean wasButton1Down;
	private boolean wasButton2Down;
	
	public abstract void init();
	
	protected void setBackground()
	{
		Label bg = new Label(0, 0, Display.getWidth(), Display.getHeight(), "");
		bg.setTexture("/graphics/textures/ui/paper.png");
		content.add(bg);
	}
	
	public void update()
	{
		for (IGuiElement i : content)
			if (i instanceof IClickableGui)
			{
				((IClickableGui) i).onTick();
			}
		handleInput();
		for (IGuiElement g : content)
			if (g.isVisible()) g.render();
	}
	
	public void handleInput()
	{
		int x = Mouse.getX();
		int y = Mouse.getY();
		int flag = 0;
		
		flag += Mouse.isButtonDown(0) ? 1 : 0;
		flag += wasButton0Down ? 2 : 0;
		flag += Mouse.isButtonDown(1) ? 4 : 0;
		flag += wasButton1Down ? 8 : 0;
		flag += Mouse.isButtonDown(2) ? 16 : 0;
		flag += wasButton2Down ? 32 : 0;
		
		wasButton0Down = Mouse.isButtonDown(0);
		wasButton1Down = Mouse.isButtonDown(1);
		wasButton2Down = Mouse.isButtonDown(2);
		
		if (!handleMouseGUI(x, y, flag)) handleMouseWorld(x, y, flag);
	}
	
	protected boolean handleMouseGUI(int posX, int posY, int flag)
	{
		IClickableGui iG = getObjectUnderCursor();
		if (iG != null)
		{
			iG.handleMouse(posX - (int) iG.getPos().x, posY - (int) iG.getPos().y, flag);
			return true;
		}
		return false;
	}
	
	public IClickableGui getObjectUnderCursor()
	{
		for (IGuiElement i : content)
			if (i instanceof IClickableGui)
			{
				IClickableGui iG = (IClickableGui) i;
				if (iG.isUnderCursor()) return iG;
			}
		return null;
	}
	
	protected void handleMouseWorld(int x, int y, int flag)
	{}
	
	protected void lockScene()
	{
		for (IGuiElement i : content)
			if (i instanceof IClickableGui)
			{
				((IClickableGui) i).setEnabled(false);
			}
	}
}
