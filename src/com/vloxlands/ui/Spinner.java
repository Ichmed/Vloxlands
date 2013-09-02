package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Spinner extends IClickableGui
{
	public static final int HEIGHT = 52;
	
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	int min, max, value, step;
	ArrowButton minus, plus;
	
	public Spinner(int x, int y, int width, int min, int max, int val, int step)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		height = HEIGHT;
		value = val;
		this.min = min;
		this.max = max;
		this.step = step;
		
		minus = new ArrowButton(x, y, ArrowButton.MINUS_HOR);
		minus.setClickEvent(new IClickEvent()
		{
			
			@Override
			public void onClick()
			{
				value = (value >= Spinner.this.min + Spinner.this.step) ? value - Spinner.this.step : Spinner.this.min;
			}
		});
		plus = new ArrowButton(x + width - ArrowButton.WIDTH, y, ArrowButton.PLUS_HOR);
		plus.setClickEvent(new IClickEvent()
		{
			
			@Override
			public void onClick()
			{
				value = (value <= Spinner.this.max - Spinner.this.step) ? value + Spinner.this.step : Spinner.this.max;
			}
		});
	}
	
	@Override
	public void onTick()
	{
		minus.onTick();
		if (value == min) minus.setEnabled(false);
		else if (enabled) minus.setEnabled(true);
		
		plus.onTick();
		if (value == max) plus.setEnabled(false);
		else if (enabled) plus.setEnabled(true);
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if (minus.isUnderCursor()) minus.handleMouse(posX, posY, flag);
		if (plus.isUnderCursor()) plus.handleMouse(posX, posY, flag);
	}
	
	@Override
	public void render()
	{
		minus.render();
		plus.render();
		
		int tx = FontAssistant.getFont(font).getWidth(value + "");
		int mx = width / 2 - tx / 2;
		if (enabled) glColor3f(1, 1, 1);
		else glColor3f(0.5f, 0.5f, 0.5f);
		RenderAssistant.renderText(x + ((width > -1) ? mx : 0), y + ((height > -1) ? height / 4f : 0), value + "", font);
	}
	
}
