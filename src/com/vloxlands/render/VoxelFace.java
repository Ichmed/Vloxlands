package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix3f;
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
	Vector3f tl = new Vector3f(0, 1, 0), tr = new Vector3f(1, 1, 0), bl = new Vector3f(0, 0, 0), br = new Vector3f(1, 0, 0);
	
	public VoxelFace(Direction dir, Vector3f pos, Voxel v)
	{
		super();
		this.dir = dir;
		this.pos = pos;
		this.textureIndex = v.getTextureIndex();
		
		rotate();
	}
	
	public void rotate()
	{
		Vector3f v = Direction.getNeededRotation(Direction.EAST, dir);
		
		Matrix3f rotX = new Matrix3f();
		rotX.loadTranspose(FloatBuffer.wrap(new float[] { 1, 0, 0, 0, (float) Math.cos(v.x), (float) -Math.sin(v.x), 0, (float) Math.sin(v.x), (float) Math.cos(v.x) }));
		
		Matrix3f rotY = new Matrix3f();
		rotY.loadTranspose(FloatBuffer.wrap(new float[] { (float) Math.cos(v.y), 0, (float) Math.sin(v.y), 0, 1, 0, (float) -Math.sin(v.y), 0, (float) Math.cos(v.y) }));
		
		Matrix3f rotZ = new Matrix3f();
		rotZ.loadTranspose(FloatBuffer.wrap(new float[] { (float) Math.cos(v.z), (float) -Math.sin(v.z), 0, (float) Math.sin(v.z), (float) Math.cos(v.z), 0, 0, 0, 1 }));
		
		
	}
	
	public void render()
	{
		int texX = textureIndex % 32;
		int texY = textureIndex / 32;
		
		double squareSize = 0.03125d;
		
		RenderAssistant.bindTexture("graphics/textures/voxelTextures.png");
		
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
				glNormal3d(0, 0, -1);
				glVertex3f(tl.x, tl.y, tl.z);
				
				glTexCoord2d((texX + 1) * squareSize, (texY + 1) * squareSize);
				glNormal3d(0, 0, -1);
				glVertex3f(tr.x, tr.y, tr.z);
				
				glTexCoord2d((texX + 1) * squareSize, texY * squareSize);
				glNormal3d(0, 0, -1);
				glVertex3f(br.x, br.y, br.z);
				
				glTexCoord2d(texX * squareSize, texY * squareSize);
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
