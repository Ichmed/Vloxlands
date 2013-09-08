package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;

import com.vloxlands.util.RenderAssistant;

public class Container extends ClickableGui
{
	public boolean filled, doubled, border;
	
	public ArrayList<IGuiElement> components = new ArrayList<>();
	
	public Container(int x, int y, int width, int height)
	{
		this(x, y, width, height, true, true);
	}
	
	public Container(int x, int y, int width, int height, boolean filled)
	{
		this(x, y, width, height, filled, true);
	}
	
	public Container(int x, int y, int width, int height, boolean filled, boolean doubled)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		zIndex = 0;
		this.doubled = doubled;
		this.filled = filled;
		border = true;
	}
	
	@Override
	public void render()
	{
		if (border)
		{
			if (filled) RenderAssistant.renderContainer(x, y, width, height, doubled);
			else RenderAssistant.renderOutline(x, y, width, height, doubled);
		}
		glTranslatef(x, y, 0);
		for (IGuiElement g : components)
		{
			if (g.isVisible() && g.wantsRender()) g.render();
		}
		glTranslatef(-x, -y, 0);
	}
	
	@Override
	public void onTick()
	{
		for (IGuiElement g : components)
		{
			g.onTick();
		}
	}
	
	@Override
	public void handleKeyboard(int key, char chr, boolean down)
	{
		for (IGuiElement g : components)
		{
			g.handleKeyboard(key, chr, down);
		}
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		for (IGuiElement g : components)
		{
			if (g instanceof ClickableGui && ((ClickableGui) g).isUnderCursor(x, y)) ((ClickableGui) g).handleMouse(posX, posY, flag);
		}
	}
	
	public void pack(boolean hor, boolean ver)
	{
		int w = width;
		int h = height;
		if (components.size() == 0) return;
		
		for (IGuiElement g : components)
		{
			if (g.getX() + g.getWidth() > w && hor) w = g.getX() + g.getWidth();
			if (g.getY() + g.getHeight() > h && ver) h = g.getY() + g.getHeight();
		}
		
		width = w;
		height = h;
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		for (IGuiElement ig : components)
		{
			ig.setEnabled(enabled);
		}
	}
	
	public void add(IGuiElement g)
	{
		components.add(g);
	}
}
