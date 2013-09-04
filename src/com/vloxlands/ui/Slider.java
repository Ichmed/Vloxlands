package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.util.HashMap;

import com.vloxlands.settings.CFG;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.MathHelper;
import com.vloxlands.util.RenderAssistant;

public class Slider extends ClickableGui
{
	HashMap<Float, String> customTitles;
	
	public static final int WIDTH = 205;
	public static final int HEIGHT = 18;
	
	protected float value;
	public float minValue, maxValue;
	public float sliderPos = 2.5f;
	public GuiRotation guiRot;
	public boolean integerMode = false;
	
	private boolean stepMode = false;
	private float stepSize;
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
			width = HEIGHT;
		}
		guiRot = rot;
		texture = "/graphics/textures/ui/gui.png";
		minValue = min;
		maxValue = max;
		value = startValue;
		sliderPos = (value - min) / (max - min) * (size - 14);
		
		customTitles = new HashMap<>();
	}
	
	public void addCustomTitle(float value, String title)
	{
		customTitles.put(value, title);
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
				float percent = MathHelper.clamp(z - 5, 0, height - 14) / (height - 14) * 100;
				
				if (stepMode) percent = MathHelper.round(percent, stepSize);
				
				sliderPos = percent / 100 * (height - 14);
				value = (maxValue - minValue) * (percent / 100) + minValue;
			}
			else
			{
				float percent = MathHelper.clamp(x - 5, 0, width - 14) / (width - 14) * 100;
				
				if (stepMode) percent = MathHelper.round(percent, stepSize);
				
				sliderPos = percent / 100 * (width - 14);
				value = (maxValue - minValue) * (percent / 100) + minValue;
			}
		}
		
		if (event != null) event.trigger();
		
	}
	
	@Override
	public void render()
	{
		glPushMatrix();
		{
			RenderAssistant.disable(GL_LIGHTING);
			glEnable(GL_BLEND);
			glColor3f(1, 1, 1);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			RenderAssistant.bindTexture(texture);
			
			if (guiRot == GuiRotation.HORIZONTAL)
			{
				RenderAssistant.renderRect(x, y, 7, height, 794 / 1024.0f, 72 / 1024.0f, 7 / 1024.0f, 20 / 1024.0f);
				RenderAssistant.renderRect(x + width - 7, y, 7, height, 994 / 1024.0f, 72 / 1024.0f, 7 / 1024.0f, 20 / 1024.0f);
				for (int i = 0; i < (width - 14) / 193; i++)
					RenderAssistant.renderRect(x + 7 + i * 193, y, 193, height, 801 / 1024.0f, 72 / 1024.0f, 193 / 1024.0f, 20 / 1024.0f);
				RenderAssistant.renderRect(x + 7 + (width - 14) / 193 * 193, y, (width - 14) % 193, height, 801 / 1024.0f, 72 / 1024.0f, (width - 14) % 193 / 1024.0f, 20 / 1024.0f);
				
				RenderAssistant.renderRect(x + sliderPos - 5, y - (38 - height) / 2, 20, 35, 889 / 1024.0f, 16 / 1024.0f, 25 / 1024.0f, 44 / 1024.0f);
			}
			else
			{
				glTranslatef(x + HEIGHT / 2, y + HEIGHT / 2, 0);
				glRotatef(90, 0, 0, 1);
				glTranslatef(-x - HEIGHT / 2, -y - HEIGHT / 2, 0);
				
				RenderAssistant.renderRect(x, y, 7, width, 794 / 1024.0f, 72 / 1024.0f, 7 / 1024.0f, 20 / 1024.0f);
				RenderAssistant.renderRect(x + height - 7, y, 7, width, 994 / 1024.0f, 72 / 1024.0f, 7 / 1024.0f, 20 / 1024.0f);
				for (int i = 0; i < (height - 14) / 193; i++)
					RenderAssistant.renderRect(x + 7 + i * 193, y, 193, width, 801 / 1024.0f, 72 / 1024.0f, 193 / 1024.0f, 20 / 1024.0f);
				RenderAssistant.renderRect(x + 7 + (height - 14) / 193 * 193, y, (height - 14) % 193, width, 801 / 1024.0f, 72 / 1024.0f, (height - 14) % 193 / 1024.0f, 20 / 1024.0f);
				
				RenderAssistant.renderRect(x + sliderPos - 5, y - (38 - width) / 2, 20, 35, 889 / 1024.0f, 16 / 1024.0f, 25 / 1024.0f, 44 / 1024.0f);
				glTranslatef(x + HEIGHT / 2, y + HEIGHT / 2, 0);
				glRotatef(-90, 0, 0, 1);
				glTranslatef(-x - HEIGHT / 2, -y - HEIGHT / 2, 0);
			}
			
			String displayString = ((title != null) ? title + ": " : "") + ((integerMode) ? ((int) value) + "" : value);
			
			if (customTitles.containsKey(value)) displayString = customTitles.get(value);
			
			int tx = FontAssistant.getFont(font).getWidth(displayString);
			int mx = width / 2 - tx / 2;
			if (enabled) glColor3f(1, 1, 1);
			else glColor3f(0.5f, 0.5f, 0.5f);
			RenderAssistant.renderText(x + mx, y - 34, displayString, font);
			
			glDisable(GL_BLEND);
		}
		glPopMatrix();
	}
	
	public void setIntegerMode(boolean b)
	{
		integerMode = b;
	}
	
	public float getValue()
	{
		return value;
	}
	
	public void setStepSize(float f)
	{
		stepSize = f / (maxValue - minValue) * 100;
		CFG.p(stepSize);
		stepMode = true;
	}
	
	public void setTitle(String s)
	{
		title = s;
	}
	
	public void setEvent(IGuiEvent e)
	{
		event = e;
	}
}
