package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.util.Point;

public class SimplePositionInterpolator implements PositionInterpolator
{
	@Override
	public Point getRelativePosition(double w0, double wt, double theta0, double thetat, double v0, double vt, double s, double dt)
	{
		double dx = Math.sin((theta0 + thetat) / 2) * s;
		double dy = Math.cos((theta0 + thetat) / 2) * s;
		return new Point(dx, dy);
	}
}
