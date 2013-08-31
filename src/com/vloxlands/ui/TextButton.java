package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class TextButton implements IClickableGui
{
	String title;
	IClickEvent clickEvent;
	String texture = "/graphics/textures/ui/gui.png";
	boolean isActive = true;
	
	int x, y, width, height;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	int texX = 12;
	int texY = 124;
	private boolean isVisible = true;
	
	public TextButton(int x, int y, String title)
	{
		this.x = x - 144;
		this.y = y;
		width = 288;
		height = 59;
		this.title = title;
	}
	
	@Override
	public void render()
	{
		RenderAssistant.disable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glColor3f(1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderAssistant.bindTexture(texture);
		
		RenderAssistant.renderRect(x, y, width, height, texX / 1024.0f, texY / 1024.0f, 288 / 1024.0f, 59 / 1024.0f);
		glDisable(GL_BLEND);
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		RenderAssistant.renderText(x + mx, y + height / 4f, title, (isActive ? Color.white : Color.gray), font);
		glColor3f(1, 1, 1);
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	@Override
	public void onTick()
	{
		if (!isActive) texY = 358;
		else texY = 124;
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if (!isActive) return;
		if ((flag & 1) != 0) texY = 202;
		else if ((flag & 2) != 0) clickEvent.onClick();
		else texY = 280;
	}
	
	@Override
	public Vector2f getPos()
	{
		return new Vector2f(x, y);
	}
	
	@Override
	public Vector2f getSize()
	{
		return new Vector2f(width, height);
	}
	
	public void setClickEvent(IClickEvent i)
	{
		clickEvent = i;
	}
	
	@Override
	public boolean shouldRender()
	{
		return isVisible;
	}
	
	public void setVisible(boolean b)
	{
		isVisible = b;
	}
	
	@Override
	public void setActive(boolean b)
	{
		isActive = b;
	}
}
