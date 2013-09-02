package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.ui.IClickEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.RenderAssistant;


public class SceneNewGame extends Scene
{
	@Override
	public void init()
	{
		setBackground();
		
		Label header = new Label(0, 0, Display.getWidth(), 60, "Neues Spiel");
		header.font = header.font.deriveFont(60f);
		
		content.add(header);
		
		TextButton skip = new TextButton(Display.getWidth() - TextButton.WIDTH / 2, Display.getHeight() - TextButton.HEIGHT, "Weiter");
		skip.setClickEvent(new IClickEvent()
		{
			
			@Override
			public void onClick()
			{
				Game.currentGame.setScene(new SceneGame());
			}
		});
		content.add(skip);
	}
	
	@Override
	public void update()
	{
		super.update();
		RenderAssistant.renderOutline(-15, 80, Display.getWidth() + 30, 1, false);
		glEnable(GL_BLEND);
		glColor4f(0.4f, 0.4f, 0.4f, 0.6f);
		glBindTexture(GL_TEXTURE_2D, 0);
		RenderAssistant.renderRect(30, 100, Display.getWidth() / 5 * 3, Display.getHeight() / 5 * 4 - 100);
		glDisable(GL_BLEND);
		glColor3f(1, 1, 1);
		RenderAssistant.renderOutline(25, 95, Display.getWidth() / 5 * 3 + 10, Display.getHeight() / 5 * 4 - 90, true);
	}
}
