package com.vloxlands.util;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.settings.CFG;


/*
 * @author Felix Schmidt
 */
public class Plane
{
	public Vector3f normal = new Vector3f();
	public Vector3f startingPoint = new Vector3f();
	float d;
	
	public Plane(Vector3f normal, Vector3f startingPoint)
	{
		this.normal.set(normal.getX(), normal.getY(), normal.getZ());
		this.startingPoint.set(startingPoint.getX(), startingPoint.getY(), startingPoint.getZ());
		this.calculateD();
	}
	
	private void calculateD()
	{
		this.d = -((this.normal.x * this.startingPoint.x) + (this.normal.y * this.startingPoint.y) + (this.normal.z * this.startingPoint.z));
	}
	
	public Plane(Plane plane)
	{
		this(plane.normal, plane.startingPoint);
	}
	
	public Plane(Vector3f a, Vector3f b, Vector3f c)
	{
		this(Vector3f.cross(Vector3f.sub(b, a, null), Vector3f.sub(c, a, null), null), a);
	}
	
	public Vector3f getNormal()
	{
		return new Vector3f(normal.x, normal.y, normal.z);
	}
	
	public Vector3f getStartingPoint()
	{
		return new Vector3f(startingPoint.x, startingPoint.y, startingPoint.z);
	}
	
	// public void setNormal(Vector3f newNormal)
	// {
	// normal = MathHelper.cloneVector(newNormal);
	// }
	//
	// public void setStartingPoint(Vector3f newStartingPoint)
	// {
	// startingPoint = MathHelper.cloneVector(newStartingPoint);
	// }
	
	public void negateNormal()
	{
		this.normal.setX(this.normal.getX() * -1);
		this.normal.setY(this.normal.getY() * -1);
		this.normal.setZ(this.normal.getZ() * -1);
	}
	
	public Vector3f getPoint(float x, float y)
	{
		return new Vector3f(x, y, (Vector3f.dot(normal, startingPoint) - normal.x * x - normal.y * y) / normal.z);
	}
	
	public void transformToHesseNormalForm()
	{
		normal = MathHelper.normalise(normal);
		// last factor of the coordinate-form
		// float lastFactor = Vector3f.dot(normal, (Vector3f) MathHelper.cloneVector(startingPoint).negate());
		// if(lastFactor > 0)
		normal.negate();
	}
	
	/**
	 * calculates the distance between a point and a plane
	 * 
	 * @param point the point
	 * @return the distance
	 */
	public float calculateDistancePoint(Vector3f point)
	{
		this.transformToHesseNormalForm();
		return (Vector3f.dot(this.normal, point) - Vector3f.dot(this.startingPoint, this.normal));
	}
	
	/**
	 * calculates the distance between a point and a plane
	 * 
	 * @param transformToHesseNormalFormFirst if this plane should be transformed
	 * @param point the point
	 * @return the distance
	 */
	public float calculateDistancePoint(boolean transformToHesseNormalFormFirst, Vector3f point)
	{
		if (transformToHesseNormalFormFirst) this.transformToHesseNormalForm();
		return (Vector3f.dot(this.normal, point) - Vector3f.dot(this.startingPoint, this.normal));
	}
	
	@Override
	public String toString()
	{
		return "normal: " + normal.toString() + "\nstartingPoint: " + startingPoint.toString();
	}
	
	public double getSignedDistanceToPoint(Vector3f point)
	{
		float f = Vector3f.dot(this.normal, Vector3f.sub(point, startingPoint, null));
		CFG.p(f);
		return f;
	}
}
