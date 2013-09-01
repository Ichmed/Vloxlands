package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.CFG;
import com.vloxlands.ui.Label;


public class Scene_Logo extends Scene
{
	float alpha;
	boolean update;
	
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
			
		}
		update = false;
	}
	
	@Override
	public void update()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float val = (float) (0.5 * Math.sin(alpha - 0.5 * Math.PI) + 0.5);
		if (alpha >= Math.PI * 2)
		{
			content.get(0).setVisible(false);
			content.get(1).setVisible(true);
		}
		glColor4f(1, 1, 1, val);
		super.update();
		glDisable(GL_BLEND);
		alpha += 0.05f;
		if (alpha >= Math.PI * 4) Game.currentGame.setScene(new Scene_Mainmenu());
	}
}
