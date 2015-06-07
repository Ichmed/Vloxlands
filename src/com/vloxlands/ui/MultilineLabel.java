package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class MultilineLabel extends IGuiElement {
	String[] lines;
	public boolean centered;
	Font f;
	
	public MultilineLabel(int x, int y, int width, int height, String str) {
		this(x, y, width, height, FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f), str);
	}
	
	public MultilineLabel(int x, int y, int width, int height, Font f, String str) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		centered = false;
		this.f = f;
		lines = RenderAssistant.wrap(str, f, width);
	}
	
	@Override
	public void render() {
		glColor3f(1, 1, 1);
		int h = FontAssistant.getFont(f).getHeight();
		for (int i = 0; i < lines.length; i++) {
			if (centered) {
				int tx = FontAssistant.getFont(f).getWidth(lines[i]);
				int mx = width / 2 - tx / 2;
				RenderAssistant.renderText(x + mx, y + 16 + 50 + i * h, lines[i], f);
			} else {
				RenderAssistant.renderText(x, y + 16 + 50 + i * h, lines[i], f);
			}
		}
	}
	
	@Override
	public void onTick() {}
}
