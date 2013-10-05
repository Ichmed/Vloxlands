package com.vloxlands.util.math;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.util.Assistant;

public class MathHelper
{
	public static final Plane PITCH_PLANE = new Plane(new Vector3f(1, 0, 0), new Vector3f());
	public static final Plane YAW_PLANE = new Plane(new Vector3f(0, 1, 0), new Vector3f());
	public static final Plane ROLL_PLANE = new Plane(new Vector3f(0, 0, 1), new Vector3f());
	
	
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
	
	public static float roundToDecimal(float f, int decimals)
	{
		return (int) (f * Math.pow(10, decimals)) / (float) Math.pow(10, decimals);
	}
	
	/**
	 * A method for rotating a vector in a plane (around its normal vector) about degree degree
	 * 
	 * @param vector
	 *          the vector which should be rotated
	 * @param degree
	 *          the degree about which the vector should be rotated
	 * @param rotationPlane
	 *          the plane within the vector should be rotated
	 */
	public static Vector3f rotateVector(Vector3f vector, float degree, Plane rotationPlane)
	{
		double tempDegree = Math.toRadians(degree);
		rotationPlane.transformToHesseNormalForm();
		// Creating a Matrix for rotating the Vector
		Matrix3f rotationMatrix = new Matrix3f();
		rotationMatrix.m00 = (float) (rotationPlane.getNormal().x * rotationPlane.getNormal().x * (1 - Math.cos(tempDegree)) + Math.cos(tempDegree));
		rotationMatrix.m01 = (float) (rotationPlane.getNormal().y * rotationPlane.getNormal().x * (1 - Math.cos(tempDegree)) + rotationPlane.getNormal().z * Math.sin(tempDegree));
		rotationMatrix.m02 = (float) (rotationPlane.getNormal().z * rotationPlane.getNormal().x * (1 - Math.cos(tempDegree)) - rotationPlane.getNormal().y * Math.sin(tempDegree));
		rotationMatrix.m10 = (float) (rotationPlane.getNormal().x * rotationPlane.getNormal().y * (1 - Math.cos(tempDegree)) - rotationPlane.getNormal().z * Math.sin(tempDegree));
		rotationMatrix.m11 = (float) (rotationPlane.getNormal().y * rotationPlane.getNormal().y * (1 - Math.cos(tempDegree)) + Math.cos(tempDegree));
		rotationMatrix.m12 = (float) (rotationPlane.getNormal().z * rotationPlane.getNormal().y * (1 - Math.cos(tempDegree)) + rotationPlane.getNormal().x * Math.sin(tempDegree));
		rotationMatrix.m20 = (float) (rotationPlane.getNormal().x * rotationPlane.getNormal().z * (1 - Math.cos(tempDegree)) + rotationPlane.getNormal().y * Math.sin(tempDegree));
		rotationMatrix.m21 = (float) (rotationPlane.getNormal().y * rotationPlane.getNormal().z * (1 - Math.cos(tempDegree)) - rotationPlane.getNormal().x * Math.sin(tempDegree));
		rotationMatrix.m22 = (float) (rotationPlane.getNormal().z * rotationPlane.getNormal().z * (1 - Math.cos(tempDegree)) + Math.cos(tempDegree));
		Vector3f temp = new Vector3f(vector);
		// adopt rotatoinMatrix on vector
		Matrix3f.transform(rotationMatrix, temp, vector);
		return vector;
	}
	
	public static Vector3f rotateVectorByCameraRotation(Vector3f v)
	{
		v = rotateVector(v, Game.camera.rotation.x, PITCH_PLANE);
		v = rotateVector(v, Game.camera.rotation.y, YAW_PLANE);
		v = rotateVector(v, Game.camera.rotation.z, ROLL_PLANE);
		return v;
	}
	
	// -- intersection methods -- //
	/**
	 * @param fs: pointers
	 */
	private static boolean clipLine(int dimension, AABB aabb, PickingRay ray, float[] fs)
	{
		float fdLow, fdHigh;
		
		float dimLength = new Vector(ray.end).get(dimension) - new Vector(ray.start).get(dimension);
		
		fdLow = (new Vector(aabb.min).get(dimension) - new Vector(ray.start).get(dimension)) / dimLength;
		fdHigh = (new Vector(aabb.max).get(dimension) - new Vector(ray.start).get(dimension)) / dimLength;
		
		if (fdHigh < fdLow)
		{
			// swap
			Float temp = new Float(fdHigh);
			fdHigh = fdLow;
			fdLow = temp;
		}
		
		if (fdHigh < fs[0]) return false;
		
		if (fdLow > fs[1]) return false;
		
		fs[0] = Math.max(fdLow, fs[0]);
		fs[1] = Math.min(fdHigh, fs[1]);
		
		if (fs[0] > fs[1]) return false;
		
		return true;
	}
	
	public static boolean intersects(PickingRay ray, AABB aabb)
	{
		float[] fs = new float[] { 0, 1 };
		
		if (!clipLine(0, aabb, ray, fs)) return false;
		if (!clipLine(1, aabb, ray, fs)) return false;
		if (!clipLine(2, aabb, ray, fs)) return false;
		
		return true;
	}
}
