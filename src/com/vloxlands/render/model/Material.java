package com.vloxlands.render.model;

import org.lwjgl.util.vector.Vector4f;

public class Material {
	public Vector4f diffuseColor = new Vector4f(1, 1, 1, 1);
	
	public Vector4f ambientColor = new Vector4f(1, 1, 1, 1);
	
	public Vector4f specularColor = new Vector4f(1, 1, 1, 1);
	public float shininess = 10;
	
	public float transparency = 1;
	
	public String texturePath = "";
	public boolean hasTexture = false;
	public String name;
	
	@Override
	public String toString() {
		return "[diffuseColor=" + diffuseColor + ", ambientColor=" + ambientColor + ", specularColor=" + specularColor + ", shininess=" + shininess + ", transparency=" + transparency + ", texturePath=" + texturePath + ", hasTexture=" + Boolean.toString(hasTexture) + ", name=" + name + "]";
	}
}
