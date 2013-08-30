package com.vloxlands.game.voxel;

import java.util.HashMap;

import com.vloxlands.util.CSVReader;

public class Voxel
{
	private static HashMap<String, Voxel> voxels = new HashMap<>();
	
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
	
	public String toString()
	{
		return getClass().getName() + "." + name.toUpperCase().replace(" ", "_");
	}
	
	public static Voxel get(String name)
	{
		return voxels.get(name);
	}
	
	public static void loadVoxels()
	{
		CSVReader csv = new CSVReader("data/voxels.csv");
		String[] categories = csv.readRow();
		String[] defaults = csv.readRow();
		String cell;
		Voxel voxel = null;
		while ((cell = csv.readNext()) != null)
		{
			if (csv.getIndex() == 0)
			{
				if (voxel != null)
				{
					voxels.put(voxel.getName().toUpperCase().replace(" ", "_"), voxel);
				}
				voxel = new Voxel(Integer.parseInt(cell) - 128);
			}
			
			switch (categories[csv.getIndex()])
			{
				case "Texture(x*y)":
				{
					if (cell.length() > 0) voxel.setTextureIndex(Integer.parseInt(cell.split("\\*")[1]) * 32 + Integer.parseInt(cell.split("\\*")[0]));
					else voxel.setTextureIndex(Integer.parseInt(defaults[csv.getIndex()].split("\\*")[1]) * 32 + Integer.parseInt(defaults[csv.getIndex()].split("\\*")[0]));
					break;
				}
				
				// -- Strings -- //
				case "Name":
				{
					if (cell.length() > 0) voxel.setName(cell);
					else voxel.setName(defaults[csv.getIndex()]);
					break;
				}
				
				// -- booleans -- //
				case "Opaque":
				{
					if (cell.length() > 0) voxel.setOpaque(cell.equals("1"));
					else voxel.setOpaque(defaults[csv.getIndex()].equals("1"));
					break;
				}
				
				// -- floats -- //
				case "Weight":
				case "Uplift":
				case "Brightness":
				case "Smoothness":
				{
					try
					{
						if (cell.length() > 0) voxel.getClass().getMethod("set" + categories[csv.getIndex()], Float.TYPE).invoke(voxel, Float.parseFloat(cell));
						else voxel.getClass().getMethod("set" + categories[csv.getIndex()], Float.TYPE).invoke(voxel, Float.parseFloat(defaults[csv.getIndex()]));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					break;
				}
			}
		}
		
		voxels.put(voxel.getName().toUpperCase().replace(" ", "_"), voxel);
	}
}
