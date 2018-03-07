package org.jmhsrobotics.mockhardware;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveController;

public class MockDriveController extends DriveController
{
	@Override
	public void drive(double speed, double turn)
	{
		driveRaw(speed, turn);
	}

	@Override
	public void drive(double speed, Angle angle)
	{
		System.out.println("Driving " + speed + " " + angle);
	}

	@Override
	public void setTarget(Point point, boolean reverse)
	{
		System.out.println("Setting target " + point + (reverse ? " reverse" : ""));
	}

	@Override
	public void setRelativeTarget(Point point, boolean reverse)
	{
		System.out.println("Setting relative target " + point + (reverse ? " reverse" : ""));
	}

	@Override
	public void setTarget(Angle angle)
	{
		System.out.println("Setting target " + angle);
	}

	@Override
	public void setRelativeTarget(Angle angle)
	{
		System.out.println("Setting relative target " + angle);
	}

	@Override
	public void removeTarget()
	{
		System.out.println("Clearing target");
	}

	@Override
	public double getDistanceToTargetPoint()
	{
		return 0;
	}

	@Override
	public Angle getDistanceToTargetAngle()
	{
		return Angle.ZERO;
	}
}