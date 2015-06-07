package com.vloxlands.net.packet;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.entity.EntityBuilding;

/**
 * @author Dakror
 */
public class Packet10EntityBuilding extends Packet {
	EntityBuilding building;
	int islandIndex;
	
	public Packet10EntityBuilding(EntityBuilding building, int islandIndex) {
		super(10);
		this.building = building;
		this.islandIndex = islandIndex;
	}
	
	public Packet10EntityBuilding(byte[] b) {
		super(10);
		
		ByteBuffer bb = ByteBuffer.wrap(b, 1, b.length - 1);
		islandIndex = bb.getInt();
		
		Vector3f pos = new Vector3f(bb.getFloat(), bb.getFloat(), bb.getFloat());
		Vector3f size = new Vector3f(bb.getFloat(), bb.getFloat(), bb.getFloat());
		int nameLength = bb.getInt();
		
		byte[] name = new byte[nameLength];
		bb.get(name, 0, nameLength);
		building = new EntityBuilding(pos, size, new String(name));
	}
	
	public EntityBuilding getEntityBuilding() {
		return building;
	}
	
	public int getIslandIndex() {
		return islandIndex;
	}
	
	@Override
	protected byte[] getPacketData() {
		ByteBuffer bb = ByteBuffer.allocate(32 + building.modelName.getBytes().length);
		bb.putInt(islandIndex);
		bb.putFloat(building.pos.x);
		bb.putFloat(building.pos.y);
		bb.putFloat(building.pos.z);
		bb.putFloat(building.size.x);
		bb.putFloat(building.size.y);
		bb.putFloat(building.size.z);
		bb.putInt(building.modelName.getBytes().length);
		
		bb.put(building.modelName.getBytes());
		
		return bb.array();
	}
}
