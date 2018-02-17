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
	public Point getRelativePosition(double w0, double wt, double theta0, double thetat, double v0, double vt, double s, double dt)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
