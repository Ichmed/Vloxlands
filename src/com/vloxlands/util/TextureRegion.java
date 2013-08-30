package com.vloxlands.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TextureRegion
{
	int x, y, width, height;
	String file;
	
	public TextureRegion(String file, int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.file = file;
		
		loadTexture();
	}
	
	public Texture loadTexture()
	{
		try
		{
			BufferedImage bi = ImageIO.read(new File(file));
			return BufferedImageUtil.getTexture(file, bi.getSubimage(x, y, width, height));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof TextureRegion)) return false;
		
		TextureRegion t = (TextureRegion) o;
		
		return file.equals(t.file) && x == t.x && y == t.y && width == t.width && height == t.height;
	}
}
