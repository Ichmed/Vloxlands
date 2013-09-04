package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.TextureImpl;

import com.vloxlands.game.Game;
import com.vloxlands.settings.Tr;
import com.vloxlands.util.RenderAssistant;


public class FlagButton extends ClickableGui
{
	public static final int SIZE = 48;
	
	String lang;
	
	public FlagButton(int x, int y, final String lang)
	{
		this.x = x;
		this.y = y;
		this.lang = lang;
		width = SIZE;
		height = SIZE;
		
		clickEvent = new IGuiEvent()
		{
			@Override
			public void activate()
			{
				Tr.loadLanguage(lang);
				Game.currentGame.updateViewport();
			}
		};
	}
	
	@Override
	public void render()
	{
		glEnable(GL_BLEND);
		RenderAssistant.bindTexture("/graphics/textures/lang/" + lang + ".png");
		if (!enabled) glColor3f(0.8f, 0.8f, 0.8f);
		else glColor3f(1, 1, 1);
		RenderAssistant.renderRect(x, y, width, height, 0, 0, 0.99f, 0.99f);
		TextureImpl.bindNone();
		glDisable(GL_BLEND);
	}
	
	@Override
	public void onTick()
	{}
	
	@Override
	public void handleMouse(int posX, int posY, int flag)
	{
		if (!enabled) return;
		if ((flag & 2) != 0) clickEvent.activate();
	}
}
