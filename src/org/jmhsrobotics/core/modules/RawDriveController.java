package org.jmhsrobotics.core.modules;

import java.util.Optional;

import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Module;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;
import org.jmhsrobotics.hardwareinterface.DriveMechanism;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class RawDriveController extends DriveController implements Module
{
	private @Submodule DriveMechanism driveTrain;
	
	@Override
	public void enable()
	{
	}

	@Override
	public void disable()
	{
	}

	@Override
	public void drive(double speed, double turn)
	{
		driveRaw(speed, turn);
	}

	@Override
	public Command getTurnCommand(Angle angle)
	{
		return new InstantCommand();
	}

	@Override
	public Command getDriveCommand(double distance)
	{
		return new InstantCommand();
	}

	@Override
	public Optional<Angle> getLockAngle()
	{
		return Optional.empty();
	}
}
