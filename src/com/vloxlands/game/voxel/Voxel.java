package com.vloxlands.game.voxel;

public abstract class Voxel
{
	private static Voxel[] voxelList = new Voxel[256];
	
	
	public static Voxel getVoxelForId(int id)
	{
		return voxelList[id];
	}
	
	public abstract void onTick(int x, int y, int z);
	public abstract void onNeighbourChange(int x, int y, int z);
	public abstract void onPlaced(int x, int y, int z);
	public abstract void onRemoved(int x, int y, int z);
	public abstract void onDestroyed(int x, int y, int z);
	public abstract void onDestroyedByExplosion(int x, int y, int z);
}