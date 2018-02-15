package org.jmhsrobotics.core.modulesystem;

import org.jmhsrobotics.hardwareinterface.DriveMechanism;

public abstract class DriveController extends CommandModule
{
	private @Submodule DriveMechanism driveTrain;

	protected void driveRaw(double speed, double turn)
	{
		driveTrain.drive(speed, turn);
	}
	
	public abstract void drive(double speed, double turn);
	public abstract void setTarget(double x, double y);
	
	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
