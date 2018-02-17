package org.jmhsrobotics.core.modulesystem;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

public abstract class DriveController extends CommandModule
{
	private @Submodule DriveMechanism driveTrain;

	protected void driveRaw(double speed, double turn)
	{
		driveTrain.drive(speed, turn);
	}
	
	public abstract void drive(double speed, double turn);
	public abstract void setTarget(Point point);
	public abstract void setRelativeTarget(Point point);
	public abstract void setTarget(Angle angle);
	public abstract void setRelativeTarget(Angle angle);
	public abstract void removeTarget();
	public abstract double getDistanceToTargetPoint();
	public abstract Angle getDistanceToTargetAngle();
	
	public void setTarget(double x, double y)
	{
		setTarget(new Point(x, y));
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
