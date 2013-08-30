package com.vloxlands.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
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
	Vector3f[] verts;
	Vector2f[] texVerts;
	
	public VoxelFace(Direction dir, Vector3f pos, Voxel v)
	{
		super();
		this.dir = dir;
		this.pos = pos;
		this.textureIndex = v.getTextureIndex();
		
		place();
	}
	
	public void place()
	{
		// -- vertices -- //
		verts = new Vector3f[] { new Vector3f(bl), new Vector3f(br), new Vector3f(tr), new Vector3f(tl) };
		switch (dir)
		{
			case WEST:
			{
				verts[3].z = 1;
				verts[2].z = 1;
				verts[0].z = 1;
				verts[1].z = 1;
				break;
			}
			case NORTH:
			{
				verts[3].z = 1;
				verts[3].x = 1;
				
				verts[0].z = 1;
				verts[0].x = 1;
				break;
			}
			case SOUTH:
			{
				verts[3].z = 1;
				verts[2].x = 0;
				
				verts[0].z = 1;
				verts[1].x = 0;
				break;
			}
			
			case UP:
			{
				verts[3].z = 1;
				verts[2].z = 1;
				
				verts[0].y = 1;
				verts[1].y = 1;
				break;
			}
			case DOWN:
			{
				verts[3].z = 1;
				verts[3].y = 0;
				verts[2].z = 1;
				verts[2].y = 0;
				break;
			}
			default:
				break;
		}
		verts[0].translate(pos.x, pos.y, pos.z);
		verts[1].translate(pos.x, pos.y, pos.z);
		verts[2].translate(pos.x, pos.y, pos.z);
		verts[3].translate(pos.x, pos.y, pos.z);
		
		// -- texture vertices -- //
		int texX = textureIndex % 32;
		int texY = textureIndex / 32;
		
		float squareSize = 0.03125f;
		texVerts = new Vector2f[] { new Vector2f(texX * squareSize, texY * squareSize), new Vector2f((texX + 1) * squareSize, texY * squareSize), new Vector2f((texX + 1) * squareSize, (texY + 1) * squareSize), new Vector2f(texX * squareSize, (texY + 1) * squareSize) };
	}
	
	public void render()
	{
		RenderAssistant.bindTexture("graphics/textures/voxelTextures.png");
		
		glPushMatrix();
		{
			glEnable(GL_BLEND);
			
			// glTranslatef(pos.x, pos.y, pos.z);
			
			Vector3f v = Direction.getNeededRotation(Direction.EAST, dir);
			glTranslatef(0.5f, 0.5f, 0.5f);
			glRotatef(v.x, 1, 0, 0);
			glRotatef(v.y, 0, 1, 0);
			glRotatef(v.z, 0, 0, 1);
			glTranslatef(-0.5f, -0.5f, -0.5f);
			glBegin(GL_QUADS);
			{
				glTexCoord2f(texVerts[3].x, texVerts[3].y);
				glNormal3d(0, 0, -1);
				glVertex3f(tl.x, tl.y, tl.z);
				
				glTexCoord2f(texVerts[2].x, texVerts[2].y);
				glNormal3d(0, 0, -1);
				glVertex3f(tr.x, tr.y, tr.z);
				
				glTexCoord2f(texVerts[1].x, texVerts[1].y);
				glNormal3d(0, 0, -1);
				glVertex3f(br.x, br.y, br.z);
				
				glTexCoord2f(texVerts[0].x, texVerts[0].y);
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
