package com.vloxlands.ui;

import java.awt.Font;
import java.security.InvalidParameterException;

import org.lwjgl.opengl.Display;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class Dialog extends ClickableGui
{
	String title, message;
	boolean modal;
	TextButton[] buttons;
	
	int renderWidth, renderHeight, renderX, renderY;
	
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	public Dialog(String title, String message, boolean modal, Action... buttons)
	{
		this.title = title;
		this.message = message;
		this.modal = modal;
		
		renderWidth = TextButton.WIDTH * 2 + 20;
		String[] lines = RenderAssistant.wrap(message, font, renderWidth);
		// renderHeight = 20 + TextButton.HEIGHT +
		renderX = Display.getWidth() / 2 - width / 2;
		if (!modal)
		{
			width = renderWidth;
			height = renderHeight;
			x = renderX;
			y = renderY;
		}
		else
		{
			width = Display.getWidth();
			height = Display.getHeight();
			x = 0;
			y = 0;
		}
		
		if (buttons.length > 2) throw new InvalidParameterException("Max button count is 2");
		this.buttons = new TextButton[buttons.length];
		if (buttons.length == 1)
		{
			this.buttons[0] = new TextButton(renderX + renderWidth / 2 - TextButton.WIDTH / 2, renderY + renderHeight - 20 - TextButton.HEIGHT, buttons[0]);
		}
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{}
	
	@Override
	public void render()
	{}
	
	@Override
	public void onTick()
	{}
	
}
