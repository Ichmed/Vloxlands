package com.vloxlands.game.entity;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.DamageType;
import com.vloxlands.game.Game;
import com.vloxlands.game.voxel.Voxel;
import com.vloxlands.game.world.Island;

public abstract class Entity
{
	
	public Island island = null;
	
	public Vector3f pos = new Vector3f();
	public Vector3f velocity = new Vector3f();

	public boolean affectedByGravity = true;
	public boolean canMove = true;
	public boolean takeFallDmg = true;
	private boolean canDie = true;
	
	int health = 100;

	
	public void onTick()
	{
		this.onUpdate();
		if(affectedByGravity) this.applyGravity();
		if(canMove) this.move();
		if(canDie && this.health <= 0) this.die();
	}

	private void move()
	{
		this.pos.translate(velocity.x, velocity.y, velocity.z);
	}

	private void applyGravity()
	{
		if(this.island == null || this.island.getVoxelId((int)this.pos.x, (int)this.pos.y - 1, (int)this.pos.z) == Voxel.get("AIR").getId())
			this.velocity.y -= 0.002f;
		else
		{
			if(takeFallDmg)this.onImpact();
			this.velocity.y = 0;
		}
	}

	protected void onImpact()
	{
		//TODO: calculate fallDamage
	}	

	public abstract void onUpdate();
	
	public void hurt(int strength, DamageType d)
	{
		//TODO: add resistances/weaknesses
		this.health -= strength;
	}
	
	private void die()
	{
		this.onDeath();
		//TODO: remove entity from entity-list
	}
	
	protected abstract void onDeath();
}
