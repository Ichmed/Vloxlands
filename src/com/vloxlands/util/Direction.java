package com.vloxlands.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public enum Direction {
	NORTH(1, 0, 0, 0, -90, 0),
	
	UP(0, 1, 0, 90, 0, 0),
	
	EAST(0, 0, -1, 0, 0, 0),
	
	SOUTH(-1, 0, 0, 0, 90, 0),
	
	DOWN(0, -1, 0, -90, 0, 0),
	
	WEST(0, 0, 1, 0, 180, 0);
	
	
	public Vector3f dir;
	Vector3f rot;
	
	Direction(int x, int y, int z, int rotX, int rotY, int rotZ) {
		dir = new Vector3f(x, y, z);
		rot = new Vector3f(rotX, rotY, rotZ);
	}
	
	public static Vector3f getNeededRotation(Direction a, Direction b) {
		return new Vector3f(b.rot.x - a.rot.x, b.rot.y - a.rot.y, b.rot.z - a.rot.z);
	}
	
	public static List<Direction> get90DegreeDiretions(Direction d) {
		List<Direction> l = new ArrayList<>();
		for (Direction e : Direction.values())
			if (e.ordinal() != d.ordinal() && e.ordinal() != (d.ordinal() + 3) % 6) l.add(e);
		System.out.println(l);
		return l;
	}
}
