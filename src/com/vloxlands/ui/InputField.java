package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.input.Keyboard;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class InputField extends ClickableGui
{
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.PLAIN, 30f);
	boolean focused;
	String text, hint;
	
	public InputField(int x, int y, int width)
	{
		this(x, y, width, "");
	}
	
	public InputField(int x, int y, int width, String text)
	{
		this(x, y, width, text, "");
	}
	
	public InputField(int x, int y, int width, String text, String hint)
	{
		this.x = x - width / 2;
		this.y = y;
		this.width = width;
		height = 35;
		this.text = text;
		this.hint = hint;
	}
	
	@Override
	public void render()
	{
		RenderAssistant.renderContainer(x, y, width + 20, height + 20, false);
		if (text.length() == 0 && hint.length() > 0)
		{
			glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
			RenderAssistant.renderText(x + 15, y + 13, hint, font);
		}
		glColor3f(1, 1, 1);
		RenderAssistant.renderText(x + 15, y + 13, text, font);
	}
	
	@Override
	public void onTick()
	{
		focused = false;
	}
	
	@Override
	public void handleKeyboard(int key, char chr, boolean down)
	{
		if (!down || !focused) return;
		if (Keyboard.getKeyName(key).equals("BACK"))
		{
			if (text.length() > 0) text = text.substring(0, text.length() - 1);
		}
		else if (font.canDisplay(chr)) text += chr;
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		focused = true;
	}
}
