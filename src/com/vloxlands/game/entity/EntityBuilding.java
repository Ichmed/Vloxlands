package com.vloxlands.game.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import com.vloxlands.render.model.Model;
import com.vloxlands.render.util.ModelLoader;

public class EntityBuilding extends Entity {
	public String modelName;
	public Vector3f size;
	public Model model;
	
	public EntityBuilding(Vector3f pos, Vector3f size, String modelName) {
		this.pos = pos;
		this.size = size;
		this.modelName = modelName;
		model = ModelLoader.loadModel("/graphics/models/entity/building/" + modelName + "/" + modelName + ".obj");
		model.enablePlayerRecoloring(Color.red);
		canDie = false;
		affectedByGravity = false;
		canMove = false;
	}
	
	@Override
	public void render() {
		GL11.glTranslatef(pos.x, pos.y, pos.z);
		model.render();
	}
	
	@Override
	public void onUpdate() {}
	
	@Override
	protected void onDeath() {}
	
	@Override
	protected void onHurt() {}
}
