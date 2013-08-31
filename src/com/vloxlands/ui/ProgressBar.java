package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import com.vloxlands.util.RenderAssistant;

public class ProgressBar extends Component
{
	float value;
	
	public ProgressBar(int x, int y, int width, float value)
	{
		super(x - width / 2, y, width, 39);
		this.value = 0;
	}
	
	@Override
	public void draw()
	{
		glEnable(GL_BLEND);
		RenderAssistant.bindTexture("graphics/textures/ui/progressBar.png");
		RenderAssistant.renderRect(x + 7, y + 8, value * (width - 14), 24, 0, 0, value, 1);
		
		RenderAssistant.bindTexture("graphics/textures/ui/gui.png");
		RenderAssistant.renderRect(x, y, 18, 39, 793 / 1024.0f, 548 / 1024.0f, 18 / 1024.0f, 39 / 1024.0f);
		for (int i = 0; i < (width - 36) / 129; i++)
			RenderAssistant.renderRect(x + 18 + i * 129, y, 129, 39, 831 / 1024.0f, 548 / 1024.0f, 129 / 1024.0f, 39 / 1024.0f);
		RenderAssistant.renderRect(x + 18 + (width - 36) / 129 * 129, y, (width - 36) % 129, 39, 831 / 1024.0f, 548 / 1024.0f, ((width - 36) % 129) / 1024.0f, 39 / 1024.0f);
		
		glDisable(GL_SCISSOR_TEST);
		RenderAssistant.renderRect(x + width - 18, y, 18, 39, 980 / 1024.0f, 548 / 1024.0f, 18 / 1024.0f, 39 / 1024.0f);
		glDisable(GL_BLEND);
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
