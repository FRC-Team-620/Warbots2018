package org.jmhsrobotics.core.modulesystem;

import java.util.Optional;

import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import edu.wpi.first.wpilibj.command.Command;

public abstract class DriveController implements Module
{
	private @Submodule DriveMechanism driveTrain;

	protected void driveRaw(double speed, double turn)
	{
		driveTrain.drive(speed, turn);
	}
	
	public abstract void enable();
	public abstract void disable();
	public abstract void drive(double speed, double turn);
	public abstract Command getTurnCommand(Angle angle);
	public abstract Command getDriveCommand(double distance);
	public abstract Optional<Angle> getLockAngle();
	
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
