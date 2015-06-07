package com.vloxlands.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TextureAtlas {
	Texture[][] tiles;
	
	public TextureAtlas(String path, int cw, int ch) {
		try {
			BufferedImage bi = ImageIO.read(new File("src/" + path));
			tiles = new Texture[bi.getWidth() / cw][bi.getHeight() / ch];
			for (int i = 0; i < bi.getWidth() / cw; i++) {
				for (int j = 0; j < bi.getHeight() / ch; j++) {
					Texture t = BufferedImageUtil.getTexture(path, bi.getSubimage(i * cw, j * ch, cw, ch));
					RenderAssistant.improveTexture(t);
					tiles[i][j] = t;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Texture getTile(int x, int y) {
		return tiles[x][y];
	}
}
