package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector4f;

import com.vloxlands.util.RenderAssistant;


/**
 * @author Dakror
 */
public class ImageButton extends ClickableGui {
	public boolean stackTexture;
	public int texW, texH;
	int flag;
	public Vector4f disabledColor, activeColor, hoverColor;
	
	public ImageButton(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		stackTexture = false;
		texW = 0;
		texH = 0;
		disabledColor = activeColor = hoverColor = new Vector4f(1, 1, 1, 1);
	}
	
	@Override
	public void render() {
		glEnable(GL_BLEND);
		if (!enabled) glColor4f(disabledColor.x, disabledColor.y, disabledColor.z, disabledColor.w);
		RenderAssistant.bindTexture(texture);
		if (!stackTexture) RenderAssistant.renderRect(x, y, width, height);
		else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			RenderAssistant.renderRect(x, y, width, height, 0, 0, width / (float) texW, height / (float) texH);
		}
		glDisable(GL_BLEND);
		glColor4f(1, 1, 1, 1);
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag) {
		if (!enabled) return;
		this.flag = flag;
		if (flag == 2 && clickEvent != null) clickEvent.trigger();
	}
	
	@Override
	public void onTick() {}
	
}
