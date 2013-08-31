package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Dimension;
import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import com.vloxlands.settings.CFG;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class TextButton implements IClickableGui
{
	String title;
	IClickEvent clickEvent;
	String texture = "graphics/textures/ui/gui.png";
	
	int x, y, width, height;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	int texX = 12;
	int texY = 124;
	private boolean isVisible = true;
	
	public TextButton(int x, int y, String title)
	{
		this.x = x - 144;
		this.y = y;
		this.width = 288;
		this.height = 59;
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
		RenderAssistant.renderText(x + mx, y + height / 4f, title, Color.white, font);
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
		texY = 124;
	}

	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if ((flag & 1) != 0) texY = 202;
		else if ((flag & 2) != 0) this.clickEvent.onClick();
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
		this.clickEvent = i;
	}

	@Override
	public boolean shouldRender()
	{
		return this.isVisible;
	}

	public void setVisible(boolean b)
	{
		this.isVisible = b;
	}
}
