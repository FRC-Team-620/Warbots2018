package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.util.Point;

public interface PositionInterpolator
{
	public Point getRelativePosition(double w0, double wt, double theta0, double thetat, double v0, double vt, double s, double dt);
}
