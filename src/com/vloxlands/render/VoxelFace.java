package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.Direction;
import com.vloxlands.util.MathHelper;
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
		
//		if (dir != Direction.WEST && dir != Direction.EAST) rotate();
	}
	
	private void rotate()
	{
		CFG.p(dir);
		float theta = (float) Math.acos(Vector3f.dot(Direction.WEST.dir, dir.dir));
		float c = (float) Math.cos(theta);
		
		Vector3f axis = (Vector3f) Vector3f.cross(Direction.WEST.dir, dir.dir, null).normalise();
		float s = (float) Math.sin(theta);
		float C = 1 - c;
		
		Matrix3f m = new Matrix3f();
		m.loadTranspose(FloatBuffer.wrap(new float[] { //
		axis.x * axis.x * C + c /*    */, axis.x * axis.y * C - axis.z * s /*   */, axis.x * axis.z * C + axis.y * s, //
		axis.y * axis.x * C + axis.z * s, axis.y * axis.y * C + c /*            */, axis.y * axis.z * C - axis.x * s, //
		axis.z * axis.x * C - axis.y * s, axis.z * axis.y * C + axis.x * s /*   */, axis.z * axis.z * C + c //
		}));
		
		tl = MathHelper.vector3fDotMatrix3f(tl, m);
		tr = MathHelper.vector3fDotMatrix3f(tr, m);
		bl = MathHelper.vector3fDotMatrix3f(bl, m);
		br = MathHelper.vector3fDotMatrix3f(br, m);
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
