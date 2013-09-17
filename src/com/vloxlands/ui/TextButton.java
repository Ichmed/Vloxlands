package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.TrueTypeFont;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class TextButton extends ClickableGui
{
	public static final int WIDTH = 288;
	public static final int HEIGHT = 59;
	
	String title;
	public Vector3f textColor;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	boolean toggleMode;
	boolean active;
	int texX = 12;
	int texY = 124;
	
	public TextButton(int x, int y, String title)
	{
		this.x = x - 144;
		this.y = y;
		width = WIDTH;
		height = HEIGHT;
		this.title = title;
		toggleMode = false;
		active = false;
		textColor = new Vector3f(1, 1, 1);
		texture = "/graphics/textures/ui/gui.png";
	}
	
	public TextButton(int x, int y, Action action)
	{
		this(x, y, action.getText());
		setClickEvent(action.getEvent());
	}
	
	@Override
	public void render()
	{
		glEnable(GL_BLEND);
		glColor3f(1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderAssistant.bindTexture(texture);
		if (!enabled && toggleMode) glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
		RenderAssistant.renderRect(x, y, width, height, texX / 1024.0f, texY / 1024.0f, WIDTH / 1024.0f, HEIGHT / 1024.0f);
		glColor4f(1, 1, 1, 1);
		glDisable(GL_BLEND);
		TrueTypeFont ttf = FontAssistant.getFont(font);
		int tx = ttf.getWidth(title);
		int mx = width / 2 - tx / 2;
		int my = height / 2 - ttf.getHeight() / 2;
		if (enabled || toggleMode) glColor3f(textColor.x, textColor.y, textColor.z);
		else glColor3f(0.5f, 0.5f, 0.5f);
		
		RenderAssistant.renderText(x + mx, y + my, title, font);
		glColor3f(1, 1, 1);
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	@Override
	public void setWidth(int width)
	{
		x = x + this.width / 2 - width / 2;
		this.width = width;
	}
	
	@Override
	public void onTick()
	{
		if (!toggleMode)
		{
			if (!enabled) texY = 358;
			else texY = 124;
			
		}
		else
		{
			if (active) texY = 202;
			else texY = 124;
		}
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if (!enabled) return;
		if (!toggleMode)
		{
			if ((flag & 1) != 0) texY = 202;
			else if ((flag & 2) != 0)
			{
				if (clickEvent != null) clickEvent.trigger();
			}
			else texY = 280;
		}
		else
		{
			if (flag == 2)
			{
				active = !active;
				if (clickEvent != null) clickEvent.trigger();
			}
		}
	}
	
	/**
	 * @return only useful stuff if in toggleMode
	 */
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean b)
	{
		active = b;
	}
	
	public boolean isInToggleMode()
	{
		return toggleMode;
	}
	
	public void setToggleMode(boolean toggleMode)
	{
		this.toggleMode = toggleMode;
	}
}
