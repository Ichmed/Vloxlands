package com.vloxlands.ui;

import java.awt.Font;

import org.newdawn.slick.Color;

import com.vloxlands.render.util.RenderHelper;

public class Button extends Component
{
	String title;

	/**
	 * 0 = unselected 1 = hovered 2 = active
	 */
	int state;

	public Button(int x, int y, int width, int height, String title)
	{
		super(x, y, width, height);
		this.title = title;
		this.state = 0;
	}

	public void setState(int s)
	{
		state = s;
	}

	@Override
	public void draw()
	{
		if (state != 1) RenderHelper.glColorHex("222222", 0.6f);
		else RenderHelper.glColorHex("ff9933", 0.6f);

		RenderHelper.renderRect(x, y, width, height);

		RenderHelper.renderText(x, y, title, Color.gray, new Font(Font.SANS_SERIF, Font.BOLD, 25));
	}

	@Override
	public void mouseEvent(int posX, int posY, byte b, boolean c)
	{
		if (!c) state = 0;
		else if ((b & 1) == 1) state = 2;
		else state = 1;
	}
}
