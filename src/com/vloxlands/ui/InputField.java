package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.TextureImpl;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class InputField extends ClickableGui
{
	public static final int HEIGHT = 35;
	
	public static InputField currentInputfield;
	
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.PLAIN, 30f);
	boolean hidden;
	public boolean doubled = false;
	String text, hint, hiddenShowText;
	public String allowedChars = "";
	
	
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
		this.x = x;
		this.y = y;
		this.width = width;
		height = HEIGHT;
		this.text = text;
		this.hint = hint;
		hidden = false;
		hiddenShowText = "";
	}
	
	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}
	
	@Override
	public void render()
	{
		RenderAssistant.renderContainer(x, y, width + 20, height + 20, doubled);
		if (text.length() == 0 && hint.length() > 0)
		{
			glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
			RenderAssistant.renderText(x + 15, y + 13, hint, font);
		}
		glColor3f(1, 1, 1);
		if (!enabled) glColor3f(0.6f, 0.6f, 0.6f);
		RenderAssistant.renderText(x + 15, y + 13, hidden ? hiddenShowText : text, font);
		if (isFocused())
		{
			TextureImpl.bindNone();
			glColor3f(1, 1, 1);
			RenderAssistant.renderRect(x + 15 + FontAssistant.getFont(font).getWidth(hidden ? hiddenShowText : text), y + 13, 3, height - 5);
		}
	}
	
	@Override
	public void handleKeyboard(int key, char chr, boolean down)
	{
		if (!down || !isFocused() || !enabled) return;
		if (key == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))
		{
			try
			{
				String string = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				if (string.length() > 0)
				{
					setText(string);
					return;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		switch (Keyboard.getKeyName(key))
		{
			case "TAB":
				return;
			case "RETURN":
				if (clickEvent != null) clickEvent.trigger();
				return;
		}
		if (Keyboard.getKeyName(key).equals("BACK"))
		{
			if (text.length() > 0)
			{
				text = text.substring(0, text.length() - 1);
				if (hidden) hiddenShowText = hiddenShowText.substring(0, hiddenShowText.length() - 1);
			}
		}
		else if (font.canDisplay(chr) && FontAssistant.getFont(font).getWidth((hidden ? hiddenShowText + "*" : text + chr)) < width - 10 && (allowedChars.length() == 0 || allowedChars.indexOf(chr) > -1))
		{
			text += chr;
			if (hidden) hiddenShowText += "*";
		}
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String t)
	{
		text = t;
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if ((flag & 1) != 0) currentInputfield = this;
	}
	
	public boolean isFocused()
	{
		return currentInputfield == this;
	}
	
	@Override
	public void resetElement()
	{
		super.resetElement();
		currentInputfield = null;
	}
	
	@Override
	public void onTick()
	{}
	
	public String getHint()
	{
		return hint;
	}
}
