package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Label extends IGuiElement {
	String title;
	public boolean stackTexture;
	boolean center;
	public Vector3f color;
	public int texW, texH;
	
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	public Label(int x, int y, int width, int height, String title) {
		this(x, y, width, height, title, true);
	}
	
	public Label(int x, int y, int width, int height, String title, boolean center) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = title;
		stackTexture = false;
		texW = 0;
		texH = 0;
		texture = null;
		this.center = center;
		color = new Vector3f(1, 1, 1);
	}
	
	@Override
	public void render() {
		glEnable(GL_BLEND);
		if (texture != null) {
			RenderAssistant.bindTexture(texture);
			if (!stackTexture) RenderAssistant.renderRect(x, y, width, height);
			else {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
				RenderAssistant.renderRect(x, y, width, height, 0, 0, width / (float) texW, height / (float) texH);
			}
		}
		glDisable(GL_BLEND);
		int tx = FontAssistant.getFont(font).getWidth(title);
		int mx = width / 2 - tx / 2;
		glColor3f(color.x, color.y, color.z);
		RenderAssistant.renderText(x + ((width > -1 && center) ? mx : 0), y + ((height > -1) ? height / 4f : 0), title, font);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public void onTick() {}
}
