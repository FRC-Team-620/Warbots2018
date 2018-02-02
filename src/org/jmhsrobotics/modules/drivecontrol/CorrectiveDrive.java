package org.jmhsrobotics.modules.drivecontrol;

import org.jmhsrobotics.core.modulesystem.DriveController;
import org.jmhsrobotics.core.modulesystem.Submodule;
import org.jmhsrobotics.core.util.Angle;

import edu.wpi.first.wpilibj.command.Command;

public class CorrectiveDrive extends DriveController
{
	private @Submodule Localization localization;
	
	@Override
	public void drive(double speed, double turn)
	{
		driveRaw(speed, turn);
	}
	
	public Command getDriveCommand(double dist)
	{
		return new DriveDistance(this, localization, dist);
	}
	
	public Command getTurnCommand(Angle angle)
	{
		return new TurnAngle(this, localization, angle);
	}
}
