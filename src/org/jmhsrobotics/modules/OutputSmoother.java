package org.jmhsrobotics.modules;

import org.jmhsrobotics.core.util.PlainSendable;
import org.jmhsrobotics.core.util.RobotMath;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class OutputSmoother extends PlainSendable
{
	private double outputCurve;
	
	private double maxSecondDerivative;
	private double maxFirstDerivative;
	private double maxValue;
	
	private double target;
	private double current;
	private double currentDerivative;
	
	public OutputSmoother(double curve, double maxValue, double maxFirstDerivative, double maxSecondDerivative)
	{
		outputCurve = curve;
		this.maxSecondDerivative = maxSecondDerivative;
		this.maxFirstDerivative = maxFirstDerivative;
		this.maxValue = maxValue;
	}
	
	public OutputSmoother()
	{
		this(.5, 1, 1000000, 1000000);
	}
	
	public void setLimits(double value, double firstDerivative, double secondDerivative)
	{
		maxValue = value;
		maxFirstDerivative = firstDerivative;
		maxSecondDerivative = secondDerivative;
	}
	
	public void setCurve(double outputCurve)
	{
		this.outputCurve = outputCurve;
	}
	
	public void setTarget(double target)
	{
		target = RobotMath.constrain(target, -maxValue, maxValue);
		this.target = RobotMath.curve(target, 1 / outputCurve);
	}

	public void reset()
	{
		target = 0;
		current = 0;
		currentDerivative = 0;
	}

	public double update()
	{
		double targetDelta = target - current;
		double minimumAccessibleDelta = Math.pow(currentDerivative, 2) / (2 * maxSecondDerivative);
		
		double secondDerivative;
		if (Math.abs(targetDelta) <= minimumAccessibleDelta)
			secondDerivative = -Math.signum(currentDerivative) * maxSecondDerivative;
		else
			secondDerivative = Math.signum(targetDelta) * maxSecondDerivative;
		
		if (isAllowableJump(targetDelta, currentDerivative))
		{
			current = target;
			currentDerivative = 0;
		}
		else
		{
			current += currentDerivative;
			currentDerivative = RobotMath.constrain(currentDerivative + secondDerivative, -maxFirstDerivative, maxFirstDerivative);
		}
		
		return get();
	}
	
	public double get()
	{
		return RobotMath.curve(current, outputCurve);
	}
	
	private boolean isAllowableJump(double speedDelta, double acceleration)
	{
		if (Math.abs(speedDelta) > maxFirstDerivative)
			return false;
		
		if (Math.abs(speedDelta - acceleration) > maxSecondDerivative)
			return false;
		
		return true;
	}

	@Override
	public void initSendable(SendableBuilder builder)
	{
		// TODO Auto-generated method stub
		
	}
}