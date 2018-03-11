package org.jmhsrobotics.hardwareinterface;

import org.jmhsrobotics.core.modulesystem.CommandModule;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.core.util.Point;
import org.jmhsrobotics.modules.drivecontrol.OutputSmoother;

public abstract class DriveController extends CommandModule
{
	private @Submodule DriveMechanism driveTrain;

	protected void driveRaw(double speed, double turn)
	{
		driveTrain.drive(speed, turn);
	}
	
	public abstract void drive(double speed, double turn);
	public abstract void drive(double speed, Angle angle);
	public abstract void setTarget(Point point, boolean reverse);
	public abstract void setRelativeTarget(Point point, boolean reverse);
	public abstract void setTarget(Angle angle);
	public abstract void setRelativeTarget(Angle angle);
	
	public void setTarget(Point point)
	{
		setTarget(point, false);
	}
	
	public void setRelativeTarget(Point point)
	{
		setRelativeTarget(point, false);
	}
	
	public abstract void removeTarget();
	public abstract double getDistanceToTargetPoint();
	public abstract Angle getDistanceToTargetAngle();
	public abstract void setSpeedOutputSmoother(OutputSmoother smoother);
	public abstract void setTurnOutputSmoother(OutputSmoother smoother);
	public abstract OutputSmoother getSpeedOutputSmoother();
	public abstract OutputSmoother getTurnOutputSmoother();
	
	public void setTarget(double x, double y, boolean reverse)
	{
		setTarget(new Point(x, y), reverse);
	}
	
	public void setTarget(double x, double y)
	{
		setTarget(x, y, false);
	}
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
