package com.vloxlands.render.util;

import java.io.IOException;

import com.vloxlands.render.model.Model;

public class ModelLoader {
	public static Model loadModel(String path) {
		String format = path.substring(path.lastIndexOf("."));
		if (format.equals(".obj")) {
			try {
				return OBJLoader.loadModel(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
