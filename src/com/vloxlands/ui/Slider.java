package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.MathHelper;
import com.vloxlands.util.RenderAssistant;

public class Slider extends ClickableGui
{
	protected float value;
	public float minValue, maxValue;
	public float sliderPos = 2.5f;
	public GuiRotation guiRot = GuiRotation.HORIZONTAL;
	public boolean integerMode = false;

	int texX = 795;
	int texY = 72;
//	private boolean stepMode = false;
//	private float stepSize;
	public String title;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	IGuiEvent event;

	public Slider(float min, float max)
	{
		this(min, max, min);
	}
	
	public Slider(float min, float max, float startValue)
	{
		super();
		this.height = 18;
		this.width = 205;
		texture = "/graphics/textures/ui/gui.png";
		this.minValue = min;
		this.maxValue = max;
		this.value = startValue;
	}

	@Override
	public void onTick()
	{
	}

	@Override
	public void handleMouse(int x, int z, int flag)
	{
		if ((flag & 1) != 0)
		{
			if (guiRot == GuiRotation.VERTICAL)
			{
				sliderPos = MathHelper.clamp(y, 2.5f, 197.5f);
				value = (maxValue - minValue) * ((sliderPos - 2.5f) / 195) + minValue;
			} else
			{
				sliderPos = MathHelper.clamp(x, 2.5f, 197.5f);
				value = (maxValue - minValue) * ((sliderPos - 2.5f) / 195) + minValue;
			}
		}
		
		this.event.activate();

	}

	@Override
	public void render()
	{
		RenderAssistant.disable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glColor3f(1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderAssistant.bindTexture(texture);

		RenderAssistant.renderRect(x, y, width, height, texX / 1024.0f, texY / 1024.0f, this.width / 1024.0f, this.height / 1024.0f);
		if (guiRot == GuiRotation.HORIZONTAL) RenderAssistant.renderRect(x + sliderPos - 5, y - 1, 10, height + 2, 889 / 1024.0f, 17 / 1024.0f, 26 / 1024.0f, 42 / 1024.0f);
		
		String displayString = this.title + ": " + this.getValue();
		
		int tx = FontAssistant.getFont(font).getWidth(displayString);
		int mx = width / 2 - tx / 2;
		if (enabled) glColor3f(1, 1, 1);
		else glColor3f(0.5f, 0.5f, 0.5f);
		RenderAssistant.renderText(x + mx, y - 28, displayString, font);
		
		glDisable(GL_BLEND);
	}
	
	public void setIntegerMode(boolean b)
	{
		this.integerMode = b;
	}
	
	public float getValue()
	{
		if(this.integerMode) return (int)this.value;
		return this.value;
	}
	
//	public void setStepSize(float f)
//	{
//		this.stepSize = f;
//		this.stepMode = true;
//	}
	
	public void setTitle(String s)
	{
		this.title = s;
	}
	
	public void setEvent(IGuiEvent e)
	{
		this.event = e;
	}
}
