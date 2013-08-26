package com.vloxlands.ui;

import java.awt.Font;

import org.newdawn.slick.Color;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Button extends Component
{
	String title;

	/**
	 * 0 = unselected 1 = hovered 2 = active
	 */
	int state;

	Font font;

	public Button(int x, int y, int width, int height, String title)
	{
		super(x, y, width, height);
		this.title = title;
		this.state = 0;
		this.font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
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
		//TODO:  Get the background to trancparency of 0.6f
		
		if (state != 1) Color.decode("#222222").bind();
		else Color.decode("#ff9933").bind();

		RenderAssistant.renderRect(x, y, width, height);

		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		RenderAssistant.renderText(x + mx, y + height / 4f, title, Color.white, font);
	}

	@Override
	public void mouseEvent(int posX, int posY, byte b, boolean c)
	{
		if (!c) state = 0;
		else if ((b & 1) == 1) state = 2;
		else state = 1;
	}
}
