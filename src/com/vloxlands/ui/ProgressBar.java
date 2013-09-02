package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class ProgressBar extends IGuiElement
{
	public static final int HEIGHT = 39;
	
	float value;
	public String title;
	boolean showPercentage;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	
	public ProgressBar(int x, int y, int width, float value, boolean showPercentage)
	{
		this.x = x - (width / 2);
		this.y = y;
		this.width = width;
		height = HEIGHT;
		this.value = 0;
		this.showPercentage = showPercentage;
		font = font.deriveFont(20f);
	}
	
	@Override
	public void render()
	{
		glEnable(GL_BLEND);
		RenderAssistant.bindTexture("/graphics/textures/ui/progressBar.png");
		RenderAssistant.renderRect(x + 7, y + 8, value * (width - 14), 24, 0, 0, value, 1);
		
		RenderAssistant.renderOutline(x, y, width, height, false);
		
		glDisable(GL_BLEND);
		
		if (showPercentage)
		{
			int tx = FontAssistant.getFont(font).getWidth(((int) (value * 100)) + "%");
			int mx = width / 2 - tx / 2;
			glColor3f(0.2f, 0.2f, 0.2f);
			RenderAssistant.renderText(x + mx, y + height / 4f, ((int) (value * 100)) + "%", font);
		}
		else if (title != null)
		{
			int tx = FontAssistant.getFont(font).getWidth(title);
			int mx = width / 2 - tx / 2;
			glColor3f(0.2f, 0.2f, 0.2f);
			RenderAssistant.renderText(x + mx, y + height / 4f, title, font);
		}
	}
	
	public float getValue()
	{
		return value;
	}
	
	public void setValue(float value)
	{
		this.value = value;
	}
}
