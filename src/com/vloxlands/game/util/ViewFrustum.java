package com.vloxlands.game.util;

import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;


public class ViewFrustum
{
	private static float angle;
	private static float aspect;
	private static float near;
	private static float far;
	
	private Vector3f ntl, ntr, nbl, nbr;
	private Vector3f ftl, ftr, fbl, fbr;
	
	public static void initViewFrustum(float angle, float aspect, float near, float far)
	{
		ViewFrustum.angle = angle;
		ViewFrustum.aspect = aspect;
		ViewFrustum.near = near;
		ViewFrustum.far = far;
		gluPerspective(angle, aspect, near, far);
	}
	
	public static void updateViewFrustum()
	{
		Vector3f pos = Game.currentGame.camera.getPosition();
		
	}
}
