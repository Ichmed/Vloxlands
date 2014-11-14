package com.vloxlands.render.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.vloxlands.render.model.Face;
import com.vloxlands.render.model.Material;
import com.vloxlands.render.model.Model;

public class OBJLoader {
	/**
	 * Loads a .obj file and creates a Model object from it
	 * 
	 * @param path
	 *          The path to the file relative to the .jar's directory
	 * @return an instance of Model
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Model loadModel(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(OBJLoader.class.getResourceAsStream(path)));
		Model m = new Model();
		String line;
		
		ArrayList<Face> faces = new ArrayList<Face>();
		
		String material = null;
		
		while ((line = reader.readLine()) != null) {
			
			if (line.startsWith("mtllib")) {
				loadMaterialsFile(path.substring(0, path.lastIndexOf("/") + 1), line.split(" ")[1], m);
			} else if (line.startsWith("v ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				
				m.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				
				m.hasNormals = true;
				m.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vt ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				
				m.hasTextures = true;
				m.tetxures.add(new Vector2f(x, y));
			} else if (line.startsWith("f ")) {
				Vector3f[] indices = new Vector3f[line.split(" ").length - 1];
				
				for (int i = 1; i < line.split(" ").length; i++) {
					String s = line.split(" ")[i];
					float v = 0;
					float t = 0;
					float n = 0;
					
					try {
						v = Float.valueOf(s.split("/")[0]);
					} catch (Exception e) {}
					try {
						t = Float.valueOf(s.split("/")[1]);
					} catch (Exception e) {}
					try {
						n = Float.valueOf(s.split("/")[2]);
					} catch (Exception e) {}
					
					indices[i - 1] = new Vector3f(v, t, n);
				}
				faces.add(new Face(indices));
			} else if (line.startsWith("usemtl")) {
				String name = line.split(" ")[1];
				if (material != null) {
					m.faces.add((List<Face>) faces.clone());
					
					faces.clear();
					
					m.faceMaterials.add(material);
				}
				material = name;
			}
		}
		
		if (material != null) {
			m.faces.add((List<Face>) faces.clone());
			m.faceMaterials.add(material);
		}
		
		reader.close();
		return m;
	}
	
	private static void loadMaterialsFile(String path, String file, Model model) throws IOException {
		model.usesMaterials = true;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(OBJLoader.class.getResourceAsStream(path + file)));
		
		Material m = null;
		String line;
		
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("newmtl ")) {
				m = new Material();
				m.name = line.replace("newmtl ", "");
				model.materials.put(line.split(" ")[1], m);
			} else if (line.startsWith("Ka ")) {
				float red = Float.valueOf(line.split(" ")[1]);
				float green = Float.valueOf(line.split(" ")[2]);
				float blue = Float.valueOf(line.split(" ")[3]);
				float alpha = 1;
				if (line.split(" ").length > 4) alpha = Float.valueOf(line.split(" ")[4]);
				m.ambientColor = new Vector4f(red, green, blue, alpha);
			} else if (line.startsWith("Kd ")) {
				float red = Float.valueOf(line.split(" ")[1]);
				float green = Float.valueOf(line.split(" ")[2]);
				float blue = Float.valueOf(line.split(" ")[3]);
				float alpha = 1;
				if (line.split(" ").length > 4) alpha = Float.valueOf(line.split(" ")[4]);
				m.diffuseColor = new Vector4f(red, green, blue, alpha);
			} else if (line.startsWith("Ks ")) {
				float red = Float.valueOf(line.split(" ")[1]);
				float green = Float.valueOf(line.split(" ")[2]);
				float blue = Float.valueOf(line.split(" ")[3]);
				float alpha = 1;
				if (line.split(" ").length > 4) alpha = Float.valueOf(line.split(" ")[4]);
				m.specularColor = new Vector4f(red, green, blue, alpha);
				
			} else if (line.startsWith("Ns ")) {
				m.shininess = Float.valueOf(line.split(" ")[1]);
			} else if (line.startsWith("d ") || line.startsWith("Tr ")) {
				m.transparency = Float.valueOf(line.split(" ")[1]);
			} else if (line.startsWith("map_Kd ")) {
				m.hasTexture = true;
				m.texturePath = path + line.split(" ")[1];
			}
		}
		reader.close();
	}
}
