package com.vloxlands.util;

public enum Direction
{
	NORTH(1, 0, 0),
	UP(0, 1, 0),
	EAST(0, 0, 1),
	SOUTH(-1, 0, 0),
	DOWN(0, -1, 0),
	WEST(0, 0, -1);
	
	int x;
	int y;
	int z;
	
	Direction(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
