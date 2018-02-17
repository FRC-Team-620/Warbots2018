package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.util.Point;

public class LinearAccelRiemannInterpolator implements PositionInterpolator
{
	private int accuracy;
	
	public LinearAccelRiemannInterpolator(int accuracy)
	{
		this.accuracy = accuracy;
	}
	
	@Override
	public Point getRelativePosition(double wo, double wt, double thetao, double thetat, double vo, double vt, double s, double dt)
	{
		double av = (-6*s+3*dt*(vt+vo))/(dt*dt*dt);
		double bv = (6*s-2*dt*(vt+vo))/(dt*dt);
		double cv = vo;
		double at = (-6*(thetat-thetao)+3*dt*(wt+wo))/(dt*dt*dt);
		double bt = (6*(thetat-thetao)-2*dt*(wt+wo))/(dt*dt);
		double ct = wo;
		double delta = dt/accuracy;
		double xVal = 0;
		double yVal = 0;
		
		for(int k=0;k<dt;k+=delta)
		{
			xVal += delta*(av*k*k+bv*k+cv)*Math.sin((1/3)*at*k*k*k+(1/2)*bt*k*k+ct*k+thetao);
			yVal += delta*(av*k*k+bv*k+cv)*Math.cos((1/3)*at*k*k*k+(1/2)*bt*k*k+ct*k+thetao);
		}
		return new Point(xVal, yVal);
	}
}
