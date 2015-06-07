package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.security.InvalidParameterException;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.TextureImpl;

import com.vloxlands.game.Game;
import com.vloxlands.scene.Scene;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class Dialog extends Scene {
	public static final IGuiEvent CLOSE_EVENT = new IGuiEvent() {
		@Override
		public void trigger() {
			Game.currentGame.removeActiveScene();
		}
	};
	
	String title;
	String[] lines;
	public boolean centered;
	public TextButton[] buttons;
	Container container;
	
	int width, height, x, y;
	
	public Font titleFont = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	public Font messageFont = FontAssistant.GAMEFONT.deriveFont(Font.PLAIN, 30f);
	
	String rawTitle, rawMessage;
	Action[] rawActions;
	
	public Dialog(String title, String message, Action... buttons) {
		width = TextButton.WIDTH * 2 + 20;
		rawTitle = title;
		rawMessage = message;
		rawActions = buttons;
		
		container = new Container(x, y, width, 0);
		container.border = false;
		content.add(container);
	}
	
	@Override
	public void render() {
		glEnable(GL_BLEND);
		glColor4f(IGuiElement.gray.x, IGuiElement.gray.y, IGuiElement.gray.z, IGuiElement.gray.w);
		TextureImpl.bindNone();
		RenderAssistant.renderRect(0, 0, Display.getWidth(), Display.getHeight());
		glColor4f(1, 1, 1, 1);
		
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
		for (int i = 0; i < lines.length; i++) {
			if (centered) {
				tx = FontAssistant.getFont(messageFont).getWidth(lines[i]);
				mx = width / 2 - tx / 2;
				RenderAssistant.renderText(x + mx, y + 16 + 50 + i * h, lines[i], messageFont);
			}
		}
		
		super.render();
	}
	
	@Override
	public void init() {
		content.clear();
		title = rawTitle;
		centered = true;
		
		lines = RenderAssistant.wrap(rawMessage, messageFont, width - 60);
		height = 90 + lines.length * FontAssistant.getFont(messageFont).getHeight() + container.getHeight();
		x = Display.getWidth() / 2 - width / 2;
		y = Display.getHeight() / 2 - height / 2;
		
		container.setWidth(width - 30);
		container.setX(x + 15);
		container.setY(y + lines.length * FontAssistant.getFont(messageFont).getHeight() + 70);
		
		if (rawActions.length > 0) height += TextButton.HEIGHT;
		
		if (rawActions.length > 2) throw new InvalidParameterException("Max button count is 2");
		buttons = new TextButton[rawActions.length];
		if (rawActions.length == 1) buttons[0] = new TextButton(x + width / 2, y + height - 20 - TextButton.HEIGHT, rawActions[0]);
		else {
			buttons[0] = new TextButton(x + width / 4 + 10, y + height - 20 - TextButton.HEIGHT, rawActions[0]);
			buttons[1] = new TextButton(x + width / 4 * 3 - 10, y + height - 20 - TextButton.HEIGHT, rawActions[1]);
		}
		
		for (TextButton b : buttons)
			content.add(b);
		
		content.add(container);
	}
	
	public void addComponent(IGuiElement g) {
		container.add(g);
		container.pack(false, true);
		container.setHeight(container.getHeight() + 30);
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
