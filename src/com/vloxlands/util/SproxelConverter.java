package com.vloxlands.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import com.vloxlands.render.VoxelFace;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;
import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class SproxelConverter
{
	static class Block
	{
		int x, y, z;
		long c;
		Color color;
		
		Block(int x, int y, int z, long c)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.c = c;
			color = loadColor(c);
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		JFileChooser jfc = new JFileChooser("C:/");
		jfc.setFileFilter(new FileNameExtensionFilter("Sproxel CSV Files (*.csv)", "csv"));
		jfc.setMultiSelectionEnabled(false);
		jfc.setFileHidingEnabled(false);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		switch (jfc.showOpenDialog(null))
		{
			case JFileChooser.APPROVE_OPTION:
			{
				convertFile(jfc.getSelectedFile());
				break;
			}
		}
	}
	
	public static void convertFile(File f)
	{
		CSVReader csv = new CSVReader(f);
		csv.sep = ",";
		String[] d = csv.readRow();
		Block[][][] blocks = new Block[Integer.parseInt(d[0])][Integer.parseInt(d[1])][Integer.parseInt(d[2])];
		
		String cell;
		int index = 0;
		
		CFG.p("> reading cell data");
		
		
		while ((cell = csv.readNext()) != null)
		{
			int x = index / (blocks.length * blocks[0].length);
			int y = index / blocks.length % blocks[0].length;
			int z = index % blocks[0][0].length;
			try
			{
				blocks[x][y][z] = new Block(x, y, z, parseLong(cell));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			index++;
		}
		
		CFG.p("> generating faces");
		HashMap<VoxelFaceKey, VoxelFace>[] faces = generateFaces(blocks);
		
		CFG.p("> running greedy meshing");
		HashMap<VoxelFaceKey, VoxelFace> meshes = generateGreedyMesh(faces[0], blocks.length, blocks[0].length, blocks[0][0].length);
		
		CFG.p("> saving obj file");
		saveOBJ(new File(f.getPath().replace(".csv", ".obj")), blocks.length, blocks[0].length, blocks[0][0].length, meshes);
		CFG.p("> DONE");
	}
	
	public static long parseLong(String hex)
	{
		hex = hex.toLowerCase().replace("0x", "").replace("#", "");
		
		long value = 0;
		for (int i = 0; i < hex.length(); i++)
		{
			value += Math.pow(16, hex.length() - i - 1) * Integer.parseInt(hex.substring(i, i + 1), 16);
		}
		
		return value;
	}
	
	public static Color loadColor(long value)
	{
		long r = (value & 0xFF000000) >> 24;
		long g = (value & 0x00FF0000) >> 16;
		long b = (value & 0x0000FF00) >> 8;
		long a = (value & 0x000000FF);
		
		return new Color((int) r, (int) g, (int) b, (int) a);
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<VoxelFaceKey, VoxelFace>[] generateFaces(Block[][][] blocks)
	{
		HashMap<VoxelFaceKey, VoxelFace> faces = new HashMap<>();
		HashMap<VoxelFaceKey, VoxelFace> transparentFaces = new HashMap<>();
		for (int x = 0; x < blocks.length; x++)
		{
			for (int y = 0; y < blocks[x].length; y++)
			{
				for (int z = 0; z < blocks[x][y].length; z++)
				{
					Color v = blocks[x][y][z].color;
					for (Direction d : Direction.values())
					{
						int x1 = x + (int) d.dir.x;
						int y1 = y + (int) d.dir.y;
						int z1 = z + (int) d.dir.z;
						Color w = null;
						if (x1 >= 0 && y1 >= 0 && z1 >= 0 && x1 < blocks.length && y1 < blocks[x].length && z1 < blocks[x][y].length) w = blocks[x1][y1][z1].color;
						
						if (w == null || w.a != 1 && !w.equals(v))
						{
							VoxelFace f = new VoxelFace(d, new Vector3f(x, y, z), blocks[x][y][z].c);
							if (v.a == 1 || w == null) faces.put(new VoxelFaceKey(x, y, z, d.ordinal()), f);
							else transparentFaces.put(new VoxelFaceKey(x, y, z, d.ordinal()), f);
						}
					}
				}
			}
		}
		
		return new HashMap[] { faces, transparentFaces };
	}
	
	public static HashMap<VoxelFaceKey, VoxelFace> generateGreedyMesh(HashMap<VoxelFaceKey, VoxelFace> originalMap, int width, int height, int depth)
	{
		HashMap<VoxelFaceKey, VoxelFace> strips0 = new HashMap<>();
		
		if (originalMap.size() == 0) return originalMap;
		
		// greedy-mode along Z - axis
		for (int posX = 0; posX < width; posX++)
		{
			for (int posY = 0; posY < height; posY++)
			{
				VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
				for (int posZ = 0; posZ < depth; posZ++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						VoxelFaceKey key = new VoxelFaceKey(posX, posY, posZ, i);
						VoxelFace val = originalMap.get(key);
						
						if (activeStrips[i] != null)
						{
							if (val == null)
							{
								strips0.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
								activeStrips[i] = null;
							}
							else if (val.textureIndex == activeStrips[i].textureIndex)
							{
								activeStrips[i].increaseSize(0, 0, 1);
							}
							else
							{
								strips0.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
								activeStrips[i] = new VoxelFace(Direction.values()[i], new Vector3f(posX, posY, posZ), val.textureIndex);
							}
						}
						else if (val != null)
						{
							activeStrips[i] = new VoxelFace(Direction.values()[i], new Vector3f(posX, posY, posZ), val.textureIndex);
						}
					}
				}
				for (int i = 0; i < activeStrips.length; i++)
					if (activeStrips[i] != null) strips0.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
			}
		}
		
		HashMap<VoxelFaceKey, VoxelFace> strips1 = new HashMap<>();
		
		// greedy-mode along X - axis
		for (int posY = 0; posY < height; posY++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int posZ = 0; posZ < depth; posZ++)
			{
				for (int posX = 0; posX < width; posX++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						
						VoxelFaceKey key = new VoxelFaceKey(posX, posY, posZ, i);
						VoxelFace val = strips0.get(key);
						
						if (val != null)
						{
							if (activeStrips[i] == null)
							{
								activeStrips[i] = new VoxelFace(val);
							}
							else
							{
								if (val.textureIndex == activeStrips[i].textureIndex && val.sizeZ == activeStrips[i].sizeZ && val.pos.z == activeStrips[i].pos.z)
								{
									activeStrips[i].increaseSize(1, 0, 0);
								}
								else
								{
									strips1.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
									
									activeStrips[i] = new VoxelFace(val);
								}
							}
						}
						else if (activeStrips[i] != null)
						{
							strips1.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
							activeStrips[i] = null;
						}
					}
				}
			}
			for (int i = 0; i < activeStrips.length; i++)
				if (activeStrips[i] != null) strips1.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
		}
		
		HashMap<VoxelFaceKey, VoxelFace> strips2 = new HashMap<>();
		
		// greedy-mode along Y - axis
		for (int posX = 0; posX < width; posX++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int posZ = 0; posZ < depth; posZ++)
			{
				for (int posY = 0; posY < height; posY++)
				{
					for (int i = 0; i < activeStrips.length; i++)
					{
						
						VoxelFaceKey key = new VoxelFaceKey(posX, posY, posZ, i);
						VoxelFace val = strips1.get(key);
						
						if (val != null)
						{
							if (activeStrips[i] == null)
							{
								activeStrips[i] = new VoxelFace(val);
							}
							else
							{
								if (val.textureIndex == activeStrips[i].textureIndex && val.sizeZ == activeStrips[i].sizeZ && val.sizeX == activeStrips[i].sizeX && val.pos.x == activeStrips[i].pos.x && val.pos.z == activeStrips[i].pos.z)
								{
									activeStrips[i].increaseSize(0, 1, 0);
								}
								else
								{
									strips2.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
									
									activeStrips[i] = new VoxelFace(val);
								}
							}
						}
						else if (activeStrips[i] != null)
						{
							strips2.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
							activeStrips[i] = null;
						}
					}
				}
			}
			for (int i = 0; i < activeStrips.length; i++)
				if (activeStrips[i] != null) strips2.put(new VoxelFaceKey(activeStrips[i]), activeStrips[i]);
		}
		
		return strips2;
	}
	
	public static void saveOBJ(File f, int width, int height, int depth, HashMap<VoxelFaceKey, VoxelFace> meshes)
	{
		try
		{
			String br = "\r\n";
			String mtlTemplate = "newmtl mtl%i%" + br //
					+ "illum 4" + br //
					+ "Kd %r% %g% %b% %a%" + br //
					+ "Ka 0.00 0.00 0.00" + br //
					+ "Tf 1.00 1.00 1.00" + br //
					+ "Ni 1.00" + br + br;
			
			BufferedWriter obj = new BufferedWriter(new FileWriter(f));
			BufferedWriter mtl = new BufferedWriter(new FileWriter(new File(f.getPath().replace(".obj", ".mtl"))));
			
			obj.write("mtllib " + f.getName().replace(".obj", ".mtl") + br);
			
			ArrayList<Vector3f> vertices = new ArrayList<>();
			ArrayList<Color> materials = new ArrayList<>();
			
			CFG.p("  > sorting vertices & materials");
			
			for (VoxelFace vf : meshes.values())
			{
				if (!materials.contains(loadColor(vf.textureIndex))) materials.add(loadColor(vf.textureIndex));
				
				if (!vertices.contains(vf.bl)) vertices.add(vf.bl);
				if (!vertices.contains(vf.br)) vertices.add(vf.br);
				if (!vertices.contains(vf.tl)) vertices.add(vf.tl);
				if (!vertices.contains(vf.tr)) vertices.add(vf.tr);
			}
			
			CFG.p("  > writing vertices");
			
			for (Vector3f v : vertices)
				obj.write("v " + (v.x / width) + " " + (v.y / height) + " " + (v.z / depth) + br);
			
			CFG.p("  > writing materials");
			
			for (int i = 0; i < materials.size(); i++)
			{
				Color c = materials.get(i);
				mtl.write(mtlTemplate.replace("%i%", "" + i).replace("%r%", "" + c.r).replace("%g%", "" + c.g).replace("%b%", "" + c.b).replace("%a%", "" + c.a));
			}
			
			obj.write(br);
			
			ArrayList<VoxelFace> values = new ArrayList<>(meshes.values());
			
			CFG.p("  > writing faces");
			
			for (int i = 0; i < values.size(); i++)
			{
				VoxelFace face = values.get(i);
				
				obj.write("usemtl mtl" + materials.indexOf(loadColor(face.textureIndex)) + br);
				obj.write("f " + (vertices.indexOf(face.bl) + 1) + " " + (vertices.indexOf(face.br) + 1) + " " + (vertices.indexOf(face.tl) + 1) + " " + (vertices.indexOf(face.tr) + 1) + br);
			}
			
			
			obj.close();
			mtl.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
