package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Spinner extends ClickableGui
{
	public static final int HEIGHT = 52;
	
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	int min, max, value, step;
	boolean horizontal;
	ArrowButton minus, plus;
	
	public Spinner(int x, int y, int size, int min, int max, int val, int step, boolean horizontal)
	{
		this.x = x;
		this.y = y;
		if (horizontal)
		{
			width = size;
			height = HEIGHT;
		}
		else
		{
			width = HEIGHT;
			height = size;
		}
		value = val;
		this.min = min;
		this.max = max;
		this.step = step;
		this.horizontal = horizontal;
		
		minus = new ArrowButton(x, y + ((horizontal) ? 0 : height - ArrowButton.HEIGHT), (horizontal) ? ArrowButton.MINUS_HOR : ArrowButton.MINUS_VER);
		minus.setClickEvent(new IGuiEvent()
		{
			
			@Override
			public void activate()
			{
				value = (value >= Spinner.this.min + Spinner.this.step) ? value - Spinner.this.step : Spinner.this.min;
			}
		});
		plus = new ArrowButton(x + ((horizontal) ? width - ArrowButton.WIDTH : 0), y, (horizontal) ? ArrowButton.PLUS_HOR : ArrowButton.PLUS_VER);
		plus.setClickEvent(new IGuiEvent()
		{
			
			@Override
			public void activate()
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
		else minus.setEnabled(enabled);
		
		plus.onTick();
		if (value == max) plus.setEnabled(false);
		else plus.setEnabled(enabled);
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
		
		if (enabled) glColor3f(1, 1, 1);
		else glColor3f(0.5f, 0.5f, 0.5f);
		if (horizontal)
		{
			int tx = FontAssistant.getFont(font).getWidth(value + "");
			int mx = width / 2 - tx / 2;
			RenderAssistant.renderText(x + ((width > -1) ? mx : 0), y + ((height > -1) ? height / 4f : 0), value + "", font);
		}
		else
		{
			int tx = FontAssistant.getFont(font).getWidth(value + "");
			int mx = width / 2 - tx / 2;
			int ty = FontAssistant.getFont(font).getHeight(value + "");
			int my = height / 2 - ty / 2;
			RenderAssistant.renderText(x + mx / 2, y + ((width > -1) ? my : 0), value + "", font);
		}
	}
	
	public int getMin()
	{
		return min;
	}
	
	public void setMin(int min)
	{
		this.min = min;
	}
	
	public int getMax()
	{
		return max;
	}
	
	public void setMax(int max)
	{
		this.max = max;
	}
	
	public int getStep()
	{
		return step;
	}
	
	public void setStep(int step)
	{
		this.step = step;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
}
