package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.util.Direction;
import com.vloxlands.util.RenderAssistant;

public class VoxelFace
{
	Direction dir;
	Vector3f pos;
	int textureIndex;
	Vector3f tl, tr, bl, br;
	
	public VoxelFace(Direction dir, Vector3f pos, Voxel v)
	{
		this(dir, pos, v, 1, 1);
	}
	
	public VoxelFace(Direction dir, Vector3f pos, Voxel v, float sizeX, float sizeY)
	{
		super();
		this.dir = dir;
		this.pos = pos;
		this.textureIndex = v.getTextureIndex();
		tl = new Vector3f(0, sizeY, 0);
		tr = new Vector3f(sizeX, sizeY, 0);
		bl = new Vector3f(0, 0, 0);
		br = new Vector3f(sizeX, 0, 0);
	}
	
	public void render()
	{
		int texX = textureIndex % 32;
		int texY = textureIndex / 32;
		
		RenderAssistant.bindTextureAtlasTile("graphics/textures/voxelTextures.png", texX, texY);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glPushMatrix();
		{
			glEnable(GL_BLEND);
			
			glTranslatef(pos.x, pos.y, pos.z);
			
			Vector3f v = Direction.getNeededRotation(Direction.EAST, dir);
			glTranslatef(0.5f, 0.5f, 0.5f);
			glRotatef(v.x, 1, 0, 0);
			glRotatef(v.y, 0, 1, 0);
			glRotatef(v.z, 0, 0, 1);
			glTranslatef(-0.5f, -0.5f, -0.5f);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glBegin(GL_QUADS);
			{
				glTexCoord2d(0, 1);
				glNormal3d(0, 0, -1);
				glVertex3f(tl.x, tl.y, tl.z);
				
				glTexCoord2d(1, 1);
				glNormal3d(0, 0, -1);
				glVertex3f(tr.x, tr.y, tr.z);
				
				glTexCoord2d(1, 0);
				glNormal3d(0, 0, -1);
				glVertex3f(br.x, br.y, br.z);
				
				glTexCoord2d(0, 0);
				glNormal3d(0, 0, -1);
				glVertex3f(bl.x, bl.y, bl.z);
			}
			glEnd();
		}
		glPopMatrix();		
	}
	
	public double getDistanceToCamera()
	{
		return Vector3f.sub(Game.currentGame.camera.position, this.pos, null).length();
	}
}
