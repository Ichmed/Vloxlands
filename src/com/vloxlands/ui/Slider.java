package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.MathHelper;
import com.vloxlands.util.RenderAssistant;

//TODO: Dakror claims to rework this class. Date: 04.09.2013

public class Slider extends ClickableGui
{
	public static final int WIDTH = 205;
	public static final int HEIGHT = 18;
	
	protected float value;
	public float minValue, maxValue;
	public float sliderPos = 2.5f;
	public GuiRotation guiRot = GuiRotation.HORIZONTAL;
	public boolean integerMode = false;
	
	int texX = 795;
	int texY = 72;
	// private boolean stepMode = false;
	// private float stepSize;
	public String title;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	IGuiEvent event;
	
	public Slider(int x, int y, int size, float min, float max, GuiRotation rot)
	{
		this(x, y, size, min, max, min, rot);
	}
	
	public Slider(int x, int y, int size, float min, float max, float startValue, GuiRotation rot)
	{
		super();
		this.x = x;
		this.y = y;
		if (rot == GuiRotation.HORIZONTAL)
		{
			height = HEIGHT;
			width = size;
		}
		else
		{
			height = size;
			width = WIDTH;
		}
		texture = "/graphics/textures/ui/gui.png";
		minValue = min;
		maxValue = max;
		value = startValue;
		zIndex = 1;
	}
	
	@Override
	public void onTick()
	{}
	
	@Override
	public void handleMouse(int x, int z, int flag)
	{
		if ((flag & 1) != 0)
		{
			if (guiRot == GuiRotation.VERTICAL)
			{
				sliderPos = MathHelper.clamp(y - 5, 2.5f, 190f);
				value = (maxValue - minValue) * ((sliderPos - 0.8f) / 193) + minValue;
			}
			else
			{
				sliderPos = MathHelper.clamp(x - 5, 2.5f, 193f);
				value = (maxValue - minValue) * ((sliderPos - 2.5f) / 190f) + minValue;
			}
		}
		
		event.activate();
		
	}
	
	@Override
	public void render()
	{
		RenderAssistant.disable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glColor3f(1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderAssistant.bindTexture(texture);
		
		RenderAssistant.renderRect(x, y, width, height, texX / 1024.0f, texY / 1024.0f, width / 1024.0f, height / 1024.0f);
		if (guiRot == GuiRotation.HORIZONTAL)
		{
			RenderAssistant.renderRect(x + sliderPos - 5, y - (35 - height) / 2, 20, 35, 889 / 1024.0f, 16 / 1024.0f, 25 / 1024.0f, 44 / 1024.0f);
		}
		
		String displayString = ((title != null) ? title + ": " : "") + ((integerMode) ? ((int) value) + "" : value);
		
		int tx = FontAssistant.getFont(font).getWidth(displayString);
		int mx = width / 2 - tx / 2;
		if (enabled) glColor3f(1, 1, 1);
		else glColor3f(0.5f, 0.5f, 0.5f);
		RenderAssistant.renderText(x + mx, y - 28, displayString, font);
		
		glDisable(GL_BLEND);
	}
	
	public void setIntegerMode(boolean b)
	{
		integerMode = b;
	}
	
	public float getValue()
	{
		return value;
	}
	
	// public void setStepSize(float f)
	// {
	// this.stepSize = f;
	// this.stepMode = true;
	// }
	
	public void setTitle(String s)
	{
		title = s;
	}
	
	public void setEvent(IGuiEvent e)
	{
		event = e;
	}
}
