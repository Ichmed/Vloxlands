package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.gen.IslandGenerator;
import com.vloxlands.ui.IClickEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.ProgressBar;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.RenderAssistant;


public class SceneNewGame extends Scene
{
	ProgressBar progress;
	
	@Override
	public void init()
	{
		setBackground();
		
		Label header = new Label(0, 0, Display.getWidth(), 60, "Neues Spiel");
		header.font = header.font.deriveFont(60f);
		
		content.add(header);
		
		progress = new ProgressBar(Display.getWidth() / 2, Display.getHeight() / 2 - ProgressBar.HEIGHT / 2, 400, 0, true);
		progress.setVisible(false);
		content.add(progress);
		
		TextButton skip = new TextButton(Display.getWidth() - TextButton.WIDTH / 2, Display.getHeight() - TextButton.HEIGHT, "Weiter");
		skip.setClickEvent(new IClickEvent()
		{
			
			@Override
			public void onClick()
			{
				Game.currentMap.islandGenerator = new IslandGenerator();
				lockScene();
				progress.setVisible(true);
			}
		});
		content.add(skip);
	}
	
	@Override
	public void update()
	{
		super.update();
		glPushMatrix();
		{
			RenderAssistant.renderOutline(-15, 85, Display.getWidth() + 30, 1, false);
		}
		glPopMatrix();
		glPushMatrix();
		{
			glEnable(GL_BLEND);
			glColor4f(0.4f, 0.4f, 0.4f, 0.6f);
			glBindTexture(GL_TEXTURE_2D, 0);
			RenderAssistant.renderRect(5, 120, Display.getWidth() - TextButton.WIDTH - 100, Display.getHeight() - 250);
			glColor4f(1, 1, 1, 1);
			
			glBindTexture(GL_TEXTURE_2D, RenderAssistant.textures.get("/graphics/textures/ui/gui.png").getTextureID());
			RenderAssistant.renderOutline(0, 115, Display.getWidth() - TextButton.WIDTH - 90, Display.getHeight() - 240, true);
			glDisable(GL_BLEND);
		}
		glPopMatrix();
		
		if (progress.isVisible())
		{
			glEnable(GL_BLEND);
			glColor4f(0.4f, 0.4f, 0.4f, 0.6f);
			glBindTexture(GL_TEXTURE_2D, 0);
			RenderAssistant.renderRect(0, 0, Display.getWidth(), Display.getHeight());
			glColor4f(1, 1, 1, 1);
			progress.setValue(Game.currentMap.islandGenerator.progress);
			progress.render();
			glDisable(GL_BLEND);
		}
		
		if (Game.currentMap.islandGenerator != null && Game.currentMap.islandGenerator.finishedIsland != null) Game.currentGame.setScene(new SceneGame());
	}
}
