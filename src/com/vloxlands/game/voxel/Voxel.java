package com.vloxlands.game.voxel;

public class Voxel
{
	public static final Voxel AIR = new Voxel(0).setName("Air").setTextureIndex(cTI(0, 0)).setOpaque(false).setWeight(0);
	public static final Voxel STONE = new Voxel(-127).setName("Stone").setTextureIndex(cTI(1, 0)).setSmoothness(0.1f);
	public static final Voxel DIRT = new Voxel(-126).setName("Dirt").setTextureIndex(cTI(2, 1)).setSmoothness(0.3f);
	public static final Voxel WEAK_CRYSTAL = new Voxel(-125).setName("Weak Crystal").setTextureIndex(cTI(3, 0)).setOpaque(false).setWeight(0).setUplift(1).setBrightness(0.2f);
	public static final Voxel MEDIUM_CRYSTAL = new Voxel(-124).setName("Medium Crystal").setTextureIndex(cTI(3, 1)).setOpaque(false).setWeight(0).setUplift(5).setBrightness(0.5f);
	public static final Voxel STRONG_CRYSTAL = new Voxel(-123).setName("Strong Crystal").setTextureIndex(cTI(3, 2)).setOpaque(false).setWeight(0).setUplift(20).setBrightness(0.7f);
	public static final Voxel GRASS = new Voxel(-122).setName("Grass").setTextureIndex(cTI(2, 0)).setSmoothness(0.5f);
	public static final Voxel COAL_ORE = new Voxel(-122).setName("Coal Ore").setTextureIndex(cTI(5, 0)).setSmoothness(0.5f);
	public static final Voxel IRON_ORE = new Voxel(-122).setName("Iron Ore").setTextureIndex(cTI(5, 1)).setSmoothness(0.5f);
	
	private static Voxel[] voxelList = new Voxel[256];
	
	private String name = "NA";
	int textureIndex = 0;
	boolean opaque = true;
	boolean replaceable = false;
	float smoothness = 0;
	private byte id;
	private float weight = 1f;
	private float uplift = 0;
	private float brightness;
	
	public Voxel(int id)
	{
		if (voxelList[id + 128] == null) voxelList[id + 128] = this;
		else
		{
			System.err.println("[Voxel]: The ID " + id + " was already taken up by \"" + voxelList[id + 128].name + "\"");
			System.exit(1);
		}
		this.id = (byte) id;
	}
	
	public static Voxel getVoxelForId(byte id)
	{
		return voxelList[(int) id + 128];
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
		return false;
	}
	
	public void setReplaceable(boolean replaceable)
	{
		this.replaceable = replaceable;
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
	
	public float getSmoothness()
	{
		return smoothness;
	}
	
	public Voxel setSmoothness(float smooth)
	{
		this.smoothness = smooth;
		return this;
	}
	
	public byte getId()
	{
		return id;
	}
	
	public float getWeight()
	{
		return weight;
	}
	
	public Voxel setWeight(float weight)
	{
		this.weight = weight;
		return this;
	}
	
	public float getUplift()
	{
		return uplift;
	}
	
	public Voxel setUplift(float uplift)
	{
		this.uplift = uplift;
		return this;
	}
	
	public float getBrightness()
	{
		return brightness;
	}
	
	public Voxel setBrightness(float brightness)
	{
		this.brightness = brightness;
		return this;
	}
	
	public static int cTI(int x, int y)
	{
		return y * 32 + x;
	}
}
