package com.vloxlands.game.util;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;

public class Camera {
	public Vector3f position = new Vector3f();
	public Vector3f rotation = new Vector3f();
	
	public Vector3f getPosition() {
		return new Vector3f(position.x, position.y, position.z);
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rotation.x, rotation.y, rotation.z);
	}
	
	public void setPosition(Vector3f v) {
		this.setPosition(v.x, v.y, v.z);
	}
	
	public void setPosition(float x, float y, float z) {
		position = new Vector3f(x, y, z);
	}
	
	public void setRotation(Vector3f v) {
		this.setRotation(v.x, v.y, v.z);
	}
	
	public void setRotation(float x, float y, float z) {
		rotation = new Vector3f(x, y, z);
	}
	
	public void move(float x, float y, float z) {
		float speed = Game.currentGame.cameraSpeed;
		position.translate(x * speed, y * speed, z * speed);
	}
	
	public void move(double d, double y, double e) {
		this.move((float) d, (float) y, (float) e);
	}
	
	public void move(Vector3f v) {
		this.move(v.x, v.y, v.z);
	}
	
	public void rotate(float x, float y, float z) {
		rotation.translate(x, y, z);
	}
}
