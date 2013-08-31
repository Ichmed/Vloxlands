package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class TextButton extends Component
{
	String title;
	
	public TextButton(int x, int y, String title)
	{
		super(x - 145, y, 290, 60);
		this.title = title;
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
		if (state == 2) texY = 124;
		if (state == 1) texY = 175;
		
		RenderAssistant.renderRect(x, y, width, 85, texX / 1024.0f, texY / 666.0f, 288 / 1024.0f, 59 / 666.0f);
		glDisable(GL_BLEND);
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		RenderAssistant.renderText(x + mx, y + 85 / 4f, title, Color.white, font);
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
