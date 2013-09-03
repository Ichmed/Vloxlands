package com.vloxlands.game.util;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.util.Plane;

public class ViewFrustum
{
	Vector3f ftl, ftr, fbl, fbr;
	Vector3f ntl, ntr, nbl, nbr;

	Plane[] planes = new Plane[6];

	float Hnear, Wnear, Hfar, Wfar;

	public void calculateViewFrustum(Vector3f camPos, Vector3f camRot, Vector3f up, float nearDist, float farDist)
	{
		Vector3f right = Vector3f.cross(camRot, up, null);
		Vector3f fc = Vector3f.add(camPos, (Vector3f) camRot.scale(farDist), null);
		Vector3f nc = Vector3f.add(camPos, (Vector3f) camRot.scale(nearDist), null);

		float ratio = Display.getWidth() / Display.getHeight();

		Hnear = (float) (2 * Math.tan(Game.fov / 2) * nearDist);
		Wnear = Hnear * ratio;

		Hfar = (float) (2 * Math.tan(Game.fov / 2) * farDist);
		Wfar = Hfar * ratio;

		ftl = Vector3f.sub(Vector3f.add(fc, (Vector3f) up.scale(Hfar / 2), null), (Vector3f) right.scale(Wfar / 2), null);
		ftr = Vector3f.add(Vector3f.add(fc, (Vector3f) up.scale(Hfar / 2), null), (Vector3f) right.scale(Wfar / 2), null);
		fbl = Vector3f.sub(Vector3f.sub(fc, (Vector3f) up.scale(Hfar / 2), null), (Vector3f) right.scale(Wfar / 2), null);
		fbr = Vector3f.add(Vector3f.sub(fc, (Vector3f) up.scale(Hfar / 2), null), (Vector3f) right.scale(Wfar / 2), null);

		ntl = Vector3f.sub(Vector3f.add(nc, (Vector3f) up.scale(Hnear / 2), null), (Vector3f) right.scale(Wnear / 2), null);
		ntr = Vector3f.add(Vector3f.add(nc, (Vector3f) up.scale(Hnear / 2), null), (Vector3f) right.scale(Wnear / 2), null);
		nbl = Vector3f.sub(Vector3f.sub(nc, (Vector3f) up.scale(Hnear / 2), null), (Vector3f) right.scale(Wnear / 2), null);
		nbr = Vector3f.add(Vector3f.sub(nc, (Vector3f) up.scale(Hnear / 2), null), (Vector3f) right.scale(Wnear / 2), null);

		planes[0] = new Plane(ntl, ntr, nbr);
		planes[1] = new Plane(ftr, ftl, fbl);
		planes[2] = new Plane(ntl, nbl, fbl);
		planes[3] = new Plane(nbr, ntr, nbr);
		planes[4] = new Plane(ntr, ntl, ftl);
		planes[5] = new Plane(nbl, nbr, fbr);
	}

	public boolean isPointInsideFrustum(Vector3f point)
	{
		for(Plane p : planes) if(p.getSignedDistanceToPoint(point) < 0) return false;
		return true;
	}
}
