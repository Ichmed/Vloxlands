package com.vloxlands.util;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TextureAtlas
{
	Texture[][] tiles;
	
	public TextureAtlas(String path, int cw, int ch)
	{
		try
		{
			BufferedImage bi = ImageIO.read(new File(path));
			tiles = new Texture[bi.getWidth() / cw][bi.getHeight() / ch];
			for (int i = 0; i < bi.getWidth() / cw; i++)
			{
				for (int j = 0; j < bi.getHeight() / ch; j++)
				{
					Texture t = BufferedImageUtil.getTexture(path, bi.getSubimage(i * cw, j * ch, cw, ch));
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
					tiles[i][j] = t;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Texture getTile(int x, int y)
	{
		return tiles[x][y];
	}
}
