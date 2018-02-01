package org.jmhsrobotics.core.modulesystem;

import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import edu.wpi.first.wpilibj.command.Command;

public abstract class DriveController implements Module
{
	private @Submodule DriveMechanism driveTrain;

	protected void driveRaw(double speed, double turn)
	{
		driveTrain.drive(speed, turn);
	}

	public abstract void drive(double speed, double turn);

	@Override
	public Command getTest()
	{
		return new Command()
		{
			@Override
			protected boolean isFinished()
			{
				return timeSinceInitialized() > 5;
			}
		};
	}
}
