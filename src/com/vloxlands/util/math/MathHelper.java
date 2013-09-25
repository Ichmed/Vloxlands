package com.vloxlands.util.math;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.util.Assistant;

public class MathHelper
{
	public static float clamp(float x, float i, float j)
	{
		return Math.max(i, Math.min(x, j));
	}
	
	public static float round(float i, float step)
	{
		if (i % step > step / 2.0f) return i + (step - (i % step));
		else return i - (i % step);
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
		
		return Vector2f.add(MathHelper.scaleVector2f(bs0, t), b0, null);
	}
	
	public static void renderBezierCurve(float[] c, String file)
	{
		String s = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" >";
		
		String color = "rgb(" + (int) Math.round(Math.random() * 256) + "," + (int) Math.round(Math.random() * 256) + "," + (int) Math.round(Math.random() * 256) + ")";
		s += "<polyline fill=\"none\" stroke=\"" + color + "\" stroke-width=\"10\" points=\"";
		for (float i = 0; i < 1.0f; i += 0.01f)
		{
			Vector2f point = bezierCurve(c, i);
			s += Math.round(point.x * 300 + 200) + "," + Math.round(point.y * 300 + 200) + " ";
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
	
	public static Vector3f vector3fDotMatrix3f(Vector3f v, Matrix3f m)
	{
		return new Vector3f( //
		m.m00 * v.x + m.m01 * v.y + m.m02 * v.z, //
		m.m10 * v.x + m.m11 * v.y + m.m12 * v.z, //
		m.m20 * v.x + m.m21 * v.y + m.m22 * v.z //
		);
	}
	
	public static Vector3f getNormalizedRotationVector(Vector3f v)
	{
		double x = Math.sin(Math.toRadians(v.y)) * Math.cos(Math.toRadians(v.x));
		double y = Math.sin(Math.toRadians(v.x));
		double z = Math.cos(Math.toRadians(v.y)) * Math.cos(Math.toRadians(v.x));
		
		return new Vector3f((float) -x, (float) -y, (float) z);
	}
	
	public static Vector3f normalise(Vector3f v)
	{
		double l = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2) + Math.pow(v.z, 2));
		Vector3f w = new Vector3f(v);
		w.x = (float) (v.x / l);
		w.y = (float) (v.y / l);
		w.z = (float) (v.z / l);
		
		return w;
	}
	
	public static Vector3f mul(Vector3f l, Vector3f r)
	{
		return new Vector3f(l.x * r.x, l.y * r.y, l.z * r.z);
	}
	
	public static Vector2f mul(Vector2f l, Vector2f r)
	{
		return new Vector2f(l.x * r.x, l.y * r.y);
	}
}
