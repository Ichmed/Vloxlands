package com.vloxlands.game.voxel;

public class Voxel
{
	private static Voxel[] voxelList = new Voxel[256];
	
	private String name = "NA";
	int textureIndex = 0;
	boolean opaque = true;
	
	static
	{
		voxelList[0] = new VoxelAir().setName("Air").setTextureIndex(0).setOpaque(false);		
		voxelList[1] = new Voxel().setName("Stone").setTextureIndex(1);
	}
	
	public static Voxel getVoxelForId(int id)
	{
		return voxelList[id];
	}	

	public void onTick(int x, int y, int z)
	{
	}

	public void onNeighbourChange(int x, int y, int z)
	{
	}

	public void onPlaced(int x, int y, int z)
	{
	}

	public void onRemoved(int x, int y, int z)
	{
	}

	public void onDestroyed(int x, int y, int z)
	{
	}

	public void onDestroyedByExplosion(int x, int y, int z)
	{
	}
	
	public boolean isReplaceable()
	{
		return false;
	}
	
	public Voxel setName(String s)
	{
		if(this.name.equals("NA")) this.name = s;
		else System.err.println("[Voxel] [" + this.name + "] already has a name");
		return this;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getIdForName(String name)
	{
		for(int i = 0; i < voxelList.length; i++)
		{
			Voxel v = Voxel.getVoxelForId(i);
			if(v.getName().equals(name)) return i;
		}
		System.err.println("[Voxel] [" + this.name + "] not found");
		return -1;
	}
	
	public Voxel setTextureIndex(int i)
	{
		this.textureIndex = i;
		return this;
	}
	
	public int getTextureIndex()
	{
		return textureIndex;		
	}
	
	public Voxel setOpaque(boolean b)
	{
		this.opaque = b;
		return this;
	}

	public boolean isOpaque()
	{
		return opaque;
	}
}