package com.vloxlands.game.voxel;

public class Voxel
{
	public static enum VoxelType
	{
		AIR(0, 0, "Air", false, true),
		STONE(1, 1, "Stone", true, false),
		// TODO finish
		;
		
		private Voxel voxel;
		
		private VoxelType(int id, int tid, String name, boolean opaque, boolean replaceable)
		{
			this.voxel = new Voxel(id);
			this.voxel.setTextureIndex(tid);
			this.voxel.setName(name);
			this.voxel.setOpaque(opaque);
			this.voxel.setReplaceable(replaceable);
		}
		
		public Voxel getVoxel()
		{
			return this.voxel;
		}
	}
	
	private static Voxel[] voxelList = new Voxel[256];
	
	private String name = "NA";
	int textureIndex = 0;
	boolean opaque = true;
	boolean replaceable = false;
	
	// public static final Voxel air = new VoxelAir(0).setName("Air").setTextureIndex(0).setOpaque(false);
	// public static final Voxel stone = new Voxel(1).setName("Stone").setTextureIndex(1);
	// public static final Voxel dirt = new Voxel(2).setName("Dirt").setTextureIndex(2);
	// public static final Voxel crystall = new Voxel(3).setName("Crystall").setTextureIndex(3).setOpaque(false);
	// public static final Voxel grass = new Voxel(4).setName("Grass").setTextureIndex(4);
	
	public Voxel(int id)
	{
		if (voxelList[id] == null) voxelList[id] = this;
		else
		{
			System.err.println("[Voxel]: The ID " + id + " was already taken up by \"" + voxelList[id].name + "\"");
			System.exit(1);
		}
	}
	
	public synchronized static Voxel getVoxelForId(int id)
	{
		return voxelList[id];
	}
	
	public void onTick(int x, int y, int z)
	{}
	
	public void onNeighbourChange(int x, int y, int z)
	{}
	
	public void onPlaced(int x, int y, int z)
	{}
	
	public void onRemoved(int x, int y, int z)
	{}
	
	public void onDestroyed(int x, int y, int z)
	{}
	
	public void onDestroyedByExplosion(int x, int y, int z)
	{}
	
	public boolean isReplaceable()
	{
		return replaceable;
	}
	
	public void setReplaceable(boolean b)
	{
		replaceable = b;
	}
	
	public Voxel setName(String s)
	{
		if (this.name.equals("NA")) this.name = s;
		else System.err.println("[Voxel] [" + this.name + "] already has a name");
		return this;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getIdForName(String name)
	{
		for (int i = 0; i < voxelList.length; i++)
		{
			Voxel v = Voxel.getVoxelForId(i);
			if (v.getName().equals(name)) return i;
		}
		System.err.println("[Voxel] [" + this.name + "] not found");
		return -1;
	}
	
	public void setTextureIndex(int i)
	{
		this.textureIndex = i;
	}
	
	public int getTextureIndex()
	{
		return textureIndex;
	}
	
	public void setOpaque(boolean b)
	{
		this.opaque = b;
	}
	
	public boolean isOpaque()
	{
		return opaque;
	}
}
