package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.glColor4f;

import org.lwjgl.opengl.Display;

import com.vloxlands.ui.Label;


public class Scene_Logo extends Scene
{
	float alpha;
	
	@Override
	public void init()
	{
		Label dakror = new Label(Display.getWidth() / 2 - 768 / 2, Display.getHeight() / 2 - 384 / 2, 768, 384, "");
		dakror.setTexture("/graphics/logo/dakror.png");
		content.add(dakror);
		
		alpha = 0;
	}
	
	@Override
	public void update()
	{
		// glEnable(GL_BLEND);
		glColor4f(1, 1, 1, 0.01f);
		super.update();
		// alpha += 0.000001f;
		
		// glDisable(GL_BLEND);
	}
}
