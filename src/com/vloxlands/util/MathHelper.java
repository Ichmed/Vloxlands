package com.vloxlands.util;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class MathHelper
{
	public static float clamp(float x, int i, int j)
	{
		return Math.max(i, Math.min(x, j));
	}
	
	public static FloatBuffer asFloatBuffer(float[] fs)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(fs.length);
		buffer.put(fs);
		buffer.flip();
		return buffer;
	}
	
	public static Vector2f scaleVector2f(Vector2f v, float scale)
	{
		return new Vector2f(v.x * scale, v.y * scale);
	}
	
	public static Vector2f bezierCurve(float[] c, float t)
	{
		Vector2f p0 = new Vector2f(c[0], c[1]);
		Vector2f p1 = new Vector2f(c[2], c[3]);
		Vector2f p2 = new Vector2f(c[4], c[5]);
		Vector2f p3 = new Vector2f(c[6], c[7]);
		
		Vector2f s0 = Vector2f.sub(p1, p0, null);
		Vector2f s1 = Vector2f.sub(p2, p1, null);
		Vector2f s2 = Vector2f.sub(p3, p2, null);
		
		Vector2f g0 = Vector2f.add(MathHelper.scaleVector2f(s0, t), p0, null);
		
		Vector2f g1 = Vector2f.add(MathHelper.scaleVector2f(s1, t), p1, null);
		
		Vector2f g2 = Vector2f.add(MathHelper.scaleVector2f(s2, t), p2, null);
		
		Vector2f gs0 = Vector2f.sub(g1, g0, null);
		Vector2f gs1 = Vector2f.sub(g2, g1, null);
		
		Vector2f b0 = Vector2f.add(MathHelper.scaleVector2f(gs0, t), g0, null);
		
		Vector2f b1 = Vector2f.add(MathHelper.scaleVector2f(gs1, t), g1, null);
		
		Vector2f bs0 = Vector2f.sub(b1, b0, null);
		
		return Vector2f.add((Vector2f) MathHelper.scaleVector2f(bs0, t), b0, null);
	}
	
	public static void renderBezierCurve(float[] c, String file)
	{
		String s = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" >";
		
		String color = "rgb(" + (int) Math.round(Math.random() * 256) + "," + (int) Math.round(Math.random() * 256) + "," + (int) Math.round(Math.random() * 256) + ")";
		s += "<polyline fill=\"none\" stroke=\"" + color + "\" stroke-width=\"10\" points=\"";
		for (float i = 0; i < 1.0f; i += 0.01f)
		{
			Vector2f point = bezierCurve(c, i);
			s += (int) Math.round(point.x * 300 + 200) + "," + (int) Math.round(point.y * 300 + 200) + " ";
		}
		s += "\" />";
		s += "<rect x='200' y='200' width='300' height='300' stroke='blue' fill='none'></rect>";
		
		s += "</svg>";
		
		Assistant.setFileContent(new File("src/test/bezier/" + file + ".svg"), s);
	}
	
	public static Vector2f getRandomCircleInCircle(Vector2f center, int radius, int rad2)
	{
		Vector2f v = new Vector2f();
		do
		{
			v = new Vector2f((int) Math.round(Math.random() * radius * 2 - radius + center.x), (int) Math.round(Math.random() * radius * 2 - radius + center.y));
		}
		while (Vector2f.sub(v, center, null).length() > radius - rad2);
		
		return v;
	}
	
	public static Vector3f rotateVector3f(Vector3f npos, Vector3f nrot, float rotation)
	{
		Matrix4f matrix = new Matrix4f();
		
		Vector3f pos = new Vector3f(npos);
		
		matrix.m03 = pos.x;
		matrix.m13 = pos.y;
		matrix.m23 = pos.z;
		
		Vector3f rot = new Vector3f(nrot);
		
		Matrix4f.rotate((float) Math.toRadians(rotation), rot, matrix, matrix);
		
		return new Vector3f(matrix.m03, matrix.m13, matrix.m23);
	}
}
