package com.vloxlands.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import com.vloxlands.render.VoxelFace;
import com.vloxlands.render.VoxelFace.VoxelFaceKey;
import com.vloxlands.settings.CFG;
import com.vloxlands.util.math.Vector;

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
			JFileChooser jfc = new JFileChooser(new File(SproxelConverter.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void packFilesInFolder(File csvFile)
	{
		String name = csvFile.getName().substring(0, csvFile.getName().lastIndexOf("."));
		if (csvFile.getParentFile().getName().equals(name))
		{
			CFG.p("  > already sorted");
			return;
		}
		File dir = new File(csvFile.getParentFile(), name);
		dir.mkdir();
		
		int sorted = 0;
		for (File f : csvFile.getParentFile().listFiles())
		{
			if (f.isDirectory()) continue;
			
			if (f.getName().startsWith(name + "."))
			{
				sorted++;
				f.renameTo(new File(dir, f.getName()));
			}
		}
		CFG.p("  > sorted " + sorted + " files");
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
	
	public static void convertFile(File csvFile)
	{
		CSVReader csv = new CSVReader(csvFile);
		csv.sep = ",";
		String[] d = csv.readRow();
		
		int width = Integer.parseInt(d[0]);
		int height = Integer.parseInt(d[1]);
		int depth = Integer.parseInt(d[2]);
		
		Block[][][] blocks = new Block[width][width][width];
		
		String cell;
		int index = 0;
		
		CFG.p("> reading cell data");
		
		if (width != height || width != depth || height != depth)
		{
			JOptionPane.showMessageDialog(null, "The Input Sproxel-CSV-Model has to have cubic dimensions! ", "Error!", JOptionPane.ERROR_MESSAGE);
			CFG.p("> ABORT");
			return;
		}
		
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
		
		System.gc();
		
		CFG.p("> generating faces");
		HashMap<VoxelFaceKey, VoxelFace> faces = generateFaces(blocks);
		
		CFG.p("  > got " + faces.size() + " faces");
		
		CFG.p("> running greedy meshing");
		HashMap<VoxelFaceKey, VoxelFace> meshes = generateGreedyMesh(faces, blocks.length);
		
		CFG.p("  > got " + meshes.size() + " meshes");
		
		CFG.p("> saving obj file");
		saveOBJ(new File(csvFile.getPath().replace(".csv", ".obj")), blocks.length, meshes);
		
		CFG.p("> sorting files");
		packFilesInFolder(csvFile);
		
		CFG.p("> DONE");
	}
	
	public static HashMap<VoxelFaceKey, VoxelFace> generateFaces(Block[][][] blocks)
	{
		HashMap<VoxelFaceKey, VoxelFace> faces = new HashMap<>();
		for (int x = 0; x < blocks.length; x++)
		{
			for (int y = 0; y < blocks[x].length; y++)
			{
				for (int z = 0; z < blocks[x][y].length; z++)
				{
					if (blocks[x][y][z].c == 0) continue;
					
					Color v = blocks[x][y][z].color;
					
					for (Direction d : Direction.values())
					{
						int x1 = x + (int) d.dir.x;
						int y1 = y + (int) d.dir.y;
						int z1 = z + (int) d.dir.z;
						
						boolean outOfBounds = !(x1 >= 0 && y1 >= 0 && z1 >= 0 && x1 < blocks.length && y1 < blocks[x].length && z1 < blocks[x][y].length);
						
						boolean filled = (outOfBounds) ? true : !blocks[x1][y1][z1].color.equals(v);
						if (filled)
						{
							VoxelFace f = new VoxelFace(d, new Vector3f(x, y, z), blocks[x][y][z].c);
							faces.put(new VoxelFaceKey(x, y, z, d.ordinal()), f);
						}
					}
				}
			}
		}
		
		return faces;
	}
	
	public static HashMap<VoxelFaceKey, VoxelFace> generateGreedyMesh(HashMap<VoxelFaceKey, VoxelFace> originalMap, int size)
	{
		HashMap<VoxelFaceKey, VoxelFace> strips0 = new HashMap<>();
		
		if (originalMap.size() == 0) return originalMap;
		
		// greedy-mode along Z - axis
		for (int posX = 0; posX < size; posX++)
		{
			for (int posY = 0; posY < size; posY++)
			{
				VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
				for (int posZ = 0; posZ < size; posZ++)
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
		for (int posY = 0; posY < size; posY++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int posZ = 0; posZ < size; posZ++)
			{
				for (int posX = 0; posX < size; posX++)
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
		for (int posX = 0; posX < size; posX++)
		{
			VoxelFace[] activeStrips = new VoxelFace[Direction.values().length];
			for (int posZ = 0; posZ < size; posZ++)
			{
				for (int posY = 0; posY < size; posY++)
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
	
	public static void saveOBJ(File f, int size, HashMap<VoxelFaceKey, VoxelFace> meshes)
	{
		try
		{
			int texSize = 16;
			float texMalus = 0.4f / 32f; // 0.0125
			
			String br = "\r\n";
			
			BufferedWriter obj = new BufferedWriter(new FileWriter(f));
			BufferedWriter mtl = new BufferedWriter(new FileWriter(new File(f.getPath().replace(".obj", ".mtl"))));
			
			obj.write("mtllib " + f.getName().replace(".obj", ".mtl") + br);
			
			obj.write(br);
			
			ArrayList<Vector> vertices = new ArrayList<>();
			ArrayList<Color> materials = new ArrayList<>();
			ArrayList<Vector> normals = new ArrayList<>();
			
			CFG.p("  > sorting data");
			
			for (VoxelFace vf : meshes.values())
			{
				Vector bl = transformVector(new Vector(vf.bl).add(new Vector(vf.pos)), size, size, size);
				Vector br2 = transformVector(new Vector(vf.br).add(new Vector(vf.pos)), size, size, size);
				Vector tl = transformVector(new Vector(vf.tl).add(new Vector(vf.pos)), size, size, size);
				Vector tr = transformVector(new Vector(vf.tr).add(new Vector(vf.pos)), size, size, size);
				
				if (!vertices.contains(bl)) vertices.add(bl);
				if (!vertices.contains(br2)) vertices.add(br2);
				if (!vertices.contains(tl)) vertices.add(tl);
				if (!vertices.contains(tr)) vertices.add(tr);
				
				Color c = loadColor(vf.textureIndex);
				if (!materials.contains(c)) materials.add(c);
				
				if (!normals.contains(new Vector(vf.dir.dir))) normals.add(new Vector(vf.dir.dir));
			}
			
			int grid = (int) Math.ceil(Math.sqrt(materials.size()));
			BufferedImage textureSheet = new BufferedImage(grid * texSize, grid * texSize, BufferedImage.TYPE_INT_ARGB);
			
			CFG.p("  > writing vertices");
			
			
			
			for (Vector v : vertices)
				// obj.write("v " + (v.z / min) + " " + ((size - v.x) / min) + " " + (v.y / min) + br);
				obj.write("v " + (v.x * size / 16f) + " " + (v.y * size / 16f) + " " + (v.z * size / 16f) + br);
			
			obj.write(br);
			
			CFG.p("  > writing materials");
			
			mtl.write("newmtl mtl0" + br //
					+ "Ns 0.000000" + br //
					+ "Ka 0.500000 0.500000 0.500000" + br //
					+ "Kd 1.000000 1.000000 1.000000" + br //
					+ "Ks 0.000000 0.000000 0.000000" + br //
					+ "Ni 1.000000" + br //
					+ "d 0.000000" + br //
					+ "illum 2" + br //
					+ "map_Kd " + f.getName().replace(".obj", ".png") + br);
			
			mtl.write("newmtl white" + br //
					+ "Ns 0.000000" + br //
					+ "Ka 0.500000 0.500000 0.500000" + br //
					+ "Kd 1.000000 1.000000 1.000000" + br //
					+ "Ks 0.000000 0.000000 0.000000" + br //
					+ "Ni 1.000000" + br //
					+ "d 0.000000" + br //
					+ "illum 2" + br);
			
			CFG.p("  > writing texture vertices");
			
			Graphics g = textureSheet.getGraphics();
			
			ArrayList<Vector> textureVertices = new ArrayList<>();
			
			for (int i = 0; i < materials.size(); i++)
			{
				Color c = materials.get(i);
				g.setColor(new java.awt.Color(c.r, c.g, c.b, c.a));
				g.fillRect((i % grid) * texSize, (i / grid) * texSize, texSize, texSize);
				
				for (int j = 0; j < 2; j++)
				{
					for (int k = 0; k < 2; k++)
					{
						float U = (((i % grid) + j) / (float) grid) + (j == 0 ? texMalus : -texMalus);
						float V = 1 - (((i / grid) + k) / (float) grid) + (k == 0 ? -texMalus : texMalus);
						
						if (textureVertices.contains(new Vector(U, V, 0))) continue;
						
						obj.write("vt " + U + " " + V + br);
						textureVertices.add(new Vector(U, V, 0));
					}
				}
			}
			
			obj.write(br);
			
			CFG.p("  > writing normals");
			
			for (Vector v : normals)
				obj.write(("vn " + v.x + " " + v.z + " " + v.y).replace("-0.0", "0.0") + br);
			
			obj.write(br);
			
			CFG.p("  > writing texture sheet");
			ImageIO.write(textureSheet, "PNG", new File(f.getPath().replace(".obj", ".png")));
			
			CFG.p("  > writing faces");
			
			ArrayList<VoxelFace> values = new ArrayList<>(meshes.values());
			Collections.sort(values, new Comparator<VoxelFace>()
			{
				@Override
				public int compare(VoxelFace o1, VoxelFace o2)
				{
					Color c1 = loadColor(o1.textureIndex);
					Color c2 = loadColor(o2.textureIndex);
					
					if (c1.equals(c2))
					{
						return 0;
					}
					else if (c1.equals(Color.white))
					{
						return -1;
					}
					
					return 1;
				}
			});
			
			boolean prev = false;
			
			obj.write("usemtl white" + br);
			
			for (int i = 0; i < values.size(); i++)
			{
				VoxelFace face = values.get(i);
				
				boolean white = loadColor(face.textureIndex).equals(Color.white);
				
				if (!white && !prev)
				{
					obj.write("usemtl mtl0" + br);
					prev = true;
				}
				
				int material = materials.indexOf(loadColor(face.textureIndex));
				
				float step = 1 / (float) grid;
				float U = (((material % grid)) / (float) grid);
				float V = 1 - (((material / grid)) / (float) grid);
				
				// tl, br, bl, tr
				int[] v = { //
				vertices.indexOf(transformVector(new Vector(face.tl).add(new Vector(face.pos)), size, size, size)) + 1,//
				vertices.indexOf(transformVector(new Vector(face.br).add(new Vector(face.pos)), size, size, size)) + 1,//
				vertices.indexOf(transformVector(new Vector(face.bl).add(new Vector(face.pos)), size, size, size)) + 1,//
				vertices.indexOf(transformVector(new Vector(face.tr).add(new Vector(face.pos)), size, size, size)) + 1 //
				};
				
				int[] vt = { //
				textureVertices.indexOf(new Vector(U + texMalus, V - texMalus, 0)) + 1, //
				textureVertices.indexOf(new Vector(U + step - texMalus, V - step + texMalus, 0)) + 1, //
				textureVertices.indexOf(new Vector(U + texMalus, V - step + texMalus, 0)) + 1, //
				textureVertices.indexOf(new Vector(U + step - texMalus, V - texMalus, 0)) + 1, //
				};
				int vn = normals.indexOf(new Vector(face.dir.dir.negate(null))) + 1;
				
				obj.write("f " + v[3] + "/" + vt[3] + "/" + vn + " " + v[0] + "/" + vt[0] + "/" + vn + " " + v[2] + "/" + vt[2] + "/" + vn + " " + v[1] + "/" + vt[1] + "/" + vn + br);
			}
			
			obj.close();
			mtl.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Vector transformVector(Vector v, int w, int h, int d)
	{
		float min = Math.min(d, Math.min(w, h));
		return new Vector(v.z / min, (h - v.x) / min, v.y / min);
	}
}
