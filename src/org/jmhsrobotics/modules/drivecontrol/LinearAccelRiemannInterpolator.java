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
		double av = (-6 * s + 3 * dt * (vt + vo)) / (dt * dt * dt);
		double bv = (6 * s - 2 * dt * (vt + vo)) / (dt * dt);

		double dtheta = thetat - thetao;
		double at = (-2 * dtheta + dt * (wt + wo)) / (dt * dt * dt);
		double bt = (3 * dtheta - dt * (wt + wo)) / (dt * dt);

		double delta = dt / accuracy;
		double x = 0;
		double y = 0;
		for (double k = 0; k < dt; k += delta)
		{
			double v = (av * k * k + bv * k + vo);
			double theta = at * k * k * k + bt * k * k + wo * k + thetao;
			x += v * Math.sin(theta) * delta;
			y += v * Math.cos(theta) * delta;
		}

		return new Point(x, y);
	}
}
