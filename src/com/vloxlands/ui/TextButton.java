package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class TextButton extends Component
{
	String title;
	
	/**
	 * 0 = unselected 1 = hovered 2 = active
	 */
	int state;
	
	public TextButton(int x, int y, String title)
	{
		super(x, y, 280, 60);
		this.title = title;
		state = 0;
	}
	
	public void setState(int s)
	{
		state = s;
	}
	
	public int getState()
	{
		return state;
	}
	
	@Override
	public void draw()
	{
		RenderAssistant.disable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glColor3f(1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderAssistant.bindTexture(texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		int texX = 12;
		int texY = 73;
		if (state == 1) texY = 124;
		if (state == 2) texY = 175;
		
		RenderAssistant.renderRect(x, y, width, 85, texX / 1024.0f, texY / 666.0f, 288 / 1024.0f, 59 / 666.0f);
		glDisable(GL_BLEND);
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		RenderAssistant.renderText(x + mx, y + 85 / 4f, title, Color.white, font);
	}
	
	@Override
	public void mouseEvent(int posX, int posY, byte b, boolean c)
	{
		if (!c) state = 0;
		else if ((b & 1) == 1) state = 2;
		else state = 1;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
}
