package com.vloxlands.ui;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class LobbySlot extends ClickableGui
{
	public static final int HEIGHT = 80;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 40f);
	String username;
	
	public LobbySlot(String username)
	{
		x = 0;
		y = 0;
		height = HEIGHT;
		this.username = username;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	@Override
	public void render()
	{
		RenderAssistant.renderText(x + 15, y + height / 6, username, font);
		RenderAssistant.renderLine(x, y + height - 20, width, true, false);
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{}
	
	@Override
	public void onTick()
	{}
	
}
