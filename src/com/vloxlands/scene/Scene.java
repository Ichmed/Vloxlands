package com.vloxlands.scene;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.ui.ClickableGui;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.IGuiElement;
import com.vloxlands.ui.Label;

public abstract class Scene
{
	public ArrayList<IGuiElement> content = new ArrayList<>();
	private boolean wasButton0Down;
	private boolean wasButton1Down;
	private boolean wasButton2Down;
	public boolean initialized = false;
	
	public abstract void init();
	
	boolean uiActive = true, worldActive = true;
	
	protected void setBackground()
	{
		Label bg = new Label(0, 0, Display.getWidth(), Display.getHeight(), "");
		bg.setZIndex(-1);
		bg.setTexture("/graphics/textures/ui/paper.png");
		bg.stackTexture = true;
		bg.texW = 512;
		bg.texH = 512;
		content.add(bg);
	}
	
	protected void setTitle(String title)
	{
		Label l = new Label(0, 0, Display.getWidth(), 60, title);
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		Container c = new Container(-15, 85, Display.getWidth() + 30, 1);
		c.doubled = false;
		c.filled = false;
		c.setZIndex(1);
		content.add(c);
	}
	
	public void onTickContent()
	{
		ArrayList<IGuiElement> sorted = getSortedContent();
		
		for (IGuiElement i : sorted)
			if (i instanceof ClickableGui)
			{
				((ClickableGui) i).onTick();
			}
	}
	
	public void onTick()
	{
		onTickContent();
		handleMouse();
	}
	
	public void render()
	{
		renderContent();
	}
	
	public void renderContent()
	{
		ArrayList<IGuiElement> sorted = getSortedContent();
		for (IGuiElement g : sorted)
			if (g.isVisible()) g.render();
	}
	
	public void handleMouse()
	{
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
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
		
		if ((!uiActive || !handleMouseGUI(x, y, flag)) && worldActive) handleMouseWorld(x, y, flag);
	}
	
	public void handleKeyboard(int key, boolean down)
	{
		for (IGuiElement iG : content)
		{
			iG.handleKeyboard(key, down);
		}
	}
	
	// not abstract so that implementing won't be forced
	public boolean handleMouseGUI(int posX, int posY, int flag)
	{
		ClickableGui iG = getObjectUnderCursor();
		if (iG != null && iG.isVisible() && iG.isEnabled())
		{
			iG.handleMouse(posX - (int) iG.getPos().x, posY - (int) iG.getPos().y, flag);
			return true;
		}
		return false;
	}
	
	private ClickableGui getObjectUnderCursor()
	{
		for (IGuiElement i : getSortedContent())
			if (i instanceof ClickableGui)
			{
				ClickableGui iG = (ClickableGui) i;
				if (iG.isUnderCursor()) return iG;
			}
		return null;
	}
	
	private ArrayList<IGuiElement> getSortedContent()
	{
		@SuppressWarnings("unchecked")
		final ArrayList<IGuiElement> sorted = (ArrayList<IGuiElement>) content.clone();
		if (sorted.size() == 0 || sorted.get(0) == null) return new ArrayList<>();
		
		Collections.sort(sorted, new Comparator<IGuiElement>()
		{
			
			@Override
			public int compare(IGuiElement o1, IGuiElement o2)
			{
				return o1.getZIndex() - o2.getZIndex();
			}
		});
		
		return sorted;
	}
	
	// not abstract so that implementing won't be forced
	public void handleMouseWorld(int x, int y, int flag)
	{}
	
	protected void lockScene()
	{
		for (IGuiElement i : content)
			if (i instanceof ClickableGui)
			{
				((ClickableGui) i).setEnabled(false);
			}
	}
}
