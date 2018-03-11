package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.util.RobotMath;

public class ConstraintOutputSmoother implements OutputSmoother
{
	private double value;
	private double target;
	private double max;
	
	public ConstraintOutputSmoother(double maxValue)
	{
		max = maxValue;
	}
	
	@Override
	public void setTarget(double target)
	{
		this.target = RobotMath.constrain(target, -max, max);
	}

	@Override
	public void reset()
	{
		value = target = 0;
	}

	@Override
	public void update()
	{
		value = target;
	}

	@Override
	public double get()
	{
		return value;
	}

	@Override
	public double getTarget()
	{
		return target;
	}

	@Override
	public void copyRelevantData(OutputSmoother oldOutputSmoother)
	{
		target = oldOutputSmoother.getTarget();
		value = oldOutputSmoother.get();
	}
}