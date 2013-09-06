package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.security.InvalidParameterException;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.scene.Scene;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class Dialog extends Scene
{
	public static final IGuiEvent CLOSE_EVENT = new IGuiEvent()
	{
		@Override
		public void trigger()
		{
			Game.currentGame.removeActiveScene();
		}
	};
	
	String title;
	String[] lines;
	boolean modal;
	public boolean centered;
	TextButton[] buttons;
	
	int width, height, x, y;
	
	public Font titleFont = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	public Font messageFont = FontAssistant.GAMEFONT.deriveFont(Font.PLAIN, 30f);
	
	public Dialog(String title, String message, boolean modal, Action... buttons)
	{
		this.title = title;
		this.modal = modal;
		centered = true;
		
		width = TextButton.WIDTH * 2 + 20;
		lines = RenderAssistant.wrap(message, messageFont, width - 60);
		height = 90 + lines.length * FontAssistant.getFont(messageFont).getHeight();
		x = Display.getWidth() / 2 - width / 2;
		y = Display.getHeight() / 2 - height / 2;
		
		if (buttons.length > 0) height += TextButton.HEIGHT;
		
		if (buttons.length > 2) throw new InvalidParameterException("Max button count is 2");
		this.buttons = new TextButton[buttons.length];
		if (buttons.length == 1)
		{
			this.buttons[0] = new TextButton(x + width / 2, y + height - 20 - TextButton.HEIGHT, buttons[0]);
		}
		for (TextButton b : this.buttons)
		{
			content.add(b);
		}
	}
	
	@Override
	public void render()
	{
		if (modal)
		{
			glEnable(GL_BLEND);
			glColor4f(0.4f, 0.4f, 0.4f, 0.6f);
			glBindTexture(GL_TEXTURE_2D, 0);
			RenderAssistant.renderRect(0, 0, Display.getWidth(), Display.getHeight());
			glColor4f(1, 1, 1, 1);
		}
		RenderAssistant.bindTexture("/graphics/textures/ui/paper.png");
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		RenderAssistant.renderRect(x, y, width, height, 0, 0, width / 512f, height / 512f);
		RenderAssistant.renderOutline(x, y, width, height, true);
		
		int tx = FontAssistant.getFont(titleFont).getWidth(title);
		int mx = width / 2 - tx / 2;
		RenderAssistant.renderText(x + mx, y + 16, title, titleFont);
		RenderAssistant.renderLine(x + 10, y + 46, width - 20, true, false);
		int h = FontAssistant.getFont(messageFont).getHeight();
		for (int i = 0; i < lines.length; i++)
		{
			if (centered)
			{
				tx = FontAssistant.getFont(messageFont).getWidth(lines[i]);
				mx = width / 2 - tx / 2;
				RenderAssistant.renderText(x + mx, y + 16 + 50 + i * h, lines[i], messageFont);
			}
		}
		
		super.render();
	}
	
	@Override
	public void init()
	{}
}
