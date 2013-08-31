package com.vloxlands.ui;

import org.newdawn.slick.Color;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Label extends Component
{
	String title;
	
	public Label(int x, int y, int width, int height, String title)
	{
		super(x, y, width, height);
		this.title = title;
		texture = null;
	}
	
	@Override
	public void draw()
	{
		RenderAssistant.bindTexture(texture);
		RenderAssistant.renderRect(x, y, width, height);
		
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		RenderAssistant.renderText(x + ((width > -1) ? mx : 0), y + ((height > -1) ? height / 4f : 0), title, Color.white, font);
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
