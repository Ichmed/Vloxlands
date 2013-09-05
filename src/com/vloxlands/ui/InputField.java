package com.vloxlands.ui;

import java.awt.Font;

import com.vloxlands.game.Game;
import com.vloxlands.util.FontAssistant;

/**
 * @author Dakror
 */
public class InputField extends ClickableGui
{
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	String text, hint;
	boolean focused;
	
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
		height = 35;
		this.text = text;
		this.hint = hint;
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if ((flag & 2) != 0)
		{
			focused = true;
			Game.currentGame.mouseGrabbed = true;
		}
		else focused = false;
	}
	
	@Override
	public void render()
	{}
	
	@Override
	public void onTick()
	{}
	
	@Override
	public void handleKeyboard(int key, boolean down)
	{	
		
	}
	
	/**
	 * Don't use!
	 */
	@Override
	public void setClickEvent(IGuiEvent clickEvent)
	{}
}
