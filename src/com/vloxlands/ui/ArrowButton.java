package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.util.RenderAssistant;

public class ArrowButton extends ClickableGui {
	public static final Vector2f MINUS_HOR = new Vector2f(322, 5);
	public static final Vector2f PLUS_HOR = new Vector2f(360, 5);
	public static final Vector2f MINUS_VER = new Vector2f(419, 5);
	public static final Vector2f PLUS_VER = new Vector2f(457, 5);
	public static final Vector2f ARROW_L_HOR = new Vector2f(556, 5);
	public static final Vector2f ARROW_R_HOR = new Vector2f(595, 5);
	public static final Vector2f ARROW_U_VER = new Vector2f(653, 5);
	public static final Vector2f ARROW_D_VER = new Vector2f(691, 5);
	
	public static final int WIDTH = 38;
	public static final int HEIGHT = 52;
	
	Vector2f type;
	int texY;
	
	public ArrowButton(int x, int y, Vector2f type) {
		this.x = x;
		this.y = y;
		this.type = type;
		width = WIDTH;
		height = HEIGHT;
		texY = (int) this.type.y;
		texture = "/graphics/textures/ui/gui.png";
	}
	
	@Override
	public void onTick() {
		if (!enabled) texY = 161;
		else texY = (int) type.y;
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag) {
		if (!enabled) return;
		if ((flag & 1) != 0) texY = 57;
		else if ((flag & 2) != 0) clickEvent.trigger();
		else texY = 109;
	}
	
	@Override
	public void render() {
		RenderAssistant.disable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glColor3f(1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderAssistant.bindTexture(texture);
		
		RenderAssistant.renderRect(x, y, width, height, type.x / 1024.0f, texY / 1024.0f, WIDTH / 1024.0f, HEIGHT / 1024.0f);
	}
	
	public void setType(Vector2f type) {
		this.type = type;
	}
}
