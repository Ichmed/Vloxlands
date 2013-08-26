package com.vloxlands.render;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

import com.vloxlands.util.Direction;
import com.vloxlands.util.RenderAssistant;

public class Face
{
	Direction dir;
	Vector3f pos;
	int textureIndex;
	
	public Face(Direction dir, Vector3f pos, int textureIndex)
	{
		super();
		this.dir = dir;
		this.pos = pos;
		this.textureIndex = textureIndex;
	}

	public void render()
	{
		int texX = textureIndex % 32;
		int texY = textureIndex / 32;

		double squareSize = 0.03125d;

		RenderAssistant.bindTexture("textures/voxelTextures.png");
		
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
			
			glBegin(GL_QUADS);
			{
				glTexCoord2d(texX * squareSize, (texY + 1) * squareSize);
				glVertex3f(0, 1, 0);
				glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
				glVertex3f(1, 1, 0);
				glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
				glVertex3f(1, 0, 0);
				glTexCoord2d(texX * squareSize, texY * squareSize);
				glVertex3f(0, 0, 0);
			}
			glEnd();
		}
		glPopMatrix();
	}
}
