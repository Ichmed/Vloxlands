package com.vloxlands.util.math;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public abstract class AABB {
	public static class Box extends AABB {
		public Box(Vector3f min, Vector3f max) {
			super(min, max);
		}
	};
	
	public AABB parent;
	public Vector3f min;
	protected Vector3f max;
	
	public boolean cubic;
	
	public AABB() {
		min = max = new Vector3f(0, 0, 0);
	}
	
	public AABB(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
	}
	
	public AABB(Vector3f pos, int width, int height, int depth) {
		this(pos, Vector3f.add(pos, new Vector3f(width, height, depth), null));
	}
	
	public boolean inViewFrustum() {
		Vector3f center = getCenter();
		
		AABB full = getFullAABB();
		
		center = Vector3f.add(full.min, center, null);
		
		return Game.frustum.sphereInFrustum(center.x, center.y, center.z, getDiameter() * (cubic ? (float) Math.sqrt(2) / 2f : 1));
	}
	
	public AABB getFullAABB() {
		Vector3f min = new Vector3f(this.min), max = new Vector3f(this.max);
		AABB ref = this;
		while (ref.parent != null) {
			Vector3f.add(min, ref.parent.min, min);
			Vector3f.add(max, ref.parent.min, max);
			
			ref = ref.parent;
		}
		
		return new Box(min, max);
	}
	
	public float getDiameter() {
		return Vector3f.sub(max, min, null).length();
	}
	
	public Vector3f getCenter() {
		return (Vector3f) Vector3f.sub(max, min, null).scale(0.5f);
	}
	
	public Vector3f getSize() {
		return Vector3f.sub(max, min, null);
	}
	
	public float intersects() {
		return MathHelper.intersects(Game.pickingRay, getFullAABB());
	}
	
	public void render() {
		AABB full = getFullAABB();
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		float malus = 0.01f;
		
		RenderAssistant.renderCuboid(full.min.x - malus, full.min.y - malus, full.min.z - malus, full.getSize().x + malus * 2, full.getSize().y + malus * 2, full.getSize().z + malus
				* 2);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
}
