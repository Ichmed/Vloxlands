package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.CFG;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.ProgressBar;
import com.vloxlands.util.ZipAssistant;


public class SceneLogo extends Scene
{
	ProgressBar download;
	float alpha;
	boolean update;
	ZipAssistant downloader;
	
	@Override
	public void init()
	{
		Label dakror = new Label(Display.getWidth() / 2 - 768 / 2, Display.getHeight() / 2 - 384 / 2, 768, 384, "");
		dakror.setTexture("/graphics/logo/dakror.png");
		content.add(dakror);
		
		Label ichmed = new Label(Display.getWidth() / 2 - 768 / 2, Display.getHeight() / 2 - 384 / 2, 768, 384, "Ichmed"); // just a placeholder
		ichmed.font = ichmed.font.deriveFont(55f);
		ichmed.setVisible(false);
		content.add(ichmed);
		
		alpha = 0;
		if (CFG.INTERNET)
		{
			try
			{
				downloader = new ZipAssistant();
				// if (MediaAssistant.needMediaUpdate("natives")) downloader.addDownload(new URL("http://dakror.de/vloxlands/GAMECONTENT/natives.zip"), new File(CFG.DIR, "natives"), true);
				
				if (downloader.hasDownloads())
				{
					download = new ProgressBar(Display.getWidth() / 2, Display.getHeight() - 40, Display.getWidth() - 40, 0, true);
					content.add(download);
					downloader.start();
					update = true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else update = false;
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		if (alpha >= Math.PI * 2)
		{
			content.get(0).setVisible(false);
			content.get(1).setVisible(true);
		}
		alpha += 0.05f;
		if (!update)
		{
			if (alpha >= Math.PI * 4 || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) Game.currentGame.setScene(new SceneLogin());
		}
		else
		{
			download.setValue(downloader.progress / (float) downloader.fullsize);
			// download.title = Assistant.formatBinarySize(downloader.progress, 2) + " / " + Assistant.formatBinarySize(downloader.fullsize, 2) + " @ " + Assistant.formatBinarySize(downloader.speed, 2) + "/s"; // want that?
			if (downloader.state.equals("Fertig")) Game.currentGame.setScene(new SceneMainMenu());
		}
	}
	
	@Override
	public void render()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float val = (float) (0.5 * Math.sin(alpha - 0.5 * Math.PI) + 0.5);
		glColor4f(1, 1, 1, val);
		
		super.renderContent();
		
		glDisable(GL_BLEND);
	}
}
