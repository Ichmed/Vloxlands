package com.vloxlands.game.entity;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.DamageType;
import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;
import com.vloxlands.render.IRendering;

public abstract class Entity implements IRendering {
	public Island island = null;
	
	public Vector3f pos = new Vector3f();
	public Vector3f velocity = new Vector3f();
	
	public boolean affectedByGravity = true;
	public boolean canMove = true;
	public boolean takeFallDmg = true;
	protected boolean canDie = true;
	
	int health = 100;
	
	public void onTick() {
		onUpdate();
		if (affectedByGravity) applyGravity();
		if (canMove) move();
		if (canDie && health <= 0) die();
	}
	
	private void move() {
		pos.translate(velocity.x, velocity.y, velocity.z);
	}
	
	private void applyGravity() {
		if (island == null || island.getVoxelId((int) pos.x, (int) pos.y - 1, (int) pos.z) == Voxel.get("AIR").getId()) velocity.y -= 0.002f;
		else {
			if (takeFallDmg) onImpact();
			velocity.y = 0;
		}
	}
	
	protected void onImpact() {
		// TODO: calculate fallDamage
	}
	
	public abstract void onUpdate();
	
	public void hurt(int strength, DamageType d) {
		// TODO: add resistances/weaknesses
		onHurt();
		health -= strength;
	}
	
	private void die() {
		onDeath();
		Game.currentMap.entities.remove(this);
	}
	
	protected abstract void onDeath();
	
	protected abstract void onHurt();
}
